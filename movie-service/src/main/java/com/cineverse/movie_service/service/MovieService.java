package com.cineverse.movie_service.service;

import com.cineverse.movie_service.domain.model.Movie;
import com.cineverse.movie_service.domain.repository.MovieRepository;
import com.cineverse.movie_service.dto.request.UpdateMovieRequest;
import com.cineverse.movie_service.dto.request.UploadMovieRequest;
import com.cineverse.movie_service.exception.NotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class MovieService {

    private final MovieRepository movieRepository;

    public Movie uploadMovie(UploadMovieRequest req) {

        if (movieRepository.existsByTitle(req.getTitle())) {
            throw new IllegalArgumentException("Movie is already exist");
        }

        Movie movie = Movie.create(req);

        return movieRepository.save(movie);
    }

    public void updateMovie(String movieId, UpdateMovieRequest req) {

        Movie movie = movieRepository.findById(movieId)
                .orElseThrow(() -> new NotFoundException("Movie not found"));

        movie.update(req);

        movieRepository.save(movie);
    }




}
