import { useState } from 'react';
import { TextInput, PasswordInput, Button, Paper, Title, Container, Text, Alert } from '@mantine/core';
import { Link, useNavigate } from 'react-router-dom';
import { IconAlertCircle } from '@tabler/icons-react';
import { useAuth } from '../context/AuthContext';

export default function RegisterPage() {
  const [username, setUsername] = useState('');
  const [email, setEmail] = useState('');
  const [password, setPassword] = useState('');
  const [error, setError] = useState('');
  const [fieldErrors, setFieldErrors] = useState({});
  const [loading, setLoading] = useState(false);
  const navigate = useNavigate();
  const { register } = useAuth();

  const handleSubmit = async (e) => {
    e.preventDefault();
    setError('');
    setFieldErrors({});
    setLoading(true);

    try {
      await register(username, email, password);
      navigate('/');
    } catch (err) {
      const data = err.response?.data;
      // Field-level validation errors from backend
      if (data?.details && typeof data.details === 'object') {
        setFieldErrors(data.details);
        setError('Please fix the validation errors below.');
      } else {
        setError(data?.message || 'Failed to register account');
      }
    } finally {
      setLoading(false);
    }
  };

  return (
    <Container size={420} my={40}>
      <Title ta="center">Create an account</Title>
      <Text c="dimmed" size="sm" ta="center" mt={5}>
        Already have an account?{' '}
        <Text component={Link} to="/login" size="sm" fw={500} c="pokeRed">
          Login
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
            placeholder="Your username (min. 3 characters)"
            required
            value={username}
            onChange={(e) => setUsername(e.currentTarget.value)}
            error={fieldErrors.username}
          />
          <TextInput
            label="Email"
            placeholder="your@email.com"
            required
            mt="md"
            type="email"
            value={email}
            onChange={(e) => setEmail(e.currentTarget.value)}
            error={fieldErrors.email}
          />
          <PasswordInput
            label="Password"
            description="Minimum 6 characters"
            placeholder="Your password"
            required
            mt="md"
            value={password}
            onChange={(e) => setPassword(e.currentTarget.value)}
            error={fieldErrors.password}
          />
          <Button fullWidth mt="xl" type="submit" loading={loading}>
            Register
          </Button>
        </form>
      </Paper>
    </Container>
  );
}
