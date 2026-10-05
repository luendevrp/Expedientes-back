package com.uv.api_expedientes.Pacientes;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.uv.api_expedientes.Pacientes.dtos.AllPacientesDto;
import com.uv.api_expedientes.Pacientes.dtos.IdPacienteDto;
import com.uv.api_expedientes.Pacientes.dtos.PacienteEditDto;

import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;

import org.springframework.boot.test.mock.mockito.MockBean;

import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.when;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import java.util.Collections;
import java.util.Date;

@SpringBootTest
@AutoConfigureMockMvc(addFilters = false)
public class PacienteControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    private PacienteService pacienteService;

    // Probar ver todos los pacientes
    @Test
    void testObtenerPacientes_Returns200() throws Exception {
        AllPacientesDto.PacienteInfo info = new AllPacientesDto.PacienteInfo();
        info.setId(1);
        info.setNombre("Juan");
        info.setSexo(Paciente.Sexo.MASCULINO);
        info.setTelefono("2345678901");
        info.setFecha_creacion(new Date());
        info.setFecha_nacimiento(new Date());

        AllPacientesDto dto = new AllPacientesDto();
        dto.setCantidad_registros(1);
        dto.setCantidad_mujeres(0);
        dto.setCantidad_hombres(1);
        dto.setPacientes(Collections.singletonList(info));

        when(pacienteService.obtenerPacientes()).thenReturn(dto);

        mockMvc.perform(get("/api/pacientes/Ver"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.cantidad_registros").value(1))
                .andExpect(jsonPath("$.cantidad_mujeres").value(0))
                .andExpect(jsonPath("$.cantidad_hombres").value(1))
                .andExpect(jsonPath("$.pacientes[0].nombre").value("Juan"))
                .andExpect(jsonPath("$.pacientes[0].sexo").value("MASCULINO"));
    }

    // Probar crear un nuevo paciente
    @Test
    void testGuardarPaciente_CreatesSuccessfully() throws Exception {

        Paciente paciente = new Paciente();
        paciente.setNombre("Nuevo Paciente");

        doNothing().when(pacienteService).guardarPaciente(any(Paciente.class));

        mockMvc.perform(post("/api/pacientes/Crear")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(paciente)))
                .andExpect(status().isOk());
    }

    // Probar ver paciente por ID
    @Test
    void testObtenerPacientePorId_ReturnsPaciente() throws Exception {

        IdPacienteDto dto = new IdPacienteDto();
        dto.setId(1);
        dto.setNombre("Luis");

        when(pacienteService.obtenerPorId(1)).thenReturn(dto);

        mockMvc.perform(
                get("/api/pacientes/Ver/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.nombre").value("Luis"));
    }

    // Probar editar paciente
    @Test
    void testActualizarPaciente_ReturnsOk() throws Exception {

        PacienteEditDto edit = new PacienteEditDto();
        edit.setNombre("Nombre Nuevo");

        doNothing().when(pacienteService).actualizarPaciente(eq(1), any(PacienteEditDto.class));

        mockMvc.perform(put("/api/pacientes/Editar/1")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(edit)))
                .andExpect(status().isOk());
    }

    // Probar desactivar paciente
    @Test
    void testDesactivarPaciente_ReturnsMessage() throws Exception {

        when(pacienteService.desactivarPaciente(1))
                .thenReturn("Paciente desactivado");

        mockMvc.perform(
                delete("/api/pacientes/Eliminar/1"))
                .andExpect(status().isOk())
                .andExpect(content().string("Paciente desactivado"));
    }
}
