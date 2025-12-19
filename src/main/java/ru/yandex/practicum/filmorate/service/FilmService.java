package ru.yandex.practicum.filmorate.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.yandex.practicum.filmorate.exception.NotFoundException;
import ru.yandex.practicum.filmorate.exception.ValidationException;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.storage.FilmStorage;
import ru.yandex.practicum.filmorate.storage.GenreStorage;
import ru.yandex.practicum.filmorate.storage.UserStorage;

import java.time.LocalDate;
import java.util.Collection;

@Slf4j
@Service
@Transactional
public class FilmService {

    private final FilmStorage filmStorage;
    private final UserStorage userStorage;
    private final GenreStorage genreStorage;

    @Autowired
    public FilmService(@Qualifier("FilmDbStorage") FilmStorage filmStorage,
                       @Qualifier("UserDbStorage") UserStorage userStorage,
                       @Qualifier("GenreDbStorage") GenreStorage genreStorage) {
        this.filmStorage = filmStorage;
        this.userStorage = userStorage;
        this.genreStorage = genreStorage;
    }

    public Collection<Film> findAll() {
        return filmStorage.findAll();
    }

    public Film create(Film film) {
        validateFilm(film);
        film = filmStorage.create(film);
        if (film.getGenres() != null && !film.getGenres().isEmpty()) {
            genreStorage.addGenresToFilm(film);
        }
        return film;
    }

    public Film update(Film newFilm) {
        if (newFilm.getId() == null) {
            throw new NotFoundException("Фильм не найден");
        }
        validateFilm(newFilm);
        return filmStorage.update(newFilm);
    }

    public Film findFilmById(int id) {
        if (!filmStorage.contains(id)) {
            throw new NotFoundException("Фильм с id = " + id + " не найден");
        }
        return filmStorage.findFilmById(id);
    }

    public void remove(int id) {
        if (!filmStorage.contains(id)) {
            throw new NotFoundException("Фильм с id = " + id + " не найден");
        }
        filmStorage.remove(id);
    }

    public Film addLike(int id, int userId) {
        if (!filmStorage.contains(id)) {
            throw new NotFoundException("Фильм с id = " + id + " не найден");
        }
        if (!userStorage.contains(userId)) {
            throw new NotFoundException("Пользователь с id = " + userId + " не найден");
        }
        Integer likes = filmStorage.addLike(id, userId);
        Film film = filmStorage.findFilmById(id);
        film.setLikes(likes);
        return film;
    }

    public Film deleteLike(int id, int userId) {
        if (!filmStorage.contains(id)) {
            throw new NotFoundException("Фильм с id = " + id + " не найден");
        }
        if (!userStorage.contains(userId)) {
            throw new NotFoundException("Пользователь с id = " + userId + " не найден");
        }
        Integer likes = filmStorage.deleteLike(id, userId);
        Film film = filmStorage.findFilmById(id);
        film.setLikes(likes);
        return film;
    }

    public Collection<Film> getPopular(int count) {
        return filmStorage.getPopular(count);
    }

    private void validateFilm(Film film) {
        if (film.getDescription() != null && film.getDescription().length() > 200) {
            log.warn("Ошибка лимита");
            throw new ValidationException("Описание превышает 200 символов");
        }
        if (film.getReleaseDate().isBefore(LocalDate.of(1895, 12, 28))) {
            log.warn("Ошибка даты");
            throw new ValidationException("Неверная дата релиза");
        }

        if (film.getName() == null || film.getName().isBlank()) {
            log.warn("Пустое название фильма");
            throw new ValidationException("Название не может быть пустым");
        }

        if (film.getDuration() < 1) {
            log.warn("Ошибка длительности");
            throw new ValidationException("Длительность не может быть меньше 1");
        }
    }
}