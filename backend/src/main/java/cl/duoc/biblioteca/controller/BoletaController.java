package cl.duoc.biblioteca.controller;

import cl.duoc.biblioteca.model.Boleta;
import cl.duoc.biblioteca.service.TiendaService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/boletas")
@Tag(name = "Boletas / Órdenes", description = "Emisión y consulta de compras realizadas")
public class BoletaController {

    private final TiendaService tiendaService;

    public BoletaController(TiendaService tiendaService) {
        this.tiendaService = tiendaService;
    }

    @GetMapping
    @Operation(summary = "Listar todas las boletas (ADMIN y VENDEDOR)")
    public List<Boleta> listarTodas() {
        return tiendaService.listarBoletas();
    }

    @GetMapping("/mis-boletas")
    @Operation(summary = "Listar boletas del usuario autenticado")
    public List<Boleta> misBoletas(Authentication auth) {
        return tiendaService.listarBoletasPorEmail(auth.getName());
    }

    @PostMapping
    @Operation(summary = "Crear boleta / finalizar compra")
    public ResponseEntity<Boleta> crear(Authentication auth, @RequestBody List<TiendaService.ItemCompraRequest> items) {
        Boleta boleta = tiendaService.crearBoleta(auth.getName(), items);
        return ResponseEntity.status(HttpStatus.CREATED).body(boleta);
    }
}