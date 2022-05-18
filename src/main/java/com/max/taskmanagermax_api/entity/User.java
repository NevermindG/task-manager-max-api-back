package com.max.taskmanagermax_api.entity;

import lombok.*;


import javax.persistence.*;
import java.util.HashSet;
import java.util.Set;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table (name = "usuario", uniqueConstraints = {@UniqueConstraint (columnNames = {"username"}), @UniqueConstraint (columnNames = "email")})
public class User {
    
    @Id
    @GeneratedValue (strategy = GenerationType.IDENTITY)
    private Long id;
    
    private String nombre;
    private String apellido;
    private String username;
    private String email;
    private String password;
    private String tokenPassword;
    private int    estado;
    
    
    @ManyToMany (fetch = FetchType.EAGER, cascade = CascadeType.ALL)
    @JoinTable (name = "usuario_rol", joinColumns = @JoinColumn (name = "usuario_id", referencedColumnName = "id"),
            inverseJoinColumns = @JoinColumn (name = "rol_id", referencedColumnName = "id"))
    private Set<Role> roles = new HashSet<>();
    
    public User(String nombre, String apellido, String username, String email, String password, int estado) {
        this.nombre = nombre;
        this.apellido = apellido;
        this.username = username;
        this.email = email;
        this.password = password;
        this.estado = estado;
    }
    
}
