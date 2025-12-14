package ru.yandex.practicum.filmorate.storage;

import ru.yandex.practicum.filmorate.model.Film;

import java.util.Collection;

public interface FilmStorage {

    Collection<Film> findAll();

    Film create(Film film);

    Film update(Film newFilm);

    Film findFilmById(int id);

    Film remove(int id);

    Collection<Film> getPopular(int count);

    Integer addLike(int id, int userId);

    Integer deleteLike(int id, int userId);

    boolean contains(Integer id);
}