package com.pokemon.pokeapi.dto.error;

import java.time.LocalDateTime;
import java.util.Map;

public record ErrorResponseDTO(int status, String message, LocalDateTime timestamp, Map<String, String> details) {
    public ErrorResponseDTO(int status, String message) {
        this(status, message, LocalDateTime.now(), null);
    }
}
