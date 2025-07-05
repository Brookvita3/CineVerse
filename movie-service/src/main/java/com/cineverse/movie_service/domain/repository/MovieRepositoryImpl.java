package com.cineverse.movie_service.domain.repository;

import com.cineverse.movie_service.domain.model.Movie;
import org.springframework.stereotype.Component;

import java.util.Optional;

@Component
public class MovieRepositoryImpl implements MovieRepository{
    @Override
    public boolean existsByTitle(String title) {
        return false;
    }

    @Override
    public Optional<Movie> findById(String id) {
        return Optional.empty();
    }

    @Override
    public Movie save(Movie movie) {
        return null;
    }
}
