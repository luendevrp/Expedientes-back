package com.uv.api_expedientes.AccessControl.Permisos;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface PermisoRepository extends JpaRepository<Permiso, Integer> {
    List<Permiso> findByRol_Id(Integer rolId);

    @Modifying
    @Query("DELETE FROM Permiso p WHERE p.rol.id = :rolId")
    void deleteByRolId(@Param("rolId") Integer rolId);

    Optional<Permiso> findByRolIdAndRecursoIdAndAccionId(Integer rolId, Integer recursoId, Integer accionId);
}