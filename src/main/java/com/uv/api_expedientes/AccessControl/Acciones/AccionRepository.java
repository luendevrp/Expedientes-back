package com.uv.api_expedientes.AccessControl.Acciones;

import java.util.Optional;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface AccionRepository extends CrudRepository<Accion, Integer> {

    Optional<Accion> findByNombre(String nombre);
}