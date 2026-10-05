package com.uv.api_expedientes.AccessControl.Permisos.dtos;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class PermisosDto {
    private String Recurso;
    private String[] Acciones;
}
