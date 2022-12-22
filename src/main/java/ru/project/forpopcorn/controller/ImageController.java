package ru.project.forpopcorn.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import ru.project.forpopcorn.entity.ImageModel;
import ru.project.forpopcorn.payload.response.MessageResponse;
import ru.project.forpopcorn.service.ImageService;
import ru.project.forpopcorn.validations.ResponseErrorValidation;

import java.io.IOException;
import java.security.Principal;

@CrossOrigin
@RestController
@RequestMapping("/api/image")
public class ImageController {
    private final ImageService imageService;
    private final ResponseErrorValidation responseErrorValidation;

    @Autowired
    public ImageController(ImageService imageService, ResponseErrorValidation responseErrorValidation) {
        this.imageService = imageService;
        this.responseErrorValidation = responseErrorValidation;
    }

    @PostMapping("/upload")
    public ResponseEntity<Object> uploadImageToUser(@RequestParam("file")MultipartFile file, Principal principal) throws IOException {
        imageService.uploadImageToUser(file, principal);
        return ResponseEntity.ok(new MessageResponse("Image upload!"));
    }

    @PostMapping("/{movieId}/upload")
    public ResponseEntity<Object> uploadImageToMovie(@RequestParam("file")MultipartFile file,
                                                    @PathVariable("movieId") int movieId) throws IOException {
        imageService.uploadImageToMovie(file, movieId);
        return ResponseEntity.ok(new MessageResponse("Image upload!"));
    }

    @GetMapping("/profileImage")
    public ResponseEntity<ImageModel> getUserImage(Principal principal){
        return new ResponseEntity<>(imageService.getImageToUser(principal), HttpStatus.OK);
    }
    @GetMapping("/{userId}/user")
    public ResponseEntity<ImageModel> getUserImageById(@PathVariable("userId") int id){
        return new ResponseEntity<>(imageService.getImageToUserById(id), HttpStatus.OK);
    }
    @GetMapping("/{movieId}/movie1")
    public ResponseEntity<ImageModel> getImageByMovieId(@PathVariable("movieId") int movieId){
        return new ResponseEntity<>(imageService.getImageToMovie(movieId), HttpStatus.OK);
    }
    @GetMapping("/{title}/movie2")
    public ResponseEntity<ImageModel> getImageByMovieId(@PathVariable("title") String title){
        return new ResponseEntity<>(imageService.getImageToMovieByTitle(title), HttpStatus.OK);
    }
}
