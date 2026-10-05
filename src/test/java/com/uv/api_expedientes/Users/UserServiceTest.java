package com.uv.api_expedientes.Users;

import static org.mockito.Mockito.*;
import static org.junit.jupiter.api.Assertions.*;

import java.util.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.*;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;

import com.uv.api_expedientes.AccessControl.Roles.Rol;
import com.uv.api_expedientes.AccessControl.Roles.RolRepository;
import com.uv.api_expedientes.Users.dtos.AllUsersDto;
import com.uv.api_expedientes.Users.dtos.UserDto;
import com.uv.api_expedientes.Users.dtos.UserEditDto;

@ExtendWith(MockitoExtension.class)
class UserServiceTest {

    @Mock
    private UserRepository userRepository;

    @Mock
    private RolRepository rolRepository;

    @InjectMocks
    private UserService userService;

    private User user;
    private Rol rol;

    @BeforeEach
    void setUp() {
        rol = new Rol();
        rol.setId(1);
        rol.setNombre("Administrador");

        user = new User();
        user.setId(10);
        user.setUsername("luis");
        user.setNombre("Luis");
        user.setTelefono("123456789");
        user.setFacultad("UV");
        user.setEspecialidad("ISW");
        user.setRol(rol);
        user.setActivo(true);
    }

    // Obtener todos los usuarios activos
    @Test
    void testGetUsers_ReturnsList() {
        when(userRepository.findByActivoTrue()).thenReturn(Arrays.asList(user));

        List<AllUsersDto> result = userService.getUsers();

        assertEquals(1, result.size());
        assertEquals("luis", result.get(0).getUsername());
        verify(userRepository).findByActivoTrue();

        System.out.println("[TEST] testGetUsers_ReturnsList ejecutado correctamente");
    }

    // Verifica que se lance una excepción si no hay usuarios activos
    @Test
    void testGetUsers_EmptyList_ThrowsException() {
        when(userRepository.findByActivoTrue()).thenReturn(Collections.emptyList());

        assertThrows(RuntimeException.class, () -> userService.getUsers());

        System.out.println("[TEST] testGetUsers_EmptyList_ThrowsException lanzó excepción correctamente");
    }

    // verifica obtener usuario por ID
    @Test
    void testGetUserById_ReturnsUser() {
        when(userRepository.findById(10)).thenReturn(Optional.of(user));

        UserDto result = userService.getUserById(10);

        assertEquals("luis", result.getUsername());
        assertEquals(1, result.getRolId());

        System.out.println("[TEST] testGetUserById_ReturnsUser funcionando");
    }

    // Verifica que se lance una excepción si el usuario no es encontrado por ID
    @Test
    void testGetUserById_NotFound_ThrowsException() {
        when(userRepository.findById(10)).thenReturn(Optional.empty());

        assertThrows(RuntimeException.class, () -> userService.getUserById(10));

        System.out.println("[TEST] testGetUserById_NotFound_ThrowsException lanzó la excepción esperada");
    }

    // Verifica desactivar usuario
    @Test
    void testDeactivateUser_Success() {
        when(userRepository.findById(10)).thenReturn(Optional.of(user));

        String result = userService.deactivateUser(10);

        assertEquals("Se desactivo el usuario", result);
        assertFalse(user.isActivo());
        verify(userRepository).save(user);

        System.out.println("[TEST] testDeactivateUser_Success completado con éxito");
    }

    // Verifica que se lance una excepción si el usuario no es encontrado al
    // desactivar
    @Test
    void testDeactivateUser_AlreadyInactive() {
        user.setActivo(false);
        when(userRepository.findById(10)).thenReturn(Optional.of(user));

        String result = userService.deactivateUser(10);

        assertEquals("Usuario ya está desactivado", result);
        verify(userRepository, never()).save(any());

        System.out.println("[TEST] testDeactivateUser_AlreadyInactive funcionando correctamente");
    }

    // Verifica actualización de usuario
    @Test
    void testUpdateUser_Success() {
        UserEditDto dto = new UserEditDto();
        dto.setNombre("Nuevo Nombre");
        dto.setRolId(1);

        when(userRepository.findById(10)).thenReturn(Optional.of(user));
        when(rolRepository.findById(1)).thenReturn(Optional.of(rol));

        String result = userService.UpdateUser(10, dto);

        assertEquals("Usuario actualizado correctamente", result);
        assertEquals("Nuevo Nombre", user.getNombre());
        verify(userRepository).save(user);

        System.out.println("[TEST] testUpdateUser_Success ejecutado correctamente");
    }

    // Verifica que se lance una excepción si el usuario no es encontrado al
    // actualizar
    @Test
    void testUpdateUser_UserNotFound() {
        UserEditDto dto = new UserEditDto();
        dto.setRolId(1);

        when(userRepository.findById(10)).thenReturn(Optional.empty());

        assertThrows(RuntimeException.class, () -> userService.UpdateUser(10, dto));

        System.out.println("[TEST] testUpdateUser_UserNotFound lanzó excepción como se esperaba");
    }

    // Veifica que el usuario pueda ser reactivado
    @Test
    void testReactivateUser_Success() {
        user.setActivo(false);
        when(userRepository.findById(10)).thenReturn(Optional.of(user));

        String result = userService.reactivateUser(10);

        assertEquals("Se reactivo el usuario", result);
        assertTrue(user.isActivo());
        verify(userRepository).save(user);

        System.out.println("[TEST] testReactivateUser_Success ejecutado con éxito");
    }

    // Verifica que se lance una excepción si el usuario ya esta activo
    @Test
    void testReactivateUser_AlreadyActive() {
        user.setActivo(true);
        when(userRepository.findById(10)).thenReturn(Optional.of(user));

        String result = userService.reactivateUser(10);

        assertEquals("Usuario ya está activo", result);
        verify(userRepository, never()).save(any());

        System.out.println("[TEST] testReactivateUser_AlreadyActive funcionando correctamente");
    }
}
