package ru.project.forpopcorn.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.project.forpopcorn.dto.RatingDTO;
import ru.project.forpopcorn.entity.Rating;
import ru.project.forpopcorn.repository.RatingRepository;

import java.util.List;
import java.util.Optional;

@Slf4j
@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class RatingService {

    private final RatingRepository ratingRepository;
    private final MovieService movieService;
    private final UserService userService;

    public Integer getAverageRatingMovie(int id) {
        List<Rating> ratings = ratingRepository.findAllByMovieRateId(id);
        if (ratings.isEmpty()) { //если вообще нет никаких оценок, тогда общая = 0
            return 0;
        }

        int sum = ratings.stream()
                .mapToInt(Rating::getStars)
                .sum();

        int count = (int) ratings.stream() // если рейтинг фильма от пользователя = 0, то есть он его еще не оценил, то мы эту оценку не учитываем
                .filter(r -> r.getStars() > 0)
                .count();

        if(sum == 0) return 0;
        return sum / count;
    }


    @Transactional
    public Rating getRateByUserIdAndMovieId(int userId, int movieId) {
        Optional<Rating> rating = ratingRepository.findRatingByUserRateIdAndMovieRateId(userId, movieId);
        if (rating.isPresent()) {
            return rating.get();
        } else {
            Rating rating1 = new Rating();
            rating1.setStars(0);
            rating1.setUserRate(userService.getUserById(userId));
            rating1.setMovieRate(movieService.getMovieById(movieId));
            ratingRepository.save(rating1);
            return rating1;
        }
    }

    @Transactional
    public Rating editRating(RatingDTO ratingDTO) {
        Rating rating = ratingRepository.findById(ratingDTO.getId())
                .orElseThrow(() -> new RuntimeException("Не найден рейтинг с таким айди"));
//        if(ratingDTO.getStars() == 0) {
//            ratingRepository.delete(rating);
//            return new Rating();
//        }
        rating.setStars(ratingDTO.getStars());
        return ratingRepository.save(rating);
    }
}
