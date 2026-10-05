package com.uv.api_expedientes.Notas.NotaEvolucion;

import java.util.Date;

import org.springframework.stereotype.Service;

import com.uv.api_expedientes.Pacientes.Paciente;
import com.uv.api_expedientes.Pacientes.PacienteRepository;
import com.uv.api_expedientes.Users.User;
import com.uv.api_expedientes.Users.UserRepository;
import com.uv.api_expedientes.jwt.JwtService;

import com.uv.api_expedientes.Notas.NotaEvolucion.dtos.NotaEvolucionDto;

import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class NotaEvolucionService {

        private final NotaEvolucionRepository notaEvolucionRepository;

        private final UserRepository usuarioRepository;

        private final PacienteRepository pacienteRepository;

        private final com.uv.api_expedientes.Services.PdfService pdfService;

        private final JwtService jwtService;

        public String guardarNota(HttpServletRequest request, Integer idPaciente,
                        NotaEvolucionDto notaEvolucionDto) {

                NotaEvolucion nuevaNotaEvolucion = new NotaEvolucion();

                String Username = jwtService.getUsernameFromToken(jwtService.getTokenFromRequest(request));

                User usuario = usuarioRepository.findByUsername(Username)
                                .orElseThrow(() -> new RuntimeException("Usuario no encontrado"));

                Paciente pacienteRegistro = pacienteRepository.findById(idPaciente)
                                .orElseThrow(() -> new RuntimeException("Paciente no encontrado"));

                nuevaNotaEvolucion.setUsuario(usuario);
                nuevaNotaEvolucion.setPaciente(pacienteRegistro);
                nuevaNotaEvolucion.setFecha_creacion(new Date());
                nuevaNotaEvolucion.setMotivo_consulta(notaEvolucionDto.getMotivo_consulta());
                nuevaNotaEvolucion.setInterrogatorio(notaEvolucionDto.getInterrogatorio());
                nuevaNotaEvolucion.setPadecimiento_actual(notaEvolucionDto.getPadecimiento_actual());
                nuevaNotaEvolucion.setExploracion_fisica(notaEvolucionDto.getExploracion_fisica());
                nuevaNotaEvolucion.setPeso(notaEvolucionDto.getPeso());
                nuevaNotaEvolucion.setTalla(notaEvolucionDto.getTalla());
                nuevaNotaEvolucion.setImc(notaEvolucionDto.getImc());
                nuevaNotaEvolucion.setTension_arterial(notaEvolucionDto.getTension_arterial());
                nuevaNotaEvolucion.setFrecuencia_cardiaca(notaEvolucionDto.getFrecuencia_cardiaca());
                nuevaNotaEvolucion.setFrecuencia_respiratoria(notaEvolucionDto.getFrecuencia_respiratoria());
                nuevaNotaEvolucion.setTemperatura(notaEvolucionDto.getTemperatura());
                nuevaNotaEvolucion.setSaturacion(notaEvolucionDto.getSaturacion());
                nuevaNotaEvolucion.setGlicemia(notaEvolucionDto.getGlicemia());
                nuevaNotaEvolucion.setHemoglobina(notaEvolucionDto.getHemoglobina());
                nuevaNotaEvolucion.setHemotipo(notaEvolucionDto.getHemotipo());
                nuevaNotaEvolucion.setDiagnostico(notaEvolucionDto.getDiagnostico());
                nuevaNotaEvolucion.setTratamiento(notaEvolucionDto.getTratamiento());
                nuevaNotaEvolucion.setPlan_tratamiento(notaEvolucionDto.getPlan_tratamiento());
                nuevaNotaEvolucion.setObservaciones(notaEvolucionDto.getObservaciones());
                notaEvolucionRepository.save(nuevaNotaEvolucion);
                return "Registro con éxito";
        }

        public NotaEvolucionDto obtenerPorId(int id) {
                NotaEvolucion nota = notaEvolucionRepository.findById(id)
                                .orElseThrow(() -> new RuntimeException("Nota de evolución no encontrada"));

                return NotaEvolucionDto.builder()
                                .id(nota.getId())
                                .motivo_consulta(nota.getMotivo_consulta())
                                .interrogatorio(nota.getInterrogatorio())
                                .padecimiento_actual(nota.getPadecimiento_actual())
                                .exploracion_fisica(nota.getExploracion_fisica())
                                .peso(nota.getPeso())
                                .talla(nota.getTalla())
                                .imc(nota.getImc())
                                .tension_arterial(nota.getTension_arterial())
                                .frecuencia_cardiaca(nota.getFrecuencia_cardiaca())
                                .frecuencia_respiratoria(nota.getFrecuencia_respiratoria())
                                .temperatura(nota.getTemperatura())
                                .saturacion(nota.getSaturacion())
                                .glicemia(nota.getGlicemia())
                                .hemoglobina(nota.getHemoglobina())
                                .hemotipo(nota.getHemotipo())
                                .diagnostico(nota.getDiagnostico())
                                .tratamiento(nota.getTratamiento())
                                .plan_tratamiento(nota.getPlan_tratamiento())
                                .observaciones(nota.getObservaciones())
                                .fecha_creacion(nota.getFecha_creacion())
                                .build();

        }

        public byte[] generarPdf(Integer id) {
                NotaEvolucion nota = notaEvolucionRepository.findById(id)
                                .orElseThrow(() -> new RuntimeException("Nota no encontrada"));
                try {
                        return pdfService.generarNotaMedicaPdf(nota);
                } catch (java.io.IOException e) {
                        throw new RuntimeException("Error al generar PDF de nota médica", e);
                }
        }
}
