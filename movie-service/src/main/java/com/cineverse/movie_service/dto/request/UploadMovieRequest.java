package com.cineverse.movie_service.dto.request;

import com.cineverse.movie_service.domain.model.Gerne;
import jakarta.validation.constraints.NotBlank;
import lombok.Getter;

import java.time.Instant;
import java.util.List;

@Getter
public class UploadMovieRequest {

    @NotBlank
    public String title;

    @NotBlank
    public String description;

    public Instant releaseDate;

    public List<Gerne> genres;

    public List<String> actorIds;

    public String thumbnailUrl;

    public String videoFileName;

    public Boolean isPublic;
}
