package com.uv.api_expedientes.Notas.NotaEvolucion;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface NotaEvolucionRepository extends JpaRepository<NotaEvolucion, Integer> {
    List<NotaEvolucion> findByPacienteId(Integer idPaciente);
}