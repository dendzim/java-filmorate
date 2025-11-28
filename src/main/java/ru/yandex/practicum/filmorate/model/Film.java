package ru.yandex.practicum.filmorate.model;

import lombok.Data;

import java.time.LocalDate;
import java.util.HashSet;
import java.util.Set;

/**
 * Film.
 */
@Data
public class Film {
    private Integer id;
    private String name;
    private String description;
    private LocalDate releaseDate;
    private int duration;
    private Rating mpa;
    private Set<Integer> likes = new HashSet<>();
    private Set<Integer> genres = new HashSet<>();


    public int getRating() {
        return likes.size();
    }
}