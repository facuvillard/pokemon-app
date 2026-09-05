import { AppShell, Burger, Group, Text, NavLink, Avatar, Divider, Stack, Box, Tooltip } from '@mantine/core';
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

const SIDEBAR_FULL = 220;
const SIDEBAR_MINI = 64;

export default function AppLayout() {
  const [collapsed, { toggle }] = useDisclosure(false);
  const { isAuthenticated, user, logout } = useAuth();
  const navigate = useNavigate();
  const location = useLocation();

  const handleLogout = () => {
    logout();
    navigate('/login');
  };

  const navItems = [
    { label: 'Pokédex', icon: IconHome, to: '/' },
    { label: 'My Pokémon', icon: IconList, to: '/my-pokemon' },
  ];

  const sidebarWidth = collapsed ? SIDEBAR_MINI : SIDEBAR_FULL;

  return (
    <AppShell
      navbar={{ width: sidebarWidth, breakpoint: 'xs' }}
      padding="md"
    >
      <AppShell.Navbar
        p={collapsed ? 'xs' : 'md'}
        style={{ transition: 'width 200ms ease', overflow: 'hidden' }}
      >
        {/* Header: burger + logo */}
        <AppShell.Section>
          <Group justify={collapsed ? 'center' : 'space-between'} mb="xl" mt={4} wrap="nowrap">
            {!collapsed && (
              <Group gap="xs" wrap="nowrap">
                <IconPokeball size={28} color="#f22529" />
                <Text size="lg" fw={800} style={{ whiteSpace: 'nowrap' }}>PokéApp</Text>
              </Group>
            )}
            <Burger
              opened={!collapsed}
              onClick={toggle}
              size="sm"
              aria-label="Toggle sidebar"
            />
          </Group>
        </AppShell.Section>

        {/* Nav Links */}
        <AppShell.Section grow>
          <Stack gap={4}>
            {navItems.map((item) => {
              const isActive = location.pathname === item.to;
              return collapsed ? (
                <Tooltip key={item.to} label={item.label} position="right" withArrow>
                  <NavLink
                    component={RouterNavLink}
                    to={item.to}
                    leftSection={<item.icon size={20} stroke={1.5} />}
                    active={isActive}
                    radius="md"
                    style={{ justifyContent: 'center', padding: '10px 0' }}
                  />
                </Tooltip>
              ) : (
                <NavLink
                  key={item.to}
                  component={RouterNavLink}
                  to={item.to}
                  label={item.label}
                  leftSection={<item.icon size={18} stroke={1.5} />}
                  active={isActive}
                  radius="md"
                  style={{ fontWeight: 500 }}
                />
              );
            })}
          </Stack>
        </AppShell.Section>

        <Divider my="sm" />

        {/* Bottom: User info + actions */}
        <AppShell.Section>
          {collapsed ? (
            <Stack align="center" gap="sm">
              <ThemeToggle />
              <Tooltip label="Logout" position="right" withArrow>
                <NavLink
                  leftSection={<IconLogout size={20} stroke={1.5} />}
                  onClick={handleLogout}
                  radius="md"
                  color="red"
                  style={{ justifyContent: 'center', padding: '10px 0' }}
                />
              </Tooltip>
            </Stack>
          ) : (
            <>
              <Group justify="space-between" align="center" mb="sm">
                <Group gap="sm" wrap="nowrap">
                  <Avatar radius="xl" size="sm" color="red">
                    {user?.username?.[0]?.toUpperCase() || <IconUser size={14} />}
                  </Avatar>
                  <Box style={{ overflow: 'hidden' }}>
                    <Text size="sm" fw={600} truncate>{user?.username}</Text>
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
            </>
          )}
        </AppShell.Section>
      </AppShell.Navbar>

      <AppShell.Main>
        <Outlet />
      </AppShell.Main>
    </AppShell>
  );
}
