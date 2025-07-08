package com.cineverse.movie_service.dto;

import com.cineverse.movie_service.domain.model.Actor;
import com.cineverse.movie_service.domain.model.Genre;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.Instant;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
public class MovieDTO {
    public String title;

    public String description;

    public Instant releaseDate;

    public List<Genre> genres;

    public List<Actor> actors;

    public String thumbnailUrl;

    public String movieFileName;

    public Boolean isPublic;
}
