package ru.yandex.practicum.filmorate.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.service.UserService;
import ru.yandex.practicum.filmorate.storage.InMemoryUserStorage;

import java.util.Collection;

@Slf4j
@RestController
@RequestMapping("/users")
public class UserController {

    private final InMemoryUserStorage inMemoryUserStorage;
    private final UserService userService;

    public UserController(InMemoryUserStorage inMemoryUserStorage, UserService userService) {
        this.inMemoryUserStorage = inMemoryUserStorage;
        this.userService = userService;
    }

    @GetMapping
    public Collection<User> findAll() {
        log.info("Список пользователей выведен");
        return inMemoryUserStorage.findAll();
    }

    @GetMapping("/{id}")
    public User findUserById(@PathVariable("id") int userId) {
        log.info("Пользователь с id: {} выведен", userId);
        return inMemoryUserStorage.findUserById(userId);
    }

    @GetMapping("/{id}/friends")
    public Collection<User> findAllFriends(@PathVariable("id") int userId) {
        log.info("Список друзей пользователя с id: {} выведен", userId);
        return userService.getFriendList(userId);
    }

    @GetMapping("/{id}/friends/common/{otherId}")
    public Collection<User> findAllCommonFriends(@PathVariable int id, @PathVariable int otherId) {
        log.info("Список общих друзей пользователей с id: {} и {} выведен", id, otherId);
        return userService.getCommonFriendList(id, otherId);
    }

    @PostMapping
    public User create(@RequestBody User user) {
        log.info("Пользоввтель: {} создан и добавлен", user);
        return inMemoryUserStorage.create(user);
    }

    @PutMapping
    public User update(@RequestBody User newUser) {
        log.info("Данные о пользователе: {} обновлены", newUser);
        return inMemoryUserStorage.update(newUser);
    }

    @PutMapping("/{id}/friends/{friendId}")
    public void addFriend(@PathVariable("id") int userId, @PathVariable int friendId) {
        log.info("Пользователь с id: {} добавил пользователя с id: {} в друзья", userId, friendId);
        userService.addFriend(userId, friendId);
    }

    @DeleteMapping("/{id}/friends/{friendId}")
    public void deleteFriend(@PathVariable("id") int userId, @PathVariable int friendId) {
        log.info("Пользователь с id: {} удалил пользователя с id: {} из друзей", userId, friendId);
        userService.deleteFriend(userId, friendId);
    }
}