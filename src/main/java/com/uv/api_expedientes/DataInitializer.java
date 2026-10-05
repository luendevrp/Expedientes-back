package com.uv.api_expedientes;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Optional;

import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import com.uv.api_expedientes.AccessControl.Acciones.Accion;
import com.uv.api_expedientes.AccessControl.Acciones.AccionRepository;
import com.uv.api_expedientes.AccessControl.Permisos.Permiso;
import com.uv.api_expedientes.AccessControl.Permisos.PermisoRepository;
import com.uv.api_expedientes.AccessControl.Recursos.Recurso;
import com.uv.api_expedientes.AccessControl.Recursos.RecursoRepository;
import com.uv.api_expedientes.AccessControl.Roles.Rol;
import com.uv.api_expedientes.AccessControl.Roles.RolRepository;
import com.uv.api_expedientes.Users.User;
import com.uv.api_expedientes.Users.UserRepository;

import lombok.AllArgsConstructor;

@Component
@AllArgsConstructor
public class DataInitializer implements CommandLineRunner {

    private final AccionRepository accionRepository;
    private final RolRepository rolRepository;
    private final RecursoRepository recursoRepository;
    private final PermisoRepository permisoRepository;
    private final UserRepository usuarioRepository;
    private final PasswordEncoder passwordEncoder;

    @Override
    public void run(String... args) {

        // Crear acciones básicas con sus respectivas descripciones
        List<Accion> accionesBasicas = List.of(
                new Accion("Crear", "Permite crear"),
                new Accion("Editar", "Permite editar"),
                new Accion("Eliminar", "Permite eliminar"),
                new Accion("Ver", "Permite ver"),
                new Accion("Estadisticas", "Permite generar estadisticas"));

        for (Accion accion : accionesBasicas) {
            if (accionRepository.findByNombre(accion.getNombre()).isEmpty()) {
                accionRepository.save(accion);
                System.out.println("Acción creada: " + accion.getNombre());
            }
        }

        // Crear Roles básicos con sus respectivas descripciones
        List<Rol> rolesBasicos = List.of(
                new Rol("superAdmin", "Tiene acceso a todo el sistema"));

        for (Rol rol : rolesBasicos) {
            if (rolRepository.findByNombre(rol.getNombre()).isEmpty()) {
                rolRepository.save(rol);
                System.out.println("Rol creado: " + rol.getNombre());
            }
        }

        // Crear Recursos básicos con sus respectivas descripciones
        List<Recurso> recursosBasiscos = List.of(
                new Recurso("pacientes", "Recurso de paciente"),
                new Recurso("usuarios", "Recurso de paciente"),
                new Recurso("registros", "Recurso de paciente"),
                new Recurso("roles", "Recurso de roles"));

        for (Recurso recurso : recursosBasiscos) {
            if (recursoRepository.findByNombre(recurso.getNombre()).isEmpty()) {
                recursoRepository.save(recurso);
                System.out.println("Recurso creado: " + recurso.getNombre());
            }
        }

        Rol rolUsuario = rolRepository.findByNombre("superAdmin")
                .orElseThrow(() -> new RuntimeException("Rol no encontrado"));

        // Crear usuario Administrador
        User usuario = User.builder()
                .username("superAdmin")
                .nombre("Super Administrador")
                .curp("SADMIN000000HDFRRN09")
                .rfc("SADMIN000000")
                .cedulaProfesional("00000000")
                .especialidad("Administración")
                .password(passwordEncoder.encode("SuperAdmin123$"))
                .telefono("0000000000")
                .facultad("Administración")
                .activo(true)
                .fecha_creacion(new Date())
                .rol(rolUsuario)
                .build();

        if (usuarioRepository.findByUsername(usuario.getUsername()).isEmpty()) {

            usuarioRepository.save(usuario);

            System.out.println("Usuario " + usuario + " creado ");
        }

        // Añadir permisos a super Administrador

        // Obtener todos los recursos y acciones
        List<Recurso> todosRecursos = (ArrayList<Recurso>) recursoRepository.findAll();
        List<Accion> todasAcciones = (ArrayList<Accion>) accionRepository.findAll();

        for (Recurso recurso : todosRecursos) {
            for (Accion accion : todasAcciones) {
                Optional<Permiso> permisoExistente = permisoRepository.findByRolIdAndRecursoIdAndAccionId(
                        rolUsuario.getId(), recurso.getId(), accion.getId());

                if (permisoExistente.isEmpty()) {
                    // Crear un nuevo permiso con los objetos obtenidos
                    Permiso nuevoPermiso = new Permiso();
                    nuevoPermiso.setRol(rolUsuario);
                    nuevoPermiso.setRecurso(recurso);
                    nuevoPermiso.setAccion(accion);

                    // Guardar el nuevo permiso
                    permisoRepository.save(nuevoPermiso);

                    System.out.println("Permiso creado para el rol super Admin: Recurso " +
                            recurso.getNombre() +
                            " - Acción " + accion.getNombre());
                }
            }
        }

    }

}
