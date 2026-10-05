package com.uv.api_expedientes.Notas.HistoriaClinica;

import java.util.HashMap;
import java.util.Map;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.uv.api_expedientes.Notas.HistoriaClinica.dtos.HistoriaClinicaDto;

import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/registros")
@RequiredArgsConstructor
public class HistoriaClinicaController {

    private final HistoriaClinicaService historiaClinicaService;

    @PostMapping("/Crear/Historia/{idPaciente}")
    public ResponseEntity<Map<String, String>> guardarHistoria(HttpServletRequest request,
            @PathVariable Integer idPaciente, @RequestBody HistoriaClinicaDto historiaClinicaDto) {
        Map<String, String> response = new HashMap<>();
        response.put("mensaje", historiaClinicaService.guardarHistoria(request, idPaciente, historiaClinicaDto));
        return ResponseEntity.ok(response);
    }

    @GetMapping("Ver/Historia/{id}")
    public ResponseEntity<HistoriaClinicaDto> obtenerHistoria(@PathVariable Integer id) {
        return ResponseEntity.ok(historiaClinicaService.obtenerPorId(id));
    }

    @GetMapping("Ver/Historia/{id}/Pdf")
    public ResponseEntity<byte[]> descargaHistoriaPdf(@PathVariable Integer id) {
        byte[] pdfContent = historiaClinicaService.generarPdf(id);

        org.springframework.http.HttpHeaders headers = new org.springframework.http.HttpHeaders();
        headers.setContentType(org.springframework.http.MediaType.APPLICATION_PDF);
        java.text.SimpleDateFormat sdf = new java.text.SimpleDateFormat("yyyy-MM-dd");
        String filename = "Historia_Clinica_" + sdf.format(new java.util.Date()) + ".pdf";

        headers.add(org.springframework.http.HttpHeaders.CONTENT_DISPOSITION,
                "attachment; filename=\"" + filename + "\"");
        headers.add("Access-Control-Expose-Headers", "Content-Disposition");
        headers.setCacheControl("must-revalidate, post-check=0, pre-check=0");

        return ResponseEntity.ok()
                .headers(headers)
                .body(pdfContent);
    }
}