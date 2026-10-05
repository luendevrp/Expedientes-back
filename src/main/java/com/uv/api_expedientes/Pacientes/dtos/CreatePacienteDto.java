package com.uv.api_expedientes.Pacientes.dtos;

import java.util.Date;

import com.uv.api_expedientes.Pacientes.Paciente.Sexo;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class CreatePacienteDto {
    private String matricula;
    private String nombre;
    private Sexo sexo;
    private Date fecha_nacimiento;
    private int grupo;
    private int semestre;
    private String telefono;
    private String programa_educativo;
    private String residencia;
    private String religion;
    private String escolaridad;
    private String nss;
    private String origen;
    private String estado_civil;
    private String facultad;

    // Identificación
    private String tipo_paciente;
    private String curp;

    // Contacto y Ubicación
    private String email;
    private String calle;
    private String numero_exterior;
    private String numero_interior;
    private String colonia;
    private String cp;
    private String municipio;
    private String entidad_federativa;

    // Responsable
    private String nombre_responsable;
    private String parentesco_responsable;
    private String telefono_responsable;
    private String calle_responsable;
    private String numero_exterior_responsable;
    private String numero_interior_responsable;
    private String colonia_responsable;
    private String cp_responsable;
    private String municipio_responsable;
    private String entidad_federativa_responsable;

    // Trabajador
    private String numero_personal;
    private String puesto;
    private String facultad_adscripcion;
    private String tipo_contratacion;

    // Sociodemográfico
    // private boolean habla_lengua_indigena;
    private String lengua_indigena;
}