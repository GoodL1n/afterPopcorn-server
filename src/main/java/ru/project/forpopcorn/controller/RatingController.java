package ru.project.forpopcorn.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.util.ObjectUtils;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import ru.project.forpopcorn.dto.RatingDTO;
import ru.project.forpopcorn.mapper.RatingMapper;
import ru.project.forpopcorn.service.RatingService;
import ru.project.forpopcorn.validations.ResponseErrorValidation;

import javax.validation.Valid;

@CrossOrigin
@RestController
@RequestMapping("/api/rating")
public class RatingController {
    private final RatingService ratingService;
    private final RatingMapper ratingMapper;
    private final ResponseErrorValidation responseErrorValidation;

    @Autowired
    public RatingController(RatingService ratingService, RatingMapper ratingMapper, ResponseErrorValidation responseErrorValidation) {
        this.ratingService = ratingService;
        this.ratingMapper = ratingMapper;
        this.responseErrorValidation = responseErrorValidation;
    }

    @GetMapping("/{idMovie}")
    public ResponseEntity<Integer> getAverageRate(@PathVariable("idMovie") int idMovie){
        int average = ratingService.getAverageRatingMovie(idMovie);
        return new ResponseEntity<>(average, HttpStatus.OK);
    }
    @GetMapping("/{idMovie}/{idUser}")
    public ResponseEntity<RatingDTO> getRate(@PathVariable("idMovie") int idMovie, @PathVariable("idUser") int idUser){
        RatingDTO rating = ratingMapper.convertToRatingDTO(ratingService.getRateByUserIdAndMovieId(idUser, idMovie));
        return new ResponseEntity<>(rating, HttpStatus.OK);
    }

    @PostMapping("/edit")
    public ResponseEntity<Object>editRating(@Valid @RequestBody RatingDTO ratingDTO, BindingResult bindingResult){
        ResponseEntity<Object> errors = responseErrorValidation.mapValidationService(bindingResult);
        if(!ObjectUtils.isEmpty(errors)) return errors;

        RatingDTO ratingDTO1 = ratingMapper.convertToRatingDTO(ratingService.editRating(ratingDTO));
        return new ResponseEntity<>(ratingDTO1, HttpStatus.OK);
    }

}
