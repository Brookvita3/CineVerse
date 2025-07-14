package com.cineverse.movie_service.infracstructure;

import com.cineverse.movie_service.domain.model.Movie;
import com.cineverse.movie_service.domain.repository.MovieRepository;
import com.cineverse.movie_service.dto.MinioUploadDTO;
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
            MinioUploadDTO event = mapper.readValue(messageJson, MinioUploadDTO.class);

            if (event.getRecords() == null || event.getRecords().isEmpty()) {
                throw new RuntimeException("Error while parse messageJson");
            }

            for (MinioUploadDTO.Record record : event.getRecords()) {
                String key = record.getS3().getObject().getKey();
                String bucket = record.getS3().getBucket().getName();
                String contentType = record.getS3().getObject().getContentType();

                System.out.printf("File uploaded: [%s] in bucket [%s] (type: %s)%n", key, bucket, contentType);
                Movie movie = movieRepository.findByTitle(key).orElseThrow(() -> new NotFoundException("movie not found"));
                movie.ready();
                movieRepository.save(movie);
            }

        } catch (Exception e) {
            throw new RuntimeException("Error while mark movie ready", e);
        }
    }
}
