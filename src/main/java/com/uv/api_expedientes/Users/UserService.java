package com.uv.api_expedientes.Users;

import java.util.List;
import java.util.Optional;

import org.springframework.stereotype.Service;

import com.uv.api_expedientes.AccessControl.Roles.Rol;
import com.uv.api_expedientes.AccessControl.Roles.RolRepository;
import com.uv.api_expedientes.Users.dtos.AllUsersDto;
import com.uv.api_expedientes.Users.dtos.UserDto;
import com.uv.api_expedientes.Users.dtos.UserEditDto;

import lombok.RequiredArgsConstructor;

import org.springframework.security.crypto.password.PasswordEncoder;

@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;
    private final RolRepository rolRepository;
    private final PasswordEncoder passwordEncoder;

    public List<AllUsersDto> getUsers() {
        List<User> users = userRepository.findByActivoTrue();

        if (users.isEmpty()) {
            throw new RuntimeException("No hay usuarios activos");
        }

        List<AllUsersDto> allUsersDto = users.stream()
                .map(user -> new AllUsersDto(
                        user.getId(),
                        user.getUsername(),
                        user.getNombre(),
                        user.getTelefono(),
                        user.getFacultad(),
                        user.getEspecialidad(),
                        user.getRol() != null ? user.getRol().getNombre() : "No hay rol asignado",
                        user.isPasante()))
                .toList();

        return allUsersDto;
    }

    public UserDto getUserById(Integer id) {
        User user = userRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Usuario no encontrado con ID: " + id));

        return UserDto.builder()
                .id(user.getId())
                .username(user.getUsername())
                .nombre(user.getNombre())
                .curp(user.getCurp())
                .rfc(user.getRfc())
                .cedulaProfesional(user.getCedulaProfesional())
                .especialidad(user.getEspecialidad())
                // .password(user.getPassword())
                .telefono(user.getTelefono())
                .facultad(user.getFacultad())
                .activo(user.isActivo())
                .pasante(user.isPasante())
                .fecha_creacion(user.getFecha_creacion())
                .rolId(user.getRol() != null ? user.getRol().getId() : null)
                .build();
    }

    public String deactivateUser(Integer id) {
        try {
            User user = userRepository.findById(id).orElseThrow(() -> new RuntimeException("Usuario no encontrado"));
            if (!user.isActivo()) {
                return "Usuario ya está desactivado";
            }
            user.setActivo(false);
            userRepository.save(user);
            return "Se desactivo el usuario";
        } catch (Exception e) {
            throw new RuntimeException("No se pudo desactivar el usuario" + e.getMessage());
        }
    }

    public String UpdateUser(Integer id, UserEditDto userEditDto) {

        User user = userRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Usuario no encontrado con ID: " + id));

        Rol newrol = rolRepository.findById(userEditDto.getRolId())
                .orElseThrow(() -> new RuntimeException("Usuario no encontrado con ID: " + id));

        Optional.ofNullable(userEditDto.getUsername()).ifPresent(user::setUsername);
        Optional.ofNullable(userEditDto.getNombre()).ifPresent(user::setNombre);
        Optional.ofNullable(userEditDto.getCurp()).ifPresent(user::setCurp);
        Optional.ofNullable(userEditDto.getRfc()).ifPresent(user::setRfc);
        Optional.ofNullable(userEditDto.getCedulaProfesional()).ifPresent(user::setCedulaProfesional);
        Optional.ofNullable(userEditDto.getEspecialidad()).ifPresent(user::setEspecialidad);
        Optional.ofNullable(userEditDto.getPassword())
                .ifPresent(password -> user.setPassword(passwordEncoder.encode(password)));
        Optional.ofNullable(userEditDto.getTelefono()).ifPresent(user::setTelefono);
        Optional.ofNullable(userEditDto.getFacultad()).ifPresent(user::setFacultad);
        Optional.ofNullable(userEditDto.getFecha_creacion()).ifPresent(user::setFecha_creacion);
        user.setPasante(userEditDto.isPasante());
        Optional.ofNullable(userEditDto.getRolId()).ifPresent(rolId -> user.setRol(newrol));
        userRepository.save(user);
        return "Usuario actualizado correctamente";

    }

    public String reactivateUser(Integer id) {
        try {
            User user = userRepository.findById(id).orElseThrow(() -> new RuntimeException("Usuario no encontrado"));
            if (user.isActivo()) {
                return "Usuario ya está activo";
            }
            user.setActivo(true);
            userRepository.save(user);
            return "Se reactivo el usuario";
        } catch (Exception e) {
            throw new RuntimeException("No se pudo reactivar el usuario" + e.getMessage());
        }
    }

    // public String exportar() {
    // return "Pdf generado correctamente";
    // }
}
