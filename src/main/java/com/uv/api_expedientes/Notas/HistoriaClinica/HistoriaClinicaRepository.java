package com.uv.api_expedientes.Notas.HistoriaClinica;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface HistoriaClinicaRepository extends JpaRepository<HistoriaClinica, Integer> {
    List<HistoriaClinica> findByPacienteId(Integer idPaciente);
}
