package com.uv.api_expedientes.Auth.dtos;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class RegisterUserDto {
    private String nombre;
    private String curp;
    private String rfc;
    private String cedulaProfesional;
    private String especialidad;
    private String password;
    private String telefono;
    private String facultad;
    private Integer rolId;
    private boolean pasante;
}
