package com.pokemon.pokeapi.model;

import jakarta.persistence.*;
import lombok.*;
import java.time.LocalDateTime;
import java.util.List;

@Entity
@Table(name = "pokemon")
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Pokemon {
    @Id
    private Long id;

    private String name;

    @Column(name = "sprite_url")
    private String spriteUrl;

    private Integer weight;
    private Integer height;

    @Column(name = "base_experience")
    private Integer baseExperience;

    @Column(columnDefinition = "TEXT")
    private String description;

    @ElementCollection
    @CollectionTable(name = "pokemon_types", joinColumns = @JoinColumn(name = "pokemon_id"))
    @Column(name = "type_name")
    private List<String> types;

    @ElementCollection
    @CollectionTable(name = "pokemon_abilities", joinColumns = @JoinColumn(name = "pokemon_id"))
    @Column(name = "ability_name")
    private List<String> abilities;

    @OneToMany(mappedBy = "pokemon", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<PokemonStat> stats;

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
