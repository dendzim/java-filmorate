package ru.yandex.practicum.filmorate.dao;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;

import java.util.Collection;

@Repository
public abstract class BaseDao<T> {
    protected final JdbcTemplate jdbc;
    protected final RowMapper<T> mapper;

    public BaseDao(JdbcTemplate jdbc, RowMapper<T> mapper) {
        this.jdbc = jdbc;
        this.mapper = mapper;
    }

    protected T get(String query, Object... params) {
        return jdbc.queryForObject(query, mapper, params);
    }

    public Collection<T> getAll(String query, Object... params) {
        return jdbc.query(query, mapper, params);
    }

    public void delete(String query, Object... params) {
        jdbc.update(query, params);
    }

    public void update() {

    }
}
