import { create } from 'zustand';

const TOKEN_KEY = "token";
const readToken = () => localStorage.getItem(TOKEN_KEY);

export const useAuthStore = create((set) => ({
  token: readToken(),
  setToken: (token) => {
    localStorage.setItem(TOKEN_KEY, token);
    set({ token });
  },
  logout: () => {
    localStorage.removeItem(TOKEN_KEY);
    set({ token: null });
  },
}));
