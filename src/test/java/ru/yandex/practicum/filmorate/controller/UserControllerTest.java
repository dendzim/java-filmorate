package ru.yandex.practicum.filmorate.controller;

import org.junit.jupiter.api.Test;
import ru.yandex.practicum.filmorate.exception.ValidationException;
import ru.yandex.practicum.filmorate.model.User;

import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

public class UserControllerTest  {
    UserController userController = new UserController();

    @Test
    void getFilmTest() {
        User user = new User();
        user.setName("test1");
        user.setLogin("test2");
        user.setBirthday(LocalDate.of(2000, 12, 27));
        user.setEmail("test@");
        userController.create(user);
        user.setEmail("  ");
        assertThrows(ValidationException.class, () -> userController.update(user));
        user.setEmail("test2@");
        user.setLogin("TEST ETS");
        assertThrows(ValidationException.class, () -> userController.update(user));
        user.setLogin("test2");
        user.setName(" ");
        userController.update(user);
        assertEquals(user.getLogin(), user.getName(), "Имя совпадает с логином");
        user.setBirthday(LocalDate.of(2200, 12, 27));
        assertThrows(ValidationException.class, () -> userController.update(user));
    }
}