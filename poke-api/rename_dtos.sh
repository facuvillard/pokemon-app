#!/bin/bash
FILES=$(find src -name "*.java")

for file in $FILES; do
  sed -i '' 's/\bPokeApiPokemon\b/PokeApiPokemonDTO/g' $file
  sed -i '' 's/\bPokeApiListResponse\b/PokeApiListResponseDTO/g' $file
  sed -i '' 's/\bPokeApiSpecies\b/PokeApiSpeciesDTO/g' $file
  sed -i '' 's/\bPokeApiEvolutionChainData\b/PokeApiEvolutionChainDataDTO/g' $file
  sed -i '' 's/\bPokeApiNamedResource\b/PokeApiNamedResourceDTO/g' $file
  sed -i '' 's/\bPokeApiSprites\b/PokeApiSpritesDTO/g' $file
  sed -i '' 's/\bPokeApiTypeSlot\b/PokeApiTypeSlotDTO/g' $file
  sed -i '' 's/\bPokeApiAbilitySlot\b/PokeApiAbilitySlotDTO/g' $file
  sed -i '' 's/\bPokeApiStatSlot\b/PokeApiStatSlotDTO/g' $file
  sed -i '' 's/\bPokeApiFlavorText\b/PokeApiFlavorTextDTO/g' $file
  sed -i '' 's/\bPokeApiEvolutionChainRef\b/PokeApiEvolutionChainRefDTO/g' $file
  sed -i '' 's/\bPokeApiChainLink\b/PokeApiChainLinkDTO/g' $file
  sed -i '' 's/\bPokeApiEvolutionDetail\b/PokeApiEvolutionDetailDTO/g' $file
done

mv src/main/java/com/pokemon/pokeapi/dto/external/PokeApiPokemon.java src/main/java/com/pokemon/pokeapi/dto/external/PokeApiPokemonDTO.java
mv src/main/java/com/pokemon/pokeapi/dto/external/PokeApiListResponse.java src/main/java/com/pokemon/pokeapi/dto/external/PokeApiListResponseDTO.java
mv src/main/java/com/pokemon/pokeapi/dto/external/PokeApiSpecies.java src/main/java/com/pokemon/pokeapi/dto/external/PokeApiSpeciesDTO.java
mv src/main/java/com/pokemon/pokeapi/dto/external/PokeApiEvolutionChainData.java src/main/java/com/pokemon/pokeapi/dto/external/PokeApiEvolutionChainDataDTO.java
mv src/main/java/com/pokemon/pokeapi/dto/external/PokeApiNamedResource.java src/main/java/com/pokemon/pokeapi/dto/external/PokeApiNamedResourceDTO.java

