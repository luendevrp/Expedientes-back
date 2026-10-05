package com.uv.api_expedientes.AccessControl.Roles;

import java.util.Optional;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface RolRepository extends CrudRepository<Rol, Integer> {

    Optional<Rol> findByNombre(String nombre);
}