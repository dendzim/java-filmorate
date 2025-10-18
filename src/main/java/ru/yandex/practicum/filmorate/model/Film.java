package ru.yandex.practicum.filmorate.model;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.time.LocalDate;

/**
 * Film.
 */
@Data
public class Film {
    private Integer id;
    @NotBlank
    private String name;
    private String description;
    @NotNull
    private LocalDate releaseDate;
    @Min(1)
    private int duration;
}
