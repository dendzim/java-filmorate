package ru.yandex.practicum.filmorate.dao;

import org.springframework.stereotype.Repository;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.Genre;
import ru.yandex.practicum.filmorate.storage.GenreStorage;

import java.util.Collection;
import java.util.List;

@Repository("GenreDbStorage")
public class GenreDbStorage implements GenreStorage {
    @Override
    public Collection<Genre> findAll() {
        return List.of();
    }

    @Override
    public Genre findGenreById(int id) {
        return null;
    }

    @Override
    public void addGenresToFilm(Film film) {

    }

    @Override
    public void updateFilmGenres(Film film) {

    }

    @Override
    public boolean contains(Integer id) {
        return false;
    }
}
