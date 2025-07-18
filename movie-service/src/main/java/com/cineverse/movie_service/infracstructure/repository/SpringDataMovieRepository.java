package com.cineverse.movie_service.infracstructure.repository;

import com.cineverse.movie_service.domain.model.Movie;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface SpringDataMovieRepository extends JpaRepository<Movie, String> {
    boolean existsByTitle(String title);
    Optional<Movie> findByTitle(String title);
}
