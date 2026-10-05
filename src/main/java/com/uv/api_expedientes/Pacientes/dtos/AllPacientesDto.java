package com.uv.api_expedientes.Pacientes.dtos;

import java.util.Date;
import java.util.List;

import com.uv.api_expedientes.Pacientes.Paciente.Sexo;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class AllPacientesDto {
    private int cantidad_registros;
    private int cantidad_mujeres;
    private int cantidad_hombres;
    private List<PacienteInfo> pacientes;

    @Data
    public static class PacienteInfo {
        private int id;
        // private String matricula;
        private String nombre;
        private Sexo sexo;
        private String telefono;
        private Date fecha_nacimiento;
        private Date fecha_creacion;
    }

}
