package cl.duoc.biblioteca.repository;

import cl.duoc.biblioteca.model.DetalleBoleta;
import org.springframework.data.jpa.repository.JpaRepository;

public interface DetalleBoletaRepository extends JpaRepository<DetalleBoleta, Long> { 
}
