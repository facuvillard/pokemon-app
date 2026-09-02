package com.pokemon.pokeapi.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "pokemon_stats")
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class PokemonStat {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "stat_name")
    private String statName;

    @Column(name = "base_stat")
    private Integer baseStat;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "pokemon_id")
    @JsonIgnore
    @ToString.Exclude
    private Pokemon pokemon;
}
