import { createContext, useContext, useState } from 'react';
import { tiendaService } from '../services/tiendaService';

const AuthContext = createContext(null);

function parseJwt(token) {
  try {
    const base64 = token.split('.')[1].replace(/-/g, '+').replace(/_/g, '/');
    const payload = JSON.parse(atob(base64));
    // Verifica expiración si existe claim exp
    if (payload.exp && payload.exp * 1000 < Date.now()) return null;
    return {
      email: payload.sub || payload.email,
      rol: payload.rol || payload.role,
    };
  } catch {
    return null;
  }
}

export function AuthProvider({ children }) {
  const [user, setUser] = useState(() => {
    const token = localStorage.getItem('token');
    if (!token) return null;
    const decoded = parseJwt(token);
    if (!decoded) localStorage.removeItem('token');
    return decoded;
  });

  const login = async (email, password) => {
    const data = await tiendaService.login(email, password);
    localStorage.setItem('token', data.token);
    setUser({ email: data.email, rol: data.rol, nombre: data.nombre });
  };

  const register = async (datos) => {
    const data = await tiendaService.register(datos);
    localStorage.setItem('token', data.token);
    setUser({ email: data.email, rol: data.rol, nombre: data.nombre });
  };

  const logout = () => {
    localStorage.removeItem('token');
    setUser(null);
  };

  return (
    <AuthContext.Provider value={{ user, login, register, logout }}>
      {children}
    </AuthContext.Provider>
  );
}

export const useAuth = () => useContext(AuthContext);