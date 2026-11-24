package cl.duoc.biblioteca.controller;

import cl.duoc.biblioteca.model.Producto;
import cl.duoc.biblioteca.service.TiendaService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/productos")
@Tag(name = "Productos", description = "Operaciones CRUD para los productos de la tienda")
public class ProductoController {

    private final TiendaService tiendaService;

    public ProductoController(TiendaService tiendaService) {
        this.tiendaService = tiendaService;
    }

    @GetMapping
    @Operation(summary = "Listar productos", description = "Obtiene todos los productos disponibles en la tienda")
    public List<Producto> listar() {
        return tiendaService.listarProductos();
    }

    @GetMapping("/{id}")
    @Operation(summary = "Obtener producto por ID")
    public Producto obtener(@PathVariable Long id) {
        return tiendaService.obtenerProducto(id);
    }

    @PostMapping
    @Operation(summary = "Crear producto (Solo ADMIN)")
    public ResponseEntity<Producto> crear(@RequestBody ProductoRequest req) {
        Producto creado = tiendaService.crearProducto(
                req.nombre(), req.precio(), req.stock(), req.disponible(), req.imagen(), req.categoriaId()
        );
        return ResponseEntity.status(HttpStatus.CREATED).body(creado);
    }

    @PutMapping("/{id}")
    @Operation(summary = "Actualizar producto (Solo ADMIN)")
    public Producto actualizar(@PathVariable Long id, @RequestBody ProductoRequest req) {
        return tiendaService.actualizarProducto(
                id, req.nombre(), req.precio(), req.stock(), req.disponible(), req.imagen(), req.categoriaId()
        );
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Eliminar producto (Solo ADMIN)")
    public ResponseEntity<Void> eliminar(@PathVariable Long id) {
        tiendaService.eliminarProducto(id);
        return ResponseEntity.noContent().build();
    }

    public record ProductoRequest(String nombre, Integer precio, Integer stock, Boolean disponible, String imagen, Long categoriaId) {}
}