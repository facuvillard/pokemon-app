import { useState, useEffect } from 'react';
import { Title, Table, Image, Button, Group, Center, Loader, Text, Pagination, ActionIcon, Container, Badge, TextInput, Tooltip } from '@mantine/core';
import { IconEdit, IconTrash, IconSearch } from '@tabler/icons-react';
import { modals } from '@mantine/modals';
import { notifications } from '@mantine/notifications';
import { Link, useNavigate } from 'react-router-dom';
import { pokemonService } from '../services/pokemonService';
import EditPokemonModal from '../components/Pokemon/EditPokemonModal';

export default function MyPokemonPage() {
  const navigate = useNavigate();
  const [pokemon, setPokemon] = useState([]);
  const [loading, setLoading] = useState(true);
  const [page, setPage] = useState(1);
  const [totalPages, setTotalPages] = useState(0);

  const [search, setSearch] = useState('');
  const [debouncedSearch, setDebouncedSearch] = useState('');

  const [editingPokemon, setEditingPokemon] = useState(null);

  // Debounce search input
  useEffect(() => {
    const handler = setTimeout(() => {
      setDebouncedSearch(search);
      setPage(1); // Reset page on new search
    }, 500);
    return () => clearTimeout(handler);
  }, [search]);

  const fetchLocalPokemon = async (pageNumber, query) => {
    setLoading(true);
    try {
      const res = query
        ? await pokemonService.searchLocalPokemon(query, pageNumber - 1, 20)
        : await pokemonService.getLocalPokemon(pageNumber - 1, 20);
      setPokemon(res.data.content);
      setTotalPages(res.data.totalPages);
    } catch (error) {
      console.error("Error fetching local pokemon", error);
      notifications.show({
        title: 'Error',
        message: 'Failed to fetch your collection.',
        color: 'red'
      });
    } finally {
      setLoading(false);
    }
  };

  useEffect(() => {
    fetchLocalPokemon(page, debouncedSearch);
  }, [page, debouncedSearch]);

  const handleDelete = (id, name) => {
    modals.openConfirmModal({
      title: 'Delete Pokémon',
      centered: true,
      children: (
        <Text size="sm">
          Are you sure you want to delete <strong>{name}</strong> from your collection? This action cannot be undone.
        </Text>
      ),
      labels: { confirm: 'Delete', cancel: "No don't delete it" },
      confirmProps: { color: 'red' },
      onConfirm: async () => {
        try {
          await pokemonService.deleteLocalPokemon(id);
          notifications.show({
            title: 'Deleted',
            message: `${name} has been removed.`,
            color: 'green'
          });
          fetchLocalPokemon(page, debouncedSearch);
        } catch (error) {
          notifications.show({
            title: 'Error',
            message: 'Failed to delete Pokémon.',
            color: 'red'
          });
        }
      },
    });
  };

  if (loading && pokemon.length === 0) {
    return <Center h={400}><Loader size="xl" /></Center>;
  }

  return (
    <Container size="xl">
      <Title order={1} mb="xl">My Pokémon Collection</Title>

      <TextInput
        placeholder="Search my collection..."
        leftSection={<IconSearch size={16} />}
        value={search}
        onChange={(e) => setSearch(e.currentTarget.value)}
        mb="xl"
        size="md"
        radius="md"
      />

      {pokemon.length === 0 ? (
        <Center h={200} style={{ flexDirection: 'column' }}>
          {debouncedSearch ? (
            <Text size="lg" mb="md">No Pokémon found matching "{debouncedSearch}".</Text>
          ) : (
            <>
              <Text size="lg" mb="md">No Pokémon synced yet. Browse the Pokédex to add some!</Text>
              <Button component={Link} to="/">Go to Pokédex</Button>
            </>
          )}
        </Center>
      ) : (
        <>
          <Table.ScrollContainer minWidth={800}>
            <Table striped highlightOnHover withTableBorder>
              <Table.Thead>
                <Table.Tr>
                  <Table.Th>ID</Table.Th>
                  <Table.Th>Sprite</Table.Th>
                  <Table.Th>Name</Table.Th>
                  <Table.Th>Custom Name</Table.Th>
                  <Table.Th>Region</Table.Th>
                  <Table.Th>Tag</Table.Th>
                  <Table.Th>Description</Table.Th>
                  <Table.Th>Actions</Table.Th>
                </Table.Tr>
              </Table.Thead>
              <Table.Tbody>
                {pokemon.map((p) => (
                  <Table.Tr key={p.id} onClick={() => navigate(`/pokemon/${p.id}`)} style={{ cursor: 'pointer' }}>
                    <Table.Td>#{String(p.id).padStart(3, '0')}</Table.Td>
                    <Table.Td>
                      <Image
                        src={p.spriteUrl}
                        w={40}
                        h={40}
                        fit="contain"
                        fallbackSrc="https://placehold.co/40x40"
                      />
                    </Table.Td>
                    <Table.Td style={{ textTransform: 'capitalize', fontWeight: 500 }}>{p.name}</Table.Td>
                    <Table.Td>{p.customName || '-'}</Table.Td>
                    <Table.Td>{p.region || '-'}</Table.Td>
                    <Table.Td>
                      {p.classificationTag ? <Badge>{p.classificationTag}</Badge> : '-'}
                    </Table.Td>
                    <Table.Td>
                      <Tooltip label={p.description || 'No description'} multiline w={300} withArrow>
                        <Text truncate w={150}>{p.description || '-'}</Text>
                      </Tooltip>
                    </Table.Td>
                    <Table.Td>
                      <Group gap="sm">
                        <ActionIcon variant="light" color="blue" onClick={(e) => { e.stopPropagation(); setEditingPokemon(p); }}>
                          <IconEdit size={16} />
                        </ActionIcon>
                        <ActionIcon variant="light" color="red" onClick={(e) => { e.stopPropagation(); handleDelete(p.id, p.name); }}>
                          <IconTrash size={16} />
                        </ActionIcon>
                      </Group>
                    </Table.Td>
                  </Table.Tr>
                ))}
              </Table.Tbody>
            </Table>
          </Table.ScrollContainer>

          {totalPages > 1 && (
            <Center mt="xl">
              <Pagination total={totalPages} value={page} onChange={setPage} color="pokeRed" />
            </Center>
          )}
        </>
      )}

      <EditPokemonModal
        opened={!!editingPokemon}
        onClose={() => setEditingPokemon(null)}
        pokemon={editingPokemon}
        onSuccess={() => fetchLocalPokemon(page, debouncedSearch)}
      />
    </Container>
  );
}
