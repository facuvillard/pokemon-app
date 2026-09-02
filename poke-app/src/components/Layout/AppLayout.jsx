import { AppShell, Burger, Group, Button, Text } from '@mantine/core';
import { useDisclosure } from '@mantine/hooks';
import { NavLink as RouterNavLink, Outlet, useNavigate } from 'react-router-dom';
import { useAuth } from '../../context/AuthContext';
import ThemeToggle from './ThemeToggle';
import { IconPokeball } from '@tabler/icons-react';

export default function AppLayout() {
  const [opened, { toggle, close }] = useDisclosure();
  const { isAuthenticated, user, logout } = useAuth();
  const navigate = useNavigate();

  const handleLogout = () => {
    logout();
    navigate('/');
    close();
  };

  return (
    <AppShell
      header={{ height: 60 }}
      navbar={{
        width: 250,
        breakpoint: 'sm',
        collapsed: { desktop: true, mobile: !opened },
      }}
      padding="md"
    >
      <AppShell.Header>
        <Group h="100%" px="md" justify="space-between">
          <Group>
            <Burger opened={opened} onClick={toggle} hiddenFrom="sm" size="sm" />
            <Group component={RouterNavLink} to="/" style={{ textDecoration: 'none', color: 'inherit' }} gap="xs">
              <IconPokeball size={30} color="#f22529" />
              <Text size="xl" fw={700}>PokéApp</Text>
            </Group>
          </Group>

          <Group visibleFrom="sm" gap="lg">
            <Button component={RouterNavLink} to="/" variant="subtle">Home</Button>
            {isAuthenticated && (
              <Button component={RouterNavLink} to="/my-pokemon" variant="subtle">My Pokémon</Button>
            )}
          </Group>

          <Group>
            <ThemeToggle />
            <Group visibleFrom="sm">
              {isAuthenticated ? (
                <Group>
                  <Text fw={500}>{user?.username}</Text>
                  <Button variant="light" onClick={handleLogout}>Logout</Button>
                </Group>
              ) : (
                <Group>
                  <Button component={RouterNavLink} to="/login" variant="light">Login</Button>
                  <Button component={RouterNavLink} to="/register">Register</Button>
                </Group>
              )}
            </Group>
          </Group>
        </Group>
      </AppShell.Header>

      <AppShell.Navbar py="md" px={4}>
        <Button component={RouterNavLink} to="/" variant="subtle" fullWidth onClick={close} mb="sm">Home</Button>
        {isAuthenticated && (
          <Button component={RouterNavLink} to="/my-pokemon" variant="subtle" fullWidth onClick={close} mb="sm">My Pokémon</Button>
        )}
        {isAuthenticated ? (
          <>
            <Text fw={500} ta="center" my="sm">User: {user?.username}</Text>
            <Button variant="light" fullWidth onClick={handleLogout}>Logout</Button>
          </>
        ) : (
          <>
            <Button component={RouterNavLink} to="/login" variant="light" fullWidth onClick={close} mb="sm">Login</Button>
            <Button component={RouterNavLink} to="/register" fullWidth onClick={close}>Register</Button>
          </>
        )}
      </AppShell.Navbar>

      <AppShell.Main>
        <Outlet />
      </AppShell.Main>
    </AppShell>
  );
}
