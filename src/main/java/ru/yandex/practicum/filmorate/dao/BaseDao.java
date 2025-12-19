package ru.yandex.practicum.filmorate.dao;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.stereotype.Repository;
import ru.yandex.practicum.filmorate.exception.InternalServerException;

import java.sql.PreparedStatement;
import java.sql.Statement;
import java.util.List;

@Repository
public abstract class BaseDao<T> {
    protected final JdbcTemplate jdbc;
    protected final NamedParameterJdbcTemplate namedJdbc;
    protected final RowMapper<T> mapper;

    public BaseDao(JdbcTemplate jdbc, RowMapper<T> mapper) {
        this.jdbc = jdbc;
        this.namedJdbc = new NamedParameterJdbcTemplate(jdbc);
        this.mapper = mapper;
    }

    protected T get(String query, Object... params) {
        return jdbc.queryForObject(query, mapper, params);
    }

    public List<T> getAll(String query, Object... params) {
        return jdbc.query(query, mapper, params);
    }

    public void delete(String query, Integer id) {
        jdbc.update(query, id);
    }

    public void update(String query, Object...params) {
        int rowsUpdated = jdbc.update(query, params);
        if (rowsUpdated == 0) {
            try {
                throw new InternalServerException("Не удалось обновить данные");
            } catch (InternalServerException e) {
                throw new RuntimeException(e);
            }
        }
    }

    public int insert(String query, Object... params) {
        GeneratedKeyHolder keyHolder = new GeneratedKeyHolder();
        jdbc.update(connection -> {
            PreparedStatement ps = connection
                    .prepareStatement(query, Statement.RETURN_GENERATED_KEYS);
            for (int idx = 0; idx < params.length; idx++) {
                ps.setObject(idx + 1, params[idx]);
            }
            return ps; }, keyHolder);

        Integer id = keyHolder.getKeyAs(Integer.class);

        if (id != null) {
            return id;
        } else {
            try {
                throw new InternalServerException("Не удалось сохранить данные");
            } catch (InternalServerException e) {
                throw new RuntimeException(e);
            }
        }
    }
}