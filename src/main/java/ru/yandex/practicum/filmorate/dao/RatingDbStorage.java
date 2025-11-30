package ru.yandex.practicum.filmorate.dao;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;
import ru.yandex.practicum.filmorate.model.Rating;
import ru.yandex.practicum.filmorate.storage.RatingStorage;

import java.util.List;

@Repository("RatingDbStorage")
public class RatingDbStorage extends BaseDao<Rating> implements RatingStorage {

    private static final String FIND_ALL_QUERY = "SELECT * FROM PUBLIC.\"Rating\"";
    private static final String FIND_BY_ID_QUERY = "SELECT * FROM PUBLIC.\"Rating\" WHERE RATING_ID = ?";

    public RatingDbStorage(JdbcTemplate jdbc, RowMapper<Rating> mapper) {
        super(jdbc, mapper);
    }

    @Override
    public List<Rating> findAll() {
        return getAll(FIND_ALL_QUERY);
    }

    @Override
    public Rating findRatingById(int id) {
        return get(FIND_BY_ID_QUERY);
    }
}