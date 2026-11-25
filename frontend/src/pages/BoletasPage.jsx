import { useEffect, useState } from 'react';
import { tiendaService } from '../services/tiendaService';
import { useAuth } from '../context/AuthContext';

export default function BoletasPage() {
  const { user } = useAuth();
  const [boletas, setBoletas] = useState([]);
  const [error, setError] = useState('');

  useEffect(() => {
    const cargar = async () => {
      try {
        const data =
          user.rol === 'ADMIN' || user.rol === 'VENDEDOR'
            ? await tiendaService.listarTodasBoletas()
            : await tiendaService.listarMisBoletas();
        setBoletas(data);
      } catch {
        setError('No tienes permisos o hubo un error al cargar las boletas.');
      }
    };
    cargar();
  }, [user]);

  return (
    <div style={{ maxWidth: 900, margin: '1rem auto', fontFamily: 'sans-serif' }}>
      <h2>{user.rol === 'CLIENTE' ? 'Mis Boletas' : 'Todas las Órdenes / Boletas'}</h2>
      {error && <p style={{ color: 'red' }}>{error}</p>}
      {boletas.length === 0 ? (
        <p>No hay boletas registradas.</p>
      ) : (
        <table border="1" cellPadding="8" style={{ width: '100%', borderCollapse: 'collapse' }}>
          <thead>
            <tr>
              <th>ID</th>
              <th>Fecha</th>
              <th>Cliente</th>
              <th>Total</th>
            </tr>
          </thead>
          <tbody>
            {boletas.map((b) => (
              <tr key={b.id}>
                <td>#{b.id}</td>
                <td>{b.fecha ? new Date(b.fecha).toLocaleString('es-CL') : '-'}</td>
                <td>{b.usuario?.email || user.email}</td>
                <td>${b.total?.toLocaleString('es-CL')}</td>
              </tr>
            ))}
          </tbody>
        </table>
      )}
    </div>
  );
}