package com.pokemon.pokeapi.dto.pokemon;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotEmpty;
import java.util.List;

public record SyncBatchRequestDTO(
    @NotEmpty List<@Min(1) Long> ids
) {}
