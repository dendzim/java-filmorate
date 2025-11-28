package ru.yandex.practicum.filmorate.dao;

import org.springframework.stereotype.Repository;
import ru.yandex.practicum.filmorate.model.Rating;
import ru.yandex.practicum.filmorate.storage.RatingStorage;

import java.util.Collection;
import java.util.List;

@Repository("RatingDbStorage")
public class RatingDbStorage implements RatingStorage {
    @Override
    public Collection<Rating> findAll() {
        return List.of();
    }

    @Override
    public Rating findGenreById(int id) {
        return null;
    }

    @Override
    public boolean contains(Integer id) {
        return false;
    }
}
