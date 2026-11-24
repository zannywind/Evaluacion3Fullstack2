package cl.duoc.biblioteca.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

@Entity
@Table (name = "PRODUCTOS")
public class Producto {
    @Id
    @GeneratedValue (strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank
    @Column (nullable = false)
    private String nombre;

    @NotNull
    @Min(1)
    @Column (nullable = false)
    private Integer precio;

    @NotNull
    @Min(0)
    @Column (nullable = false)
    private Integer stock = 10;

    @Column (nullable = false)
    private Boolean disponible = false;
    private String imagen;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "categoria_id", nullable = false)
    private Categoria categoria;

    public Producto() {}

    public Producto(String nombre, Integer precio, Integer stock, Boolean disponible, String imagen, Categoria categoria) {
        this.nombre = nombre;
        this.precio = precio;
        this.stock = stock;
        this.disponible = disponible;
        this.imagen = imagen;
        this.categoria = categoria;
    }

    public Long getId() {return id;}
    public void setId(Long id) {this.id = id;}

    public String getNombre() {return nombre;}
    public void setNombre(String nombre) {this.nombre = nombre;}

    public Integer getPrecio() {return precio;}
    public void setPrecio(Integer precio) {this.precio = precio;}

    public Integer getStock() {return stock;}
    public void setStock(Integer stock) {this.stock = stock;}

    public Boolean getDisponible() {return disponible;}
    public void setDisponible(Boolean disponible) {this.disponible = disponible;}

    public String getImagen() {return imagen;}
    public void setImagen(String imagen) {this.imagen = imagen;}

    public Categoria getCategoria() {return categoria;}
    public void setCategoria(Categoria categoria) {this.categoria = categoria;}

}
