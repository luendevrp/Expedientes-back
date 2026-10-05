package com.uv.api_expedientes.Users;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.List;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import com.uv.api_expedientes.AccessControl.Permisos.Permiso;
import com.uv.api_expedientes.AccessControl.Roles.Rol;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "usuarios")
public class User implements UserDetails {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(unique = true, nullable = false)
    private Integer id;
    private String username;
    private String nombre;
    private String curp;
    private String rfc;
    private String cedulaProfesional;
    private String especialidad;
    @Column(nullable = false)
    private String password;
    private String telefono;
    private String facultad;
    private boolean activo;
    private boolean pasante;
    private Date fecha_creacion;

    @ManyToOne
    @JoinColumn(name = "rol_id")
    private Rol rol;

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        List<GrantedAuthority> authorities = new ArrayList<>();

        // Agregar el rol como autoridad principal
        authorities.add(new SimpleGrantedAuthority("ROLE_" + rol.getNombre().toUpperCase()));

        // Agregar los permisos asociados al rol
        if (rol.getPermisos() != null) {
            for (Permiso permiso : rol.getPermisos()) {
                String authority = permiso.getRecurso().getNombre().toUpperCase() + "_" +
                        permiso.getAccion().getNombre().toUpperCase();
                authorities.add(new SimpleGrantedAuthority(authority));
            }
        }

        return authorities;
    }

    @Override
    public boolean isAccountNonExpired() {
        // TODO Auto-generated method stub
        return UserDetails.super.isAccountNonExpired();
    }

    @Override
    public boolean isAccountNonLocked() {
        // TODO Auto-generated method stub
        return UserDetails.super.isAccountNonLocked();
    }

    @Override
    public boolean isCredentialsNonExpired() {
        // TODO Auto-generated method stub
        return UserDetails.super.isCredentialsNonExpired();
    }

    @Override
    public boolean isEnabled() {
        // TODO Auto-generated method stub
        return this.activo;
    }

    @Override
    public String getUsername() {
        return this.username;
    }

}
