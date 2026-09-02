import api from './api';

export const pokemonService = {
  listPokemon: (page = 0, size = 20) => api.get(`/pokemon?page=${page}&size=${size}`),
  getPokemonDetail: (id) => api.get(`/pokemon/${id}`),
  syncPokemon: (id) => api.post(`/local/pokemon/sync/${id}`),
  getLocalPokemon: (page = 0, size = 20) => api.get(`/local/pokemon?page=${page}&size=${size}`),
  getLocalPokemonById: (id) => api.get(`/local/pokemon/${id}`),
  updateLocalPokemon: (id, data) => api.put(`/local/pokemon/${id}`, data),
  deleteLocalPokemon: (id) => api.delete(`/local/pokemon/${id}`),
};
