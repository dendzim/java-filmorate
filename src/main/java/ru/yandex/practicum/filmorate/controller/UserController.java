package ru.yandex.practicum.filmorate.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.exception.ValidationException;
import ru.yandex.practicum.filmorate.model.User;

import java.time.LocalDate;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

@Slf4j
@RestController
@RequestMapping("/users")
public class UserController {

    private final Map<Integer, User> users = new HashMap<>();

    @GetMapping
    public Collection<User> findAll() {
        log.info("Список фильмов выведен");
        return users.values();
    }

    private int getNextId() {
        int currentMaxId = users.keySet()
                .stream()
                .mapToInt(id -> id)
                .max()
                .orElse(0);
        return ++currentMaxId;
    }

    @PostMapping
    public User create(@RequestBody User user) {
        userValidation(user);
        user.setId(getNextId());
        users.put(user.getId(), user);
        log.info("Пользователь: {} добавлен в базу", user);
        return user;
    }

    @PutMapping
    public User update(@RequestBody User newUser) {
        if (newUser.getId() == null) {
            log.warn("Не указан id");
            throw new ValidationException("Id должен быть указан");
        }
        if (users.containsKey(newUser.getId())) {
            User oldUser = users.get(newUser.getId());
            userValidation(newUser);
            for (User usr : users.values()) {
                if (usr.getEmail().equals(newUser.getEmail())) {
                    log.warn("Попытка присвоить почту существующего пользователя");
                    throw new ValidationException("Этот Email уже используется");
                }
            }
            oldUser.setEmail(newUser.getEmail());
            oldUser.setName(newUser.getName());
            oldUser.setLogin(newUser.getLogin());
            oldUser.setBirthday(newUser.getBirthday());
            log.info("Данные о пользователе: {} обновлены", oldUser);
            return oldUser;
        }
        log.warn("Пользователь с указанным id не найден");
        throw new ValidationException("Пользователь с id = " + newUser.getId() + " не найден");
    }

    private void userValidation(User user) {
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