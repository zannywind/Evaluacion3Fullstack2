package cl.duoc.biblioteca.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;

@Entity
@Table(name = "DETALLE_BOLETAS")
public class DetalleBoleta {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column (nullable = false)
    private Integer cantidad;

    @Column (nullable = false)
    private Integer precioUnitario;

    @Column (nullable = false)
    private Integer subtotal;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "boleta_id", nullable = false)
    @JsonIgnore
    private Boleta boleta;

    @ManyToOne (fetch = FetchType.EAGER)
    @JoinColumn(name = "producto_id", nullable = false)
    private Producto producto;

    public DetalleBoleta() {}

    public DetalleBoleta(Boleta boleta, Producto producto, Integer cantidad, Integer precioUnitario) {
        this.boleta = boleta;
        this.producto = producto;
        this.cantidad = cantidad;
        this.precioUnitario = precioUnitario;
        this.subtotal = cantidad * precioUnitario;
    }

    public Long getId() {return id;}
    public void setId(Long id) {this.id = id;}

    public Integer getCantidad() {return cantidad;}
    public void setCantidad(Integer cantidad) {this.cantidad = cantidad;}

    public Integer getPrecioUnitario() {return precioUnitario;}
    public void setPrecioUnitario(Integer precioUnitario) {this.precioUnitario = precioUnitario;}

    public Integer getSubtotal() {return subtotal;}
    public void setSubtotal(Integer subtotal) {this.subtotal = subtotal;}

    public Boleta getBoleta() {return boleta;}
    public void setBoleta(Boleta boleta) {this.boleta = boleta;}

    public Producto getProducto() {return producto;}
    public void setProducto(Producto producto) {this.producto = producto;}
}