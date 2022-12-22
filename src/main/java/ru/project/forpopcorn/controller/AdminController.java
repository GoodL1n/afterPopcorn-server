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

@CrossOrigin
@RestController
@RequestMapping("/api/admin")
public class AdminController {

    private final MovieMapper mapper;
    private final MovieService movieService;
    private final ResponseErrorValidation responseErrorValidation;

    @Autowired
    public AdminController(MovieMapper mapper, MovieService movieService, ResponseErrorValidation responseErrorValidation) {
        this.mapper = mapper;
        this.movieService = movieService;
        this.responseErrorValidation = responseErrorValidation;
    }

    @PostMapping("/createMovie")
    public ResponseEntity<Object> createMovie(@Valid @RequestBody MovieDTO movieDTO, BindingResult bindingResult){
        ResponseEntity<Object> errors = responseErrorValidation.mapValidationService(bindingResult);
        if(!ObjectUtils.isEmpty(errors)) return errors;

        MovieDTO movieToBD = mapper.convertToMovieDTO(movieService.createMovie(movieDTO));
        return new ResponseEntity<>(movieToBD, HttpStatus.OK);
    }

    @PostMapping("/{idMovie}/updateMovie")
    public ResponseEntity<Object> updateMovie(@Valid @RequestBody MovieDTO movieDTO,
                                              @PathVariable("idMovie") int idMovie, BindingResult bindingResult){

        ResponseEntity<Object> errors = responseErrorValidation.mapValidationService(bindingResult);
        if(!ObjectUtils.isEmpty(errors)) return errors;

        MovieDTO updatedMovie = mapper.convertToMovieDTO(movieService.updateMovie(movieDTO, idMovie));
        return new ResponseEntity<>(updatedMovie, HttpStatus.OK);
    }

    @DeleteMapping("/{idMovie}/deleteMovie")
    public ResponseEntity<Object> deleteMovie(@PathVariable("idMovie") int idMovie){
        movieService.deleteMovie(idMovie);
        return new ResponseEntity<>(new MessageResponse("Movie was deleted"), HttpStatus.OK);
    }
}
