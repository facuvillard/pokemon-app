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
  const { user, logout } = useAuth();
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

  return (
    <AppShell
      navbar={{ width: collapsed ? SIDEBAR_MINI : SIDEBAR_FULL, breakpoint: 'xs' }}
      padding="md"
    >
      <AppShell.Navbar
        style={{ transition: 'width 200ms ease', overflow: 'hidden' }}
        p={0}
      >
        {/* Inner wrapper keeps consistent padding but allows centering */}
        <Box
          style={{
            display: 'flex',
            flexDirection: 'column',
            height: '100%',
            padding: '16px 8px',
          }}
        >
          {/* Header: burger + logo */}
          <Box mb="xl">
            <Group
              justify={collapsed ? 'center' : 'space-between'}
              wrap="nowrap"
              px={collapsed ? 0 : 4}
            >
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
          </Box>

          {/* Nav Links */}
          <Box style={{ flex: 1 }}>
            <Stack gap={4}>
              {navItems.map((item) => {
                const isActive = location.pathname === item.to;
                return collapsed ? (
                  <Tooltip key={item.to} label={item.label} position="right" withArrow>
                    <Box
                      component={RouterNavLink}
                      to={item.to}
                      style={{
                        display: 'flex',
                        alignItems: 'center',
                        justifyContent: 'center',
                        width: '100%',
                        height: 40,
                        borderRadius: 8,
                        textDecoration: 'none',
                        color: isActive ? 'var(--mantine-color-red-6)' : 'inherit',
                        backgroundColor: isActive ? 'var(--mantine-color-red-0)' : 'transparent',
                      }}
                    >
                      <item.icon size={20} stroke={1.5} />
                    </Box>
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
          </Box>

          <Divider my="sm" />

          {/* Bottom section */}
          {collapsed ? (
            <Stack align="center" gap="sm">
              <ThemeToggle />
              <Tooltip label="Logout" position="right" withArrow>
                <Box
                  onClick={handleLogout}
                  style={{
                    display: 'flex',
                    alignItems: 'center',
                    justifyContent: 'center',
                    width: '100%',
                    height: 40,
                    borderRadius: 8,
                    cursor: 'pointer',
                    color: 'var(--mantine-color-red-6)',
                  }}
                >
                  <IconLogout size={20} stroke={1.5} />
                </Box>
              </Tooltip>
            </Stack>
          ) : (
            <>
              <Group justify="space-between" align="center" mb="sm" px={4}>
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
        </Box>
      </AppShell.Navbar>

      <AppShell.Main>
        <Outlet />
      </AppShell.Main>
    </AppShell>
  );
}
