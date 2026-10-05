package com.uv.api_expedientes.Users.dtos;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.util.Date;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class UserEditDto {
    private String username;
    private String nombre;
    private String curp;
    private String rfc;
    private String cedulaProfesional;
    private String especialidad;
    private String password;
    private String telefono;
    private String facultad;
    private Date fecha_creacion;
    private boolean pasante;
    private Integer rolId;

}
