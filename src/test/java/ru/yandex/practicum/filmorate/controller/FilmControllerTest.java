package ru.yandex.practicum.filmorate.controller;

import jakarta.validation.ValidationException;
import org.junit.jupiter.api.Test;
import ru.yandex.practicum.filmorate.model.Film;
import static org.junit.jupiter.api.Assertions.*;

import java.time.LocalDate;

public class FilmControllerTest {

    FilmController filmController = new FilmController();

    @Test
    void getFilmTest() {
        Film film = new Film();
        film.setReleaseDate(LocalDate.now());
        film.setDuration(100);
        film.setName("test");
        film.setDescription("t");
        filmController.create(film);
        film.setName("  ");
        assertThrows(ValidationException.class, () -> filmController.update(film));
        film.setName("test");
        String description200 = "A".repeat(200);
        film.setDescription(film.getDescription() + description200);
        assertThrows(ValidationException.class, () -> filmController.update(film));
        film.setDescription("t");
        film.setReleaseDate(LocalDate.of(1895, 12, 27));
        assertThrows(ValidationException.class, () -> filmController.update(film));
        film.setReleaseDate(LocalDate.now());
        film.setDuration(0);
        assertThrows(ValidationException.class, () -> filmController.update(film));
    }
}