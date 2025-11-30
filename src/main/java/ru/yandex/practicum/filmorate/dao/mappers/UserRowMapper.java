package ru.yandex.practicum.filmorate.dao.mappers;

import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.model.User;

import java.sql.ResultSet;
import java.sql.SQLException;

@Component
public class UserRowMapper implements RowMapper<User> {

    @Override
    public User mapRow(ResultSet resultSet, int rowNum) throws SQLException {
        User user = new User();
        user.setId(resultSet.getInt("USER_ID"));
        user.setName(resultSet.getString("NAME"));
        user.setLogin(resultSet.getString("LOGIN"));
        user.setEmail(resultSet.getString("EMAIL"));
        user.setBirthday(resultSet.getDate("BIRTHDAY").toLocalDate());

        return user;
    }
}