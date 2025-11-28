package ru.yandex.practicum.filmorate.storage;

import ru.yandex.practicum.filmorate.model.Film;

import java.util.Collection;

public interface FilmStorage {

    Collection<Film> findAll();

    Film create(Film film);

    Film update(Film newFilm);

    Film findFilmById(int id);

    void remove(int id);

    Collection<Film> getPopular(int count);

    void addLike(int id, int userId);

    void deleteLike(int id, int userId);

    boolean contains(Integer id);
}