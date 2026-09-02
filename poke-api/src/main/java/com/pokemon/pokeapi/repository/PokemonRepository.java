package com.pokemon.pokeapi.repository;

import com.pokemon.pokeapi.model.Pokemon;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PokemonRepository extends JpaRepository<Pokemon, Long> {
    Page<Pokemon> findByNameContainingIgnoreCase(String name, Pageable pageable);
}
