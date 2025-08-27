package com.cineverse.movie_service.service;

import com.cineverse.movie_service.application.command.StreamMovieCommand;
import com.cineverse.movie_service.domain.model.Actor;
import com.cineverse.movie_service.domain.model.Movie;
import com.cineverse.movie_service.domain.repository.ActorRepository;
import com.cineverse.movie_service.domain.repository.MovieRepository;
import com.cineverse.movie_service.application.command.UpdateMovieCommand;
import com.cineverse.movie_service.application.command.UploadMovieCommand;
import com.cineverse.movie_service.dto.MovieDTO;
import com.cineverse.movie_service.exception.NotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.client.*;

import java.util.List;

@Service
@RequiredArgsConstructor
public class MovieService {

    private final MovieRepository movieRepository;
    private final ActorRepository actorRepository;

    /**
     * Uploads metadata for a new movie and returns a signed URL for uploading the
     * video file.
     * <p>
     * This method performs the following steps:
     * <ol>
     * <li>Checks if a movie with the given title already exists.</li>
     * <li>Creates a {@link Movie} entity from the provided
     * {@link UploadMovieCommand}.</li>
     * <li>Saves the movie metadata to the database with
     * {@code MovieStatus.PENDING}.</li>
     * <li>Calls the streaming service to obtain a signed URL for uploading the
     * actual video file.</li>
     * </ol>
     *
     * @param command the command object containing metadata for the new movie,
     *                including title, description, release date, genres, actors,
     *                thumbnail URL, video file name, and visibility.
     *
     * @return a signed URL as a {@link String} that can be used to upload the movie
     *         file.
     *
     * @throws IllegalArgumentException if a movie with the same title already
     *                                  exists.
     * @throws RestClientException      if the request to the streaming service
     *                                  fails.
     */
    public Movie uploadMovie(UploadMovieCommand command) {

        if (movieRepository.existsByTitle(command.getTitle())) {
            throw new IllegalArgumentException("Movie is already exist");
        }

        MovieDTO movieDTO = fromUploadMovieCommand(command);

        Movie movie = Movie.upload(movieDTO);
        return movieRepository.save(movie);
    }


    public Movie streamMovie(StreamMovieCommand command) {

        if (movieRepository.existsByTitle(command.getTitle())) {
            throw new IllegalArgumentException("Movie is already exist");
        }

        MovieDTO movieDTO = fromUploadMovieCommand(command);

        Movie movie = Movie.upload(movieDTO);
        return movieRepository.save(movie);
    }


    private MovieDTO fromUploadMovieCommand(UploadMovieCommand command) {
        if (command == null)
            return null;
        List<Actor> actors = command.getActorIds().stream()
                .map(id -> actorRepository.findById(id)
                        .orElseThrow(() -> new NotFoundException("Actor not found")))
                .toList();

        MovieDTO movieDTO = new MovieDTO();

        movieDTO.setActors(actors);
        movieDTO.setTitle(command.getTitle());
        movieDTO.setGenres(command.getGenres());
        movieDTO.setDescription(command.getDescription());
        movieDTO.setIsPublic(command.getIsPublic());
        movieDTO.setThumbnailUrl(command.getThumbnailUrl());
        movieDTO.setMovieFileName(command.getMovieFileName());
        movieDTO.setReleaseDate(command.getReleaseDate());
        movieDTO.setThumbnailUrl(command.getThumbnailUrl());

        return movieDTO;
    }

}
