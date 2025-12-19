package ru.yandex.practicum.filmorate.db;

import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.jdbc.JdbcTest;
import org.springframework.context.annotation.Import;
import org.springframework.dao.EmptyResultDataAccessException;
import ru.yandex.practicum.filmorate.dao.UserDbStorage;
import ru.yandex.practicum.filmorate.dao.mappers.UserRowMapper;
import ru.yandex.practicum.filmorate.model.User;


import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@JdbcTest
@Import({UserDbStorage.class, UserRowMapper.class})
@AutoConfigureTestDatabase
@RequiredArgsConstructor(onConstructor_ = @Autowired)
public class UserDbStorageTest {
    private final UserDbStorage userStorage;

    @Test
    public void testFindUserById() {
        boolean id = userStorage.contains(1);
        assertTrue(id);
        User user = userStorage.findUserById(1);
        assertNotNull(user);
        assertEquals("name1", user.getName());
    }

    @Test
    public void testFindAllUsers() {
        List<User> users = userStorage.findAll();
        assertNotNull(users);
        assertEquals(5, users.size());
    }

    @Test
    public void testGetNotFound() {
        boolean id = userStorage.contains(10);
        assertFalse(id);
        assertThrows(EmptyResultDataAccessException.class,() -> userStorage.findUserById(10));
    }

    @Test
    public void testAddUser() {
        User user = new User();
        user.setName("nam");
        user.setLogin("log");
        user.setEmail("mail");
        User newUser = userStorage.create(user);
        assertEquals(6, newUser.getId());
    }

    @Test
    public void testUpdateUser() {
        User user = userStorage.findUserById(1);
        user.setName("newUsername");
        User newUser = userStorage.update(user);
        assertEquals("newUsername", newUser.getName());
    }

    @Test
    public void testRemoveUser() {
        userStorage.remove(5);
        boolean id = userStorage.contains(6);
        assertFalse(id);
    }

    @Test
    public void testAddFriend() {
        User user = userStorage.findUserById(1);
        Integer id = user.getId();
        User friend = userStorage.findUserById(4);
        Integer friendId = friend.getId();
        List<User> friendList = userStorage.getFriendList(id);
        assertEquals(1, friendList.size());
        userStorage.addFriend(id,friendId);
        friendList = userStorage.getFriendList(id);
        assertEquals(2, friendList.size());
    }

    @Test
    public void testDeleteFriend() {
        User user = userStorage.findUserById(1);
        Integer id = user.getId();
        User friend = userStorage.findUserById(2);
        Integer friendId = friend.getId();
        List<User> friendList = userStorage.getFriendList(id);
        assertEquals(1, friendList.size());
        userStorage.deleteFriend(id,friendId);
        friendList = userStorage.getFriendList(id);
        assertEquals(0, friendList.size());
    }

    @Test
    public void testGetCommonFriends() {
        User user1 = userStorage.findUserById(1);
        Integer id1 = user1.getId();
        User user2 = userStorage.findUserById(4);
        Integer id2 = user2.getId();
        List<User> friendList = userStorage.getCommonFriendList(id1, id2);
        assertEquals(1, friendList.size());
    }
}
