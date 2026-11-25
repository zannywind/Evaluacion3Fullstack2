import { useState } from 'react';
import { useNavigate } from 'react-router-dom';
import { useAuth } from '../context/AuthContext';

export default function LoginPage() {
  const { login, register } = useAuth();
  const navigate = useNavigate();
  const [esRegistro, setEsRegistro] = useState(false);
  const [form, setForm] = useState({ nombre: '', email: '', password: '', direccion: '' });
  const [error, setError] = useState('');

  const handleSubmit = async (e) => {
    e.preventDefault();
    setError('');
    try {
      if (esRegistro) {
        await register(form);
      } else {
        await login(form.email, form.password);
      }
      navigate('/');
    } catch (err) {
      setError(err.response?.data?.error || 'Error de autenticación');
    }
  };

  return (
    <div style={{ maxWidth: 400, margin: '2rem auto', fontFamily: 'sans-serif' }}>
      <h2>{esRegistro ? 'Crear Cuenta (Cliente)' : 'Iniciar Sesión - Mai estor'}</h2>
      {error && <p style={{ color: 'red' }}>{error}</p>}

      <form onSubmit={handleSubmit} style={{ display: 'flex', flexDirection: 'column', gap: 10 }}>
        {esRegistro && (
          <>
            <input placeholder="Nombre" required value={form.nombre} onChange={(e) => setForm({ ...form, nombre: e.target.value })} />
            <input placeholder="Dirección" value={form.direccion} onChange={(e) => setForm({ ...form, direccion: e.target.value })} />
          </>
        )}
        <input type="email" placeholder="Correo" required value={form.email} onChange={(e) => setForm({ ...form, email: e.target.value })} />
        <input type="password" placeholder="Contraseña" required value={form.password} onChange={(e) => setForm({ ...form, password: e.target.value })} />
        <button type="submit">{esRegistro ? 'Registrarse' : 'Entrar'}</button>
      </form>

      <button onClick={() => setEsRegistro(!esRegistro)} style={{ marginTop: 10 }}>
        {esRegistro ? 'Ya tengo cuenta' : 'Crear cuenta nueva'}
      </button>

      <div style={{ marginTop: 20, fontSize: 12, color: '#555' }}>
        <p><strong>Cuentas de prueba:</strong></p>
        <p>Admin: admin@maiestor.cl / admin123</p>
        <p>Vendedor: vendedor@maiestor.cl / vendedor123</p>
        <p>Cliente: cliente@maiestor.cl / cliente123</p>
      </div>
    </div>
  );
}