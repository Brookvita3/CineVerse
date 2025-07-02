package com.cineverse.movie_service.domain.model;

import com.cineverse.movie_service.dto.request.UpdateMovieRequest;
import com.cineverse.movie_service.dto.request.UploadMovieRequest;
import lombok.AccessLevel;
import lombok.Getter;

import java.time.Instant;
import java.util.List;
import java.util.UUID;

@Getter
public class Movie {

    private final String id;

    private String title;

    private String description;

    private Instant releaseDate;

    private List<Gerne> genres;

    private List<String> actorIds;

    private String thumbnailUrl;

    private String videoFileName;

    private Boolean isPublic;

    private final Instant createdAt;

    private Instant updatedAt;

    private Movie(String title, String description, Instant releaseDate,
                 List<Gerne> genres, List<String> actorIds,
                 String thumbnailUrl, String videoFileName, Boolean isPublic) {

        this.id = UUID.randomUUID().toString();
        this.title = title;
        this.description = description;
        this.releaseDate = releaseDate;
        this.genres = genres;
        this.actorIds = actorIds;
        this.thumbnailUrl = thumbnailUrl;
        this.videoFileName = videoFileName;
        this.isPublic = isPublic;
        this.createdAt = Instant.now();
        this.updatedAt = Instant.now();

    }

    public static Movie create(UploadMovieRequest request) {
        return new Movie(
                request.title,
                request.description,
                request.releaseDate,
                request.genres,
                request.actorIds,
                request.thumbnailUrl,
                request.videoFileName,
                request.isPublic
        );
    }

    public void update(UpdateMovieRequest request) {
        if (request.getTitle() != null) {
            this.title = request.getTitle();
        }
        if (request.getDescription() != null) {
            this.description = request.getDescription();
        }
        if (request.getReleaseDate() != null) {
            this.releaseDate = request.getReleaseDate();
        }
        if (request.getGenres() != null) {
            this.genres = request.getGenres();
        }
        if (request.getActorIds() != null) {
            this.actorIds = request.getActorIds();
        }
        if (request.getThumbnailUrl() != null) {
            this.thumbnailUrl = request.getThumbnailUrl();
        }
        if (request.getVideoFileName() != null) {
            this.videoFileName = request.getVideoFileName();
        }
        if (request.getIsPublic() != null) {
            this.isPublic = request.getIsPublic();
        }
    }


}
