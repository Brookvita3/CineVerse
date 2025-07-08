package com.cineverse.movie_service.application.command;

import com.cineverse.movie_service.domain.model.Genre;
import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.Instant;
import java.util.List;
import java.util.UUID;

@Getter
@NoArgsConstructor
public class UploadMovieCommand {

    @NotBlank
    private String title;

    @NotBlank
    private String description;

    private Instant releaseDate;

    private List<Genre> genres;

    private List<UUID> actorIds;

    private String thumbnailUrl;

    private String movieFileName;

    private Boolean isPublic;
}
