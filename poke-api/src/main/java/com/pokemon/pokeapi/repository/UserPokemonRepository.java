package com.pokemon.pokeapi.repository;

import com.pokemon.pokeapi.model.Pokemon;
import com.pokemon.pokeapi.model.User;
import com.pokemon.pokeapi.model.UserPokemon;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface UserPokemonRepository extends JpaRepository<UserPokemon, Long> {

    Page<UserPokemon> findByUser(User user, Pageable pageable);

    boolean existsByUserAndPokemon(User user, Pokemon pokemon);

    Optional<UserPokemon> findByUserAndPokemon_Id(User user, Long pokemonId);

    @Query("SELECT up.pokemon.id FROM UserPokemon up WHERE up.user = :user")
    List<Long> findSyncedPokemonIdsByUser(@Param("user") User user);

    @Query("SELECT up FROM UserPokemon up WHERE up.user = :user AND (" +
           "LOWER(up.pokemon.name) LIKE LOWER(CONCAT('%', :query, '%')) OR " +
           "LOWER(COALESCE(up.customName, '')) LIKE LOWER(CONCAT('%', :query, '%')) OR " +
           "LOWER(COALESCE(up.region, '')) LIKE LOWER(CONCAT('%', :query, '%')) OR " +
           "LOWER(COALESCE(up.classificationTag, '')) LIKE LOWER(CONCAT('%', :query, '%')) OR " +
           "LOWER(COALESCE(up.notes, '')) LIKE LOWER(CONCAT('%', :query, '%')) OR " +
           "CAST(up.pokemon.id AS string) LIKE CONCAT('%', :query, '%'))")
    Page<UserPokemon> searchByUserAndAllFields(@Param("user") User user, @Param("query") String query, Pageable pageable);
}
