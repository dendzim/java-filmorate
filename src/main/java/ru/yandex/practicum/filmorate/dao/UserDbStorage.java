package ru.yandex.practicum.filmorate.dao;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.storage.UserStorage;

import java.util.Collection;
import java.util.List;

public class UserDbStorage extends BaseDao<User> implements UserStorage {

    private static final String FIND_ALL_QUERY = "SELECT * FROM PUBLIC.\"Users\"";
    private static final String FIND_BY_EMAIL_QUERY = "SELECT * FROM PUBLIC.\"Users\" WHERE USER_ID = ?";
    private static final String INSERT_QUERY = "INSERT INTO users(username, email, password, registration_date)" +
            "VALUES (?, ?, ?, ?) returning id";
    public UserDbStorage(JdbcTemplate jdbc, RowMapper<User> mapper) {
        super(jdbc, mapper);
    }

    @Override
    public Collection<User> findAll() {
        return List.of();
    }

    @Override
    public User create(User user) {
        return null;
    }

    @Override
    public User update(User newUser) {
        return null;
    }

    @Override
    public User findUserById(int id) {
        return null;
    }

    @Override
    public void remove(int id) {

    }

    @Override
    public void addFriend(int userId, int friendId) {

    }

    @Override
    public void deleteFriend(int userId, int friendId) {

    }

    @Override
    public Collection<User> getFriendList(int userId) {
        return List.of();
    }

    @Override
    public Collection<User> getCommonFriendList(int id, int otherId) {
        return List.of();
    }

    @Override
    public boolean contains(Integer id) {
        return false;
    }
}
