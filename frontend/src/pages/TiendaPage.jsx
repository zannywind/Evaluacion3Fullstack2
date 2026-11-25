import { useEffect, useState } from 'react';
import { tiendaService } from '../services/tiendaService';
import { useAuth } from '../context/AuthContext';

export default function TiendaPage() {
  const { user } = useAuth();
  const [productos, setProductos] = useState([]);
  const [categorias, setCategorias] = useState([]);
  const [carrito, setCarrito] = useState([]); // Solo en memoria, nunca localStorage
  const [mensaje, setMensaje] = useState('');
  const [editId, setEditId] = useState(null);
  const [form, setForm] = useState({ nombre: '', precio: 0, stock: 1, disponible: true, imagen: '', categoriaId: '' });

  const esAdmin = user?.rol === 'ADMIN';
  const esCliente = user?.rol === 'CLIENTE';

  const cargarDatos = async () => {
    const [prods, cats] = await Promise.all([
      tiendaService.listarProductos(),
      tiendaService.listarCategorias(),
    ]);
    setProductos(prods);
    setCategorias(cats);
    if (cats.length > 0 && !form.categoriaId) {
      setForm((f) => ({ ...f, categoriaId: cats[0].id }));
    }
  };

  useEffect(() => {
    cargarDatos();
  }, []);

  const guardarProducto = async (e) => {
    e.preventDefault();
    const payload = {
      ...form,
      precio: Number(form.precio),
      stock: Number(form.stock),
      categoriaId: Number(form.categoriaId),
    };
    if (editId) {
      await tiendaService.actualizarProducto(editId, payload);
      setMensaje('Producto actualizado');
    } else {
      await tiendaService.crearProducto(payload);
      setMensaje('Producto creado');
    }
    setEditId(null);
    setForm({ nombre: '', precio: 0, stock: 1, disponible: true, imagen: '', categoriaId: categorias[0]?.id || '' });
    cargarDatos();
  };

  const iniciarEdicion = (p) => {
    setEditId(p.id);
    setForm({
      nombre: p.nombre,
      precio: p.precio,
      stock: p.stock,
      disponible: p.disponible,
      imagen: p.imagen || '',
      categoriaId: p.categoria?.id || categorias[0]?.id || '',
    });
  };

  const eliminar = async (id) => {
    if (!confirm('¿Eliminar este producto?')) return;
    await tiendaService.eliminarProducto(id);
    setMensaje('Producto eliminado');
    cargarDatos();
  };

  const agregarAlCarrito = (p) => {
    setCarrito((prev) => {
      const existe = prev.find((i) => i.productoId === p.id);
      if (existe) {
        return prev.map((i) => (i.productoId === p.id ? { ...i, cantidad: i.cantidad + 1 } : i));
      }
      return [...prev, { productoId: p.id, nombre: p.nombre, precio: p.precio, cantidad: 1 }];
    });
  };

  const finalizarCompra = async () => {
    try {
      const items = carrito.map(({ productoId, cantidad }) => ({ productoId, cantidad }));
      await tiendaService.crearBoleta(items);
      setCarrito([]);
      setMensaje('¡Compra realizada con éxito! Revisa "Mis Boletas".');
      cargarDatos();
    } catch (err) {
      setMensaje(err.response?.data?.error || 'Error al emitir boleta');
    }
  };

  return (
    <div style={{ maxWidth: 900, margin: '1rem auto', fontFamily: 'sans-serif' }}>
      <h2>Catálogo de Productos</h2>
      {mensaje && <p style={{ padding: 8, background: '#e6fffa', border: '1px solid #38b2ac' }}>{mensaje}</p>}

      {esAdmin && (
        <form onSubmit={guardarProducto} style={{ marginBottom: 20, padding: 12, border: '1px solid #ccc', display: 'flex', flexWrap: 'wrap', gap: 8 }}>
          <strong style={{ width: '100%' }}>{editId ? 'Editar Producto' : 'Nuevo Producto (Solo ADMIN)'}</strong>
          <input placeholder="Nombre" required value={form.nombre} onChange={(e) => setForm({ ...form, nombre: e.target.value })} />
          <input type="number" placeholder="Precio" required value={form.precio} onChange={(e) => setForm({ ...form, precio: e.target.value })} />
          <input type="number" placeholder="Stock" required value={form.stock} onChange={(e) => setForm({ ...form, stock: e.target.value })} />
          <select value={form.categoriaId} onChange={(e) => setForm({ ...form, categoriaId: e.target.value })}>
            {categorias.map((c) => <option key={c.id} value={c.id}>{c.nombre}</option>)}
          </select>
          <button type="submit">{editId ? 'Actualizar' : 'Crear'}</button>
          {editId && <button type="button" onClick={() => setEditId(null)}>Cancelar</button>}
        </form>
      )}

      <table border="1" cellPadding="8" style={{ width: '100%', borderCollapse: 'collapse' }}>
        <thead>
          <tr>
            <th>Nombre</th>
            <th>Categoría</th>
            <th>Precio</th>
            <th>Stock</th>
            {(esAdmin || esCliente) && <th>Acciones</th>}
          </tr>
        </thead>
        <tbody>
          {productos.map((p) => (
            <tr key={p.id}>
              <td>{p.nombre}</td>
              <td>{p.categoria?.nombre}</td>
              <td>${p.precio?.toLocaleString('es-CL')}</td>
              <td>{p.stock}</td>
              {(esAdmin || esCliente) && (
                <td>
                  {esAdmin && (
                    <>
                      <button onClick={() => iniciarEdicion(p)}>Editar</button>{' '}
                      <button onClick={() => eliminar(p.id)}>Eliminar</button>
                    </>
                  )}
                  {esCliente && (
                    <button disabled={p.stock <= 0} onClick={() => agregarAlCarrito(p)}>
                      Agregar al carrito
                    </button>
                  )}
                </td>
              )}
            </tr>
          ))}
        </tbody>
      </table>

      {esCliente && carrito.length > 0 && (
        <div style={{ marginTop: 20, padding: 12, border: '1px solid #333' }}>
          <h3>Carrito actual</h3>
          <ul>
            {carrito.map((i) => (
              <li key={i.productoId}>{i.nombre} x {i.cantidad} (${(i.precio * i.cantidad).toLocaleString('es-CL')})</li>
            ))}
          </ul>
          <button onClick={finalizarCompra}>Finalizar Compra (Emitir Boleta)</button>
        </div>
      )}
    </div>
  );
}