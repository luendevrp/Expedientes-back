package com.uv.api_expedientes.Pacientes;

import java.time.LocalDate;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import org.springframework.stereotype.Service;

import com.uv.api_expedientes.Notas.HistoriaClinica.HistoriaClinica;
import com.uv.api_expedientes.Notas.HistoriaClinica.HistoriaClinicaRepository;
import com.uv.api_expedientes.Notas.NotaEvolucion.NotaEvolucion;
import com.uv.api_expedientes.Notas.NotaEvolucion.NotaEvolucionRepository;
import com.uv.api_expedientes.Pacientes.dtos.AllPacientesDto;
import com.uv.api_expedientes.Pacientes.dtos.IdPacienteDto;
import com.uv.api_expedientes.Pacientes.dtos.PacienteEditDto;
import com.uv.api_expedientes.Pacientes.dtos.SatisticsPacienteDto;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class PacienteService {

    private final PacienteRepository pacienteRepository;

    private final NotaEvolucionRepository notaEvolucionRepository;

    private final HistoriaClinicaRepository historiaClinicaRepository;

    public AllPacientesDto obtenerPacientes() {

        List<Paciente> pacientes = pacienteRepository.findByActivoTrue();

        int cantidad_mujeres = (int) pacientes.stream().filter(p -> p.getSexo() == Paciente.Sexo.FEMENINO).count();
        int cantidad_hombres = (int) pacientes.stream().filter(p -> p.getSexo() == Paciente.Sexo.MASCULINO).count();
        int cantidad_registros = pacientes.size();

        List<AllPacientesDto.PacienteInfo> pacientesInfo = new ArrayList<>();
        for (Paciente p : pacientes) {
            AllPacientesDto.PacienteInfo info = new AllPacientesDto.PacienteInfo();
            info.setId(p.getId());
            // info.setMatricula(p.getMatricula());
            info.setNombre(p.getNombre());
            info.setSexo(p.getSexo());
            info.setTelefono(p.getTelefono());
            info.setFecha_nacimiento(p.getFecha_nacimiento());
            info.setFecha_creacion(p.getFecha_creacion());
            pacientesInfo.add(info);
        }

        AllPacientesDto dto = AllPacientesDto.builder()
                .cantidad_registros(cantidad_registros)
                .cantidad_mujeres(cantidad_mujeres)
                .cantidad_hombres(cantidad_hombres)
                .pacientes(pacientesInfo)
                .build();

        return dto;
    }

    public Void guardarPaciente(Paciente paciente) {
        Paciente nuevoPaciente = Paciente.builder()
                .matricula(paciente.getMatricula())
                .nombre(paciente.getNombre())
                .sexo(paciente.getSexo())
                .fecha_nacimiento(paciente.getFecha_nacimiento())
                .grupo(paciente.getGrupo())
                .semestre(paciente.getSemestre())
                .telefono(paciente.getTelefono())
                .programa_educativo(paciente.getPrograma_educativo())
                .residencia(paciente.getResidencia())
                .religion(paciente.getReligion())
                .escolaridad(paciente.getEscolaridad())
                .nss(paciente.getNss())
                .origen(paciente.getOrigen())
                .estado_civil(paciente.getEstado_civil())
                .facultad(paciente.getFacultad())
                .tipo_paciente(paciente.getTipo_paciente())
                .curp(paciente.getCurp())
                .email(paciente.getEmail())
                .calle(paciente.getCalle())
                .numero_exterior(paciente.getNumero_exterior())
                .numero_interior(paciente.getNumero_interior())
                .colonia(paciente.getColonia())
                .cp(paciente.getCp())
                .municipio(paciente.getMunicipio())
                .entidad_federativa(paciente.getEntidad_federativa())
                .nombre_responsable(paciente.getNombre_responsable())
                .parentesco_responsable(paciente.getParentesco_responsable())
                .telefono_responsable(paciente.getTelefono_responsable())
                .calle_responsable(paciente.getCalle_responsable())
                .numero_exterior_responsable(paciente.getNumero_exterior_responsable())
                .numero_interior_responsable(paciente.getNumero_interior_responsable())
                .colonia_responsable(paciente.getColonia_responsable())
                .cp_responsable(paciente.getCp_responsable())
                .municipio_responsable(paciente.getMunicipio_responsable())
                .entidad_federativa_responsable(paciente.getEntidad_federativa_responsable())
                .numero_personal(paciente.getNumero_personal())
                .puesto(paciente.getPuesto())
                .tipo_contratacion(paciente.getTipo_contratacion())
                // .habla_lengua_indigena(paciente.isHabla_lengua_indigena())
                .lengua_indigena(paciente.getLengua_indigena())
                .fecha_creacion(new Date())
                .activo(true)
                .build();
        pacienteRepository.save(nuevoPaciente);
        return null;
    }

    public Void actualizarPaciente(int id, PacienteEditDto pacienteEditDto) {
        Paciente paciente = pacienteRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Paciente no encontrado"));

        if (!paciente.isActivo()) {
            throw new RuntimeException("Paciente no activo");
        }

        Optional.ofNullable(pacienteEditDto.getMatricula()).ifPresent(paciente::setMatricula);
        Optional.ofNullable(pacienteEditDto.getNombre()).ifPresent(paciente::setNombre);
        Optional.ofNullable(pacienteEditDto.getSexo()).ifPresent(paciente::setSexo);
        Optional.ofNullable(pacienteEditDto.getFecha_nacimiento()).ifPresent(paciente::setFecha_nacimiento);
        Optional.ofNullable(pacienteEditDto.getGrupo()).ifPresent(paciente::setGrupo);
        Optional.ofNullable(pacienteEditDto.getSemestre()).ifPresent(paciente::setSemestre);
        Optional.ofNullable(pacienteEditDto.getTelefono()).ifPresent(paciente::setTelefono);
        Optional.ofNullable(pacienteEditDto.getPrograma_educativo()).ifPresent(paciente::setPrograma_educativo);
        Optional.ofNullable(pacienteEditDto.getResidencia()).ifPresent(paciente::setResidencia);
        Optional.ofNullable(pacienteEditDto.getReligion()).ifPresent(paciente::setReligion);
        Optional.ofNullable(pacienteEditDto.getEscolaridad()).ifPresent(paciente::setEscolaridad);
        Optional.ofNullable(pacienteEditDto.getNss()).ifPresent(paciente::setNss);
        Optional.ofNullable(pacienteEditDto.getOrigen()).ifPresent(paciente::setOrigen);
        Optional.ofNullable(pacienteEditDto.getEstado_civil()).ifPresent(paciente::setEstado_civil);
        Optional.ofNullable(pacienteEditDto.getFacultad()).ifPresent(paciente::setFacultad);

        Optional.ofNullable(pacienteEditDto.getTipo_paciente()).ifPresent(paciente::setTipo_paciente);
        Optional.ofNullable(pacienteEditDto.getCurp()).ifPresent(paciente::setCurp);
        Optional.ofNullable(pacienteEditDto.getEmail()).ifPresent(paciente::setEmail);
        Optional.ofNullable(pacienteEditDto.getCalle()).ifPresent(paciente::setCalle);
        Optional.ofNullable(pacienteEditDto.getNumero_exterior()).ifPresent(paciente::setNumero_exterior);
        Optional.ofNullable(pacienteEditDto.getNumero_interior()).ifPresent(paciente::setNumero_interior);
        Optional.ofNullable(pacienteEditDto.getColonia()).ifPresent(paciente::setColonia);
        Optional.ofNullable(pacienteEditDto.getCp()).ifPresent(paciente::setCp);
        Optional.ofNullable(pacienteEditDto.getMunicipio()).ifPresent(paciente::setMunicipio);
        Optional.ofNullable(pacienteEditDto.getEntidad_federativa()).ifPresent(paciente::setEntidad_federativa);
        Optional.ofNullable(pacienteEditDto.getNombre_responsable()).ifPresent(paciente::setNombre_responsable);
        Optional.ofNullable(pacienteEditDto.getParentesco_responsable()).ifPresent(paciente::setParentesco_responsable);
        Optional.ofNullable(pacienteEditDto.getTelefono_responsable()).ifPresent(paciente::setTelefono_responsable);
        Optional.ofNullable(pacienteEditDto.getCalle_responsable()).ifPresent(paciente::setCalle_responsable);
        Optional.ofNullable(pacienteEditDto.getNumero_exterior_responsable())
                .ifPresent(paciente::setNumero_exterior_responsable);
        Optional.ofNullable(pacienteEditDto.getNumero_interior_responsable())
                .ifPresent(paciente::setNumero_interior_responsable);
        Optional.ofNullable(pacienteEditDto.getColonia_responsable()).ifPresent(paciente::setColonia_responsable);
        Optional.ofNullable(pacienteEditDto.getCp_responsable()).ifPresent(paciente::setCp_responsable);
        Optional.ofNullable(pacienteEditDto.getMunicipio_responsable()).ifPresent(paciente::setMunicipio_responsable);
        Optional.ofNullable(pacienteEditDto.getEntidad_federativa_responsable())
                .ifPresent(paciente::setEntidad_federativa_responsable);
        Optional.ofNullable(pacienteEditDto.getNumero_personal()).ifPresent(paciente::setNumero_personal);
        Optional.ofNullable(pacienteEditDto.getPuesto()).ifPresent(paciente::setPuesto);
        Optional.ofNullable(pacienteEditDto.getTipo_contratacion()).ifPresent(paciente::setTipo_contratacion);
        // paciente.setHabla_lengua_indigena(pacienteEditDto.isHabla_lengua_indigena());

        Optional.ofNullable(pacienteEditDto.getLengua_indigena()).ifPresent(paciente::setLengua_indigena);

        pacienteRepository.save(paciente);
        return null;
    }

    public IdPacienteDto obtenerPorId(int id) {
        Paciente paciente = pacienteRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Paciente no encontrado"));
        if (!paciente.isActivo()) {
            throw new RuntimeException("Paciente no está activo");
        }

        List<NotaEvolucion> notasEvolucion = notaEvolucionRepository.findByPacienteId(id);

        List<HistoriaClinica> historiasClinicas = historiaClinicaRepository.findByPacienteId(id);

        List<IdPacienteDto.Registros> registros = new ArrayList<>();

        for (NotaEvolucion nota : notasEvolucion) {
            IdPacienteDto.Registros registro = new IdPacienteDto.Registros();
            registro.setRegistro_id(nota.getId());
            registro.setTipo_registro("Nota de Evolución");
            registro.setFecha_creacion(nota.getFecha_creacion());
            registros.add(registro);
        }
        for (HistoriaClinica historia : historiasClinicas) {
            IdPacienteDto.Registros registro = new IdPacienteDto.Registros();
            registro.setRegistro_id(historia.getId());
            registro.setTipo_registro("Historia Clínica");
            registro.setFecha_creacion(historia.getFecha_creacion());
            registros.add(registro);
        }

        registros.sort((r1, r2) -> r2.getFecha_creacion().compareTo(r1.getFecha_creacion()));

        IdPacienteDto idPacienteDto = IdPacienteDto.builder()
                .id(paciente.getId())
                .matricula(paciente.getMatricula())
                .nombre(paciente.getNombre())
                .sexo(paciente.getSexo())
                .fecha_nacimiento(paciente.getFecha_nacimiento())
                .grupo(paciente.getGrupo())
                .semestre(paciente.getSemestre())
                .telefono(paciente.getTelefono())
                .programa_educativo(paciente.getPrograma_educativo())
                .residencia(paciente.getResidencia())
                .religion(paciente.getReligion())
                .escolaridad(paciente.getEscolaridad())
                .nss(paciente.getNss())
                .origen(paciente.getOrigen())
                .estado_civil(paciente.getEstado_civil())
                .facultad(paciente.getFacultad())
                .tipo_paciente(paciente.getTipo_paciente())
                .curp(paciente.getCurp())
                .email(paciente.getEmail())
                .calle(paciente.getCalle())
                .numero_exterior(paciente.getNumero_exterior())
                .numero_interior(paciente.getNumero_interior())
                .colonia(paciente.getColonia())
                .cp(paciente.getCp())
                .municipio(paciente.getMunicipio())
                .entidad_federativa(paciente.getEntidad_federativa())
                .nombre_responsable(paciente.getNombre_responsable())
                .parentesco_responsable(paciente.getParentesco_responsable())
                .telefono_responsable(paciente.getTelefono_responsable())
                .calle_responsable(paciente.getCalle_responsable())
                .numero_exterior_responsable(paciente.getNumero_exterior_responsable())
                .numero_interior_responsable(paciente.getNumero_interior_responsable())
                .colonia_responsable(paciente.getColonia_responsable())
                .cp_responsable(paciente.getCp_responsable())
                .municipio_responsable(paciente.getMunicipio_responsable())
                .entidad_federativa_responsable(paciente.getEntidad_federativa_responsable())
                .numero_personal(paciente.getNumero_personal())
                .puesto(paciente.getPuesto())
                .tipo_contratacion(paciente.getTipo_contratacion())
                // .habla_lengua_indigena(paciente.isHabla_lengua_indigena())
                .lengua_indigena(paciente.getLengua_indigena())
                .registros(registros)
                .build();
        return idPacienteDto;
    }

    public String desactivarPaciente(int id) {
        try {
            Paciente paciente = pacienteRepository.findById(id)
                    .orElseThrow(() -> new RuntimeException("Paciente no encontrado"));
            if (!paciente.isActivo()) {
                return "Paciente ya está desactivado";
            }
            paciente.setActivo(false);
            pacienteRepository.save(paciente);
            return "Se eliminó el paciente";
        } catch (Exception e) {
            throw new RuntimeException("No se pudo eliminar el paciente");
        }
    }

    public SatisticsPacienteDto obtenerEstadisticas() {
        List<Paciente> pacientes = pacienteRepository.findByActivoTrue();
        Iterable<NotaEvolucion> notas = notaEvolucionRepository.findAll();
        Iterable<HistoriaClinica> historiasClinicas = historiaClinicaRepository.findAll();

        long totalPacientes = pacienteRepository.countByActivoTrue();
        long totalNotas = notaEvolucionRepository.count() + historiaClinicaRepository.count();
        Map<String, Integer> porSexo = new HashMap<>();
        Map<String, Integer> porFacultad = new HashMap<>();
        Map<String, Integer> porProgramaEducativo = new HashMap<>();
        Map<Integer, Integer> porSemestre = new HashMap<>();
        Map<String, Integer> porTipoPaciente = new HashMap<>();
        Map<String, Integer> porEstadoCivil = new HashMap<>();
        Map<String, Integer> porLenguaIndigena = new HashMap<>();
        Map<String, Integer> casosPorDiaMes = new HashMap<>();
        Map<String, Integer> casosPorAnio = new HashMap<>();
        Map<String, Integer> topSintomas = new HashMap<>();

        // Estadisticas por sexo
        for (Paciente paciente : pacientes) {
            porSexo.put(paciente.getSexo().name(), porSexo.getOrDefault(paciente.getSexo().name(), 0) + 1);
        }

        // Estadisticas por facultad

        for (Paciente paciente : pacientes) {
            String facultad = paciente.getFacultad() != null ? paciente.getFacultad() : "Sin Facultad";
            porFacultad.put(facultad, porFacultad.getOrDefault(facultad, 0) + 1);
        }

        // Estadisticas por programa educativo

        for (Paciente paciente : pacientes) {
            String programa = paciente.getPrograma_educativo() != null ? paciente.getPrograma_educativo()
                    : "Sin Programa";
            porProgramaEducativo.put(programa,
                    porProgramaEducativo.getOrDefault(programa, 0) + 1);
        }

        // Estadisticas por semestre
        for (Paciente paciente : pacientes) {
            porSemestre.put(paciente.getSemestre(), porSemestre.getOrDefault(paciente.getSemestre(), 0) + 1);
        }

        // Estadisticas por tipo de paciente
        for (Paciente paciente : pacientes) {
            String tipo = paciente.getTipo_paciente() != null ? paciente.getTipo_paciente() : "Sin Tipo";
            porTipoPaciente.put(tipo,
                    porTipoPaciente.getOrDefault(tipo, 0) + 1);
        }

        // Estadisticas por estado civil
        for (Paciente paciente : pacientes) {
            String estado = paciente.getEstado_civil() != null ? paciente.getEstado_civil() : "Sin Estado Civil";
            porEstadoCivil.put(estado,
                    porEstadoCivil.getOrDefault(estado, 0) + 1);
        }

        // Estadisticas por lengua indigena
        for (Paciente paciente : pacientes) {
            String lengua = paciente.getLengua_indigena() != null ? paciente.getLengua_indigena() : "Ninguna";
            porLenguaIndigena.put(lengua,
                    porLenguaIndigena.getOrDefault(lengua, 0) + 1);
        }

        // Estadisticas por casos por dia y mes
        for (Paciente paciente : pacientes) {
            String fecha = paciente.getFecha_creacion().toString();
            String dia = fecha.substring(8, 10);
            String mes = fecha.substring(5, 7);
            casosPorDiaMes.put(dia + "-" + mes, casosPorDiaMes.getOrDefault(dia + "-" + mes, 0) + 1);
        }

        // Estadisticas por casos por anio
        for (Paciente paciente : pacientes) {
            String fecha = paciente.getFecha_creacion().toString();
            String anio = fecha.substring(0, 4);
            casosPorAnio.put(anio, casosPorAnio.getOrDefault(anio, 0) + 1);
        }

        // Estadisticas por top sintomas
        for (NotaEvolucion nota : notas) {
            String sintomas = nota.getDiagnostico() != null ? nota.getDiagnostico() : "Sin Diagnóstico";
            topSintomas.put(sintomas, topSintomas.getOrDefault(sintomas, 0) + 1);
        }

        for (HistoriaClinica historia : historiasClinicas) {
            String sintomas = historia.getDiagnostico() != null ? historia.getDiagnostico() : "Sin Diagnóstico";
            topSintomas.put(sintomas, topSintomas.getOrDefault(sintomas, 0) + 1);
        }

        return SatisticsPacienteDto.builder()
                .totalPacientes(totalPacientes)
                .totalNotas(totalNotas)
                .porSexo(porSexo)
                .porFacultad(porFacultad)
                .porProgramaEducativo(porProgramaEducativo)
                .porSemestre(porSemestre)
                .porTipoPaciente(porTipoPaciente)
                .porEstadoCivil(porEstadoCivil)
                .porLenguaIndigena(porLenguaIndigena)
                .casosPorDiaMes(casosPorDiaMes)
                .casosPorAnio(casosPorAnio)
                .topSintomas(topSintomas)
                .build();
    }

    public SatisticsPacienteDto obtenerEstadisticasRango(Date startDate, Date endDate) {

        List<Paciente> pacientes = pacienteRepository.findByActivoTrue()
                .stream()
                .filter(p -> p.getFecha_creacion() != null
                        && !p.getFecha_creacion().before(startDate)
                        && !p.getFecha_creacion().after(endDate))
                .toList();

        List<NotaEvolucion> notas = notaEvolucionRepository.findAll()
                .stream()
                .filter(n -> n.getFecha_creacion() != null
                        && !n.getFecha_creacion().before(startDate)
                        && !n.getFecha_creacion().after(endDate))
                .toList();

        List<HistoriaClinica> historiasClinicas = historiaClinicaRepository.findAll()
                .stream()
                .filter(h -> h.getFecha_creacion() != null
                        && !h.getFecha_creacion().before(startDate)
                        && !h.getFecha_creacion().after(endDate))
                .toList();

        long totalPacientes = pacientes.size();
        long totalNotas = notas.size() + historiasClinicas.size();

        Map<String, Integer> porSexo = new HashMap<>();
        Map<String, Integer> porFacultad = new HashMap<>();
        Map<String, Integer> porProgramaEducativo = new HashMap<>();
        Map<Integer, Integer> porSemestre = new HashMap<>();
        Map<String, Integer> porTipoPaciente = new HashMap<>();
        Map<String, Integer> porEstadoCivil = new HashMap<>();
        Map<String, Integer> porLenguaIndigena = new HashMap<>();
        Map<String, Integer> casosPorDiaMes = new HashMap<>();
        Map<String, Integer> casosPorAnio = new HashMap<>();
        Map<String, Integer> topSintomas = new HashMap<>();

        for (Paciente paciente : pacientes) {

            porSexo.merge(paciente.getSexo().name(), 1, Integer::sum);

            porFacultad.merge(
                    paciente.getFacultad() != null ? paciente.getFacultad() : "Sin Facultad",
                    1,
                    Integer::sum);

            porProgramaEducativo.merge(
                    paciente.getPrograma_educativo() != null ? paciente.getPrograma_educativo() : "Sin Programa",
                    1,
                    Integer::sum);

            porSemestre.merge(paciente.getSemestre(), 1, Integer::sum);

            porTipoPaciente.merge(
                    paciente.getTipo_paciente() != null ? paciente.getTipo_paciente() : "Sin Tipo",
                    1,
                    Integer::sum);

            porEstadoCivil.merge(
                    paciente.getEstado_civil() != null ? paciente.getEstado_civil() : "Sin Estado Civil",
                    1,
                    Integer::sum);

            porLenguaIndigena.merge(
                    paciente.getLengua_indigena() != null ? paciente.getLengua_indigena() : "Ninguna",
                    1,
                    Integer::sum);

            LocalDate fecha = paciente.getFecha_creacion()
                    .toInstant()
                    .atZone(ZoneId.systemDefault())
                    .toLocalDate();

            casosPorDiaMes.merge(
                    fecha.getDayOfMonth() + "-" + fecha.getMonthValue(),
                    1,
                    Integer::sum);

            casosPorAnio.merge(
                    String.valueOf(fecha.getYear()),
                    1,
                    Integer::sum);
        }

        for (NotaEvolucion nota : notas) {
            String diagnostico = nota.getDiagnostico() != null ? nota.getDiagnostico() : "Sin Diagnóstico";
            topSintomas.merge(diagnostico, 1, Integer::sum);
        }

        for (HistoriaClinica historia : historiasClinicas) {
            String diagnostico = historia.getDiagnostico() != null ? historia.getDiagnostico() : "Sin Diagnóstico";
            topSintomas.merge(diagnostico, 1, Integer::sum);
        }

        return SatisticsPacienteDto.builder()
                .totalPacientes(totalPacientes)
                .totalNotas(totalNotas)
                .porSexo(porSexo)
                .porFacultad(porFacultad)
                .porProgramaEducativo(porProgramaEducativo)
                .porSemestre(porSemestre)
                .porTipoPaciente(porTipoPaciente)
                .porEstadoCivil(porEstadoCivil)
                .porLenguaIndigena(porLenguaIndigena)
                .casosPorDiaMes(casosPorDiaMes)
                .casosPorAnio(casosPorAnio)
                .topSintomas(topSintomas)
                .build();
    }

}