package ru.yandex.practicum.filmorate.dao;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.storage.FilmStorage;

import java.util.Collection;

@Repository("FilmDbStorage")
public class FilmDbStorage extends BaseDao<Film> implements FilmStorage {

    private static final String FIND_ALL_FILMS_QUERY = "SELECT f.FILM_ID, f.NAME, f.DESCRIPTION, f.RELEASE_DATE, " +
            "f.DURATION, r.NAME AS RATING_NAME, GROUP_CONCAT(g.NAME) AS GENRES, COUNT(l.USER_ID) AS LIKES_COUNT " +
            "FROM PUBLIC.\"Film\" f LEFT JOIN PUBLIC.\"Rating\" r ON f.RATING_ID = r.RATING_ID LEFT " +
            "JOIN PUBLIC.\"Film_genre\" fg ON f.FILM_ID = fg.FILM_ID LEFT JOIN PUBLIC.\"Genre\" g " +
            "ON fg.GENRE_ID = g.GENRE_ID LEFT JOIN PUBLIC.\"Likes\" l ON f.FILM_ID = l.FILM_ID";
    private static final String INSERT_QUERY = "INSERT INTO PUBLIC.\"Film\" (NAME, DESCRIPTION, RELEASE_DATE, " +
            "DURATION, RATING_ID) VALUES (?, ?, ?, ?, ?)";
    private static final String GROUP_BY = "GROUP BY f.FILM_ID";
    private static final String DELETE_QUERY = "DELETE FROM PUBLIC.\"Film\" WHERE FILM_ID = ?";
    private static final String UPDATE_QUERY = "UPDATE PUBLIC.\"Film\" SET NAME = :NAME, DESCRIPTION = :DESCRIPTION, " +
            "RELEASE_DATE = :RELEASE_DATE, DURATION = :DURATION, RATING_ID = :RATING_ID WHERE FILM_ID = :FILM_ID";
    private static final String EXISTS_QUERY = "SELECT EXISTS(SELECT 1 FROM PUBLIC.\"Film\" WHERE FILM_ID = ?)";

    public FilmDbStorage(JdbcTemplate jdbc, RowMapper<Film> mapper) {
        super(jdbc, mapper);
    }

    @Override
    public Collection<Film> findAll() {
        return getAll(FIND_ALL_FILMS_QUERY);
    }

    @Override
    public Film create(Film film) {
        int id = insert(
                INSERT_QUERY,
                film.getName(),
                film.getDescription(),
                film.getDuration(),
                film.getReleaseDate(),
                film.getMpa()
        );
        film.setId(id);
        return film;
    }

    @Override
    public Film update(Film film) {
        update(INSERT_QUERY,
                film.getName(),
                film.getDescription(),
                film.getDuration(),
                film.getReleaseDate(),
                film.getMpa(),
                film.getId()
        );
        return film;
    }

    @Override
    public Film findFilmById(int id) {
        return get(FIND_ALL_FILMS_QUERY + " WHERE f.FILM_ID = ?", id);
    }

    @Override
    public void remove(int id) {
        delete(DELETE_QUERY, id);
    }

    @Override
    public Collection<Film> getPopular(int count) {
        return jdbc.query(FIND_ALL_FILMS_QUERY + " ORDER BY PUBLIC.\"Likes\" DESC LIMIT ?", mapper, count);
    }

    @Override
    public Integer addLike(int id, int userId) {
        String INSERT_QUERY = "INSERT INTO PUBLIC.\"Likes\"(FILM_ID, USER_ID) VALUES(?, ?)";
        String COUNT_QUERY = "SELECT COUNT(*) FROM PUBLIC.\"Likes\" WHERE film_id = ?";
        jdbc.update(INSERT_QUERY, id, userId);

        return jdbc.queryForObject(COUNT_QUERY, Integer.class, id);
    }

    @Override
    public Integer deleteLike(int id, int userId) {
        String DELETE_QUERY = "DELETE FROM PUBLIC.\"Likes\" WHERE USER_ID = ?";
        String COUNT_QUERY = "SELECT COUNT(*) FROM PUBLIC.\"Likes\" WHERE FILM_ID = ?";
        jdbc.update(DELETE_QUERY, userId);

        return jdbc.queryForObject(COUNT_QUERY, Integer.class, id);
    }

    @Override
    public boolean contains(Integer id) {
        return jdbc.queryForObject(EXISTS_QUERY, Boolean.class, id);
    }
}
