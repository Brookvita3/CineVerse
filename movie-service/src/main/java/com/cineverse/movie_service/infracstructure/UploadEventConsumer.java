package com.cineverse.movie_service.infracstructure;

import com.cineverse.movie_service.domain.model.Movie;
import com.cineverse.movie_service.domain.repository.MovieRepository;
import com.cineverse.movie_service.dto.UploadDoneEvent;
import com.cineverse.movie_service.exception.NotFoundException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
@Slf4j
public class UploadEventConsumer {
    private final MovieRepository movieRepository;
    private final ObjectMapper mapper;

    @RabbitListener(queues = "minio-upload-queue")
    public void onMessage(String messageJson) {
        try {
            UploadDoneEvent event = mapper.readValue(messageJson, UploadDoneEvent.class);
            String movieTitle = event.getTitle();

            System.out.println("Transcode done, title: " + movieTitle);

            Movie movie = movieRepository.findByTitle(movieTitle)
                    .orElseThrow(() -> new NotFoundException("Movie not found"));

            movie.ready();
            movieRepository.save(movie);

        } catch (Exception e) {
            throw new RuntimeException("Error while mark movie ready", e);
        }
    }
}
