package com.cineverse.movie_service.application.command;

import com.cineverse.movie_service.domain.model.Genre;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.Instant;
import java.util.List;

@Getter
@NoArgsConstructor
public class UploadMovieCommand {

    @NotBlank
    private String title;

    @NotBlank
    private String description;

    @NotNull
    private Instant releaseDate;

    @NotNull
    @Size(min = 1)
    private List<Genre> genres;

    @NotNull
    @Size(min = 1)
    private List<String> actorIds;

    @NotBlank
    private String thumbnailUrl;

    @NotBlank
    private String movieFileName;

    @NotNull
    private Boolean isPublic;
}
