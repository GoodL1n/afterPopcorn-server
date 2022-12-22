package ru.project.forpopcorn.dto;

import lombok.*;
import ru.project.forpopcorn.entity.Genre;
import ru.project.forpopcorn.entity.Review;

import java.util.Arrays;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class MovieDTO {

    private int idMovie;
    private String title;
    private String textMini;
    private String textLarge;
    private List<Genre> genres;
}
