package ru.yandex.practicum.filmorate.dao;

import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;
import ru.yandex.practicum.filmorate.dao.mappers.RatingRowMapper;
import ru.yandex.practicum.filmorate.model.Rating;
import ru.yandex.practicum.filmorate.storage.RatingStorage;

import java.util.List;
import java.util.Optional;

@Repository("RatingDbStorage")
public class RatingDbStorage extends BaseDao<Rating> implements RatingStorage {

    private static final String FIND_ALL_QUERY = "SELECT * FROM mpa";
    private static final String FIND_BY_ID_QUERY = "SELECT * FROM mpa WHERE mpa_id = ?";

    public RatingDbStorage(JdbcTemplate jdbc, RatingRowMapper mapper) {
        super(jdbc, mapper);
    }

    @Override
    public List<Rating> findAll() {
        return getAll(FIND_ALL_QUERY);
    }

    @Override
    public Optional<Rating> findRatingById(int id) {
        try {
            Rating rating = get(FIND_BY_ID_QUERY, id);
            return Optional.ofNullable(rating);
        } catch (EmptyResultDataAccessException e) {
            return Optional.empty();
        }
    }
}