package cl.duoc.biblioteca.controller;

import cl.duoc.biblioteca.model.Rol;
import cl.duoc.biblioteca.model.Usuario;
import cl.duoc.biblioteca.repository.UsuarioRepository;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/v1/usuarios")
@Tag(name = "Usuarios", description = "Administración de usuarios y roles (Solo ADMIN)")
public class UsuarioController {

    private final UsuarioRepository usuarioRepository;

    public UsuarioController(UsuarioRepository usuarioRepository) {
        this.usuarioRepository = usuarioRepository;
    }

    @GetMapping
    @Operation(summary = "Listar usuarios registrados")
    public List<Usuario> listar() {
        return usuarioRepository.findAll();
    }

    @PutMapping("/{id}/rol")
    @Operation(summary = "Cambiar rol de un usuario")
    public Usuario cambiarRol(@PathVariable Long id, @RequestBody Map<String, String> body) {
        Usuario u = usuarioRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Usuario no encontrado"));
        u.setRol(Rol.valueOf(body.get("rol").toUpperCase()));
        return usuarioRepository.save(u);
    }
}
