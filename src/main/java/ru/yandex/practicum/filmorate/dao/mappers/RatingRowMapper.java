package ru.yandex.practicum.filmorate.dao.mappers;

import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.model.Genre;
import ru.yandex.practicum.filmorate.model.Rating;

import java.sql.ResultSet;
import java.sql.SQLException;

@Component
public class RatingRowMapper implements RowMapper<Rating> {

    Genre genre = new Genre();

    @Override
    public Rating mapRow(ResultSet resultSet, int rowNum) throws SQLException {
        Rating rating = new Rating();
        rating.setId(resultSet.getInt("mpa_id"));
        rating.setName(resultSet.getString("name"));
        return rating;
    }
}