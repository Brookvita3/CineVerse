package com.cineverse.movie_service.service;

import com.cineverse.movie_service.domain.model.Movie;
import com.cineverse.movie_service.domain.repository.MovieRepository;
import com.cineverse.movie_service.dto.request.UpdateMovieRequest;
import com.cineverse.movie_service.dto.request.UploadMovieRequest;
import com.cineverse.movie_service.exception.NotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.HashMap;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class MovieService {

    private final MovieRepository movieRepository;
    private final RestTemplate restTemplate = new RestTemplate();

    @Value("${streaming-service.base-url}")
    private String streamingServiceBaseUrl;

    public String uploadMovie(UploadMovieRequest req) {

        if (movieRepository.existsByTitle(req.getTitle())) {
            throw new IllegalArgumentException("Movie is already exist");
        }

        Movie movie = Movie.create(req);

        String fileName = req.getVideoFileName();
        String signedUrlEndpoint = streamingServiceBaseUrl + "/signed-url/upload";

        Map<String, String> body = new HashMap<>();
        body.put("FileName", fileName);
        body.put("ContentType", "mp4");

        return restTemplate.postForObject(signedUrlEndpoint, body, String.class);
    }

    public void updateMovie(String movieId, UpdateMovieRequest req) {

        Movie movie = movieRepository.findById(movieId)
                .orElseThrow(() -> new NotFoundException("Movie not found"));

        movie.update(req);

        movieRepository.save(movie);
    }

}
