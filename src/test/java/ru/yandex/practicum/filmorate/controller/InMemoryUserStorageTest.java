package ru.yandex.practicum.filmorate.controller;

import org.junit.jupiter.api.Test;
import ru.yandex.practicum.filmorate.exception.ValidationException;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.storage.InMemoryUserStorage;

import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

public class InMemoryUserStorageTest  {
    InMemoryUserStorage inMemoryUserStorage = new InMemoryUserStorage();

    @Test
    void getFilmTest() {
        User user = new User();
        user.setName("test1");
        user.setLogin("test2");
        user.setBirthday(LocalDate.of(2000, 12, 27));
        user.setEmail("test@");
        inMemoryUserStorage.create(user);
        user.setEmail("  ");
        assertThrows(ValidationException.class, () -> inMemoryUserStorage.update(user));
        user.setEmail("test2@");
        user.setLogin("TEST ETS");
        assertThrows(ValidationException.class, () -> inMemoryUserStorage.update(user));
        user.setLogin("test2");
        user.setName(" ");
        inMemoryUserStorage.update(user);
        assertEquals(user.getLogin(), user.getName(), "Имя совпадает с логином");
        user.setBirthday(LocalDate.of(2200, 12, 27));
        assertThrows(ValidationException.class, () -> inMemoryUserStorage.update(user));
    }
}