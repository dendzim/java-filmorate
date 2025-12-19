package ru.yandex.practicum.filmorate.db;

import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.jdbc.JdbcTest;
import org.springframework.context.annotation.Import;
import ru.yandex.practicum.filmorate.dao.FilmDbStorage;
import ru.yandex.practicum.filmorate.dao.GenreDbStorage;
import ru.yandex.practicum.filmorate.dao.RatingDbStorage;
import ru.yandex.practicum.filmorate.dao.mappers.FilmRowMapper;
import ru.yandex.practicum.filmorate.dao.mappers.GenreRowMapper;
import ru.yandex.practicum.filmorate.dao.mappers.RatingRowMapper;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.Genre;

import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.assertEquals;

@JdbcTest
@Import({FilmDbStorage.class, FilmRowMapper.class, GenreDbStorage.class, GenreRowMapper.class,
        RatingDbStorage.class, RatingRowMapper.class})
@AutoConfigureTestDatabase
@RequiredArgsConstructor(onConstructor_ = @Autowired)
public class GenreDbStorageTest {
    private final GenreDbStorage genreDbStorage;
    private final FilmDbStorage filmDbStorage;

    @Test
    public void testFindRatingById() {
        Optional<Genre> genre = genreDbStorage.findGenreById(1);
        assertTrue(genre.isPresent(), "Жанр есть");
        assertEquals("Комедия", genre.get().getName());
    }

    @Test
    public void testFindAllRating() {
        List<Genre> genres = genreDbStorage.findAll();
        assertNotNull(genres);
        assertEquals(6, genres.size());
    }

    @Test
    public void testGetEmptyGenre() {
        Optional<Genre> genre = genreDbStorage.findGenreById(100);
        assertThat(genre).isEmpty();
    }

    @Test
    public void testAddGenreToFilm() {
        Film film = filmDbStorage.findFilmById(2);
        Set<Genre> genres = film.getGenres();
        assertEquals(0, genres.size());

        List<Genre> allGenres = genreDbStorage.findAll();
        Set<Genre> filmNewGenre = new HashSet<>();

        filmNewGenre.add(allGenres.get(0));
        filmNewGenre.add(allGenres.get(1));

        film.setGenres(filmNewGenre);
        genreDbStorage.addGenresToFilm(film);

        Film newFilm = filmDbStorage.findFilmById(2);
        Set<Genre> newGenres = newFilm.getGenres();

        assertEquals(2, newGenres.size());
        assertEquals(filmNewGenre, newGenres);
    }
}