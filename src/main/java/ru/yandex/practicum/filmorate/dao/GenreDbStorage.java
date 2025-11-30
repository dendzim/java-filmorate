package ru.yandex.practicum.filmorate.dao;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.Genre;
import ru.yandex.practicum.filmorate.storage.GenreStorage;

import java.util.Collection;
import java.util.List;

@Repository("GenreDbStorage")
public class GenreDbStorage extends BaseDao<Genre> implements GenreStorage {

    private static final String FIND_ALL_QUERY = "SELECT * FROM PUBLIC.\"Genre\"";
    private static final String FIND_BY_ID_QUERY = "SELECT * FROM PUBLIC.\"Genre\" WHERE GENRE_ID = ?";
    private static final String ADD_GENRE_TO_FILM_QUERY = "INSERT INTO PUBLIC.\"Film_Genre\" (FILM_ID, GENRE_ID) " +
            "VALUES (?, ?)";
    private static final String UPDATE_FILM_GENRE_QUERY = "DELETE FROM PUBLIC.\"Film_Genre\" WHERE FILM_ID = ?";

    public GenreDbStorage(JdbcTemplate jdbc, RowMapper<Genre> mapper) {
        super(jdbc, mapper);
    }

    @Override
    public Collection<Genre> findAll() {
        return getAll(FIND_ALL_QUERY);
    }

    @Override
    public Genre findGenreById(int id) {
        return get(FIND_BY_ID_QUERY);
    }

    @Override
    public void addGenresToFilm(Film film) {
        if (film.getGenres() == null || film.getGenres().isEmpty()) {
            return;
        }
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