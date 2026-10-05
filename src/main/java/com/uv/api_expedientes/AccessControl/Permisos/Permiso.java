package com.uv.api_expedientes.AccessControl.Permisos;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.uv.api_expedientes.AccessControl.Acciones.Accion;
import com.uv.api_expedientes.AccessControl.Recursos.Recurso;
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
@Table(name = "permisos")
public class Permiso {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(unique = true, nullable = false)
    private Integer id;

    @JsonBackReference
    @ManyToOne
    @JoinColumn(name = "rol_id")
    private Rol rol;

    @ManyToOne
    @JoinColumn(name = "recurso_id")
    private Recurso recurso;

    @ManyToOne
    @JoinColumn(name = "accion_id")
    private Accion accion;
}
