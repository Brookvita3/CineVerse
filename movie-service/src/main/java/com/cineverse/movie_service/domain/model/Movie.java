package com.cineverse.movie_service.domain.model;

import com.aventrix.jnanoid.jnanoid.NanoIdUtils;
import com.cineverse.movie_service.dto.MovieDTO;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.Instant;
import java.util.List;

@Getter
@Entity
@NoArgsConstructor
@Table(name = "movie")
public class Movie {

    @Id
    private String id;

    private String title;

    private String description;

    private Instant releaseDate;

    @ElementCollection(fetch = FetchType.EAGER)
    @Enumerated(EnumType.STRING)
    @CollectionTable(name = "movie_genres", joinColumns = @JoinColumn(name = "movie_id"))
    @Column(name = "genre")
    private List<Genre> genres;

    @Enumerated(EnumType.STRING)
    private Status status;

    @ManyToMany
    @JoinTable(name = "movie_actor",
            joinColumns = @JoinColumn(name = "movie_id"),
            inverseJoinColumns = @JoinColumn(name = "actor_id"))
    private List<Actor> actors;

    private String thumbnailUrl;

    private String movieFileName;

    private Boolean isPublic;

    private Instant createdAt;

    private Instant updatedAt;

    private Movie(String title, String description, Instant releaseDate,
                  List<Genre> genres, List<Actor> actors,
                  String thumbnailUrl, String movieFileName, Boolean isPublic) {
        this.title = title;
        this.description = description;
        this.releaseDate = releaseDate;
        this.genres = genres;
        this.actors = actors;
        this.thumbnailUrl = thumbnailUrl;
        this.movieFileName = movieFileName;
        this.isPublic = isPublic;
        this.status = Status.PENDING;
    }

    @PrePersist
    protected void onCreate() {
        this.createdAt = Instant.now();
        this.updatedAt = Instant.now();
        if (id == null || id.isEmpty()) {
            this.id = NanoIdUtils.randomNanoId(NanoIdUtils.DEFAULT_NUMBER_GENERATOR, NanoIdUtils.DEFAULT_ALPHABET, 7);
        }
    }

    @PreUpdate
    protected void onUpdate() {
        this.updatedAt = Instant.now();
    }

    public static Movie upload(MovieDTO dto) {
        return new Movie(
                dto.title,
                dto.description,
                dto.releaseDate,
                dto.genres,
                dto.actors,
                dto.thumbnailUrl,
                dto.movieFileName,
                dto.isPublic
        );
    }

    public void ready() {
        this.status = Status.READY;
        this.updatedAt = Instant.now();
    }

    public void failed() {
        this.status = Status.FAILED;
        this.updatedAt = Instant.now();
    }

}
