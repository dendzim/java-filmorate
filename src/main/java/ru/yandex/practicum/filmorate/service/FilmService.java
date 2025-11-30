package ru.yandex.practicum.filmorate.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.exception.NotFoundException;
import ru.yandex.practicum.filmorate.exception.ValidationException;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.storage.FilmStorage;
import ru.yandex.practicum.filmorate.storage.GenreStorage;
import ru.yandex.practicum.filmorate.storage.UserStorage;

import java.time.LocalDate;
import java.util.Collection;
import java.util.Comparator;
import java.util.stream.Collectors;

@Slf4j
@Service
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
            log.warn("Не указан id");
            throw new ValidationException("Id должен быть указан");
        }
        validateFilm(newFilm);
        Film oldFilm = filmStorage.update(newFilm);
        if (oldFilm.getGenres() != null && !oldFilm.getGenres().isEmpty()) {
            genreStorage.updateFilmGenres(oldFilm);
        }
        return oldFilm;
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

    public void addLike(int id, int userId) {
        Film film = filmStorage.findFilmById(id);
        if (!userStorage.contains(userId)) {
            throw new NotFoundException("Пользователь с id = " + userId + " не найден");
        }
        filmStorage.addLike(id, userId);
    }

    public void deleteLike(int id, int userId) {
        Film film = filmStorage.findFilmById(id);
        userStorage.findUserById(userId);
        if (!userStorage.contains(userId)) {
            throw new NotFoundException("Пользователь с id = " + userId + " не найден");
        }
        filmStorage.deleteLike(id, userId);
    }

    public Collection<Film> getPopular(int count) {
        return filmStorage.findAll().stream()
                .sorted(comparator)
                .limit(count)
                .collect(Collectors.toList());
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

    public static final Comparator<Film> comparator = Comparator.comparingInt(Film::getAllLikes).reversed();
}