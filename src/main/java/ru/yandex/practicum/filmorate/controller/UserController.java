package ru.yandex.practicum.filmorate.controller;

import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.exception.ValidationException;
import ru.yandex.practicum.filmorate.model.User;

import java.time.LocalDate;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/users")
public class UserController {

    private final Map<Integer, User> users = new HashMap<>();

    @GetMapping
    public Collection<User> findAll() {
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
        user.setId(getNextId());
        userValidation(user);
        users.put(user.getId(), user);
        return user;
    }

    @PutMapping
    public User update(@RequestBody User newUser) {
        if (newUser.getId() == null) {
            throw new ValidationException("Id должен быть указан");
        }
        if (users.containsKey(newUser.getId())) {
            User oldUser = users.get(newUser.getId());
            userValidation(newUser);
            for (User usr : users.values()) {
                if (usr.getEmail().equals(newUser.getEmail())) {
                    throw new ValidationException("Этот имейл уже используется");
                }
            }
            oldUser.setEmail(newUser.getEmail());
            oldUser.setName(newUser.getName());
            oldUser.setLogin(newUser.getLogin());
            oldUser.setBirthday(newUser.getBirthday());
            return oldUser;
        }
        throw new ValidationException("Пользователь с id = " + newUser.getId() + " не найден");
    }

    private void userValidation(User user) {
        if (!user.getEmail().contains("@")) {
            throw new ValidationException("Неверная почта");
        }
        if (user.getBirthday().isAfter(LocalDate.now())) {
            throw new ValidationException("Неверная дата рождения");
        }
        if (!user.getLogin().contains(" ")) {
            throw new ValidationException("Неправильный формат логина");
        }
        if (user.getName().isBlank()) {
            user.setName(user.getLogin());
        }
    }
}