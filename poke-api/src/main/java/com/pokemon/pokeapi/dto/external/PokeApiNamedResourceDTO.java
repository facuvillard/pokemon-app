package com.pokemon.pokeapi.dto.external;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@JsonIgnoreProperties(ignoreUnknown = true)
public record PokeApiNamedResourceDTO(String name, String url) {
    public Long extractId() {
        if (url == null) return null;
        String[] parts = url.split("/");
        try {
            return Long.parseLong(parts[parts.length - 1]);
        } catch (NumberFormatException e) {
            return null;
        }
    }
}
