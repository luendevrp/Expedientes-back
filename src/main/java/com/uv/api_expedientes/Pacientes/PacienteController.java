package com.uv.api_expedientes.Pacientes;

import java.util.Date;

import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.uv.api_expedientes.Pacientes.dtos.AllPacientesDto;
import com.uv.api_expedientes.Pacientes.dtos.IdPacienteDto;
import com.uv.api_expedientes.Pacientes.dtos.PacienteEditDto;
import com.uv.api_expedientes.Services.PdfService;
import com.uv.api_expedientes.Pacientes.dtos.SatisticsPacienteDto;

import java.io.IOException;
import java.text.SimpleDateFormat;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;

import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/pacientes")
@RequiredArgsConstructor
public class PacienteController {

    private final PacienteService pacienteService;
    private final PdfService pdfService;

    @GetMapping("/Ver")
    public AllPacientesDto obtenerPacientes() {
        return pacienteService.obtenerPacientes();
    }

    @PostMapping("/Crear")
    public ResponseEntity<Void> guardarPaciente(@RequestBody Paciente paciente) {
        return ResponseEntity.ok(pacienteService.guardarPaciente(paciente));
    }

    @GetMapping("/Ver/{id}")
    public ResponseEntity<IdPacienteDto> obtenerPorId(@PathVariable("id") int id) {
        return ResponseEntity.ok(pacienteService.obtenerPorId(id));
    }

    @PutMapping("/Editar/{id}")
    public ResponseEntity<Void> actualizarPaciente(@PathVariable("id") int id,
            @RequestBody PacienteEditDto pacienteEditDto) {
        return ResponseEntity.ok(pacienteService.actualizarPaciente(id, pacienteEditDto));
    }

    @DeleteMapping("/Eliminar/{id}")
    public ResponseEntity<String> desactivarPaciente(@PathVariable("id") int id) {
        return ResponseEntity.ok(pacienteService.desactivarPaciente(id));
    }

    @GetMapping("/Estadisticas")
    public ResponseEntity<SatisticsPacienteDto> obtenerEstadisticas() {
        return ResponseEntity.ok(pacienteService.obtenerEstadisticas());
    }

    @GetMapping("/Estadisticas/Rango")
    public ResponseEntity<SatisticsPacienteDto> obtenerEstadisticasRango(
            @RequestParam @DateTimeFormat(pattern = "yyyy-MM-dd'T'HH:mm:ss") Date startDate,

            @RequestParam @DateTimeFormat(pattern = "yyyy-MM-dd'T'HH:mm:ss") Date endDate) {
        return ResponseEntity.ok(
                pacienteService.obtenerEstadisticasRango(startDate, endDate));
    }

    @GetMapping("/Estadisticas/Pdf")
    public ResponseEntity<byte[]> obtenerEstadisticasPdf(
            @RequestParam(required = false) @DateTimeFormat(pattern = "yyyy-MM-dd'T'HH:mm:ss") Date startDate,
            @RequestParam(required = false) @DateTimeFormat(pattern = "yyyy-MM-dd'T'HH:mm:ss") Date endDate)
            throws IOException {

        SatisticsPacienteDto stats;
        if (startDate != null && endDate != null) {
            stats = pacienteService.obtenerEstadisticasRango(startDate, endDate);
        } else {
            stats = pacienteService.obtenerEstadisticas();
        }

        byte[] pdfContent = pdfService.generarEstadisticasPdf(stats, startDate, endDate);

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_PDF);
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd_HH-mm");
        String filename = "Estadisticas_" + sdf.format(new Date()) + ".pdf";

        headers.add(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" + filename + "\"");
        headers.add("Access-Control-Expose-Headers", "Content-Disposition"); // Allow frontend to read this header
        headers.setCacheControl("must-revalidate, post-check=0, pre-check=0");

        return ResponseEntity.ok()
                .headers(headers)
                .body(pdfContent);
    }
}