package com.cineverse.movie_service.controller;

import com.cineverse.movie_service.domain.model.Movie;
import com.cineverse.movie_service.application.command.UpdateMovieCommand;
import com.cineverse.movie_service.application.command.UploadMovieCommand;
import com.cineverse.movie_service.service.MinioService;
import com.cineverse.movie_service.service.MovieService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/movies")
public class MovieController {

    private final MovieService movieService;
    private final MinioService minioService;


    @PostMapping("")
    public ResponseEntity<?> uploadMovieMetadata(@RequestBody @Valid UploadMovieCommand request) {

        Movie movie = movieService.uploadMovie(request);

        return ResponseEntity.status(HttpStatus.OK)
                .body(movie);
    }

    @GetMapping("/{movieId}/upload-url")
    public ResponseEntity<?> createUploadUrl(@PathVariable String movieId) {

        String signedUrl = minioService.generatePresignedUploadUrl(movieId, 15);

        return ResponseEntity.status(HttpStatus.OK)
                .body(signedUrl);
    }

    @PutMapping("/update")
    public ResponseEntity<?> updateMovie(
            @PathVariable String movieId,
            @RequestBody @Valid UpdateMovieCommand request) {

        movieService.updateMovie(movieId, request);

        return ResponseEntity.status(HttpStatus.OK)
                .body("update successfully");
    }

}