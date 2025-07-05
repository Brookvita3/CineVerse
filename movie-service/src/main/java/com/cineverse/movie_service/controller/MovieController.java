package com.cineverse.movie_service.controller;

import com.cineverse.movie_service.domain.model.Movie;
import com.cineverse.movie_service.dto.mapper.MovieMaper;
import com.cineverse.movie_service.dto.request.UpdateMovieRequest;
import com.cineverse.movie_service.dto.request.UploadMovieRequest;
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
    private final MovieMaper movieMaper;

    @GetMapping("/ping")
    public String ping() {
        return "🎬 Hello from Spring Boot Movie Service!";
    }

    @PostMapping("/upload")
    public ResponseEntity<?> uploadMovie(@RequestBody @Valid UploadMovieRequest request) {

        String signedUrl =  movieService.uploadMovie(request);

        return ResponseEntity.status(HttpStatus.OK)
                .body(signedUrl);
    }

    @PutMapping("/update")
    public ResponseEntity<?> updateMovie(
            @PathVariable String movieId,
            @RequestBody @Valid UpdateMovieRequest request) {

        movieService.updateMovie(movieId, request);

        return ResponseEntity.status(HttpStatus.OK)
                .body("update successfully");
    }

}