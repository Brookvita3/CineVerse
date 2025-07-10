package com.cineverse.movie_service.service;

import com.cineverse.movie_service.domain.model.Movie;
import com.cineverse.movie_service.domain.repository.MovieRepository;
import com.cineverse.movie_service.exception.NotFoundException;
import io.minio.GetPresignedObjectUrlArgs;
import io.minio.MinioClient;
import io.minio.http.Method;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.concurrent.TimeUnit;

@Service
@RequiredArgsConstructor
@Slf4j
public class MinioService {

    @Value("${minio.bucket}")
    private String bucketName;

    private final MovieRepository movieRepository;
    private final MinioClient minioClient;

    public String generatePresignedUploadUrl(String movieId, Integer duration) {
        Movie movie = movieRepository.findById(movieId)
                .orElseThrow(() -> new NotFoundException("Movie not found"));
        try {
            return minioClient.getPresignedObjectUrl(
                    GetPresignedObjectUrlArgs.builder()
                            .method(Method.PUT)
                            .bucket(bucketName)
                            .object(movie.getMovieFileName())
                            .expiry(duration, TimeUnit.MINUTES)
                            .build()
            );
        } catch (Exception e) {
            log.error(e.getMessage());
            throw new RuntimeException("Failed to generate signed upload URL", e);
        }
    }

}
