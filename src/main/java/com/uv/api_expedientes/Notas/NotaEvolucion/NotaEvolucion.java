package com.uv.api_expedientes.Notas.NotaEvolucion;

import java.util.Date;

import com.uv.api_expedientes.Pacientes.Paciente;
import com.uv.api_expedientes.Users.User;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "notas_evolucion")
public class NotaEvolucion {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    // Identificacion
    @ManyToOne
    @JoinColumn(name = "usuario_id")
    private User usuario;

    @ManyToOne
    @JoinColumn(name = "paciente_id")
    private Paciente paciente;

    @Column(nullable = false)
    private Date fecha_creacion;

    // Interrogatorio y exploración
    private String motivo_consulta;

    private String interrogatorio;

    private String padecimiento_actual;

    private String exploracion_fisica;

    // Signos vitales
    private int peso;
    private int talla;
    private float imc;

    @Column(length = 20)
    private String tension_arterial;

    private int frecuencia_cardiaca;
    private int frecuencia_respiratoria;
    private int temperatura;
    private int saturacion;
    private int glicemia;
    private int hemoglobina;

    @Column(length = 10)
    private String hemotipo;

    // Diagnóstico y tratamiento
    private String diagnostico;

    private String tratamiento;

    private String plan_tratamiento;

    private String observaciones;

}