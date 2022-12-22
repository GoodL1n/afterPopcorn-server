package ru.project.forpopcorn.mapper;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import ru.project.forpopcorn.dto.MovieDTO;
import ru.project.forpopcorn.entity.Genre;
import ru.project.forpopcorn.entity.Movie;
import ru.project.forpopcorn.repository.GenreRepository;

import java.util.ArrayList;
import java.util.List;

@Component
public class MovieMapper {

    private final GenreRepository genreRepository;

    public MovieMapper(GenreRepository genreRepository) {
        this.genreRepository = genreRepository;
    }

    public MovieDTO convertToMovieDTO(Movie movie){
        int id = movie.getId();
        String title = movie.getTitle();
        String textMini = movie.getTextMini();
        String textLarge = movie.getTextLarge();
        List<Genre> genres = movie.getGenres();
        return new MovieDTO(id, title, textMini, textLarge, genres);
    }
}
