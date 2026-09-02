import { createTheme } from '@mantine/core';

const pokeRed = [
  '#ffe8e8', '#ffd1d1', '#fba0a1', '#f76c6d', '#f34144',
  '#f22529', '#f2141a', '#d7050f', '#c0000a', '#a80004',
];

export const theme = createTheme({
  primaryColor: 'pokeRed',
  colors: { pokeRed },
  fontFamily: 'Inter, system-ui, -apple-system, sans-serif',
  defaultRadius: 'md',
});
