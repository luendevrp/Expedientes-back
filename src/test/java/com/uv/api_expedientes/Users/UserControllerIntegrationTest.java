package com.uv.api_expedientes.Users;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.uv.api_expedientes.Auth.AuthService;
import com.uv.api_expedientes.Auth.dtos.RegisterUserDto;
import com.uv.api_expedientes.Users.dtos.*;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;

import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.util.*;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc(addFilters = false)
public class UserControllerIntegrationTest {

        @Autowired
        private MockMvc mockMvc;

        @Autowired
        private ObjectMapper objectMapper;

        @MockBean
        private UserService userService;

        @MockBean
        private AuthService authService;

        // Probar ver todos los usuarios activos
        @Test
        void testGetAllActiveUsers_ReturnsList() throws Exception {

                List<AllUsersDto> users = Arrays.asList(
                                AllUsersDto.builder()
                                                .id(1)
                                                .username("LuisUser")
                                                .nombre("Luis")
                                                .telefono("123456")
                                                .facultad("FC")
                                                .especialidad("Medicina")
                                                .rolNombre("Admin")
                                                .build(),
                                AllUsersDto.builder()
                                                .id(2)
                                                .username("AnaUser")
                                                .nombre("Ana")
                                                .telefono("789456")
                                                .facultad("FEI")
                                                .especialidad("Enfermería")
                                                .rolNombre("Medico")
                                                .build());

                when(userService.getUsers()).thenReturn(users);

                mockMvc.perform(
                                get("/api/usuarios/Ver"))
                                .andExpect(status().isOk())
                                .andExpect(jsonPath("$.length()").value(2))
                                .andExpect(jsonPath("$[0].nombre").value("Luis"))
                                .andExpect(jsonPath("$[1].nombre").value("Ana"));
        }

        // Probar obtener usuario por ID
        @Test
        void testGetUserById_ReturnsUser() throws Exception {

                UserDto dto = UserDto.builder()
                                .id(1)
                                .nombre("Luis")
                                .username("luis123")
                                .activo(true)
                                .rolId(1)
                                .build();

                when(userService.getUserById(1)).thenReturn(dto);

                mockMvc.perform(
                                get("/api/usuarios/Ver/1"))
                                .andExpect(status().isOk())
                                .andExpect(jsonPath("$.nombre").value("Luis"))
                                .andExpect(jsonPath("$.username").value("luis123"))
                                .andExpect(jsonPath("$.rolId").value(1));
        }

        // Probar crear usuario
        @Test
        void testRegister_CreatesUser() throws Exception {

                RegisterUserDto req = new RegisterUserDto();
                req.setNombre("Luis");

                MatriculaDto resp = new MatriculaDto("MAT123");

                when(authService.register(any(RegisterUserDto.class))).thenReturn(resp);

                mockMvc.perform(
                                post("/api/usuarios/Crear")
                                                .contentType(MediaType.APPLICATION_JSON)
                                                .content(objectMapper.writeValueAsString(req)))
                                .andExpect(status().isOk())
                                .andExpect(jsonPath("$.matricula").value("MAT123"));
        }

        // Probar eliminar usuario
        @Test
        void testDeactivateUser_ReturnsMessage() throws Exception {

                when(userService.deactivateUser(1)).thenReturn("Usuario desactivado");

                mockMvc.perform(
                                delete("/api/usuarios/Eliminar/1"))
                                .andExpect(status().isOk())
                                .andExpect(jsonPath("$.mensaje").value("Usuario desactivado"));
        }

        // Probar editar usuario
        @Test
        void testUpdateUser_ReturnsMessage() throws Exception {

                UserEditDto edit = new UserEditDto();
                edit.setNombre("Luis Nuevo");

                when(userService.UpdateUser(Mockito.eq(1), any(UserEditDto.class)))
                                .thenReturn("Usuario actualizado");

                mockMvc.perform(
                                put("/api/usuarios/Editar/1")
                                                .contentType(MediaType.APPLICATION_JSON)
                                                .content(objectMapper.writeValueAsString(edit)))
                                .andExpect(status().isOk())
                                .andExpect(jsonPath("$.mensaje").value("Usuario actualizado"));
        }

        // Probar reactivar usuario
        @Test
        void testReactivateUser_ReturnsMessage() throws Exception {

                when(userService.reactivateUser(1)).thenReturn("Usuario reactivado");

                mockMvc.perform(
                                post("/api/usuarios/Reactivar/1"))
                                .andExpect(status().isOk())
                                .andExpect(content().string("Usuario reactivado"));
        }
}
