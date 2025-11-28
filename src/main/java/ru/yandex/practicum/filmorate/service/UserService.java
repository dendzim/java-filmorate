package ru.yandex.practicum.filmorate.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.exception.NotFoundException;
import ru.yandex.practicum.filmorate.exception.ValidationException;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.storage.UserStorage;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

@Slf4j
@Service
public class UserService {

    private final UserStorage userStorage;

    public UserService(@Qualifier("UserDbStorage") UserStorage userStorage) {
        this.userStorage = userStorage;
    }

    public Collection<User> findAll() {
        return userStorage.findAll();
    }

    public User create(User user) {
        validateUser(user);
        user = userStorage.create(user);
        return user;
    }

    public User update(User newUser) {
        if (newUser.getId() == null) {
            log.warn("Не указан id");
            throw new ValidationException("Id должен быть указан");
        }
        validateUser(newUser);
        return userStorage.update(newUser);
    }

    public User findUserById(int userId) {
        if (!userStorage.contains(userId)) {
            log.warn("Пользователь не найден");
            throw new NotFoundException("Пользователя с таким id не найдено");
        }
        return userStorage.findUserById(userId);
    }

    public void remove(int id) {
        if (!userStorage.contains(id)) {
            log.warn("Пользователь не найден");
            throw new NotFoundException("Пользователя с таким id не найдено");
        }
        userStorage.remove(id);
    }

    private void validateUser(User user) {
        if (!user.getEmail().contains("@")) {
            log.warn("Ошибка в формате почты");
            throw new ValidationException("Неверная почта");
        }
        if (user.getBirthday().isAfter(LocalDate.now())) {
            log.warn("Ошибка в дате рождения");
            throw new ValidationException("Неверная дата рождения");
        }
        if (user.getLogin().contains(" ")) {
            log.warn("Ошибка в формате логина");
            throw new ValidationException("Неправильный формат логина");
        }
        if (user.getName() == null || user.getName().trim().isEmpty()) {
            log.info("Пустое имя пользователя заменено на логин");
            user.setName(user.getLogin());
        }
    }

    public void addFriend(int userId, int friendId) {
        if (userId == friendId) {
            log.warn("Ошибка добавления в друзья");
            throw new ValidationException("Пользователь не может добавить сам себя в друзья");
        }
        User user = userStorage.findUserById(userId);
        User usersFriend = userStorage.findUserById(friendId);
        if (user.getFriends().contains(friendId)) {
            log.warn("Ошибка добавления в друзья");
            throw new ValidationException("Пользователь уже добавлен в друзья");
        }
        user.getFriends().add(friendId);
        usersFriend.getFriends().add(userId);
    }

    public void deleteFriend(int userId, int friendId) {
        if (userId == friendId) {
            log.warn("Ошибка удаления из друзей");
            throw new ValidationException("Пользователь не может удалить сам себя");
        }
        User user = userStorage.findUserById(userId);
        User usersFriend = userStorage.findUserById(friendId);
        user.getFriends().remove(friendId);
        usersFriend.getFriends().remove(userId);
    }

    public Collection<User> getFriendList(int userId) {
        User user = userStorage.findUserById(userId);
        List<User> list = new ArrayList<>();
        for (Integer id : user.getFriends()) {
            list.add(userStorage.findUserById(id));
        }
        return list;
    }

    public Collection<User> getCommonFriendList(int id, int otherId) {
        User user1 = userStorage.findUserById(id);
        User user2 = userStorage.findUserById(otherId);
        List<User> list = new ArrayList<>();
        for (Integer userid : user1.getFriends()) {
            if (user2.getFriends().contains(userid)) {
                list.add(userStorage.findUserById(userid));
            }
        }
        return list;
    }
}