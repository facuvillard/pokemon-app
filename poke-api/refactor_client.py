import os

base_dir = "/Users/facuvillard/.gemini/antigravity/scratch/pokemon-app/poke-api/src/main/java/com/pokemon/pokeapi"
test_dir = "/Users/facuvillard/.gemini/antigravity/scratch/pokemon-app/poke-api/src/test/java/com/pokemon/pokeapi"

# 1. Create client package and class
with open(f"{base_dir}/service/PokeApiService.java", "r") as f:
    content = f.read()

content = content.replace("package com.pokemon.pokeapi.service;", "package com.pokemon.pokeapi.client;\n\nimport org.springframework.stereotype.Component;")
content = content.replace("public class PokeApiService", "public class PokeApiClient")
content = content.replace("@Service", "@Component")

os.makedirs(f"{base_dir}/client", exist_ok=True)
with open(f"{base_dir}/client/PokeApiClient.java", "w") as f:
    f.write(content)

os.remove(f"{base_dir}/service/PokeApiService.java")

# 2. Update PokemonService
with open(f"{base_dir}/service/PokemonService.java", "r") as f:
    content = f.read()
content = content.replace("private final PokeApiService pokeApiService;", "private final PokeApiClient pokeApiClient;")
content = content.replace("pokeApiService.", "pokeApiClient.")
content = content.replace("import com.pokemon.pokeapi.utils.PokeApiMapper;", "import com.pokemon.pokeapi.utils.PokeApiMapper;\nimport com.pokemon.pokeapi.client.PokeApiClient;")
with open(f"{base_dir}/service/PokemonService.java", "w") as f:
    f.write(content)

# 3. Update LocalPokemonService
with open(f"{base_dir}/service/LocalPokemonService.java", "r") as f:
    content = f.read()
content = content.replace("private final PokeApiService pokeApiService;", "private final PokeApiClient pokeApiClient;")
content = content.replace("pokeApiService.", "pokeApiClient.")
content = content.replace("import com.pokemon.pokeapi.utils.PokeApiMapper;", "import com.pokemon.pokeapi.utils.PokeApiMapper;\nimport com.pokemon.pokeapi.client.PokeApiClient;")
with open(f"{base_dir}/service/LocalPokemonService.java", "w") as f:
    f.write(content)

# 4. Update Tests
for test_file in [f"{test_dir}/service/PokemonServiceTest.java", f"{test_dir}/service/LocalPokemonServiceTest.java"]:
    with open(test_file, "r") as f:
        content = f.read()
    content = content.replace("PokeApiService", "PokeApiClient")
    content = content.replace("pokeApiService", "pokeApiClient")
    content = content.replace("import com.pokemon.pokeapi.service.PokeApiClient;", "import com.pokemon.pokeapi.client.PokeApiClient;")
    with open(test_file, "w") as f:
        f.write(content)

print("Refactor script complete.")
