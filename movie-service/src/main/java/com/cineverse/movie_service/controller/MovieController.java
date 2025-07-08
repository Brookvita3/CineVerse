package com.cineverse.movie_service.controller;

import com.cineverse.movie_service.domain.model.Movie;
import com.cineverse.movie_service.application.command.UpdateMovieCommand;
import com.cineverse.movie_service.application.command.UploadMovieCommand;
import com.cineverse.movie_service.service.MovieService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@RequiredArgsConstructor
@RequestMapping("/movies")
public class MovieController {

    private final MovieService movieService;

    @GetMapping("/ping")
    public String ping() {
        return "🎬 Hello from Spring Boot Movie Service!";
    }

    @PostMapping("/upload")
    public ResponseEntity<?> uploadMovie(@RequestBody @Valid UploadMovieCommand request) {

        String signedUrl =  movieService.uploadMovie(request);

        return ResponseEntity.status(HttpStatus.OK)
                .body(signedUrl);
    }

    @PutMapping("/update")
    public ResponseEntity<?> updateMovie(
            @PathVariable UUID movieId,
            @RequestBody @Valid UpdateMovieCommand request) {

        movieService.updateMovie(movieId, request);

        return ResponseEntity.status(HttpStatus.OK)
                .body("update successfully");
    }

    @PutMapping("/{id}/mark-ready")
    public ResponseEntity<?> markReady(@PathVariable UUID id) {

        Movie movie = movieService.markReady(id);

        return ResponseEntity.status(HttpStatus.OK)
                .body(movie);
    }

}