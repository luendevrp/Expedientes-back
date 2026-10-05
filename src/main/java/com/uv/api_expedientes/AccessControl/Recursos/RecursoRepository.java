package com.uv.api_expedientes.AccessControl.Recursos;

import java.util.Optional;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface RecursoRepository extends CrudRepository<Recurso, Integer> {

    Optional<Recurso> findByNombre(String nombre);
}
