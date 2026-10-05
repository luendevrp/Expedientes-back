package com.uv.api_expedientes.Pacientes;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import static org.junit.jupiter.api.Assertions.*;

import java.util.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.*;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;

import com.uv.api_expedientes.Notas.HistoriaClinica.HistoriaClinica;
import com.uv.api_expedientes.Notas.HistoriaClinica.HistoriaClinicaRepository;
import com.uv.api_expedientes.Notas.NotaEvolucion.NotaEvolucion;
import com.uv.api_expedientes.Notas.NotaEvolucion.NotaEvolucionRepository;
import com.uv.api_expedientes.Pacientes.dtos.AllPacientesDto;
import com.uv.api_expedientes.Pacientes.dtos.IdPacienteDto;
import com.uv.api_expedientes.Pacientes.dtos.PacienteEditDto;

@ExtendWith(MockitoExtension.class)
public class PacientesServiceTest {

    @Mock
    private PacienteRepository pacienteRepository;

    @Mock
    private NotaEvolucionRepository notaEvolucionRepository;

    @Mock
    private HistoriaClinicaRepository historiaClinicaRepository;

    @InjectMocks
    private PacienteService pacienteService;

    private Paciente paciente;

    @BeforeEach
    void setUp() {
        paciente = Paciente.builder()
                .id(1)
                .matricula("123")
                .nombre("Luis")
                .sexo(Paciente.Sexo.MASCULINO)
                .telefono("12345")
                .fecha_nacimiento(new Date())
                .fecha_creacion(new Date())
                .activo(true)
                .build();
    }

    // Verificar conteo correcto de pacientes
    @Test
    void testObtenerPacientes_ReturnsCorrectCounts() {
        when(pacienteRepository.findByActivoTrue()).thenReturn(List.of(paciente));

        AllPacientesDto result = pacienteService.obtenerPacientes();

        assertEquals(1, result.getCantidad_registros());
        assertEquals(1, result.getCantidad_hombres());
        assertEquals(0, result.getCantidad_mujeres());
        assertEquals(1, result.getPacientes().size());

        System.out.println("[TEST OK] testObtenerPacientes_ReturnsCorrectCounts");
    }

    // Verificar guardar paciente correctamente
    @Test
    void testGuardarPaciente_SavesSuccessfully() {
        pacienteService.guardarPaciente(paciente);

        verify(pacienteRepository, times(1)).save(any(Paciente.class));

        System.out.println("[TEST OK] testGuardarPaciente_SavesSuccessfully");
    }

    // Verificar actualización correcta
    @Test
    void testActualizarPaciente_Success() {
        PacienteEditDto edit = new PacienteEditDto();
        edit.setNombre("Nuevo nombre");

        when(pacienteRepository.findById(1)).thenReturn(Optional.of(paciente));

        pacienteService.actualizarPaciente(1, edit);

        assertEquals("Nuevo nombre", paciente.getNombre());
        verify(pacienteRepository).save(paciente);

        System.out.println("[TEST OK] testActualizarPaciente_Success");
    }

    // Actualizar paciente no encontrado
    @Test
    void testActualizarPaciente_NotFound_ThrowsException() {
        when(pacienteRepository.findById(1)).thenReturn(Optional.empty());

        assertThrows(RuntimeException.class,
                () -> pacienteService.actualizarPaciente(1, new PacienteEditDto()));

        System.out.println("[TEST OK] testActualizarPaciente_NotFound_ThrowsException");
    }

    // Actualizar paciente inactivo
    @Test
    void testActualizarPaciente_Inactive_ThrowsException() {
        paciente.setActivo(false);
        when(pacienteRepository.findById(1)).thenReturn(Optional.of(paciente));

        assertThrows(RuntimeException.class,
                () -> pacienteService.actualizarPaciente(1, new PacienteEditDto()));

        System.out.println("[TEST OK] testActualizarPaciente_Inactive_ThrowsException");
    }

    // Obtener por ID correctamente
    @Test
    void testObtenerPorId_ReturnsData() {
        NotaEvolucion nota = NotaEvolucion.builder()
                .id(10)
                .fecha_creacion(new Date())
                .build();

        HistoriaClinica historia = HistoriaClinica.builder()
                .id(20)
                .fecha_creacion(new Date())
                .build();

        when(pacienteRepository.findById(1)).thenReturn(Optional.of(paciente));
        when(notaEvolucionRepository.findByPacienteId(1)).thenReturn(List.of(nota));
        when(historiaClinicaRepository.findByPacienteId(1)).thenReturn(List.of(historia));

        IdPacienteDto result = pacienteService.obtenerPorId(1);

        assertEquals(1, result.getId());
        assertEquals(2, result.getRegistros().size());

        System.out.println("[TEST OK] testObtenerPorId_ReturnsData");
    }

    // Obtener por ID no encontrado
    @Test
    void testObtenerPorId_NotFound_ThrowsException() {
        when(pacienteRepository.findById(1)).thenReturn(Optional.empty());

        assertThrows(RuntimeException.class, () -> pacienteService.obtenerPorId(1));

        System.out.println("[TEST OK] testObtenerPorId_NotFound_ThrowsException");
    }

    // Obtener por ID inactivo
    @Test
    void testObtenerPorId_Inactive_ThrowsException() {
        paciente.setActivo(false);
        when(pacienteRepository.findById(1)).thenReturn(Optional.of(paciente));

        assertThrows(RuntimeException.class, () -> pacienteService.obtenerPorId(1));

        System.out.println("[TEST OK] testObtenerPorId_Inactive_ThrowsException");
    }

    // Desactivar paciente correctamente
    @Test
    void testDesactivarPaciente_Success() {
        when(pacienteRepository.findById(1)).thenReturn(Optional.of(paciente));

        String respuesta = pacienteService.desactivarPaciente(1);

        assertEquals("Se eliminó el paciente", respuesta);
        assertFalse(paciente.isActivo());
        verify(pacienteRepository).save(paciente);

        System.out.println("[TEST OK] testDesactivarPaciente_Success");
    }

    // Desactivar paciente ya inactivo
    @Test
    void testDesactivarPaciente_AlreadyInactive() {
        paciente.setActivo(false);
        when(pacienteRepository.findById(1)).thenReturn(Optional.of(paciente));

        String respuesta = pacienteService.desactivarPaciente(1);

        assertEquals("Paciente ya está desactivado", respuesta);

        System.out.println("[TEST OK] testDesactivarPaciente_AlreadyInactive");
    }

    // Desactivar paciente no encontrado
    @Test
    void testDesactivarPaciente_NotFound_ThrowsException() {
        when(pacienteRepository.findById(1)).thenReturn(Optional.empty());

        assertThrows(RuntimeException.class, () -> pacienteService.desactivarPaciente(1));

        System.out.println("[TEST OK] testDesactivarPaciente_NotFound_ThrowsException");
    }

    // Estadisticas con nulls
    @Test
    void testObtenerEstadisticas_HandlesNulls() {
        Paciente pacienteNulls = new Paciente();
        pacienteNulls.setSexo(Paciente.Sexo.MASCULINO);
        pacienteNulls.setFecha_creacion(new Date());
        // Otros campos a null por defecto

        NotaEvolucion notaNull = new NotaEvolucion();
        // Diagnostico null

        HistoriaClinica historiaNull = new HistoriaClinica();
        // Diagnostico null

        when(pacienteRepository.findByActivoTrue()).thenReturn(List.of(pacienteNulls));
        when(notaEvolucionRepository.findAll()).thenReturn(List.of(notaNull));
        when(historiaClinicaRepository.findAll()).thenReturn(List.of(historiaNull));
        when(pacienteRepository.countByActivoTrue()).thenReturn(1);
        when(notaEvolucionRepository.count()).thenReturn(1L);

        com.uv.api_expedientes.Pacientes.dtos.SatisticsPacienteDto stats = pacienteService.obtenerEstadisticas();

        assertNotNull(stats);
        assertEquals(1, stats.getPorFacultad().get("Sin Facultad"));
        assertEquals(1, stats.getPorTipoPaciente().get("Sin Tipo"));
        assertEquals(2, stats.getTopSintomas().get("Sin Diagnóstico"));

        System.out.println("[TEST OK] testObtenerEstadisticas_HandlesNulls");
    }
}
