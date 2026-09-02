import { Routes, Route } from 'react-router-dom';
import { AuthProvider } from './context/AuthContext';
import AppLayout from './components/Layout/AppLayout';
import HomePage from './pages/HomePage';
import PokemonDetailPage from './pages/PokemonDetailPage';
import MyPokemonPage from './pages/MyPokemonPage';
import LoginPage from './pages/LoginPage';
import RegisterPage from './pages/RegisterPage';
import ProtectedRoute from './components/Auth/ProtectedRoute';

export default function App() {
  return (
    <AuthProvider>
      <Routes>
        <Route element={<AppLayout />}>
          <Route path="/" element={<HomePage />} />
          <Route path="/pokemon/:id" element={<PokemonDetailPage />} />
          <Route path="/my-pokemon" element={
            <ProtectedRoute>
              <MyPokemonPage />
            </ProtectedRoute>
          } />
          <Route path="/login" element={<LoginPage />} />
          <Route path="/register" element={<RegisterPage />} />
        </Route>
      </Routes>
    </AuthProvider>
  );
}
