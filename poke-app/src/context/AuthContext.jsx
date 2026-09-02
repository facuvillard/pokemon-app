import React, { createContext, useState, useContext, useEffect } from 'react';
import { authService } from '../services/authService';

const AuthContext = createContext(null);

export const AuthProvider = ({ children }) => {
  const [user, setUser] = useState(null);
  const [token, setToken] = useState(localStorage.getItem('token') || null);
  const [loading, setLoading] = useState(true);

  useEffect(() => {
    const initAuth = async () => {
      if (token) {
        try {
          const res = await authService.getCurrentUser();
          setUser(res.data);
        } catch (error) {
          console.error("Failed to fetch user", error);
          setToken(null);
          localStorage.removeItem('token');
        }
      }
      setLoading(false);
    };
    initAuth();
  }, [token]);

  const login = async (username, password) => {
    const res = await authService.login(username, password);
    const newToken = res.data.token;
    localStorage.setItem('token', newToken);
    setToken(newToken);
    setUser(res.data);
  };

  const register = async (username, email, password) => {
    const res = await authService.register(username, email, password);
    const newToken = res.data.token;
    localStorage.setItem('token', newToken);
    setToken(newToken);
    setUser(res.data);
  };

  const logout = () => {
    localStorage.removeItem('token');
    setToken(null);
    setUser(null);
  };

  return (
    <AuthContext.Provider value={{ user, token, isAuthenticated: !!token, loading, login, register, logout }}>
      {children}
    </AuthContext.Provider>
  );
};

export const useAuth = () => useContext(AuthContext);
