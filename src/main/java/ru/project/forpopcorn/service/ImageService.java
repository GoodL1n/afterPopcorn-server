package ru.project.forpopcorn.service;

import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.ObjectUtils;
import org.springframework.web.multipart.MultipartFile;
import ru.project.forpopcorn.entity.ImageModel;
import ru.project.forpopcorn.entity.Movie;
import ru.project.forpopcorn.entity.User;
import ru.project.forpopcorn.exceptions.MovieNotFoundException;
import ru.project.forpopcorn.repository.ImageRepository;
import ru.project.forpopcorn.repository.MovieRepository;
import ru.project.forpopcorn.repository.UserRepository;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.security.Principal;
import java.util.zip.DataFormatException;
import java.util.zip.Deflater;
import java.util.zip.Inflater;


@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class ImageService {

    public static final Logger LOG = LoggerFactory.getLogger(ImageService.class);

    private final ImageRepository imageRepository;
    private final UserRepository userRepository;
    private final MovieRepository movieRepository;

    @Transactional
    public ImageModel uploadImageToUser(MultipartFile file, Principal principal) throws IOException {
        User user = userRepository.findUserByNickname(principal.getName())
                .orElseThrow(()-> new UsernameNotFoundException("User not found with nickname " + principal.getName()));

        ImageModel imageProfile = imageRepository.findByUserId(user.getId()).orElse(null);
        if(!ObjectUtils.isEmpty(imageProfile)){
            imageRepository.delete(imageProfile);
        }
        ImageModel imageModel = new ImageModel();
        imageModel.setUserId(user.getId());
        imageModel.setImageBytes(compressBytes(file.getBytes()));
        imageModel.setName(file.getOriginalFilename());

        return imageRepository.save(imageModel);
    }

    @Transactional
    public ImageModel uploadImageToMovie(MultipartFile file, int movieId) throws IOException {
        Movie movie = movieRepository.findById(movieId)
                .orElseThrow(()->new MovieNotFoundException("Movie not found"));
        ImageModel imageProfile = imageRepository.findByMovieId(movieId).orElse(null);
        if(!ObjectUtils.isEmpty(imageProfile)){
            imageRepository.delete(imageProfile);
        }

        ImageModel imageModel = new ImageModel();
        imageModel.setMovieId(movie.getId());
        imageModel.setImageBytes(compressBytes(file.getBytes()));
        imageModel.setName(file.getOriginalFilename());

        return imageRepository.save(imageModel);
    }

    public ImageModel getImageToUser(Principal principal){
        User user = userRepository.findUserByNickname(principal.getName())
                .orElseThrow(()-> new UsernameNotFoundException("User not found with nickname " + principal.getName()));
        ImageModel imageModel = imageRepository.findByUserId(user.getId()).orElse(null);
        if (!ObjectUtils.isEmpty(imageModel)) {
            imageModel.setImageBytes(decompressBytes(imageModel.getImageBytes()));
        }
        return imageModel;
    }
    public ImageModel getImageToUserById(int id){
        User user = userRepository.findUserById(id)
                .orElseThrow(()-> new UsernameNotFoundException("User not found"));
        ImageModel imageModel = imageRepository.findByUserId(id).orElse(null);
        if (!ObjectUtils.isEmpty(imageModel)) {
            imageModel.setImageBytes(decompressBytes(imageModel.getImageBytes()));
        }
        return imageModel;
    }

    public ImageModel getImageToMovieByTitle(String title){
        Movie movie = movieRepository.findByTitle(title)
                .orElseThrow(()->new MovieNotFoundException("Movie not found"));
        ImageModel imageModel = imageRepository.findByMovieId(movie.getId()).orElse(null);
        if (!ObjectUtils.isEmpty(imageModel)) {
            imageModel.setImageBytes(decompressBytes(imageModel.getImageBytes()));
        }
        return imageModel;
    }

    public ImageModel getImageToMovie(int movieId){
        Movie movie = movieRepository.findById(movieId)
                .orElseThrow(()->new MovieNotFoundException("Movie not found"));
        ImageModel imageModel = imageRepository.findByMovieId(movieId).orElse(null);
        if (!ObjectUtils.isEmpty(imageModel)) {
            imageModel.setImageBytes(decompressBytes(imageModel.getImageBytes()));
        }
        return imageModel;
    }

    private byte[] compressBytes(byte[] data) {
        Deflater deflater = new Deflater();
        deflater.setInput(data);
        deflater.finish();
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream(data.length);
        byte[] buffer = new byte[1024];
        while (!deflater.finished()) {
            int count = deflater.deflate(buffer);
            outputStream.write(buffer, 0, count);
        }
        try {
            outputStream.close();
        } catch (IOException e) {
            LOG.error("Cannot compress Bytes");
        }
        System.out.println("Compressed Image Byte Size - " + outputStream.toByteArray().length);
        return outputStream.toByteArray();
    }

    private static byte[] decompressBytes(byte[] data) {
        Inflater inflater = new Inflater();
        inflater.setInput(data);
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream(data.length);
        byte[] buffer = new byte[1024];
        try {
            while (!inflater.finished()) {
                int count = inflater.inflate(buffer);
                outputStream.write(buffer, 0, count);
            }
            outputStream.close();
        } catch (IOException | DataFormatException e) {
            LOG.error("Cannot decompress Bytes");
        }
        return outputStream.toByteArray();
    }
}
