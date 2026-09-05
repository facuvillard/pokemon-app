import { AppShell, Burger, Group, Text, NavLink, Avatar, Divider, Stack, Box, UnstyledButton } from '@mantine/core';
import { useDisclosure } from '@mantine/hooks';
import { NavLink as RouterNavLink, Outlet, useNavigate, useLocation } from 'react-router-dom';
import { useAuth } from '../../context/AuthContext';
import ThemeToggle from './ThemeToggle';
import {
  IconPokeball,
  IconHome,
  IconList,
  IconLogout,
  IconUser,
} from '@tabler/icons-react';

export default function AppLayout() {
  const [opened, { toggle, close }] = useDisclosure();
  const { isAuthenticated, user, logout } = useAuth();
  const navigate = useNavigate();
  const location = useLocation();

  const handleLogout = () => {
    logout();
    navigate('/login');
    close();
  };

  const navItems = [
    { label: 'Pokédex', icon: IconHome, to: '/' },
    { label: 'My Pokémon', icon: IconList, to: '/my-pokemon' },
  ];

  return (
    <AppShell
      navbar={{
        width: 240,
        breakpoint: 'sm',
        collapsed: { mobile: !opened },
      }}
      padding="md"
    >
      {/* Mobile burger button — shown only on small screens as a floating header */}
      <AppShell.Header hiddenFrom="sm" h={56}>
        <Group h="100%" px="md" justify="space-between">
          <Burger opened={opened} onClick={toggle} size="sm" />
          <Group gap="xs">
            <IconPokeball size={24} color="#f22529" />
            <Text fw={700} size="lg">PokéApp</Text>
          </Group>
          <ThemeToggle />
        </Group>
      </AppShell.Header>

      <AppShell.Navbar p="md">
        {/* Logo */}
        <AppShell.Section>
          <Group gap="xs" mb="xl" mt={4}>
            <IconPokeball size={32} color="#f22529" />
            <Text size="xl" fw={800} style={{ letterSpacing: -0.5 }}>PokéApp</Text>
          </Group>
        </AppShell.Section>

        {/* Nav Links */}
        <AppShell.Section grow>
          <Stack gap={4}>
            {navItems.map((item) => (
              <NavLink
                key={item.to}
                component={RouterNavLink}
                to={item.to}
                label={item.label}
                leftSection={<item.icon size={18} stroke={1.5} />}
                active={location.pathname === item.to}
                onClick={close}
                radius="md"
                style={{ fontWeight: 500 }}
              />
            ))}
          </Stack>
        </AppShell.Section>

        <Divider my="sm" />

        {/* Bottom: User info + actions */}
        <AppShell.Section>
          <Group justify="space-between" align="center" mb="sm">
            <Group gap="sm">
              <Avatar radius="xl" size="sm" color="pokeRed">
                {user?.username?.[0]?.toUpperCase() || <IconUser size={14} />}
              </Avatar>
              <Box>
                <Text size="sm" fw={600}>{user?.username}</Text>
                <Text size="xs" c="dimmed">{user?.role || 'USER'}</Text>
              </Box>
            </Group>
            <ThemeToggle />
          </Group>

          <NavLink
            label="Logout"
            leftSection={<IconLogout size={18} stroke={1.5} />}
            onClick={handleLogout}
            radius="md"
            color="red"
            style={{ fontWeight: 500 }}
          />
        </AppShell.Section>
      </AppShell.Navbar>

      <AppShell.Main>
        <Outlet />
      </AppShell.Main>
    </AppShell>
  );
}
