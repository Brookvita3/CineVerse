package com.cineverse.movie_service.application.command;

import com.cineverse.movie_service.domain.model.Genre;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.Instant;
import java.util.List;

@Getter
@NoArgsConstructor
public class UpdateMovieCommand {

    private String title;

    private String description;

    private Instant releaseDate;

    private List<Genre> genres;

    private List<String> actorIds;

    private String thumbnailUrl;

    private String movieFileName;

    private Boolean isPublic;
}
