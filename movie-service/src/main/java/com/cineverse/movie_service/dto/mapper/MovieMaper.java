package com.cineverse.movie_service.dto.mapper;

import com.cineverse.movie_service.domain.model.Movie;
import com.cineverse.movie_service.dto.response.UploadMovieResponse;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface MovieMaper {
    UploadMovieResponse fromMovie(Movie movie);
}
