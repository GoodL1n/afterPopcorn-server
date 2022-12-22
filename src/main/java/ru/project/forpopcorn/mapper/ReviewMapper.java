package ru.project.forpopcorn.mapper;

import org.springframework.stereotype.Component;
import ru.project.forpopcorn.dto.MovieDTO;
import ru.project.forpopcorn.dto.ReviewDTO;
import ru.project.forpopcorn.entity.Genre;
import ru.project.forpopcorn.entity.Movie;
import ru.project.forpopcorn.entity.Review;
import ru.project.forpopcorn.repository.ReviewRepository;

@Component
public class ReviewMapper {

    private final ReviewRepository reviewRepository;

    public ReviewMapper(ReviewRepository reviewRepository) {
        this.reviewRepository = reviewRepository;
    }

    public ReviewDTO convertToReviewDTO(Review review){
        int id = review.getId();
        String title = review.getTitle();
        String text = review.getText();
        int user_id = review.getUser().getId();
        int movie_id = review.getMovie().getId();
        Integer likes = review.getLikes();
        return new ReviewDTO(id, title, text, likes, user_id, movie_id);
    }
}
