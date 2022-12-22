package ru.project.forpopcorn.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ru.project.forpopcorn.entity.Genre;
import ru.project.forpopcorn.entity.Movie;

import java.util.List;
import java.util.Optional;

@Repository
public interface MovieRepository extends JpaRepository<Movie, Integer> {
    List<Movie> findAllByGenresTitle(String title);
    Movie findMovieById(int id);
    Optional<Movie> findById(int id);
    Optional<Movie> findByTitle(String title);
}
