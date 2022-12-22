package ru.project.forpopcorn.mapper;

import org.springframework.stereotype.Component;
import ru.project.forpopcorn.dto.GenreDTO;
import ru.project.forpopcorn.dto.MovieDTO;
import ru.project.forpopcorn.entity.Genre;
import ru.project.forpopcorn.entity.Movie;
import ru.project.forpopcorn.repository.GenreRepository;

import java.util.ArrayList;
import java.util.List;

@Component
public class GenreMapper {

    private final GenreRepository genreRepository;

    public GenreMapper(GenreRepository genreRepository) {
        this.genreRepository = genreRepository;
    }

    public GenreDTO convertToGenreDTO(Genre genre){
        int id = genre.getId();
        String title = genre.getTitle();
        return new GenreDTO(id, title);
    }
}
