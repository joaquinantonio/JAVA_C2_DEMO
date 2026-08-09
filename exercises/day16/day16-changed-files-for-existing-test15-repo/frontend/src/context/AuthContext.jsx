import { createContext, useContext, useMemo, useState } from 'react';
import { loginRequest } from '../services/api.js';

const STORAGE_KEY = 'assetTrackerAuth';
const AuthContext = createContext(null);

function readStoredAuth() {
  try {
    const stored = localStorage.getItem(STORAGE_KEY);

    if (!stored) {
      return null;
    }

    const parsed = JSON.parse(stored);

    if (!parsed?.token || !parsed?.expiresAt) {
      localStorage.removeItem(STORAGE_KEY);
      return null;
    }

    if (Date.now() > parsed.expiresAt) {
      localStorage.removeItem(STORAGE_KEY);
      return null;
    }

    return parsed;
  } catch {
    localStorage.removeItem(STORAGE_KEY);
    return null;
  }
}

export function AuthProvider({ children }) {
  const [auth, setAuth] = useState(readStoredAuth);

  async function login(email, password) {
    const response = await loginRequest(email, password);
    const expiresInMinutes = response.expiresInMinutes ?? 60;

    const nextAuth = {
      token: response.token,
      tokenType: response.tokenType,
      expiresInMinutes,
      expiresAt: Date.now() + expiresInMinutes * 60 * 1000,
      user: {
        id: response.userId,
        name: response.name,
        email: response.email,
        role: response.role
      }
    };

    localStorage.setItem(STORAGE_KEY, JSON.stringify(nextAuth));
    setAuth(nextAuth);
    return nextAuth;
  }

  function logout() {
    localStorage.removeItem(STORAGE_KEY);
    setAuth(null);
  }

  const value = useMemo(
    () => ({
      auth,
      token: auth?.token ?? '',
      user: auth?.user ?? null,
      isAuthenticated: Boolean(auth?.token),
      login,
      logout
    }),
    [auth]
  );

  return <AuthContext.Provider value={value}>{children}</AuthContext.Provider>;
}

export function useAuth() {
  const value = useContext(AuthContext);

  if (!value) {
    throw new Error('useAuth must be used inside AuthProvider');
  }

  return value;
}
