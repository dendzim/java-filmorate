package ru.yandex.practicum.filmorate.dao;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.stereotype.Repository;
import ru.yandex.practicum.filmorate.dao.mappers.UserRowMapper;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.storage.UserStorage;

import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.Set;

@Repository("UserDbStorage")
public class UserDbStorage extends BaseDao<User> implements UserStorage {

    private static final String FIND_ALL_USERS_QUERY = "SELECT * FROM users";

    private static final String FIND_USER_BY_ID_QUERY = "SELECT * FROM users WHERE user_id = ?";

    private static final String INSERT_QUERY = """
            INSERT INTO users (name, email, login, birthday)
            VALUES (?, ?, ?, ?)
            """;

    private static final String UPDATE_QUERY = """
            UPDATE users SET
            name = ?,
            email = ?,
            login = ?,
            birthday = ?
            WHERE user_id = ?
            """;

    private static final String DELETE_QUERY = "DELETE FROM users WHERE user_id = ?";

    private static final String EXISTS = "SELECT EXISTS(SELECT 1 FROM users WHERE user_id = ?)";

    private static final String ADD_FRIEND_QUERY = """
            INSERT INTO user_friends (user_id, friend_id)
            VALUES (?, ?)
            """;

    private static final String DELETE_FRIEND_QUERY = "DELETE FROM user_friends WHERE user_id = ? AND friend_id = ?";

    private static final String FIND_ALL_FRIENDS_QUERY = "SELECT friend_id FROM user_friends WHERE user_id = ?";

    private static final String FIND_COMMON_FRIENDS_QUERY = """
            SELECT T1.friend_id
            FROM user_friends AS T1
            JOIN user_friends AS T2 ON T1.friend_id = T2.friend_id
            WHERE T1.user_id = ? AND T2.user_id = ?
            """;

    private static final String SELECT_ALL_FROM_COLLECTION = "SELECT * FROM users WHERE user_id IN (:set)";

    protected final NamedParameterJdbcTemplate namedJdbc;

    public UserDbStorage(JdbcTemplate jdbc, UserRowMapper mapper, NamedParameterJdbcTemplate namedJdbc) {
        super(jdbc, mapper);

        this.namedJdbc = namedJdbc;
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
        return get(FIND_USER_BY_ID_QUERY, id);
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
    public List<User> getFriendList(int userId) {
        Set<Integer> list = Set.copyOf(jdbc.queryForList(FIND_ALL_FRIENDS_QUERY, Integer.class, userId));
         if (list.isEmpty()) {
             return Collections.emptyList();
         }
        return namedJdbc.query(SELECT_ALL_FROM_COLLECTION, Collections.singletonMap("set", list), mapper);
    }

    @Override
    public List<User> getCommonFriendList(int id, int otherId) {
        Set<Integer> list = Set.copyOf(jdbc.queryForList(FIND_COMMON_FRIENDS_QUERY, Integer.class, id, otherId));
        if (list.isEmpty()) {
            return Collections.emptyList();
        }
        return namedJdbc.query(SELECT_ALL_FROM_COLLECTION, Collections.singletonMap("set", list), mapper);
    }

    @Override
    public boolean contains(Integer id) {
        return jdbc.queryForObject(EXISTS, Boolean.class, id);
    }
}