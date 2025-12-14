package ru.yandex.practicum.filmorate.storage;

import ru.yandex.practicum.filmorate.model.User;

import java.util.Collection;

public interface UserStorage {

    Collection<User> findAll();

    User create(User user);

    User update(User newUser);

    User findUserById(int id);

    void remove(int id);

    void addFriend(int userId, int friendId);

    void deleteFriend(int userId, int friendId);

    Collection<User> getFriendList(int userId);

    Collection<User> getCommonFriendList(int id, int otherId);

    boolean contains(Integer id);
}