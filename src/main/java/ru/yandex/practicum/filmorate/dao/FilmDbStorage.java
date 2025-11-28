package ru.yandex.practicum.filmorate.dao;

import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.storage.FilmStorage;

import java.util.Collection;
import java.util.List;

public class FilmDbStorage implements FilmStorage {
    @Override
    public Collection<Film> findAll() {
        return List.of();
    }

    @Override
    public Film create(Film film) {
        return null;
    }

    @Override
    public Film update(Film newFilm) {
        return null;
    }

    @Override
    public Film findFilmById(int id) {
        return null;
    }

    @Override
    public void remove(int id) {

    }

    @Override
    public Collection<Film> getPopular(int count) {
        return List.of();
    }

    @Override
    public void addLike(int id, int userId) {

    }

    @Override
    public void deleteLike(int id, int userId) {

    }

    @Override
    public boolean contains(Integer id) {
        return false;
    }
}
