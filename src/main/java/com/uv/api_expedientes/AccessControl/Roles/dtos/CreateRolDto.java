package com.uv.api_expedientes.AccessControl.Roles.dtos;

import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CreateRolDto {
    private String nombre;
    private String descripcion;
    private List<PermisoRequest> permisos;

    @Data
    public static class PermisoRequest {
        private Integer recursoId;
        private List<Integer> accionesIds;
    }
}