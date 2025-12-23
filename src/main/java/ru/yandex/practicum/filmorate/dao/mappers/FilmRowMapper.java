package ru.yandex.practicum.filmorate.dao.mappers;

import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.Genre;
import ru.yandex.practicum.filmorate.model.Rating;

import java.sql.Array;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.HashSet;
import java.util.Set;

@Component
public class FilmRowMapper implements RowMapper<Film> {

    @Override
    public Film mapRow(ResultSet resultSet, int rowNum) throws SQLException {
        Film film = new Film();
        film.setId(resultSet.getInt("film_id"));
        film.setName(resultSet.getString("name"));
        film.setDescription(resultSet.getString("description"));
        film.setDuration(resultSet.getInt("duration"));
        LocalDate releaseDate = resultSet.getObject("release_date", LocalDate.class);
        film.setReleaseDate(releaseDate);

        mapRating(film, resultSet);
        mapGenres(film, resultSet);
        return film;
    }

    private void mapRating(Film film, ResultSet resultSet) throws SQLException {
        int ratingId = resultSet.getInt("mpa_id");
        if (!resultSet.wasNull()) {
            String ratingName = resultSet.getString("mpa_name");
            film.setMpa(new Rating(ratingId, ratingName));
        }
    }

    private void mapGenres(Film film, ResultSet resultSet) throws SQLException {
        Array genresId = resultSet.getArray("genre_ids");
        if (resultSet.wasNull()) {
            return;
        }
        Array genresName = resultSet.getArray("genre_names");
        if (genresName == null) {
            return;
        }
        Object[] ids = (Object[]) genresId.getArray();
        Object[] names = (Object[]) genresName.getArray();
        Set<Genre> genres = new HashSet<>();
        for (int i = 0; i < ids.length; i++) {
            if (ids[i] != null && names[i] != null) {
                Integer id = ((Number)ids[i]).intValue();
                 String name = names[i].toString();
                genres.add(new Genre(id, name));
            }
        }
        film.setGenres(genres);
    }
}