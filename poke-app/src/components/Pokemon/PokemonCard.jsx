import { Card, Image, Text, Badge, Group, Stack, ActionIcon, Tooltip } from '@mantine/core';
import { useNavigate } from 'react-router-dom';
import { IconCloudDownload, IconCheck } from '@tabler/icons-react';

export const TYPE_COLORS = {
  normal: '#A8A878', fire: '#F08030', water: '#6890F0', electric: '#F8D030',
  grass: '#78C850', ice: '#98D8D8', fighting: '#C03028', poison: '#A040A0',
  ground: '#E0C068', flying: '#A890F0', psychic: '#F85888', bug: '#A8B820',
  rock: '#B8A038', ghost: '#705898', dragon: '#7038F8', dark: '#705848',
  steel: '#B8B8D0', fairy: '#EE99AC',
};

export default function PokemonCard({ pokemon, synced = false, onSync }) {
  const navigate = useNavigate();

  const handleSync = async (e) => {
    e.stopPropagation();
    if (onSync) await onSync(pokemon.id);
  };

  return (
    <Card
      shadow="sm"
      padding="lg"
      radius="md"
      withBorder
      style={{ cursor: 'pointer', transition: 'transform 0.15s ease, box-shadow 0.15s ease' }}
      onMouseEnter={(e) => {
        e.currentTarget.style.transform = 'translateY(-3px)';
        e.currentTarget.style.boxShadow = 'var(--mantine-shadow-lg)';
      }}
      onMouseLeave={(e) => {
        e.currentTarget.style.transform = 'translateY(0)';
        e.currentTarget.style.boxShadow = '';
      }}
      onClick={() => navigate(`/pokemon/${pokemon.id}`)}
    >
      <Card.Section bg="var(--mantine-color-default-hover)" style={{ position: 'relative' }}>
        <Image
          src={pokemon.spriteUrl || 'https://raw.githubusercontent.com/PokeAPI/sprites/master/sprites/pokemon/0.png'}
          h={140}
          alt={pokemon.name}
          fit="contain"
          fallbackSrc="https://placehold.co/140x140?text=No+Image"
        />
        {/* Sync button — top right of the sprite */}
        {onSync && (
          <Tooltip
            label={synced ? 'Already synced' : 'Sync to My Collection'}
            position="left"
            withArrow
          >
            <ActionIcon
              variant={synced ? 'filled' : 'light'}
              color={synced ? 'green' : 'blue'}
              size="md"
              radius="md"
              disabled={synced}
              onClick={handleSync}
              style={{
                position: 'absolute',
                top: 8,
                right: 8,
              }}
            >
              {synced ? <IconCheck size={16} /> : <IconCloudDownload size={16} />}
            </ActionIcon>
          </Tooltip>
        )}
      </Card.Section>

      <Group justify="space-between" mt="md" mb="xs">
        <Text fw={700} tt="capitalize" size="lg">{pokemon.name}</Text>
        <Badge color="gray" variant="light">#{String(pokemon.id).padStart(3, '0')}</Badge>
      </Group>

      <Group gap="xs" mb="md">
        {pokemon.types?.map((type) => (
          <Badge
            key={type}
            bg={TYPE_COLORS[type.toLowerCase()] || 'gray'}
            style={{ color: '#fff', textShadow: '0 1px 2px rgba(0,0,0,0.4)' }}
          >
            {type}
          </Badge>
        ))}
      </Group>

      <Stack gap="xs">
        <Text size="sm" c="dimmed">
          Weight: {pokemon.weight ? pokemon.weight / 10 : '?'} kg
        </Text>
        {pokemon.abilities && pokemon.abilities.length > 0 && (
          <Group gap={4} wrap="wrap">
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
