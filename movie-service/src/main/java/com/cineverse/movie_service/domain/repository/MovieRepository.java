package com.cineverse.movie_service.domain.repository;

import com.cineverse.movie_service.domain.model.Movie;

import java.util.Optional;

public interface MovieRepository {
    boolean existsByTitle(String title);

    Optional<Movie> findById(String id);

    Movie save(Movie movie);
}
