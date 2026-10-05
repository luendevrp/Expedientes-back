package com.uv.api_expedientes.AccessControl.Roles.dtos;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class RolNamesResponseDTO {
    private Integer id;
    private String nombre;

}