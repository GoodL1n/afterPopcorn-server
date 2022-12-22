package ru.project.forpopcorn.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.parameters.P;
import org.springframework.util.ObjectUtils;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import ru.project.forpopcorn.dto.MovieDTO;
import ru.project.forpopcorn.dto.ReviewDTO;
import ru.project.forpopcorn.mapper.ReviewMapper;
import ru.project.forpopcorn.payload.response.MessageResponse;
import ru.project.forpopcorn.service.ReviewService;
import ru.project.forpopcorn.validations.ResponseErrorValidation;

import javax.validation.Valid;
import java.security.Principal;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@CrossOrigin
@RequestMapping("/api/review")
public class ReviewController {

    private final ReviewService reviewService;
    private final ReviewMapper mapper;
    private final ResponseErrorValidation responseErrorValidation;

    @Autowired
    public ReviewController(ReviewService reviewService, ReviewMapper mapper, ResponseErrorValidation responseErrorValidation) {
        this.reviewService = reviewService;
        this.mapper = mapper;
        this.responseErrorValidation = responseErrorValidation;
    }

    @GetMapping("/{id}")
    public ResponseEntity<ReviewDTO> showReview(@PathVariable int id){
        ReviewDTO reviewDTO = mapper.convertToReviewDTO(reviewService.getReviewById(id));
        return new ResponseEntity<>(reviewDTO, HttpStatus.OK);
    }

    @GetMapping("/user")
    public ResponseEntity<List<ReviewDTO>> showReviewsUser(Principal principal){
        List<ReviewDTO> reviewDTO = reviewService.getAllReviewsByUser(principal)
                .stream().map(mapper::convertToReviewDTO).collect(Collectors.toList());
        return new ResponseEntity<>(reviewDTO, HttpStatus.OK);
    }

    @GetMapping("/movie/{id}")
    public ResponseEntity<List<ReviewDTO>> showReviewsMovie(@PathVariable int id){
        List<ReviewDTO> reviewDTO = reviewService.getAllReviewsByMovie(id)
                .stream().map(mapper::convertToReviewDTO).collect(Collectors.toList());
        return new ResponseEntity<>(reviewDTO, HttpStatus.OK);
    }

    @PostMapping("/{id}/{username}/like")
    public ResponseEntity<ReviewDTO> likeReview(@PathVariable("id")int id,
                                                @PathVariable("username") String username){
        ReviewDTO reviewToBD = mapper.convertToReviewDTO(reviewService.likeReview(id, username));
        return new ResponseEntity<>(reviewToBD, HttpStatus.OK);
    }

    @PostMapping("/{movieId}/create")
    public ResponseEntity<Object> createReview(@Valid @RequestBody ReviewDTO reviewDTO,
                                               @PathVariable("movieId") int movieId,
                                               Principal principal,
                                               BindingResult bindingResult){
        ResponseEntity<Object> errors = responseErrorValidation.mapValidationService(bindingResult);
        if(!ObjectUtils.isEmpty(errors)) return errors;

        ReviewDTO reviewToBD = mapper.convertToReviewDTO(reviewService.createReview(reviewDTO, movieId, principal));
        return new ResponseEntity<>(reviewToBD, HttpStatus.OK);
    }

    @DeleteMapping("/{idReview}/delete")
    public ResponseEntity<Object> deleteReview(@PathVariable("idReview") int idReview){
        reviewService.deleteReview(idReview);
        return new ResponseEntity<>(new MessageResponse("Review was deleted"), HttpStatus.OK);
    }
}
