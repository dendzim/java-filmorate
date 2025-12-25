package ru.yandex.practicum.filmorate.db;

import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.jdbc.JdbcTest;
import org.springframework.context.annotation.Import;
import ru.yandex.practicum.filmorate.dao.RatingDbStorage;
import ru.yandex.practicum.filmorate.dao.mappers.RatingRowMapper;
import ru.yandex.practicum.filmorate.model.Rating;

import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;

@JdbcTest
@Import({RatingDbStorage.class, RatingRowMapper.class})
@AutoConfigureTestDatabase
@RequiredArgsConstructor(onConstructor_ = @Autowired)
public class RatingDbStorageTest {

    private final RatingDbStorage ratingDbStorage;

    @Test
    public void testFindRatingById() {
        Optional<Rating> rating = ratingDbStorage.findRatingById(1);
        assertTrue(rating.isPresent(), "Рейтинг есть");
        assertEquals("G", rating.get().getName());
    }

    @Test
    public void testFindAllRating() {
        List<Rating> ratings = ratingDbStorage.findAll();
        assertNotNull(ratings);
        assertEquals(5, ratings.size());
    }

    @Test
    public void testGetEmptyRating() {
        Optional<Rating> rating = ratingDbStorage.findRatingById(100);
        assertThat(rating).isEmpty();
    }
}