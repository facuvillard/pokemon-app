import os

test_dir = "/Users/facuvillard/.gemini/antigravity/scratch/pokemon-app/poke-api/src/test/java/com/pokemon/pokeapi"

for test_file in [f"{test_dir}/service/PokemonServiceTest.java", f"{test_dir}/service/LocalPokemonServiceTest.java"]:
    with open(test_file, "r") as f:
        content = f.read()
    
    # If the import is missing entirely, we'll add it right after package declaration
    if "import com.pokemon.pokeapi.client.PokeApiClient;" not in content:
        content = content.replace("package com.pokemon.pokeapi.service;", "package com.pokemon.pokeapi.service;\n\nimport com.pokemon.pokeapi.client.PokeApiClient;")
        
    with open(test_file, "w") as f:
        f.write(content)

print("Imports fixed.")
