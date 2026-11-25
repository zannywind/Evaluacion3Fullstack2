import { httpClient } from '../api/httpClient';

export const tiendaService = {
  // Auth
  login: async (email, password) => (await httpClient.post('/auth/login', { email, password })).data,
  register: async (datos) => (await httpClient.post('/auth/register', datos)).data,

  // Productos y Categorías
  listarProductos: async () => (await httpClient.get('/productos')).data,
  crearProducto: async (prod) => (await httpClient.post('/productos', prod)).data,
  actualizarProducto: async (id, prod) => (await httpClient.put(`/productos/${id}`, prod)).data,
  eliminarProducto: async (id) => await httpClient.delete(`/productos/${id}`),
  listarCategorias: async () => (await httpClient.get('/categorias')).data,

  // Boletas
  listarTodasBoletas: async () => (await httpClient.get('/boletas')).data,
  listarMisBoletas: async () => (await httpClient.get('/boletas/mis-boletas')).data,
  crearBoleta: async (items) => (await httpClient.post('/boletas', items)).data,
};