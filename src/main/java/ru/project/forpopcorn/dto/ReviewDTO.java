package ru.project.forpopcorn.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class ReviewDTO {

    private int id;
    private String title;
    private String text;
    private Integer likes;
    private int user_id;
    private int movie_id;
}
