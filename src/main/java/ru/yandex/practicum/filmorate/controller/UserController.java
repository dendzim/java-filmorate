package ru.yandex.practicum.filmorate.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.service.UserService;

import java.util.Collection;

@Slf4j
@RestController
@RequestMapping("/users")
public class UserController {

    private final UserService service;

    public UserController(UserService service) {
        this.service = service;
    }

    @GetMapping
    public Collection<User> findAll() {
        log.info("Список пользователей выведен");
        return service.findAll();
    }

    @GetMapping("/{id}")
    public User findUserById(@PathVariable("id") int userId) {
        log.info("Пользователь с id: {} выведен", userId);
        return service.findUserById(userId);
    }

    @GetMapping("/{id}/friends")
    public Collection<User> findAllFriends(@PathVariable("id") int userId) {
        log.info("Список друзей пользователя с id: {} выведен", userId);
        return service.getFriendList(userId);
    }

    @GetMapping("/{id}/friends/common/{otherId}")
    public Collection<User> findAllCommonFriends(@PathVariable int id, @PathVariable int otherId) {
        log.info("Список общих друзей пользователей с id: {} и {} выведен", id, otherId);
        return service.getCommonFriendList(id, otherId);
    }

    @PostMapping
    public User create(@RequestBody User user) {
        log.info("Пользоввтель: {} создан и добавлен", user);
        return service.create(user);
    }

    @PutMapping
    public User update(@RequestBody User newUser) {
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
}