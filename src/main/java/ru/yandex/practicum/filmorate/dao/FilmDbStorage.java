package ru.yandex.practicum.filmorate.dao;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;
import ru.yandex.practicum.filmorate.dao.mappers.FilmRowMapper;
import ru.yandex.practicum.filmorate.exception.NotFoundException;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.Genre;
import ru.yandex.practicum.filmorate.storage.FilmStorage;
import ru.yandex.practicum.filmorate.storage.RatingStorage;

import java.util.*;
import java.util.stream.Collectors;

@Repository("FilmDbStorage")
public class FilmDbStorage extends BaseDao<Film> implements FilmStorage {

    private static final String SELECT_ALL_FIELDS = """
            SELECT f.*,
            COUNT(DISTINCT l.user_id) AS likes,
            r.mpa_id,
            r.name AS mpa_name,
            ARRAY_AGG(g.genre_id) AS genre_ids,
            ARRAY_AGG(g.name) AS genre_names
            """;

    private static final String JOIN_ALL_TABLES = """
            FROM films AS f
            LEFT JOIN mpa AS r ON f.mpa_id = r.mpa_id
            LEFT JOIN likes AS l ON f.film_id = l.film_id
            LEFT JOIN films_genres AS fg ON f.film_id = fg.film_id
            LEFT JOIN genres AS g ON fg.genre_id = g.genre_id
            """;

    private static final String GROUP_BY = "GROUP BY f.film_id";

    private static final String INSERT_QUERY = """
            INSERT INTO films (name, description, release_date, duration, mpa_id)
            VALUES (?, ?, ?, ?, ?)
            """;

    private static final String DELETE_QUERY = "DELETE FROM films WHERE film_id = ?";

    private static final String UPDATE_QUERY = """
            UPDATE films SET
            name = ?,
            description = ?,
            release_date = ?,
            duration = ?,
            mpa_id = ?
            WHERE film_id = ?
            """;

    private static final String EXISTS_QUERY = "SELECT EXISTS(SELECT 1 FROM films WHERE film_id = ?)";

    private static final String FIND_FILM_BY_ID_QUERY = String.format("%s %s WHERE f.film_id = ? %s",
            SELECT_ALL_FIELDS, JOIN_ALL_TABLES, GROUP_BY);

    private static final String FIND_ALL_FILMS_QUERY = String.format("%s %s %s",
            SELECT_ALL_FIELDS, JOIN_ALL_TABLES, GROUP_BY);

    private final GenreDbStorage genreDbStorage;
    private final RatingStorage ratingStorage;
    public FilmDbStorage(JdbcTemplate jdbc, FilmRowMapper mapper, GenreDbStorage genreDbStorage, RatingStorage ratingStorage) {
        super(jdbc, mapper);
        this.genreDbStorage = genreDbStorage;
        this.ratingStorage = ratingStorage;
    }

    @Override
    public List<Film> findAll() {
        return getAll(FIND_ALL_FILMS_QUERY);
    }

    @Override
    public Film create(Film film) {
        if (ratingStorage.findRatingById(film.getMpa().getId()).isEmpty()) {
            throw new NotFoundException("Рейтинг с таким id " + film.getMpa().getId() + " не найден");
        }

        Set<Integer> ids = genreDbStorage.findAll().stream()
                .map(Genre::getId)
                .collect(Collectors.toSet());

        for (Genre genre : film.getGenres()) {
            if (!ids.contains(genre.getId())) {
                throw new NotFoundException("Жанр с id=" + genre.getId() + " не найден");
            }
        }

        int id = insert(INSERT_QUERY,
                film.getName(),
                film.getDescription(),
                film.getReleaseDate(),
                film.getDuration(),
                film.getMpa().getId()
        );
        film.setId(id);

        return film;
    }

    @Override
    public Film update(Film film) {
        update(UPDATE_QUERY,
                film.getName(),
                film.getDescription(),
                film.getReleaseDate(),
                film.getDuration(),
                film.getMpa().getId(),
                film.getId()
        );
        return film;
    }

    @Override
    public Film findFilmById(int id) {
        return get(FIND_FILM_BY_ID_QUERY, id);

    }

    @Override
    public Film remove(int id) {
        Film film = findFilmById(id);
        delete(DELETE_QUERY, id);
        return film;
    }

    @Override
    public List<Film> getPopular(int count) {
        return jdbc.query(FIND_ALL_FILMS_QUERY + " ORDER BY likes DESC LIMIT ?", mapper, count);
    }

    @Override
    public Integer addLike(int id, int userId) {
        String INSERT_QUERY = "INSERT INTO likes (film_id, user_id) VALUES(?, ?)";
        String COUNT_QUERY = "SELECT COUNT(DISTINCT user_id) FROM likes WHERE film_id = ?";
        jdbc.update(INSERT_QUERY, id, userId);

        return jdbc.queryForObject(COUNT_QUERY, Integer.class, id);
    }

    @Override
    public Integer deleteLike(int id, int userId) {
        String DELETE_QUERY = "DELETE FROM likes WHERE user_id = ?";
        String COUNT_QUERY = "SELECT COUNT(DISTINCT user_id) FROM likes WHERE film_id = ?";
        jdbc.update(DELETE_QUERY, userId);

        return jdbc.queryForObject(COUNT_QUERY, Integer.class, id);
    }

    @Override
    public boolean contains(Integer id) {
        return jdbc.queryForObject(EXISTS_QUERY, Boolean.class, id);
    }
}