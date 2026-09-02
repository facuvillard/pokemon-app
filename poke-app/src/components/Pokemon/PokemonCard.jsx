import { Card, Image, Text, Badge, Group, Stack } from '@mantine/core';
import { useNavigate } from 'react-router-dom';

export const TYPE_COLORS = {
  normal: '#A8A878', fire: '#F08030', water: '#6890F0', electric: '#F8D030',
  grass: '#78C850', ice: '#98D8D8', fighting: '#C03028', poison: '#A040A0',
  ground: '#E0C068', flying: '#A890F0', psychic: '#F85888', bug: '#A8B820',
  rock: '#B8A038', ghost: '#705898', dragon: '#7038F8', dark: '#705848',
  steel: '#B8B8D0', fairy: '#EE99AC',
};

export default function PokemonCard({ pokemon }) {
  const navigate = useNavigate();

  return (
    <Card
      shadow="sm"
      padding="lg"
      radius="md"
      withBorder
      style={{ cursor: 'pointer', transition: 'box-shadow 0.2s ease' }}
      onMouseEnter={(e) => (e.currentTarget.style.boxShadow = 'var(--mantine-shadow-md)')}
      onMouseLeave={(e) => (e.currentTarget.style.boxShadow = 'var(--mantine-shadow-sm)')}
      onClick={() => navigate(`/pokemon/${pokemon.id}`)}
    >
      <Card.Section bg="var(--mantine-color-gray-1)">
        <Image
          src={pokemon.spriteUrl || 'https://raw.githubusercontent.com/PokeAPI/sprites/master/sprites/pokemon/0.png'}
          h={140}
          alt={pokemon.name}
          fit="contain"
          fallbackSrc="https://placehold.co/140x140?text=No+Image"
        />
      </Card.Section>

      <Group justify="space-between" mt="md" mb="xs">
        <Text fw={700} tt="capitalize" size="lg">{pokemon.name}</Text>
        <Badge color="gray" variant="light">#{String(pokemon.id).padStart(3, '0')}</Badge>
      </Group>

      <Group gap="xs" mb="md">
        {pokemon.types?.map((type) => (
          <Badge key={type} bg={TYPE_COLORS[type.toLowerCase()] || 'gray'}>
            {type}
          </Badge>
        ))}
      </Group>

      <Stack gap="xs">
        <Text size="sm" c="dimmed">
          Weight: {pokemon.weight ? pokemon.weight / 10 : '?'} kg
        </Text>
        {pokemon.abilities && pokemon.abilities.length > 0 && (
          <Group gap={4}>
            <Text size="sm" c="dimmed">Abilities: </Text>
            {pokemon.abilities.map((ability) => (
              <Badge key={ability} variant="outline" size="sm" tt="capitalize">{ability}</Badge>
            ))}
          </Group>
        )}
      </Stack>
    </Card>
  );
}
