package ru.project.forpopcorn.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.project.forpopcorn.dto.ReviewDTO;
import ru.project.forpopcorn.entity.Movie;
import ru.project.forpopcorn.entity.Review;
import ru.project.forpopcorn.entity.User;
import ru.project.forpopcorn.exceptions.MovieNotFoundException;
import ru.project.forpopcorn.exceptions.ReviewNotFoundException;
import ru.project.forpopcorn.repository.MovieRepository;
import ru.project.forpopcorn.repository.ReviewRepository;
import ru.project.forpopcorn.repository.UserRepository;

import java.security.Principal;
import java.util.List;
import java.util.Optional;

@Service
@Transactional(readOnly = true)
public class ReviewService {

    private final MovieRepository movieRepository;
    private final UserRepository userRepository;
    private final ReviewRepository reviewRepository;

    @Autowired
    public ReviewService(MovieRepository movieRepository, UserRepository userRepository, ReviewRepository reviewRepository) {
        this.movieRepository = movieRepository;
        this.userRepository = userRepository;
        this.reviewRepository = reviewRepository;
    }

    public Review getReviewById(int id){
        return reviewRepository.findById(id)
                .orElseThrow(()->new ReviewNotFoundException("Review not found"));
    }

    public List<Review> getAllReviewsByUser(Principal principal){
        User user = userRepository.findUserByNickname(principal.getName())
                .orElseThrow(()-> new UsernameNotFoundException("User not found with nickname " + principal.getName()));
        return reviewRepository.findAllByUserOrderByCreatedDateDesc(user);
    }

    public List<Review> getAllReviewsByMovie(int movieId){
        return reviewRepository.findAllByMovieIdOrderByCreatedDateDesc(movieId);
    }

    @Transactional
    public Review createReview(ReviewDTO reviewDTO, int movieId, Principal principal){
        User user = userRepository.findUserByNickname(principal.getName())
                .orElseThrow(()-> new UsernameNotFoundException("User not found with nickname " + principal.getName()));
        Movie movie = movieRepository.findById(movieId)
                .orElseThrow(()->new MovieNotFoundException("Movie with this id not found"));
        Review review = new Review();
        review.setUser(user);
        review.setMovie(movie);
        review.setTitle(reviewDTO.getTitle());
        review.setText(reviewDTO.getText());
        review.setLikes(0);

        return reviewRepository.save(review);
    }

    @Transactional
    public Review likeReview(int id, String nickname){
        Review review = reviewRepository.findById(id)
                .orElseThrow(()->new ReviewNotFoundException("Review not found"));

        Optional<String> userLiked = review.getLikedUsers()
                .stream().filter(u -> u.equals(nickname)).findAny();

        if (userLiked.isPresent()) {
            review.setLikes(review.getLikes() - 1);
            review.getLikedUsers().remove(nickname);
        } else {
            review.setLikes(review.getLikes() + 1);
            review.getLikedUsers().add(nickname);
        }
        return reviewRepository.save(review);
    }

    @Transactional
    public void deleteReview(int id){
        Review review = reviewRepository.findById(id)
                .orElseThrow(()->new ReviewNotFoundException("Review not found"));
        reviewRepository.delete(review);
    }
}
