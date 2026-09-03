#!/bin/bash
FILES=$(find src -name "*.java")

for file in $FILES; do
  perl -pi -e 's/\bPokeApiPokemon\b/PokeApiPokemonDTO/g' "$file"
  perl -pi -e 's/\bPokeApiListResponse\b/PokeApiListResponseDTO/g' "$file"
  perl -pi -e 's/\bPokeApiSpecies\b/PokeApiSpeciesDTO/g' "$file"
  perl -pi -e 's/\bPokeApiEvolutionChainData\b/PokeApiEvolutionChainDataDTO/g' "$file"
  perl -pi -e 's/\bPokeApiNamedResource\b/PokeApiNamedResourceDTO/g' "$file"
  perl -pi -e 's/\bPokeApiSprites\b/PokeApiSpritesDTO/g' "$file"
  perl -pi -e 's/\bPokeApiTypeSlot\b/PokeApiTypeSlotDTO/g' "$file"
  perl -pi -e 's/\bPokeApiAbilitySlot\b/PokeApiAbilitySlotDTO/g' "$file"
  perl -pi -e 's/\bPokeApiStatSlot\b/PokeApiStatSlotDTO/g' "$file"
  perl -pi -e 's/\bPokeApiFlavorText\b/PokeApiFlavorTextDTO/g' "$file"
  perl -pi -e 's/\bPokeApiEvolutionChainRef\b/PokeApiEvolutionChainRefDTO/g' "$file"
  perl -pi -e 's/\bPokeApiChainLink\b/PokeApiChainLinkDTO/g' "$file"
  perl -pi -e 's/\bPokeApiEvolutionDetail\b/PokeApiEvolutionDetailDTO/g' "$file"
done
