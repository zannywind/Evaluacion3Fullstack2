package cl.duoc.biblioteca.controller;

import cl.duoc.biblioteca.model.Rol;
import cl.duoc.biblioteca.model.Usuario;
import cl.duoc.biblioteca.repository.UsuarioRepository;
import cl.duoc.biblioteca.security.JwtUtil;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/api/v1/auth")
@Tag(name = "Autenticacion", description = "Endpoints para login y registro de usuarios con JWT")

public class AuthController {
    
    private final UsuarioRepository usuarioRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtUtil jwtUtil;

    public AuthController(UsuarioRepository usuarioRepository, PasswordEncoder passwordEncoder, JwtUtil jwtUtil) {
        this.usuarioRepository = usuarioRepository;
        this.passwordEncoder = passwordEncoder;
        this.jwtUtil = jwtUtil;
    }

    @PostMapping("/login")
    @Operation(summary = "Iniciar sesion", description = "Valida credenciales y devuelve un token JWT junto con los datos del usuario")
    public ResponseEntity<?> login(@RequestBody LoginRequest request) {
        var usuarioOpt = usuarioRepository.findByEmail(request.email().trim().toLowerCase());
        if (usuarioOpt.isEmpty() || !passwordEncoder.matches(request.password(), usuarioOpt.get().getPassword())) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                    .body(Map.of("error", "Credenciales invalidas"));
        }

        Usuario u = usuarioOpt.get();
        String token = jwtUtil.generarToken(u.getEmail(), u.getRol().name());

        return ResponseEntity.ok(Map.of(
            "token", token,
            "id", u.getId(),
            "nombre", u.getNombre(),
            "email", u.getEmail(),
            "direccion", u.getDireccion() != null ? u.getDireccion() : "",
            "rol", u.getRol().name()
        ));
    }

    @PostMapping ("/register")
    @Operation(summary = "Registrar cliente", description = "Crea una nueva cuenta con rol CLIENTE y devuelve un token JWT")
    public ResponseEntity<?> register(@RequestBody RegisterRequest request) {
        String email = request.email().trim().toLowerCase();
        if (usuarioRepository.existsByEmail(email)){
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(Map.of("error", "El correo ya esta registrado"));
        }

        Usuario nuevo = new Usuario(
            request.nombre().trim(),
            email,
            passwordEncoder.encode(request.password()),
            request.direccion() != null ? request.direccion().trim() : "",
            Rol.CLIENTE
        );
        usuarioRepository.save(nuevo);

        String token = jwtUtil.generarToken(nuevo.getEmail(), nuevo.getRol().name());

        return ResponseEntity.status(HttpStatus.CREATED).body(Map.of(
            "token", token,
            "id", nuevo.getId(),
            "nombre", nuevo.getNombre(),
            "email", nuevo.getEmail(),
            "direccion", nuevo.getDireccion() != null ? nuevo.getDireccion() : "",
            "rol", nuevo.getRol().name()
        ));
    }

    public record LoginRequest(String email, String password) {}
    public record RegisterRequest(String nombre, String email, String password, String direccion) {}
}
