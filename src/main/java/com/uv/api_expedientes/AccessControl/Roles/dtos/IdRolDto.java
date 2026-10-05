package com.uv.api_expedientes.AccessControl.Roles.dtos;

import java.util.Date;
import java.util.List;

import com.uv.api_expedientes.AccessControl.Permisos.dtos.PermisosDto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class IdRolDto {
    private Integer id;
    private String nombre;
    private String descripcion;
    private Date fecha_creacion;
    private List<PermisosDto> Permisos;
}
