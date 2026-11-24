package cl.duoc.biblioteca.controller;

import cl.duoc.biblioteca.model.Categoria;
import cl.duoc.biblioteca.service.TiendaService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/categorias")
@Tag(name = "Categorías", description = "Gestión de categorías de productos")
public class CategoriaController {

    private final TiendaService tiendaService;

    public CategoriaController(TiendaService tiendaService) {
        this.tiendaService = tiendaService;
    }

    @GetMapping
    @Operation(summary = "Listar categorías")
    public List<Categoria> listar() {
        return tiendaService.listarCategorias();
    }

    @PostMapping
    @Operation(summary = "Crear categoría (Solo ADMIN)")
    public ResponseEntity<Categoria> crear(@RequestBody CategoriaRequest req) {
        Categoria creada = tiendaService.crearCategoria(req.nombre(), req.descripcion());
        return ResponseEntity.status(HttpStatus.CREATED).body(creada);
    }

    public record CategoriaRequest(String nombre, String descripcion) {}
}
