package ru.yandex.practicum.filmorate.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.exception.ValidationException;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.service.UserService;
import ru.yandex.practicum.filmorate.storage.InMemoryUserStorage;

import java.time.LocalDate;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

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
        log.info("Список фильмов выведен");
        return inMemoryUserStorage.findAll();
    }

    @GetMapping("/{id}")
    public User findUserById(@PathVariable("id") int userId) {
        return inMemoryUserStorage.findUserById(userId);
    }

    @GetMapping("/{id}/friends")
    public Collection<User> findAllFriends(@PathVariable("id") int userId) {
        return userService.getFriendList(userId);
    }

    @GetMapping("/{id}/friends/common/{otherId}")
    public Collection<User> findAllCommonFriends(@PathVariable int id, @PathVariable int otherId) {
        return userService.getCommonFriendList(id, otherId);
    }

    @PostMapping
    public User create(@RequestBody User user) {
        return inMemoryUserStorage.create(user);
    }

    @PutMapping
    public User update(@RequestBody User newUser) {
        return inMemoryUserStorage.update(newUser);
    }

    @PutMapping("/{id}/friends/{friendId}")
    public void addFriend(@PathVariable("id") int userId, @PathVariable int friendId) {
        userService.addFriend(userId, friendId);
    }

    @DeleteMapping("/{id}/friends/{friendId}")
    public void deleteFriend(@PathVariable("id") int userId, @PathVariable int friendId) {
        userService.deleteFriend(userId, friendId);
    }
}