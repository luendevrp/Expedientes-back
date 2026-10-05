package com.uv.api_expedientes.Notas.HistoriaClinica;

import java.util.Date;

import org.springframework.stereotype.Service;

import com.uv.api_expedientes.Pacientes.Paciente;
import com.uv.api_expedientes.Pacientes.PacienteRepository;
import com.uv.api_expedientes.Users.User;
import com.uv.api_expedientes.Users.UserRepository;
import com.uv.api_expedientes.jwt.JwtService;

import com.uv.api_expedientes.Notas.HistoriaClinica.dtos.HistoriaClinicaDto;

import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class HistoriaClinicaService {

        private final HistoriaClinicaRepository historiaClinicaRepository;

        private final UserRepository usuarioRepository;

        private final PacienteRepository pacienteRepository;

        private final JwtService jwtService;

        private final com.uv.api_expedientes.Services.PdfService pdfService;

        public String guardarHistoria(HttpServletRequest request, int idPaciente,
                        HistoriaClinicaDto historiaClinicaDto) {

                HistoriaClinica nuevaHistoriaClinica = new HistoriaClinica();

                String Username = jwtService.getUsernameFromToken(jwtService.getTokenFromRequest(request));

                User usuario = usuarioRepository.findByUsername(Username)
                                .orElseThrow(() -> new RuntimeException("Usuario no encontrado"));

                Paciente pacienteRegistro = pacienteRepository.findById(idPaciente)
                                .orElseThrow(() -> new RuntimeException("Paciente no encontrado"));

                nuevaHistoriaClinica.setUsuario(usuario);
                nuevaHistoriaClinica.setPaciente(pacienteRegistro);

                nuevaHistoriaClinica.setMotivo_consulta(historiaClinicaDto.getMotivo_consulta());
                nuevaHistoriaClinica.setInterrogatorio(historiaClinicaDto.getInterrogatorio());
                nuevaHistoriaClinica.setPadecimiento_actual(historiaClinicaDto.getPadecimiento_actual());
                nuevaHistoriaClinica.setExploracion_fisica(historiaClinicaDto.getExploracion_fisica());
                nuevaHistoriaClinica.setPeso(historiaClinicaDto.getPeso());
                nuevaHistoriaClinica.setTalla(historiaClinicaDto.getTalla());
                nuevaHistoriaClinica.setImc(historiaClinicaDto.getImc());
                nuevaHistoriaClinica.setTension_arterial(historiaClinicaDto.getTension_arterial());
                nuevaHistoriaClinica.setFrecuencia_cardiaca(historiaClinicaDto.getFrecuencia_cardiaca());
                nuevaHistoriaClinica.setFrecuencia_respiratoria(historiaClinicaDto.getFrecuencia_respiratoria());
                nuevaHistoriaClinica.setTemperatura(historiaClinicaDto.getTemperatura());
                nuevaHistoriaClinica.setSaturacion(historiaClinicaDto.getSaturacion());
                nuevaHistoriaClinica.setGlicemia(historiaClinicaDto.getGlicemia());
                nuevaHistoriaClinica.setHemoglobina(historiaClinicaDto.getHemoglobina());
                nuevaHistoriaClinica.setHemotipo(historiaClinicaDto.getHemotipo());
                nuevaHistoriaClinica.setAntecedentes_heredo_familiares(
                                historiaClinicaDto.getAntecedentes_heredo_familiares());
                nuevaHistoriaClinica.setAntecedentes_no_patologicos(
                                historiaClinicaDto.getAntecedentes_no_patologicos());
                nuevaHistoriaClinica.setAntecedentes_patologicos(
                                historiaClinicaDto.getAntecedentes_patologicos());
                nuevaHistoriaClinica.setAntecedentes_quirurgicos(historiaClinicaDto.getAntecedentes_quirurgicos());
                nuevaHistoriaClinica.setMedicamentos_actuales(historiaClinicaDto.getMedicamentos_actuales());
                nuevaHistoriaClinica.setAlergias(historiaClinicaDto.getAlergias());
                nuevaHistoriaClinica.setAntecedentes_gineco_obstetricos(
                                historiaClinicaDto.getAntecedentes_gineco_obstetricos());
                nuevaHistoriaClinica.setCancer_prostata(historiaClinicaDto.getCancer_prostata());
                nuevaHistoriaClinica.setVacunas(historiaClinicaDto.getVacunas());
                nuevaHistoriaClinica.setAdicciones(historiaClinicaDto.getAdicciones());
                nuevaHistoriaClinica.setDiagnostico(historiaClinicaDto.getDiagnostico());
                nuevaHistoriaClinica.setTratamiento(historiaClinicaDto.getTratamiento());
                nuevaHistoriaClinica.setPlan_tratamiento(historiaClinicaDto.getPlan_tratamiento());
                nuevaHistoriaClinica.setObservaciones(historiaClinicaDto.getObservaciones());
                nuevaHistoriaClinica.setFecha_creacion(new Date());

                historiaClinicaRepository.save(nuevaHistoriaClinica);

                return "Registro con éxito";
        }

        public HistoriaClinicaDto obtenerPorId(int id) {
                HistoriaClinica historia = historiaClinicaRepository.findById(id)
                                .orElseThrow(() -> new RuntimeException("Historia clínica no encontrada"));

                return HistoriaClinicaDto.builder()
                                .id(historia.getId())
                                .motivo_consulta(historia.getMotivo_consulta())
                                .interrogatorio(historia.getInterrogatorio())
                                .padecimiento_actual(historia.getPadecimiento_actual())
                                .exploracion_fisica(historia.getExploracion_fisica())
                                .peso(historia.getPeso())
                                .talla(historia.getTalla())
                                .imc(historia.getImc())
                                .tension_arterial(historia.getTension_arterial())
                                .frecuencia_cardiaca(historia.getFrecuencia_cardiaca())
                                .frecuencia_respiratoria(historia.getFrecuencia_respiratoria())
                                .temperatura(historia.getTemperatura())
                                .saturacion(historia.getSaturacion())
                                .glicemia(historia.getGlicemia())
                                .hemoglobina(historia.getHemoglobina())
                                .hemotipo(historia.getHemotipo())
                                .antecedentes_heredo_familiares(historia.getAntecedentes_heredo_familiares())
                                .antecedentes_no_patologicos(historia.getAntecedentes_no_patologicos())
                                .antecedentes_patologicos(historia.getAntecedentes_patologicos())
                                .antecedentes_quirurgicos(historia.getAntecedentes_quirurgicos())
                                .medicamentos_actuales(historia.getMedicamentos_actuales())
                                .alergias(historia.getAlergias())
                                .antecedentes_gineco_obstetricos(historia.getAntecedentes_gineco_obstetricos())
                                .cancer_prostata(historia.getCancer_prostata())
                                .vacunas(historia.getVacunas())
                                .adicciones(historia.getAdicciones())
                                .diagnostico(historia.getDiagnostico())
                                .tratamiento(historia.getTratamiento())
                                .plan_tratamiento(historia.getPlan_tratamiento())
                                .observaciones(historia.getObservaciones())
                                .fecha_creacion(historia.getFecha_creacion())
                                .build();
        }

        public byte[] generarPdf(Integer id) {
                HistoriaClinica historia = historiaClinicaRepository.findById(id)
                                .orElseThrow(() -> new RuntimeException("Historia clínica no encontrada"));
                try {
                        return pdfService.generarHistoriaClinicaPdf(historia);
                } catch (java.io.IOException e) {
                        throw new RuntimeException("Error al generar PDF de historia clínica", e);
                }
        }
}