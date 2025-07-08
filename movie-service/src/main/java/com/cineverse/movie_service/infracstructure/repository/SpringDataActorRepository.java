package com.cineverse.movie_service.infracstructure.repository;

import com.cineverse.movie_service.domain.model.Actor;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface SpringDataActorRepository extends JpaRepository<Actor, UUID> {
}
