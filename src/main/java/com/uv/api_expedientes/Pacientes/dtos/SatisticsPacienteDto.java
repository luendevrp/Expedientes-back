package com.uv.api_expedientes.Pacientes.dtos;

import java.util.Date;
import java.util.List;
import java.util.Map;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class SatisticsPacienteDto {
    private long totalPacientes;
    private long totalNotas;
    Map<String, Integer> porSexo;
    Map<String, Integer> porFacultad;
    Map<String, Integer> porProgramaEducativo;
    Map<Integer, Integer> porSemestre;
    Map<String, Integer> porTipoPaciente;
    Map<String, Integer> porEstadoCivil;
    Map<String, Integer> porLenguaIndigena;
    Map<String, Integer> casosPorDiaMes;
    Map<String, Integer> casosPorAnio;
    Map<String, Integer> topSintomas;
}
