package com.cineverse.movie_service.dto;

import lombok.Getter;
import lombok.Setter;

import java.util.Map;

@Getter
@Setter
public class OPAInput {
    private Map<String, Object> input;

    public OPAInput(Map<String, Object> input) {
        this.input = input;
    }
}
