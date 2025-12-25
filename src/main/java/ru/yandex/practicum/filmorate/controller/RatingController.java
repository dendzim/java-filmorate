package ru.yandex.practicum.filmorate.controller;

import jakarta.validation.constraints.Positive;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import ru.yandex.practicum.filmorate.model.Rating;
import ru.yandex.practicum.filmorate.service.RatingService;

import java.util.Collection;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/mpa")
public class RatingController {

    private final RatingService service;

    @GetMapping
    public Collection<Rating> findAll() {
        log.info("Список рейтингов выведен");
        return service.findAll();
    }

    @GetMapping("/{id}")
    public Rating findRatingById(@Positive @PathVariable("id") int id) {
        log.info("Рейтинг с id: {} выведен", id);
        return service.findRatingById(id);
    }
}
