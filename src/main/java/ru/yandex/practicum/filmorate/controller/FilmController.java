package ru.yandex.practicum.filmorate.controller;

import jakarta.validation.Valid;
import jakarta.validation.constraints.Positive;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.service.FilmService;

import java.util.Collection;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/films")
public class FilmController {

    private final FilmService service;

    @GetMapping
    public Collection<Film> findAll() {
        log.info("Список фильмов выведен");
        return service.findAll();
    }

    @GetMapping("/{id}")
    public Film findById(@PathVariable @Positive int id) {
        log.info("Фильм с id: {} выведен", id);
        return service.findFilmById(id);
    }

    @GetMapping("/popular")
    public Collection<Film> getPopular(@RequestParam(defaultValue = "10") @Positive int count) {
        log.info("Список популярных фильмов выведен");
        return service.getPopular(count);
    }

    @PostMapping
    public Film create(@Valid @RequestBody Film film) {
        log.info("Фильм: {} добавлен в базу", film);
        return service.create(film);
    }

    @PutMapping
    public Film update(@Valid @RequestBody Film newFilm) {
        log.info("Данные о фильме: {} обновлены", newFilm);
        return service.update(newFilm);
    }

    @PutMapping("/{id}/like/{userId}")
    public Film addLike(@Positive @PathVariable int id, @Positive @PathVariable int userId) {
        log.info("Фильму с id: {} поставил лайк пользователь с id: {}", id, userId);
        return service.addLike(id, userId);
    }

    @DeleteMapping("/{id}/like/{userId}")
    public Film deleteLike(@Positive @PathVariable int id, @Positive @PathVariable int userId) {
        log.info("У фильма с id: {} убрал лайк пользователь с id: {}", id, userId);
        return service.deleteLike(id, userId);
    }

    @DeleteMapping("/{id}")
    public void remove(@PathVariable @Positive int id) {
        log.info("Фильм с id: {} удален", id);
        service.remove(id);
    }
}