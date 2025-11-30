package ru.yandex.practicum.filmorate.dao.mappers;

import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.Genre;
import ru.yandex.practicum.filmorate.model.Rating;

import java.sql.Array;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashSet;
import java.util.Set;

@Component
public class FilmRowMapper implements RowMapper<Film> {

    @Override
    public Film mapRow(ResultSet resultSet, int rowNum) throws SQLException {
        Film film = new Film();
        film.setId(resultSet.getInt("FILM_ID"));
        film.setName(resultSet.getString("NAME"));
        film.setDescription(resultSet.getString("DESCRIPTION"));
        film.setDuration(resultSet.getInt("DURATION"));
        film.setReleaseDate(resultSet.getDate("RELEASE_DATE").toLocalDate());

        mapRating(film, resultSet);
        mapGenres(film, resultSet);
        return film;
    }

    private void mapRating(Film film, ResultSet resultSet) throws SQLException {
        int mpaId = resultSet.getInt("RATING_ID");
        if (!resultSet.wasNull()) {
            String ratingName = resultSet.getString("NAME");
            film.setMpa(new Rating(mpaId, ratingName));
        }
    }

    private void mapGenres(Film film, ResultSet resultSet) throws SQLException {
        Set<Genre> genres = new HashSet<>();
        Array genresId = resultSet.getArray("GENRE_ID");
        if (resultSet.wasNull()) {
            return;
        }
        Array genresName = resultSet.getArray("NAME");
        Integer[] ids = (Integer[]) genresId.getArray();
        String[] names = (String[]) genresName.getArray();
        for (int i = 0; i < ids.length; i++) {
            if (ids[i] != null && names[i] != null) {
                Integer id = ids[i];
                String name = names[i];
                genres.add(new Genre(id, name));
            }
        }
        film.setGenres(genres);
    }
}