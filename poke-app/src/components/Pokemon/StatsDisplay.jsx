import { Progress, Text, Group, Stack } from '@mantine/core';

export default function StatsDisplay({ stats }) {
  if (!stats) return null;

  const getProgressColor = (value) => {
    if (value < 50) return 'red';
    if (value < 80) return 'yellow';
    return 'green';
  };

  const capitalize = (str) => str.charAt(0).toUpperCase() + str.slice(1);

  // Map API stat names to readable labels if needed, or just capitalize
  const statLabels = {
    hp: 'HP',
    attack: 'Attack',
    defense: 'Defense',
    'special-attack': 'Sp. Atk',
    'special-defense': 'Sp. Def',
    speed: 'Speed'
  };

  return (
    <Stack gap="sm">
      {stats.map((stat) => (
        <Group key={stat.name} wrap="nowrap">
          <Text w={80} size="sm" fw={500}>{statLabels[stat.name] || capitalize(stat.name)}</Text>
          <Text w={30} size="sm" ta="right">{stat.baseStat}</Text>
          <Progress
            value={(stat.baseStat / 255) * 100}
            color={getProgressColor(stat.baseStat)}
            style={{ flex: 1 }}
            size="md"
            radius="xl"
          />
        </Group>
      ))}
    </Stack>
  );
}
