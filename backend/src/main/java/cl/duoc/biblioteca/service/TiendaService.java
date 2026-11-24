package cl.duoc.biblioteca.service;

import cl.duoc.biblioteca.model.*;
import cl.duoc.biblioteca.repository.*;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class TiendaService {

    private final ProductoRepository productoRepository;
    private final CategoriaRepository categoriaRepository;
    private final BoletaRepository boletaRepository;
    private final UsuarioRepository usuarioRepository;

    public TiendaService(ProductoRepository productoRepository,
                         CategoriaRepository categoriaRepository,
                         BoletaRepository boletaRepository,
                         UsuarioRepository usuarioRepository) {
        this.productoRepository = productoRepository;
        this.categoriaRepository = categoriaRepository;
        this.boletaRepository = boletaRepository;
        this.usuarioRepository = usuarioRepository;
    }

    public List<Producto> listarProductos() {
        return productoRepository.findAll();
    }

    public Producto obtenerProducto(Long id) {
        return productoRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Producto no encontrado con id: " + id));
    }

    public Producto crearProducto(String nombre, Integer precio, Integer stock, Boolean disponible, String imagen, Long categoriaId) {
        if (nombre == null || nombre.isBlank() || precio == null || precio <= 0) {
            throw new IllegalArgumentException("Nombre y precio mayor a cero son obligatorios");
        }
        Categoria categoria = categoriaRepository.findById(categoriaId)
                .orElseThrow(() -> new RuntimeException("Categoría no encontrada con id: " + categoriaId));

        Producto p = new Producto(
                nombre.trim(),
                precio,
                stock != null && stock >= 0 ? stock : 10,
                disponible != null ? disponible : true,
                (imagen != null && !imagen.isBlank()) ? imagen : "/product-placeholder.svg",
                categoria
        );
        return productoRepository.save(p);
    }

    public Producto actualizarProducto(Long id, String nombre, Integer precio, Integer stock, Boolean disponible, String imagen, Long categoriaId) {
        Producto existente = obtenerProducto(id);
        if (nombre != null && !nombre.isBlank()) existente.setNombre(nombre.trim());
        if (precio != null && precio > 0) existente.setPrecio(precio);
        if (stock != null && stock >= 0) existente.setStock(stock);
        if (disponible != null) existente.setDisponible(disponible);
        if (imagen != null && !imagen.isBlank()) existente.setImagen(imagen);
        if (categoriaId != null) {
            Categoria cat = categoriaRepository.findById(categoriaId)
                    .orElseThrow(() -> new RuntimeException("Categoría no encontrada con id: " + categoriaId));
            existente.setCategoria(cat);
        }
        return productoRepository.save(existente);
    }

    public void eliminarProducto(Long id) {
        Producto existente = obtenerProducto(id);
        productoRepository.delete(existente);
    }

    public List<Categoria> listarCategorias() {
        return categoriaRepository.findAll();
    }

    public Categoria crearCategoria(String nombre, String descripcion) {
        if (nombre == null || nombre.isBlank()) {
            throw new IllegalArgumentException("El nombre de la categoría es obligatorio");
        }
        return categoriaRepository.save(new Categoria(nombre.trim(), descripcion));
    }

    @Transactional
    public Boleta crearBoleta(String emailUsuario, List<ItemCompraRequest> items) {
        if (items == null || items.isEmpty()) {
            throw new IllegalArgumentException("La compra debe incluir al menos un producto");
        }
        Usuario usuario = usuarioRepository.findByEmail(emailUsuario)
                .orElseThrow(() -> new RuntimeException("Usuario no encontrado: " + emailUsuario));

        Boleta boleta = new Boleta(usuario, 0, "COMPLETADA");
        int totalCalculado = 0;

        for (ItemCompraRequest item : items) {
            Producto producto = obtenerProducto(item.productoId());
            int cantidad = item.cantidad() != null && item.cantidad() > 0 ? item.cantidad() : 1;
            if (producto.getStock() < cantidad) {
                throw new IllegalArgumentException("Stock insuficiente para: " + producto.getNombre());
            }
            producto.setStock(producto.getStock() - cantidad);
            productoRepository.save(producto);

            DetalleBoleta detalle = new DetalleBoleta(boleta, producto, cantidad, producto.getPrecio());
            boleta.getDetalles().add(detalle);
            totalCalculado += detalle.getSubtotal();
        }

        boleta.setTotal(totalCalculado);
        return boletaRepository.save(boleta);
    }

    public List<Boleta> listarBoletas() {
        return boletaRepository.findAll();
    }

    public List<Boleta> listarBoletasPorEmail(String email) {
        Usuario usuario = usuarioRepository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("Usuario no encontrado: " + email));
        return boletaRepository.findByUsuarioId(usuario.getId());
    }

    public record ItemCompraRequest(Long productoId, Integer cantidad) {}
}