import { Group, Stack, Image, Text, ActionIcon } from '@mantine/core';
import { IconArrowRight, IconArrowDown } from '@tabler/icons-react';
import { useNavigate } from 'react-router-dom';

export default function EvolutionChain({ chain }) {
  const navigate = useNavigate();

  if (!chain || chain.length === 0) {
    return <Text c="dimmed">No evolution data available.</Text>;
  }

  return (
    <Group justify="center" gap="xl" wrap="wrap">
      {chain.map((evo, index) => (
        <Group key={evo.id} gap="xl">
          <Stack
            align="center"
            gap="xs"
            style={{ cursor: 'pointer' }}
            onClick={() => navigate(`/pokemon/${evo.id}`)}
          >
            <Image
              src={evo.spriteUrl || 'https://raw.githubusercontent.com/PokeAPI/sprites/master/sprites/pokemon/0.png'}
              h={100}
              w={100}
              fit="contain"
              fallbackSrc="https://placehold.co/100x100?text=No+Image"
              style={{ borderRadius: '50%', background: 'var(--mantine-color-gray-1)', padding: 10 }}
            />
            <Text fw={600} tt="capitalize">{evo.name}</Text>
          </Stack>

          {index < chain.length - 1 && (
            <Stack align="center" gap={4}>
              <ActionIcon variant="transparent" c="dimmed" size="xl" className="desktop-arrow">
                <IconArrowRight size={32} />
              </ActionIcon>
              {chain[index + 1].minLevel && (
                <Text size="xs" c="dimmed" fw={500}>Lv. {chain[index + 1].minLevel}</Text>
              )}
            </Stack>
          )}
        </Group>
      ))}
    </Group>
  );
}
