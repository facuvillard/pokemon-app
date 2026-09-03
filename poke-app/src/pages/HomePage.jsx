import { useState, useEffect } from 'react';
import { Title, TextInput, Grid, Pagination, Loader, Center, Container, Select, Group } from '@mantine/core';
import { IconSearch } from '@tabler/icons-react';
import { pokemonService } from '../services/pokemonService';
import PokemonCard from '../components/Pokemon/PokemonCard';

export default function HomePage() {
  const [pokemon, setPokemon] = useState([]);
  const [loading, setLoading] = useState(true);
  const [page, setPage] = useState(1);
  const [totalPages, setTotalPages] = useState(0);
  const [search, setSearch] = useState('');
  const [debouncedSearch, setDebouncedSearch] = useState('');
  const [pageSize, setPageSize] = useState('20');

  // Debounce search input
  useEffect(() => {
    const handler = setTimeout(() => {
      setDebouncedSearch(search);
      setPage(1); // Reset page on new search
    }, 500);
    return () => clearTimeout(handler);
  }, [search]);

  const fetchPokemon = async (pageNumber, query, size) => {
    setLoading(true);
    try {
      const res = query 
        ? await pokemonService.searchPokemon(query, pageNumber - 1, parseInt(size))
        : await pokemonService.listPokemon(pageNumber - 1, parseInt(size));
      setPokemon(res.data.content);
      setTotalPages(res.data.totalPages);
    } catch (error) {
      console.error("Error fetching pokemon", error);
    } finally {
      setLoading(false);
    }
  };

  useEffect(() => {
    fetchPokemon(page, debouncedSearch, pageSize);
  }, [page, debouncedSearch, pageSize]);

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
          {pokemon.length === 0 ? (
            <Center h={200}>
              <Title order={3} c="dimmed">No Pokémon found.</Title>
            </Center>
          ) : (
            <Grid>
              {pokemon.map(p => (
                <Grid.Col key={p.id} span={{ base: 12, sm: 6, md: 4, lg: 3 }}>
                  <PokemonCard pokemon={p} />
                </Grid.Col>
              ))}
            </Grid>
          )}

          {pokemon.length > 0 && (
            <Group justify="center" mt="xl" gap="xl">
              {totalPages > 1 && (
                <Pagination total={totalPages} value={page} onChange={setPage} />
              )}
              <Select
                value={pageSize}
                onChange={(val) => { setPageSize(val); setPage(1); }}
                data={[
                  { value: '20', label: '20 / page' },
                  { value: '50', label: '50 / page' },
                  { value: '100', label: '100 / page' }
                ]}
                w={120}
              />
            </Group>
          )}
        </>
      )}
    </Container>
  );
}
