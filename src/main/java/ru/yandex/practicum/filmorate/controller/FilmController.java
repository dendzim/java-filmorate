package ru.yandex.practicum.filmorate.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.service.FilmService;

import java.util.Collection;

@Slf4j
@RestController
@RequestMapping("/films")
public class FilmController {

    private final FilmService service;

    @Autowired
    public FilmController(FilmService service) {
        this.service = service;
    }

    @GetMapping
    public Collection<Film> findAll() {
        log.info("Список фильмов выведен");
        return service.findAll();
    }

    @GetMapping("/{id}")
    public Film findById(@PathVariable("id") int filmId) {
        log.info("Фильм с id: {} выведен", filmId);
        return service.findFilmById(filmId);
    }

    @GetMapping("/popular")
    public Collection<Film> getPopular(@RequestParam(defaultValue = "10") int count) {
        log.info("Список популярных фильмов выведен");
        return service.getPopular(count);
    }

    @PostMapping
    public Film create(@RequestBody Film film) {
        log.info("Фильм: {} добавлен в базу", film);
        return service.create(film);
    }

    @PutMapping
    public Film update(@RequestBody Film newFilm) {
        log.info("Данные о фильме: {} обновлены", newFilm);
        return service.update(newFilm);
    }

    @PutMapping("/{id}/like/{userId}")
    public Film addLike(@PathVariable int id, @PathVariable int userId) {
        log.info("Фильму с id: {} поставил лайк пользователь с id: {}", id, userId);
        return service.addLike(id, userId);
    }

    @DeleteMapping("/{id}/like/{userId}")
    public Film deleteLike(@PathVariable int id, @PathVariable int userId) {
        log.info("У фильма с id: {} убрал лайк пользователь с id: {}", id, userId);
        return service.deleteLike(id, userId);
    }
}