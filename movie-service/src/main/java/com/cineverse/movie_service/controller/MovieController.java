package com.cineverse.movie_service.controller;

import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.GetMapping;

@RestController
public class MovieController {
    @GetMapping("/movies/ping")
    public String ping() {
        return "🎬 Hello from Spring Boot Movie Service!";
    }
}