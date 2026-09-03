import { useState, useEffect } from 'react';
import { useParams, useNavigate } from 'react-router-dom';
import { Container, Grid, Image, Title, Text, Badge, Button, Tabs, Stack, Center, Loader, Group, Paper } from '@mantine/core';
import { IconArrowLeft, IconCloudDownload, IconChartBar, IconInfoCircle, IconGitBranch } from '@tabler/icons-react';
import { notifications } from '@mantine/notifications';
import { pokemonService } from '../services/pokemonService';
import { useAuth } from '../context/AuthContext';
import StatsDisplay from '../components/Pokemon/StatsDisplay';
import EvolutionChain from '../components/Pokemon/EvolutionChain';
import { TYPE_COLORS } from '../components/Pokemon/PokemonCard';

export default function PokemonDetailPage() {
  const { id } = useParams();
  const navigate = useNavigate();
  const { isAuthenticated } = useAuth();
  
  const [pokemon, setPokemon] = useState(null);
  const [loading, setLoading] = useState(true);
  const [syncing, setSyncing] = useState(false);
  const [isSynced, setIsSynced] = useState(false);

  useEffect(() => {
    const fetchDetail = async () => {
      try {
        const res = await pokemonService.getPokemonDetail(id);
        setPokemon(res.data);
      } catch (error) {
        notifications.show({
          title: 'Error',
          message: 'Failed to load Pokémon details',
          color: 'red',
        });
      } finally {
        setLoading(false);
      }
    };

    const checkSyncStatus = async () => {
      if (isAuthenticated) {
        try {
          await pokemonService.getLocalPokemonById(id);
          setIsSynced(true);
        } catch (error) {
          setIsSynced(false);
        }
      }
    };

    fetchDetail();
    checkSyncStatus();
  }, [id, isAuthenticated]);

  const handleSync = async () => {
    setSyncing(true);
    try {
      await pokemonService.syncPokemon(id);
      setIsSynced(true);
      notifications.show({
        title: 'Success!',
        message: `${pokemon.name} has been synced to your collection.`,
        color: 'green',
      });
    } catch (error) {
      notifications.show({
        title: 'Sync Failed',
        message: error.response?.data?.message || 'Failed to sync Pokémon',
        color: 'red',
      });
    } finally {
      setSyncing(false);
    }
  };

  if (loading) {
    return <Center h={400}><Loader size="xl" /></Center>;
  }

  if (!pokemon) {
    return <Center h={400}><Text>Pokémon not found.</Text></Center>;
  }

  return (
    <Container size="lg">
      <Button variant="subtle" leftSection={<IconArrowLeft size={16} />} onClick={() => navigate(-1)} mb="lg">
        Back
      </Button>

      <Paper shadow="sm" radius="md" p="xl" withBorder>
        <Grid gutter="xl">
          <Grid.Col span={{ base: 12, md: 5 }}>
            <Center bg="var(--mantine-color-gray-0)" style={{ borderRadius: 'var(--mantine-radius-md)' }} p="xl">
              <Image
                src={pokemon.spriteUrl || 'https://raw.githubusercontent.com/PokeAPI/sprites/master/sprites/pokemon/0.png'}
                h={300}
                fit="contain"
                alt={pokemon.name}
              />
            </Center>
            
            <Group justify="center" mt="lg">
              <Title order={2} tt="capitalize">{pokemon.name}</Title>
              <Text size="xl" c="dimmed">#{String(pokemon.id).padStart(3, '0')}</Text>
            </Group>

            <Group justify="center" mt="sm">
              {pokemon.types?.map(type => (
                <Badge key={type} size="lg" bg={TYPE_COLORS[type.toLowerCase()] || 'gray'}>{type}</Badge>
              ))}
            </Group>

            {isAuthenticated && (
              <Center mt="xl">
                <Button 
                  size="md" 
                  leftSection={isSynced ? null : <IconCloudDownload size={20} />} 
                  onClick={handleSync}
                  loading={syncing}
                  disabled={isSynced}
                  fullWidth
                >
                  {isSynced ? 'Already Synced' : 'Sync to My Collection'}
                </Button>
              </Center>
            )}
          </Grid.Col>

          <Grid.Col span={{ base: 12, md: 7 }}>
            <Tabs defaultValue="stats">
              <Tabs.List grow mb="md">
                <Tabs.Tab value="stats" leftSection={<IconChartBar size={16} />}>Stats</Tabs.Tab>
                <Tabs.Tab value="about" leftSection={<IconInfoCircle size={16} />}>About</Tabs.Tab>
                <Tabs.Tab value="evolution" leftSection={<IconGitBranch size={16} />}>Evolution</Tabs.Tab>
              </Tabs.List>

              <Tabs.Panel value="stats" pt="xs">
                <StatsDisplay stats={pokemon.stats} />
              </Tabs.Panel>

              <Tabs.Panel value="about" pt="xs">
                <Stack gap="md">
                  <Text>{pokemon.description || "No description available."}</Text>
                  
                  <Paper withBorder p="md" radius="md" bg="var(--mantine-color-gray-0)">
                    <Grid>
                      <Grid.Col span={6}>
                        <Text size="sm" c="dimmed">Height</Text>
                        <Text fw={500}>{pokemon.height ? pokemon.height / 10 : '?'} m</Text>
                      </Grid.Col>
                      <Grid.Col span={6}>
                        <Text size="sm" c="dimmed">Weight</Text>
                        <Text fw={500}>{pokemon.weight ? pokemon.weight / 10 : '?'} kg</Text>
                      </Grid.Col>
                      <Grid.Col span={6}>
                        <Text size="sm" c="dimmed">Base Exp</Text>
                        <Text fw={500}>{pokemon.baseExperience || '?'}</Text>
                      </Grid.Col>
                      <Grid.Col span={6}>
                        <Text size="sm" c="dimmed">Abilities</Text>
                        <Text fw={500} tt="capitalize">{pokemon.abilities?.join(', ') || 'None'}</Text>
                      </Grid.Col>
                    </Grid>
                  </Paper>
                </Stack>
              </Tabs.Panel>

              <Tabs.Panel value="evolution" pt="xs">
                <EvolutionChain chain={pokemon.evolutionChain} />
              </Tabs.Panel>
            </Tabs>
          </Grid.Col>
        </Grid>
      </Paper>
    </Container>
  );
}
