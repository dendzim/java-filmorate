package ru.yandex.practicum.filmorate.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.service.FilmService;
import ru.yandex.practicum.filmorate.storage.FilmStorage;

import java.util.Collection;

@Slf4j
@RestController
@RequestMapping("/films")
public class FilmController {

    private final FilmStorage inMemoryFilmStorage;
    private final FilmService filmService;

    @Autowired
    public FilmController(FilmStorage inMemoryFilmStorage, FilmService filmService) {
        this.inMemoryFilmStorage = inMemoryFilmStorage;
        this.filmService = filmService;
    }

    @GetMapping
    public Collection<Film> findAll() {
        log.info("Список фильмов выведен");
        return inMemoryFilmStorage.findAll();
    }

    @GetMapping("/{id}")
    public Film findById(@PathVariable("id") int filmId) {
        log.info("Фильм с id: {} выведен", filmId);
        return inMemoryFilmStorage.findFilmById(filmId);
    }

    @GetMapping("/popular")
    public Collection<Film> getPopular(@RequestParam(defaultValue = "10") int count) {
        log.info("Список популярных фильмов выведен");
        return filmService.getPopular(count);
    }

    @PostMapping
    public Film create(@RequestBody Film film) {
        log.info("Фильм: {} добавлен в базу", film);
        return inMemoryFilmStorage.create(film);
    }

    @PutMapping
    public Film update(@RequestBody Film newFilm) {
        log.info("Данные о фильме: {} обновлены", newFilm);
        return inMemoryFilmStorage.update(newFilm);
    }

    @PutMapping("/{id}/like/{userId}")
    public void addLike(@PathVariable int id, @PathVariable int userId) {
        log.info("Фильму с id: {} поставил лайк пользователь с id: {}", id, userId);
        filmService.addLike(id, userId);
    }

    @DeleteMapping("/{id}/like/{userId}")
    public void deleteLike(@PathVariable int id, @PathVariable int userId) {
        log.info("У фильма с id: {} убрал лайк пользователь с id: {}", id, userId);
        filmService.deleteLike(id, userId);
    }
}