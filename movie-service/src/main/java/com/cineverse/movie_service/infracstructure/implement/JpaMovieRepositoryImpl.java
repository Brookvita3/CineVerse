package com.cineverse.movie_service.infracstructure.implement;

import com.cineverse.movie_service.domain.model.Movie;
import com.cineverse.movie_service.domain.repository.MovieRepository;
import com.cineverse.movie_service.infracstructure.repository.SpringDataMovieRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.UUID;

@Repository
@RequiredArgsConstructor
public class JpaMovieRepositoryImpl implements MovieRepository {

    private final SpringDataMovieRepository springDataRepo;

    @Override
    public boolean existsByTitle(String title) {
        return springDataRepo.existsByTitle(title);
    }

    @Override
    public Optional<Movie> findById(UUID id) {
        return springDataRepo.findById(id);
    }

    @Override
    public Movie save(Movie movie) {
        return springDataRepo.save(movie);
    }

}
