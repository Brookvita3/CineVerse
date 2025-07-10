package com.cineverse.movie_service.domain.repository;

import com.cineverse.movie_service.domain.model.Actor;

import java.util.Optional;
import java.util.UUID;

public interface ActorRepository {
    Optional<Actor> findById(String id);
}
