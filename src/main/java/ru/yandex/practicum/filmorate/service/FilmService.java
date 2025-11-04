package ru.yandex.practicum.filmorate.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.exception.ValidationException;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.storage.FilmStorage;
import ru.yandex.practicum.filmorate.storage.UserStorage;

import java.util.Collection;
import java.util.Comparator;
import java.util.stream.Collectors;

@Slf4j
@Service
public class FilmService {

    private final FilmStorage filmStorage;
    private final UserStorage userStorage;

    @Autowired
    public FilmService(@Qualifier("inMemoryFilmStorage") FilmStorage filmStorage,
                       @Qualifier("inMemoryUserStorage") UserStorage userStorage) {
        this.filmStorage = filmStorage;
        this.userStorage = userStorage;
    }

    public void addLike(int id, int userId) {
        Film film = filmStorage.findFilmById(id);
        userStorage.findUserById(userId);
        if (film.getLikes().contains(userId)) {
            log.warn("Ошибка добавления лайка");
            throw new ValidationException("Лайк от этого пользователя уже стоит");
        }
        film.getLikes().add(userId);
    }

    public void deleteLike(int id, int userId) {
        Film film = filmStorage.findFilmById(id);
        userStorage.findUserById(userId);
        if (!film.getLikes().contains(userId)) {
            log.warn("Ошибка удаления лайка");
            throw new ValidationException("У фильма нет лайка от этого пользователя");
        }
        film.getLikes().remove(userId);
    }

    public Collection<Film> getPopular(int count) {
        return filmStorage.findAll().stream()
                .sorted(comparator)
                .limit(count)
                .collect(Collectors.toList());
    }

    public static final Comparator<Film> comparator = Comparator.comparingInt(Film::getRating).reversed();
}