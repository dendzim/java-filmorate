package ru.yandex.practicum.filmorate.dao;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.storage.UserStorage;

import java.util.Collection;
import java.util.Set;

@Repository("UserDbStorage")
public class UserDbStorage extends BaseDao<User> implements UserStorage {

    private static final String FIND_ALL_USERS_QUERY = "SELECT * FROM PUBLIC.\"Users\"";
    private static final String FIND_USER_BY_ID_QUERY = "SELECT * FROM PUBLIC.\"Users\" WHERE USER_ID = ?";
    private static final String INSERT_QUERY = "INSERT INTO PUBLIC.\"Users\"(NAME, EMAIL, LOGIN, BIRTHDAY)" +
            "VALUES (?, ?, ?, ?)";
    private static final String UPDATE_QUERY = "UPDATE PUBLIC.\"Users\" SET NAME = ?, EMAIL = ?, LOGIN = ?," +
            "BIRTHDAY = ? WHERE USER_ID = ?";
    private static final String DELETE_QUERY = "DELETE FROM PUBLIC.\"Users\" WHERE USER_ID = ?";
    private static final String EXISTS = "SELECT EXISTS(SELECT 1 FROM PUBLIC.\"Users\" WHERE USER_ID = ?)";
    private static final String ADD_FRIEND_QUERY = "INSERT INTO PUBLIC.\"User_friends\"(USER_ID, FRIEND_ID) " +
            "VALUES (?, ?)";
    private static final String DELETE_FRIEND_QUERY = "DELETE FROM PUBLIC.\"User_friends\" WHERE USER_ID = ? " +
            "AND FRIEND_ID = ?";
    private static final String FIND_ALL_FRIENDS_QUERY = "SELECT FRIEND_ID FROM PUBLIC.\"User_friends\" " +
            "WHERE USER_ID = ?";
    private static final String FIND_COMMON_FRIENDS_QUERY = "SELECT T1.FRIEND_ID FROM PUBLIC.\"User_friends\" AS T1 " +
            "JOIN PUBLIC.\"User_friends\" AS T2 ON T1.FRIEND_ID = T2.FRIEND_ID WHERE T1.USER_ID = ? AND T2.USER_ID = ?";

    public UserDbStorage(JdbcTemplate jdbc, RowMapper<User> mapper) {
        super(jdbc, mapper);
    }

    @Override
    public Collection<User> findAll() {
        return getAll(FIND_ALL_USERS_QUERY);
    }

    @Override
    public User create(User user) {
        int id = insert(INSERT_QUERY,
                user.getName(),
                user.getEmail(),
                user.getLogin(),
                user.getBirthday()
        );
        user.setId(id);
        return user;
    }

    @Override
    public User update(User user) {
        update(UPDATE_QUERY,
                user.getName(),
                user.getEmail(),
                user.getLogin(),
                user.getBirthday(),
                user.getId()
        );
        return user;
    }

    @Override
    public User findUserById(int id) {
        return get(FIND_USER_BY_ID_QUERY);
    }

    @Override
    public void remove(int id) {
        delete(DELETE_QUERY, id);
    }

    @Override
    public void addFriend(int userId, int friendId) {
        jdbc.update(ADD_FRIEND_QUERY, userId, friendId);
    }

    @Override
    public void deleteFriend(int userId, int friendId) {
        jdbc.update(DELETE_FRIEND_QUERY, userId, friendId);
    }

    @Override
    public Set<Integer> getFriendList(int userId) {
        return Set.copyOf(jdbc.queryForList(FIND_ALL_FRIENDS_QUERY, Integer.class, userId));
    }

    @Override
    public Set<Integer> getCommonFriendList(int id, int otherId) {
        return Set.copyOf(jdbc.queryForList(FIND_COMMON_FRIENDS_QUERY, Integer.class, id, otherId));
    }

    @Override
    public boolean contains(Integer id) {
        return jdbc.queryForObject(EXISTS, Boolean.class, id);
    }
}