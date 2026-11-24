package cl.duoc.biblioteca.repository;

import cl.duoc.biblioteca.model.Boleta;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface BoletaRepository extends JpaRepository<Boleta, Long> {
    List<Boleta> findByUsuarioId(Long usuarioId);
}
