package com.uv.api_expedientes.AccessControl.Roles;

import java.util.ArrayList;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;

import com.uv.api_expedientes.AccessControl.Roles.dtos.IdRolDto;
import com.uv.api_expedientes.AccessControl.Roles.dtos.RolNamesResponseDTO;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;

import com.uv.api_expedientes.AccessControl.Acciones.Accion;
import com.uv.api_expedientes.AccessControl.Acciones.AccionRepository;
import com.uv.api_expedientes.AccessControl.Permisos.Permiso;
import com.uv.api_expedientes.AccessControl.Permisos.PermisoRepository;
import com.uv.api_expedientes.AccessControl.Permisos.dtos.PermisosDto;
import com.uv.api_expedientes.AccessControl.Recursos.Recurso;
import com.uv.api_expedientes.AccessControl.Recursos.RecursoRepository;
import com.uv.api_expedientes.AccessControl.Roles.dtos.AllRolesDto;
import com.uv.api_expedientes.AccessControl.Roles.dtos.CreateRolDto;

@Service
@RequiredArgsConstructor
public class RolService {

    private final RolRepository rolRepository;
    private final PermisoRepository permisoRepository;
    private final RecursoRepository recursoRepository;;
    private final AccionRepository accionRepository;

    public List<AllRolesDto> getAllRoles() {

        Iterable<Rol> roles = rolRepository.findAll();
        List<AllRolesDto> allRolesDtos = new ArrayList<>();

        for (Rol rol : roles) {
            // List<PermisosDto> permisosDtos = ObtenerPermisosPorRol(rol.getId());
            AllRolesDto allRolesDto = AllRolesDto.builder()
                    .id(rol.getId())
                    .nombre(rol.getNombre())
                    .descripcion(rol.getDescripcion())
                    .fecha_creacion(rol.getFecha_creacion())
                    // .Permisos(permisosDtos)
                    .build();
            allRolesDtos.add(allRolesDto);

        }

        return allRolesDtos;
    }

    public List<RolNamesResponseDTO> obtenerNombresRoles() {
        List<Rol> roles = (List<Rol>) rolRepository.findAll();

        return roles.stream()
                .map(rol -> new RolNamesResponseDTO(rol.getId(), rol.getNombre()))
                .collect(Collectors.toList());
    }

    public IdRolDto getRolById(Integer id) {

        Rol rol = rolRepository.findById(id).orElseThrow(() -> new RuntimeException("Rol not found with id: " + id));
        List<PermisosDto> permisosDtos = ObtenerPermisosPorRol(rol.getId());
        IdRolDto idRol = IdRolDto.builder()
                .id(rol.getId())
                .nombre(rol.getNombre())
                .descripcion(rol.getDescripcion())
                .fecha_creacion(rol.getFecha_creacion())
                .Permisos(permisosDtos)
                .build();
        return idRol;
    }

    @Transactional
    public Void createRol(CreateRolDto createRolDto) {

        Rol rol = Rol.builder()
                .nombre(createRolDto.getNombre())
                .descripcion(createRolDto.getDescripcion())
                .activo(true)
                .fecha_creacion(new Date())
                .build();
        rolRepository.save(rol);

        if (createRolDto.getPermisos() != null) {

            for (CreateRolDto.PermisoRequest permisoRequest : createRolDto.getPermisos()) {

                Recurso recurso = recursoRepository.findById(permisoRequest.getRecursoId()).orElseThrow(
                        () -> new RuntimeException("Recurso not found with id: " + permisoRequest.getRecursoId()));

                for (Integer accionRequest : permisoRequest.getAccionesIds()) {
                    Accion accion = accionRepository.findById(accionRequest).orElseThrow(
                            () -> new RuntimeException("Recurso not found with id: " + permisoRequest.getRecursoId()));

                    Permiso permiso = Permiso.builder()
                            .rol(rol)
                            .recurso(recurso)
                            .accion(accion)
                            .build();
                    permisoRepository.save(permiso);
                }
            }
        }

        return null;

    }

    public Void deleteRol(Integer id) {
        Rol rol = rolRepository.findById(id).orElseThrow(() -> new RuntimeException("Rol not found with id: " + id));
        rol.setActivo(false);
        rolRepository.save(rol);
        return null;
    }

    @Transactional
    public Void updateRol(Integer id, CreateRolDto createRolDto) {
        // System.out.println("Permisos antes de borrar: " +
        // permisoRepository.findByRol_Id(id).size());

        // Eliminar permisos existentes del rol
        permisoRepository.deleteByRolId(id);

        Rol rol = rolRepository.findById(id).orElseThrow(() -> new RuntimeException("Rol not found with id: " + id));

        Optional.ofNullable(createRolDto.getNombre()).ifPresent(rol::setNombre);
        Optional.ofNullable(createRolDto.getDescripcion()).ifPresent(rol::setDescripcion);
        rolRepository.save(rol);

        if (createRolDto.getPermisos() != null) {

            for (CreateRolDto.PermisoRequest permisoRequest : createRolDto.getPermisos()) {

                Recurso recurso = recursoRepository.findById(permisoRequest.getRecursoId()).orElseThrow(
                        () -> new RuntimeException("Recurso not found with id: " +
                                permisoRequest.getRecursoId()));

                for (Integer accionRequest : permisoRequest.getAccionesIds()) {
                    Accion accion = accionRepository.findById(accionRequest).orElseThrow(
                            () -> new RuntimeException("Recurso not found with id: " +
                                    permisoRequest.getRecursoId()));

                    Permiso permiso = Permiso.builder()
                            .rol(rol)
                            .recurso(recurso)
                            .accion(accion)
                            .build();
                    permisoRepository.save(permiso);
                }
            }
        }

        return null;
    }

    // Método para obtener los permisos de un rol específico
    private List<PermisosDto> ObtenerPermisosPorRol(Integer rolId) {
        Map<String, List<String>> mapa = new HashMap<>();
        List<PermisosDto> permisosDtos = new ArrayList<>();
        List<Permiso> listaPermisos = permisoRepository.findByRol_Id(rolId);

        for (Permiso permiso : listaPermisos) {
            String recursoNombre = permiso.getRecurso().getNombre();
            String accionNombre = permiso.getAccion().getNombre();

            // Si el mapa ya tiene el recurso, agrega la acción, si no crea lista nueva
            if (!mapa.containsKey(recursoNombre)) {
                mapa.put(recursoNombre, new ArrayList<>());
            }
            mapa.get(recursoNombre).add(accionNombre);
        }

        for (Map.Entry<String, List<String>> entry : mapa.entrySet()) {
            PermisosDto dto = new PermisosDto();
            dto.setRecurso(entry.getKey());
            // Convertimos la lista a array
            dto.setAcciones(entry.getValue().toArray(new String[0]));
            permisosDtos.add(dto);
        }
        return permisosDtos;
    }
}
