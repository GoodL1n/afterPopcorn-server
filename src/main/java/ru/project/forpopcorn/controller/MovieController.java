package ru.project.forpopcorn.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.util.ObjectUtils;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import ru.project.forpopcorn.dto.MovieDTO;
import ru.project.forpopcorn.mapper.MovieMapper;
import ru.project.forpopcorn.payload.response.MessageResponse;
import ru.project.forpopcorn.service.MovieService;
import ru.project.forpopcorn.validations.ResponseErrorValidation;

import javax.validation.Valid;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@CrossOrigin
@RequestMapping("/api/movie")
public class MovieController {

    private final MovieMapper mapper;
    private final MovieService movieService;
    private final ResponseErrorValidation responseErrorValidation;

    @Autowired
    public MovieController(MovieMapper mapper, MovieService movieService, ResponseErrorValidation responseErrorValidation) {
        this.mapper = mapper;
        this.movieService = movieService;
        this.responseErrorValidation = responseErrorValidation;
    }

    @GetMapping("/{id}")
    public ResponseEntity<MovieDTO> moviePage(@PathVariable("id")int id){
        MovieDTO movie = mapper.convertToMovieDTO(movieService.getMovieById(id));
        return new ResponseEntity<>(movie, HttpStatus.OK);
    }

    @GetMapping("/list")
    public ResponseEntity<List<MovieDTO>> listOfMovies(){
        List<MovieDTO> list = movieService.getAllMovies()
                .stream().map(mapper::convertToMovieDTO)
                .collect(Collectors.toList());
        return new ResponseEntity<>(list, HttpStatus.OK);
    }

    @GetMapping("/list/{genre}")
    public ResponseEntity<List<MovieDTO>> listOfMoviesByGenre(@PathVariable("genre") String genre){
        List<MovieDTO> list = movieService.getAllMoviesByGenre(genre)
                .stream().map(mapper::convertToMovieDTO)
                .collect(Collectors.toList());
        return new ResponseEntity<>(list, HttpStatus.OK);
    }

    @PostMapping("/create")
    public ResponseEntity<Object> createMovie(@Valid @RequestBody MovieDTO movieDTO, BindingResult bindingResult){
        ResponseEntity<Object> errors = responseErrorValidation.mapValidationService(bindingResult);
        if(!ObjectUtils.isEmpty(errors)) return errors;

        MovieDTO movieToBD = mapper.convertToMovieDTO(movieService.createMovie(movieDTO));
        return new ResponseEntity<>(movieToBD, HttpStatus.OK);
    }

    @PostMapping("/{id}/update")
    public ResponseEntity<Object> updateMovie(@Valid @RequestBody MovieDTO movieDTO,
                                              @PathVariable("id") int id, BindingResult bindingResult){

        ResponseEntity<Object> errors = responseErrorValidation.mapValidationService(bindingResult);
        if(!ObjectUtils.isEmpty(errors)) return errors;

        MovieDTO updatedMovie = mapper.convertToMovieDTO(movieService.updateMovie(movieDTO, id));
        return new ResponseEntity<>(updatedMovie, HttpStatus.OK);
    }

    @PostMapping("/{id}/delete")
    public ResponseEntity<Object> deleteMovie(@PathVariable("id") int id){
        movieService.deleteMovie(id);
        return new ResponseEntity<>(new MessageResponse("Movie was deleted"), HttpStatus.OK);
    }
}
