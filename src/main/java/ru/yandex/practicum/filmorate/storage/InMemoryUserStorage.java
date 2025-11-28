package ru.yandex.practicum.filmorate.storage;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.exception.NotFoundException;
import ru.yandex.practicum.filmorate.exception.ValidationException;
import ru.yandex.practicum.filmorate.model.User;

import java.time.LocalDate;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Slf4j
@Component("inMemoryUserStorage")
public class InMemoryUserStorage implements UserStorage {

    private final Map<Integer, User> users = new HashMap<>();

    @Override
    public Collection<User> findAll() {
        return users.values();
    }

    @Override
    public User create(User user) {
        validateUser(user);
        user.setId(getNextId());
        users.put(user.getId(), user);
        return user;
    }

    @Override
    public User update(User newUser) {
        if (newUser.getId() == null) {
            log.warn("Не указан id");
            throw new ValidationException("Id должен быть указан");
        }
        if (!users.containsKey(newUser.getId())) {
            log.warn("Пользователь с указанным id не найден");
            throw new NotFoundException("Пользователь с id = " + newUser.getId() + " не найден");
        }
        validateUser(newUser);
        User oldUser = users.get(newUser.getId());
        oldUser.setEmail(newUser.getEmail());
        oldUser.setName(newUser.getName());
        oldUser.setLogin(newUser.getLogin());
        oldUser.setBirthday(newUser.getBirthday());
        return oldUser;
    }

    private int getNextId() {
        int currentMaxId = users.keySet()
                .stream()
                .mapToInt(id -> id)
                .max()
                .orElse(0);
        return ++currentMaxId;
    }

    public User findUserById(int userId) {
        if (!users.containsKey(userId)) {
            log.warn("Пользователь не найден");
            throw new NotFoundException("Пользователя с таким id не найдено");
        }
        return users.get(userId);
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
}