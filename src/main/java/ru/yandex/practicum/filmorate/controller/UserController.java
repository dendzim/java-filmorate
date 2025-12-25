package ru.yandex.practicum.filmorate.controller;

import jakarta.validation.Valid;
import jakarta.validation.constraints.Positive;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.service.UserService;

import java.util.Collection;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/users")
public class UserController {

    private final UserService service;

    @GetMapping
    public Collection<User> findAll() {
        log.info("Список пользователей выведен");
        return service.findAll();
    }

    @GetMapping("/{id}")
    public User findUserById(@Positive @PathVariable int id) {
        log.info("Пользователь с id: {} выведен", id);
        return service.findUserById(id);
    }

    @GetMapping("/{id}/friends")
    public Collection<User> findAllFriends(@Positive @PathVariable("id") int userId) {
        log.info("Список друзей пользователя с id: {} выведен", userId);
        return service.getFriendList(userId);
    }

    @GetMapping("/{id}/friends/common/{otherId}")
    public Collection<User> findAllCommonFriends(@Positive @PathVariable int id, @Positive @PathVariable int otherId) {
        log.info("Список общих друзей пользователей с id: {} и {} выведен", id, otherId);
        return service.getCommonFriendList(id, otherId);
    }

    @PostMapping
    public User create(@Valid @RequestBody User user) {
        log.info("Пользоввтель: {} создан и добавлен", user);
        return service.create(user);
    }

    @PutMapping
    public User update(@Valid @RequestBody User newUser) {
        log.info("Данные о пользователе: {} обновлены", newUser);
        return service.update(newUser);
    }

    @PutMapping("/{id}/friends/{friendId}")
    public void addFriend(@PathVariable("id") int userId, @PathVariable int friendId) {
        log.info("Пользователь с id: {} добавил пользователя с id: {} в друзья", userId, friendId);
        service.addFriend(userId, friendId);
    }

    @DeleteMapping("/{id}/friends/{friendId}")
    public void deleteFriend(@PathVariable("id") int userId, @PathVariable int friendId) {
        log.info("Пользователь с id: {} удалил пользователя с id: {} из друзей", userId, friendId);
        service.deleteFriend(userId, friendId);
    }

    @DeleteMapping("/{id}")
    public void remove(@Positive @PathVariable int id) {
        log.info("Пользователь с id: {} удален", id);
        service.remove(id);
    }
}