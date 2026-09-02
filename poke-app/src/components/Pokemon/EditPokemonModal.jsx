import { Modal, TextInput, Textarea, Button, Group } from '@mantine/core';
import { useForm } from '@mantine/form';
import { notifications } from '@mantine/notifications';
import { pokemonService } from '../../services/pokemonService';
import { useState, useEffect } from 'react';

export default function EditPokemonModal({ opened, onClose, pokemon, onSuccess }) {
  const [loading, setLoading] = useState(false);

  const form = useForm({
    initialValues: {
      customName: '',
      region: '',
      classificationTag: '',
      notes: '',
    },
  });

  useEffect(() => {
    if (pokemon) {
      form.setValues({
        customName: pokemon.customName || '',
        region: pokemon.region || '',
        classificationTag: pokemon.classificationTag || '',
        notes: pokemon.notes || '',
      });
    }
  }, [pokemon]);

  const handleSubmit = async (values) => {
    setLoading(true);
    try {
      await pokemonService.updateLocalPokemon(pokemon.id, values);
      notifications.show({
        title: 'Success',
        message: 'Pokémon updated successfully',
        color: 'green',
      });
      onSuccess();
      onClose();
    } catch (error) {
      notifications.show({
        title: 'Error',
        message: 'Failed to update Pokémon',
        color: 'red',
      });
    } finally {
      setLoading(false);
    }
  };

  return (
    <Modal opened={opened} onClose={onClose} title="Edit Synced Pokémon" centered>
      <form onSubmit={form.onSubmit(handleSubmit)}>
        <TextInput
          label="Custom Name"
          placeholder="Give it a nickname"
          mb="sm"
          {...form.getInputProps('customName')}
        />
        <TextInput
          label="Region"
          placeholder="e.g. Kanto, Johto"
          mb="sm"
          {...form.getInputProps('region')}
        />
        <TextInput
          label="Classification Tag"
          placeholder="e.g. Attacker, Defender"
          mb="sm"
          {...form.getInputProps('classificationTag')}
        />
        <Textarea
          label="Notes"
          placeholder="Any notes about this Pokémon?"
          mb="xl"
          minRows={3}
          {...form.getInputProps('notes')}
        />
        <Group justify="flex-end">
          <Button variant="subtle" onClick={onClose} disabled={loading}>Cancel</Button>
          <Button type="submit" loading={loading}>Save</Button>
        </Group>
      </form>
    </Modal>
  );
}
