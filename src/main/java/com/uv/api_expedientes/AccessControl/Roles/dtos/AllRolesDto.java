package com.uv.api_expedientes.AccessControl.Roles.dtos;

import java.util.Date;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class AllRolesDto {
    private Integer id;
    private String nombre;
    private String descripcion;
    private Date fecha_creacion;
    // private List<PermisosDto> Permisos;
}
