package com.cineverse.movie_service.infracstructure.implement;

import com.cineverse.movie_service.domain.model.Actor;
import com.cineverse.movie_service.domain.repository.ActorRepository;
import com.cineverse.movie_service.infracstructure.repository.SpringDataActorRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.UUID;

@Repository
@RequiredArgsConstructor
public class JpaActorRepositoryImpl implements ActorRepository {

    private final SpringDataActorRepository springDataRepo;

    @Override
    public Optional<Actor> findById(String id) {
        return springDataRepo.findById(id);
    }
}
