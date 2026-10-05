package com.uv.api_expedientes.Notas.NotaEvolucion.dtos;

import java.util.Date;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class NotaEvolucionDto {
    private Integer id;

    // Interrogatorio y exploracion
    private String motivo_consulta;
    private String interrogatorio;
    private String padecimiento_actual;
    private String exploracion_fisica;

    // Signos vitales
    private int peso;
    private int talla;
    private float imc;
    private String tension_arterial;
    private int frecuencia_cardiaca;
    private int frecuencia_respiratoria;
    private int temperatura;
    private int saturacion;
    private int glicemia;
    private int hemoglobina;
    private String hemotipo;

    // Diagnostico y medicaciones
    private String diagnostico;
    private String tratamiento;
    private String plan_tratamiento;
    private String observaciones;
    private Date fecha_creacion;
}
