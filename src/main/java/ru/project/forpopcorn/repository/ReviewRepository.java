package ru.project.forpopcorn.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.stereotype.Repository;
import ru.project.forpopcorn.entity.Review;
import ru.project.forpopcorn.entity.User;

import java.util.List;

@Repository
public interface ReviewRepository extends JpaRepository<Review, Integer> {
    @Override
    @Modifying
    void delete(Review entity);

    List<Review> findAllByUserOrderByCreatedDateDesc(User user);
    List<Review> findAllByMovieIdOrderByCreatedDateDesc(int movieId);
}
