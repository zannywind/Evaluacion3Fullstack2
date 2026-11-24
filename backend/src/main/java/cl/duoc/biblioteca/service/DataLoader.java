package cl.duoc.biblioteca.service;

import cl.duoc.biblioteca.model.*;
import cl.duoc.biblioteca.repository.*;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

@Component
public class DataLoader implements CommandLineRunner {

    private final CategoriaRepository categoriaRepository;
    private final ProductoRepository productoRepository;
    private final UsuarioRepository usuarioRepository;
    private final PasswordEncoder passwordEncoder;

    public DataLoader(CategoriaRepository categoriaRepository,
                      ProductoRepository productoRepository,
                      UsuarioRepository usuarioRepository,
                      PasswordEncoder passwordEncoder) {
        this.categoriaRepository = categoriaRepository;
        this.productoRepository = productoRepository;
        this.usuarioRepository = usuarioRepository;
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    public void run(String... args) {
        if (usuarioRepository.count() == 0) {
            usuarioRepository.save(new Usuario(
                    "Admin Mai-estor",
                    "admin@maiestor.cl",
                    passwordEncoder.encode("admin123"),
                    "Av. Pajaritos 1234, Maipú",
                    Rol.ADMIN
            ));
            usuarioRepository.save(new Usuario(
                    "Vendedor Tienda",
                    "vendedor@maiestor.cl",
                    passwordEncoder.encode("vendedor123"),
                    "Mall Plaza Oeste Local 10, Maipú",
                    Rol.VENDEDOR
            ));
            usuarioRepository.save(new Usuario(
                    "Cliente Demo",
                    "cliente@maiestor.cl",
                    passwordEncoder.encode("cliente123"),
                    "Plaza de Maipú 500, Maipú",
                    Rol.CLIENTE
            ));
        }

        Categoria videojuegos = categoriaRepository.findByNombre("Videojuegos")
                .orElseGet(() -> categoriaRepository.save(new Categoria("Videojuegos", "Juegos físicos y digitales")));
        Categoria consolas = categoriaRepository.findByNombre("Consolas")
                .orElseGet(() -> categoriaRepository.save(new Categoria("Consolas", "Consolas de sobremesa y portátiles")));
        Categoria accesorios = categoriaRepository.findByNombre("Accesorios")
                .orElseGet(() -> categoriaRepository.save(new Categoria("Accesorios", "Audífonos, controles y periféricos")));

        if (productoRepository.count() == 0) {
            productoRepository.save(new Producto("Zelda: Tears of the Kingdom", 59990, 15, true, "/products/zelda.jpg", videojuegos));
            productoRepository.save(new Producto("PlayStation 5 Console", 549990, 8, false, "/products/ps5.jpg", consolas));
            productoRepository.save(new Producto("Xbox Series X", 499990, 10, true, "/products/xbox.jpg", consolas));
            productoRepository.save(new Producto("Super Mario Odyssey", 49990, 20, false, "/products/mario.jpg", videojuegos));
            productoRepository.save(new Producto("Audífonos Gamer Wireless", 29990, 25, true, "/products/headset.jpg", accesorios));
        }
    }
}