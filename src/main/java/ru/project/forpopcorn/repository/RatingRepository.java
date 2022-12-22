package ru.project.forpopcorn.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ru.project.forpopcorn.entity.Rating;

import java.util.List;
import java.util.Optional;

@Repository
public interface RatingRepository extends JpaRepository<Rating, Integer> {
    Optional<Rating> findRatingByUserRateIdAndMovieRateId(int userId, int movieId);
    List<Rating> findAllByMovieRateId(int movieId);
}
