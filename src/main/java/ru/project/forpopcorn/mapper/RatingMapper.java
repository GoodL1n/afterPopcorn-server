package ru.project.forpopcorn.mapper;

import org.springframework.stereotype.Component;
import ru.project.forpopcorn.dto.RatingDTO;
import ru.project.forpopcorn.entity.Rating;
import ru.project.forpopcorn.repository.RatingRepository;

@Component
public class RatingMapper {

    private final RatingRepository ratingRepository;

    public RatingMapper(RatingRepository ratingRepository) {
        this.ratingRepository = ratingRepository;
    }

    public RatingDTO convertToRatingDTO(Rating rating){
        int id = rating.getId();
        int stars = rating.getStars();
        return new RatingDTO(id, stars);
    }
}
