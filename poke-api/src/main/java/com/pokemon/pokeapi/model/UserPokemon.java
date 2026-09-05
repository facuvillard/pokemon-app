package com.pokemon.pokeapi.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Entity
@Table(name = "user_pokemon",
    uniqueConstraints = @UniqueConstraint(columnNames = {"user_id", "pokemon_id"}))
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class UserPokemon {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "pokemon_id", nullable = false)
    private Pokemon pokemon;

    // Custom fields per user
    @Column(name = "custom_name")
    private String customName;
    
    private String region;
    
    @Column(name = "classification_tag")
    private String classificationTag;
    
    @Column(columnDefinition = "TEXT")
    private String notes;
    
    @Column(name = "synced_at")
    private LocalDateTime syncedAt;
    
    @Column(name = "updated_at")
    private LocalDateTime updatedAt;
}
