import { Routes, Route, Link, Navigate } from 'react-router-dom';
import { AuthProvider, useAuth } from './context/AuthContext';
import TiendaPage from './pages/TiendaPage';
import LoginPage from './pages/LoginPage';
import BoletasPage from './pages/BoletasPage';

function Navbar() {
  const { user, logout } = useAuth();
  return (
    <nav style={{ padding: '10px 20px', background: '#222', color: '#fff', display: 'flex', gap: 15, alignItems: 'center' }}>
      <Link to="/" style={{ color: '#fff', textDecoration: 'none', fontWeight: 'bold' }}>Mai estor</Link>
      <Link to="/" style={{ color: '#ddd' }}>Productos</Link>
      {user && (
        <Link to="/boletas" style={{ color: '#ddd' }}>
          {user.rol === 'CLIENTE' ? 'Mis Boletas' : 'Órdenes'}
        </Link>
      )}
      <div style={{ marginLeft: 'auto' }}>
        {user ? (
          <>
            <span style={{ marginRight: 10 }}>{user.email} ({user.rol})</span>
            <button onClick={logout}>Cerrar Sesión</button>
          </>
        ) : (
          <Link to="/login" style={{ color: '#fff' }}>Iniciar Sesión</Link>
        )}
      </div>
    </nav>
  );
}

function RutaProtegida({ children }) {
  const { user } = useAuth();
  return user ? children : <Navigate to="/login" replace />;
}

export default function App() {
  return (
    <AuthProvider>
      <Navbar />
      <Routes>
        <Route path="/" element={<TiendaPage />} />
        <Route path="/login" element={<LoginPage />} />
        <Route path="/boletas" element={<RutaProtegida><BoletasPage /></RutaProtegida>} />
      </Routes>
    </AuthProvider>
  );
}