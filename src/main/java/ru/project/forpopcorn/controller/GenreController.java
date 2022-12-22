package ru.project.forpopcorn.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.project.forpopcorn.dto.GenreDTO;
import ru.project.forpopcorn.dto.MovieDTO;
import ru.project.forpopcorn.mapper.GenreMapper;
import ru.project.forpopcorn.service.GenreService;

import java.util.List;
import java.util.stream.Collectors;

@CrossOrigin
@RestController
@RequestMapping("/api/genre")
public class GenreController {
    private final GenreService genreService;
    private final GenreMapper genreMapper;

    @Autowired
    public GenreController(GenreService genreService, GenreMapper genreMapper) {
        this.genreService = genreService;
        this.genreMapper = genreMapper;
    }

    @GetMapping("/{id}")
    public ResponseEntity<GenreDTO> getGenreById(@PathVariable int id){
        GenreDTO genreDTO = genreMapper.convertToGenreDTO(genreService.getGenreById(id));
        return new ResponseEntity<>(genreDTO, HttpStatus.OK);
    }

    @GetMapping("/list")
    public ResponseEntity<List<GenreDTO>> getGenres(){
        List<GenreDTO> list = genreService.getGenres().stream().map(genreMapper::convertToGenreDTO)
                .collect(Collectors.toList());
        return new ResponseEntity<>(list, HttpStatus.OK);
    }
}
