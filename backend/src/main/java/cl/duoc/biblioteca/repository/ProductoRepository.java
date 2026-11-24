package cl.duoc.biblioteca.repository;

import cl.duoc.biblioteca.model.Producto;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface ProductoRepository extends JpaRepository<Producto, Long> {
    List<Producto> findByCategoriaId(Long categoriaId);
    List<Producto> findByDisponibleTrue();
}
