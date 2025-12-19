package ru.yandex.practicum.filmorate.db;

import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.jdbc.JdbcTest;
import org.springframework.context.annotation.Import;
import org.springframework.dao.EmptyResultDataAccessException;
import ru.yandex.practicum.filmorate.dao.FilmDbStorage;
import ru.yandex.practicum.filmorate.dao.GenreDbStorage;
import ru.yandex.practicum.filmorate.dao.RatingDbStorage;
import ru.yandex.practicum.filmorate.dao.mappers.FilmRowMapper;
import ru.yandex.practicum.filmorate.dao.mappers.GenreRowMapper;
import ru.yandex.practicum.filmorate.dao.mappers.RatingRowMapper;
import ru.yandex.practicum.filmorate.model.Film;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@JdbcTest
@Import({FilmDbStorage.class, FilmRowMapper.class, GenreDbStorage.class, GenreRowMapper.class,
        RatingDbStorage.class, RatingRowMapper.class})
@AutoConfigureTestDatabase
@RequiredArgsConstructor(onConstructor_ = @Autowired)
public class FilmDbStorageTest {
    private final FilmDbStorage filmDbStorage;
    private final RatingDbStorage ratingDbStorage;

    @Test
    public void testFindFilmById() {
        boolean id = filmDbStorage.contains(1);
        assertTrue(id);
        Film film = filmDbStorage.findFilmById(1);
        assertNotNull(film);
        assertEquals("name1", film.getName());
    }

    @Test
    public void testFindAllFilms() {
        List<Film> films = filmDbStorage.findAll();
        assertNotNull(films);
        assertEquals(6, films.size());
    }

    @Test
    public void testGetNotFound() {
        boolean id = filmDbStorage.contains(10);
        assertFalse(id);
        assertThrows(EmptyResultDataAccessException.class,() -> filmDbStorage.findFilmById(10));
    }

    @Test
    public void testAddFilm() {
        Film film = new Film();
        film.setName("nam");
        film.setDescription("Desc");
        film.setDuration(60);
        film.setMpa(ratingDbStorage.findRatingById(1).get());
        Film newFilm = filmDbStorage.create(film);
        assertEquals(7, newFilm.getId());
    }

    @Test
    public void testUpdateFilm() {
        Film film = filmDbStorage.findFilmById(1);
        film.setName("newFilmname");
        Film newFilm = filmDbStorage.update(film);
        assertEquals("newFilmname", newFilm.getName());
    }

    @Test
    public void testRemoveFilm() {
        filmDbStorage.remove(6);
        boolean id = filmDbStorage.contains(6);
        assertFalse(id);
    }

    @Test
    public void testGetPopular() {
        List<Film> popular = filmDbStorage.getPopular(3);
        assertEquals(5, popular.get(0).getLikes());
        assertEquals(3, popular.get(1).getLikes());
    }

    @Test
    public void testAddLikes() {
        Integer likes = filmDbStorage.addLike(1, 4);
        assertEquals(3, likes);
    }

    @Test
    public void testDeleteLikes() {
        Integer likes = filmDbStorage.deleteLike(1, 1);
        assertEquals(1, likes);
    }
}
