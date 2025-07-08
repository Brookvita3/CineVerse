package com.cineverse.movie_service.domain.repository;

import com.cineverse.movie_service.domain.model.Movie;

import java.util.Optional;
import java.util.UUID;

public interface MovieRepository {
    boolean existsByTitle(String title);
    Optional<Movie> findById(UUID id);
    Movie save(Movie movie);
}
