package ru.yandex.practicum.filmorate.dao;

import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;
import ru.yandex.practicum.filmorate.dao.mappers.GenreRowMapper;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.Genre;
import ru.yandex.practicum.filmorate.storage.GenreStorage;

import java.util.Collection;
import java.util.List;
import java.util.Optional;

@Repository("GenreDbStorage")
public class GenreDbStorage extends BaseDao<Genre> implements GenreStorage {

    private static final String FIND_ALL_QUERY = "SELECT * FROM genres";
    private static final String FIND_BY_ID_QUERY = "SELECT * FROM genres WHERE genre_id = ?";
    private static final String ADD_GENRE_TO_FILM_QUERY = """
            INSERT INTO films_genres (film_id, genre_id)
            VALUES (?, ?)
            """;
    private static final String UPDATE_FILM_GENRE_QUERY = "DELETE FROM films_genres WHERE film_id = ?";

    public GenreDbStorage(JdbcTemplate jdbc, GenreRowMapper mapper) {
        super(jdbc, mapper);
    }

    @Override
    public Collection<Genre> findAll() {
        return getAll(FIND_ALL_QUERY);
    }

    @Override
    public Optional<Genre> findGenreById(int id) {
        try {
            Genre genre = get(FIND_BY_ID_QUERY, id);
            return Optional.ofNullable(genre);
        } catch (EmptyResultDataAccessException e) {
            return Optional.empty();
        }
    }

    @Override
    public void addGenresToFilm(Film film) {
        Integer filmId = film.getId();

        List<Object[]> batchArgs = film.getGenres()
                .stream()
                .map(genre -> new Object[]{filmId, genre.getId()})
                .toList();

        jdbc.batchUpdate(ADD_GENRE_TO_FILM_QUERY, batchArgs);
    }

    @Override
    public void updateFilmGenres(Film film) {
        jdbc.update(UPDATE_FILM_GENRE_QUERY, film.getId());
        addGenresToFilm(film);
    }
}