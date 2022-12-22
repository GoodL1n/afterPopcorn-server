package ru.project.forpopcorn.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.project.forpopcorn.dto.RatingDTO;
import ru.project.forpopcorn.entity.Rating;
import ru.project.forpopcorn.repository.RatingRepository;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@Service
@Transactional(readOnly = true)
public class RatingService {

    private final RatingRepository ratingRepository;
    private final MovieService movieService;
    private final UserService userService;

    @Autowired
    public RatingService(RatingRepository ratingRepository, MovieService movieService, UserService userService) {
        this.ratingRepository = ratingRepository;
        this.movieService = movieService;
        this.userService = userService;
    }

    public Integer getAverageRatingMovie(int id){
        List<Rating> list = ratingRepository.findAllByMovieRateId(id);
        if(list.size()==0) return 0;
        else {
            int sum = list.stream()
                    .mapToInt(s -> s.getStars())
                    .filter(r -> r!=0)
                    .sum();
            int count = (int) list.stream()
                    .mapToInt(s -> s.getStars())
                    .filter(r -> r!=0)
                    .count();
            if(count == 0) return sum;
            else return sum/count;
        }
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
    public Rating editRating(RatingDTO ratingDTO){
        Rating rating = ratingRepository.findById(ratingDTO.getId())
                .orElseThrow(()->new RuntimeException("Не найден рейтинг с таким айди"));
        rating.setStars(ratingDTO.getStars());
        return ratingRepository.save(rating);
    }
}
