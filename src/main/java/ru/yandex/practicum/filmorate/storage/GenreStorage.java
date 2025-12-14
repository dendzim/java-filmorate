package ru.yandex.practicum.filmorate.storage;

import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.Genre;

import java.util.Collection;
import java.util.Optional;

public interface GenreStorage {

    Collection<Genre> findAll();

    Optional<Genre> findGenreById(int id);

    void addGenresToFilm(Film film);

    void updateFilmGenres(Film film);
}
