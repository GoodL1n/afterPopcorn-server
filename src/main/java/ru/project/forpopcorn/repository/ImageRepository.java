package ru.project.forpopcorn.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ru.project.forpopcorn.entity.ImageModel;

import java.util.Optional;

@Repository
public interface ImageRepository extends JpaRepository<ImageModel, Integer> {
    Optional<ImageModel> findByUserId(int userId);
    Optional<ImageModel> findByMovieId(int movieId);
}
