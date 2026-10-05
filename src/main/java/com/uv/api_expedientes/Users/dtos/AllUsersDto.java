package com.uv.api_expedientes.Users.dtos;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class AllUsersDto {
    private Integer id;
    private String username;
    private String nombre;
    private String telefono;
    private String facultad;
    private String especialidad;
    private String rolNombre;
    private boolean pasante;
}
