import api from './api';

export const pokemonService = {
  listPokemon: (page = 0, size = 20) => api.get(`/pokemon?page=${page}&size=${size}`),
  searchPokemon: (query, page = 0, size = 20) => api.get(`/pokemon/search?query=${query}&page=${page}&size=${size}`),
  getPokemonDetail: (id) => api.get(`/pokemon/${id}`),
  syncPokemon: (id) => api.post(`/local/pokemon/sync/${id}`),
  getLocalPokemon: (page = 0, size = 20, sortBy = 'id', sortDir = 'asc') => api.get(`/local/pokemon?page=${page}&size=${size}&sortBy=${sortBy}&sortDir=${sortDir}`),
  searchLocalPokemon: (query, page = 0, size = 20, sortBy = 'id', sortDir = 'asc') => api.get(`/local/pokemon/search?query=${query}&page=${page}&size=${size}&sortBy=${sortBy}&sortDir=${sortDir}`),
  getLocalPokemonById: (id) => api.get(`/local/pokemon/${id}`),
  updateLocalPokemon: (id, data) => api.put(`/local/pokemon/${id}`, data),
  deleteLocalPokemon: (id) => api.delete(`/local/pokemon/${id}`),
};
