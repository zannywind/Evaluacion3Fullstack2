package cl.duoc.biblioteca.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;

@Entity
@Table (name = "USUARIOS")

public class Usuario {
    
    @Id 
    @GeneratedValue (strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank 
    @Column (nullable = false)
    private String nombre;

    @NotBlank
    @Column (nullable = false)
    private String email;

    @NotBlank
    @Column (nullable = false)
    private String password;
    private String direccion;

    @Enumerated (EnumType.STRING)
    @Column (nullable = false)
    private Rol rol = Rol.CLIENTE;

    public Usuario() {}

    public Usuario(String nombre, String email, String password, String direccion, Rol rol) {
        this.nombre = nombre;
        this.email = email;
        this.password = password;
        this.direccion = direccion;
        this.rol = rol;
    }

    public Long getId() {return id;}
    public void setId(Long id) {this.id = id;}

    public String getNombre() {return nombre;}
    public void setNombre(String nombre) {this.nombre = nombre;}

    public String getEmail() {return email;}
    public void setEmail(String email) {this.email = email;}

    public String getPassword() {return password;}
    public void setPassword(String password) {this.password = password;}

    public String getDireccion() {return direccion;}
    public void setDireccion(String direccion) {this.direccion = direccion;}

    public Rol getRol() {return rol;}
    public void setRol(Rol rol) {this.rol = rol;}
}
