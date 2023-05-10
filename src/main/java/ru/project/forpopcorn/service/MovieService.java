package ru.project.forpopcorn.service;

import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.project.forpopcorn.dto.MovieDTO;
import ru.project.forpopcorn.entity.Genre;
import ru.project.forpopcorn.entity.ImageModel;
import ru.project.forpopcorn.entity.Movie;
import ru.project.forpopcorn.exceptions.MovieNotFoundException;
import ru.project.forpopcorn.repository.GenreRepository;
import ru.project.forpopcorn.repository.ImageRepository;
import ru.project.forpopcorn.repository.MovieRepository;
import ru.project.forpopcorn.repository.ReviewRepository;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class MovieService {

    private final MovieRepository movieRepository;
    private final ImageRepository imageRepository;

    public List<Movie> getAllMovies(){
        return movieRepository.findAll();
    }

    public List<Movie> getAllMoviesByGenre(String genreTitle){
        return movieRepository.findAllByGenresTitle(genreTitle);
    }

    public Movie getMovieById(int movieId){
        return movieRepository.findById(movieId)
                .orElseThrow(()->new MovieNotFoundException("Movie with this id not found"));
    }

    @Transactional
    public Movie createMovie(MovieDTO movieDTO){
        Movie movie = new Movie();
        movie.setTitle(movieDTO.getTitle());
        movie.setCountry(movieDTO.getCountry());
        movie.setDate(movieDTO.getDate());
        movie.setDuration(movieDTO.getDuration());
        movie.setTrailer(movieDTO.getTrailer());
        movie.setTextMini(movieDTO.getTextMini());
        movie.setTextLarge(movieDTO.getTextLarge());
        movie.setGenres(movieDTO.getGenres());
        return movieRepository.save(movie);
    }

    @Transactional
    public Movie updateMovie(MovieDTO movieDTO, int movieId){
        Movie movie = movieRepository.findMovieById(movieId);
        movie.setTitle(movieDTO.getTitle());
        movie.setCountry(movieDTO.getCountry());
        movie.setDate(movieDTO.getDate());
        movie.setDuration(movieDTO.getDuration());
        movie.setTrailer(movieDTO.getTrailer());
        movie.setTextMini(movieDTO.getTextMini());
        movie.setTextLarge(movieDTO.getTextLarge());
        movie.setGenres(movieDTO.getGenres());
        return movieRepository.save(movie);
    }

    @Transactional
    public void deleteMovie(int movieId){
        Movie movie = getMovieById(movieId);
        Optional<ImageModel> image = imageRepository.findByMovieId(movieId);
        movieRepository.delete(movie);
        image.ifPresent(imageRepository::delete);
    }
}
