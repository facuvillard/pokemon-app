import { useState, useEffect } from 'react';
import { Title, TextInput, Grid, Pagination, Loader, Center, Container } from '@mantine/core';
import { IconSearch } from '@tabler/icons-react';
import { pokemonService } from '../services/pokemonService';
import PokemonCard from '../components/Pokemon/PokemonCard';

export default function HomePage() {
  const [pokemon, setPokemon] = useState([]);
  const [loading, setLoading] = useState(true);
  const [page, setPage] = useState(1);
  const [totalPages, setTotalPages] = useState(0);
  const [search, setSearch] = useState('');

  const fetchPokemon = async (pageNumber) => {
    setLoading(true);
    try {
      // API expects 0-indexed page
      const res = await pokemonService.listPokemon(pageNumber - 1, 20);
      setPokemon(res.data.content);
      setTotalPages(res.data.totalPages);
    } catch (error) {
      console.error("Error fetching pokemon", error);
    } finally {
      setLoading(false);
    }
  };

  useEffect(() => {
    fetchPokemon(page);
  }, [page]);

  const filteredPokemon = pokemon.filter(p => p.name.toLowerCase().includes(search.toLowerCase()));

  return (
    <Container size="xl">
      <Title order={1} mb="lg" ta="center">Pokédex</Title>
      
      <TextInput
        placeholder="Search Pokémon..."
        leftSection={<IconSearch size={16} />}
        value={search}
        onChange={(e) => setSearch(e.currentTarget.value)}
        mb="xl"
        size="md"
        radius="md"
      />

      {loading ? (
        <Center h={400}>
          <Loader size="xl" type="dots" />
        </Center>
      ) : (
        <>
          <Grid>
            {filteredPokemon.map((p) => (
              <Grid.Col key={p.id} span={{ base: 12, xs: 12, sm: 6, md: 4, lg: 3 }}>
                <PokemonCard pokemon={p} />
              </Grid.Col>
            ))}
          </Grid>
          
          <Center mt="xl">
            <Pagination total={totalPages} value={page} onChange={setPage} color="pokeRed" />
          </Center>
        </>
      )}
    </Container>
  );
}
