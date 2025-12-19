package ru.yandex.practicum.filmorate.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.yandex.practicum.filmorate.exception.NotFoundException;
import ru.yandex.practicum.filmorate.exception.ValidationException;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.storage.UserStorage;

import java.time.LocalDate;
import java.util.Collection;

@Slf4j
@Service
@Transactional
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
        if (!userStorage.contains(newUser.getId())) {
            throw new NotFoundException("Пользователь с ID " + newUser.getId() + " не найден");
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
        if (!userStorage.contains(userId)) {
            throw new NotFoundException("Пользователь с id = " + userId + " не найден");
        }
        if (!userStorage.contains(friendId)) {
            throw new NotFoundException("Пользователь с id = " + friendId + " не найден");
        }
        userStorage.addFriend(userId, friendId);
    }

    public void deleteFriend(int userId, int friendId) {
        if (userId == friendId) {
            log.warn("Ошибка удаления из друзей");
            throw new ValidationException("Пользователь не может удалить сам себя");
        }
        if (!userStorage.contains(userId)) {
            throw new NotFoundException("Пользователь с id = " + userId + " не найден");
        }
        if (!userStorage.contains(friendId)) {
            throw new NotFoundException("Пользователь с id = " + friendId + " не найден");
        }
        userStorage.deleteFriend(userId, friendId);
    }

    public Collection<User> getFriendList(int userId) {
        if (!userStorage.contains(userId)) {
            throw new NotFoundException("Пользователь с id = " + userId + " не найден");
        }
        return userStorage.getFriendList(userId);
    }

    public Collection<User> getCommonFriendList(int id, int otherId) {
        if (!userStorage.contains(id)) {
            throw new NotFoundException("Пользователь с id = " + id + " не найден");
        }
        if (!userStorage.contains(otherId)) {
            throw new NotFoundException("Пользователь с id = " + otherId + " не найден");
        }
        return userStorage.getCommonFriendList(id, otherId);
    }
}