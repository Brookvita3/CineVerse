package com.cineverse.movie_service.dto.response;

import lombok.AllArgsConstructor;

import java.time.Instant;

@AllArgsConstructor
public class UploadMovieResponse {

    public String title;

    public String description;

    public boolean isPublic;

    public Instant createAt;

    public Instant updateAt;

}
