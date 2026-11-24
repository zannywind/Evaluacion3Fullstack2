package cl.duoc.biblioteca.model;

import jakarta.persistence.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "BOLETAS")
public class Boleta {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column (nullable = false)
    private LocalDateTime fechaEmision = LocalDateTime.now();

    @Column (nullable = false)
    private Integer total = 0;

    @Column (nullable = false)
    private String estado = "COMPLETADA";

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "usuario_id", nullable = false)
    private Usuario usuario;

    @OneToMany(mappedBy = "boleta", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<DetalleBoleta> detalles = new ArrayList<>();

    public Boleta() {}

    public Boleta(Usuario usuario, Integer total, String estado) {
        this.usuario = usuario;
        this.total = total;
        this.estado = estado;
    }

    public Long getId() {return id;}
    public void setId(Long id) {this.id = id;}

    public LocalDateTime getFechaEmision() {return fechaEmision;}
    public void setFechaEmision(LocalDateTime fechaEmision) {this.fechaEmision = fechaEmision;}

    public Integer getTotal() {return total;}
    public void setTotal(Integer total) {this.total = total;}

    public String getEstado() {return estado;}
    public void setEstado(String estado) {this.estado = estado;}

    public Usuario getUsuario() {return usuario;}
    public void setUsuario(Usuario usuario) {this.usuario = usuario;}

    public List<DetalleBoleta> getDetalles() {return detalles;}
    public void setDetalles(List<DetalleBoleta> detalles) {this.detalles = detalles;}
}
