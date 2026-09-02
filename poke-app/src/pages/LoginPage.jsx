import { useState } from 'react';
import { TextInput, PasswordInput, Button, Paper, Title, Container, Text, Alert } from '@mantine/core';
import { Link, useNavigate } from 'react-router-dom';
import { IconAlertCircle } from '@tabler/icons-react';
import { useAuth } from '../context/AuthContext';

export default function LoginPage() {
  const [username, setUsername] = useState('');
  const [password, setPassword] = useState('');
  const [error, setError] = useState('');
  const [loading, setLoading] = useState(false);
  const navigate = useNavigate();
  const { login } = useAuth();

  const handleSubmit = async (e) => {
    e.preventDefault();
    setError('');
    setLoading(true);
    
    try {
      await login(username, password);
      navigate('/');
    } catch (err) {
      setError(err.response?.data?.message || 'Invalid username or password');
    } finally {
      setLoading(false);
    }
  };

  return (
    <Container size={420} my={40}>
      <Title ta="center">Welcome back!</Title>
      <Text c="dimmed" size="sm" ta="center" mt={5}>
        Do not have an account yet?{' '}
        <Text component={Link} to="/register" size="sm" fw={500} c="pokeRed">
          Create account
        </Text>
      </Text>

      <Paper withBorder shadow="md" p={30} mt={30} radius="md">
        {error && (
          <Alert icon={<IconAlertCircle size={16} />} title="Error" color="red" mb="md" variant="light">
            {error}
          </Alert>
        )}
        <form onSubmit={handleSubmit}>
          <TextInput
            label="Username"
            placeholder="Your username"
            required
            value={username}
            onChange={(e) => setUsername(e.currentTarget.value)}
          />
          <PasswordInput
            label="Password"
            placeholder="Your password"
            required
            mt="md"
            value={password}
            onChange={(e) => setPassword(e.currentTarget.value)}
          />
          <Button fullWidth mt="xl" type="submit" loading={loading}>
            Sign in
          </Button>
        </form>
      </Paper>
    </Container>
  );
}
