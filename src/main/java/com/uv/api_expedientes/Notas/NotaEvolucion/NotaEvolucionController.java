package com.uv.api_expedientes.Notas.NotaEvolucion;

import java.util.HashMap;
import java.util.Map;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.uv.api_expedientes.Notas.NotaEvolucion.dtos.NotaEvolucionDto;

import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/registros")
@RequiredArgsConstructor
public class NotaEvolucionController {

    private final NotaEvolucionService notaEvolucionService;

    @PostMapping("/Crear/Nota/{idPaciente}")
    public ResponseEntity<Map<String, String>> crearHistoria(HttpServletRequest request,
            @PathVariable Integer idPaciente,
            @RequestBody NotaEvolucionDto notaEvolucionDto) {
        Map<String, String> response = new HashMap<>();
        response.put("mensaje", notaEvolucionService.guardarNota(request, idPaciente, notaEvolucionDto));
        return ResponseEntity.ok(response);
    }

    @GetMapping("Ver/Nota/{id}")
    public ResponseEntity<NotaEvolucionDto> obtenerNota(@PathVariable Integer id) {
        return ResponseEntity.ok(notaEvolucionService.obtenerPorId(id));
    }

    @GetMapping("Ver/Nota/{id}/Pdf")
    public ResponseEntity<byte[]> descargarNotaPdf(@PathVariable Integer id) {
        byte[] pdfContent = notaEvolucionService.generarPdf(id);

        org.springframework.http.HttpHeaders headers = new org.springframework.http.HttpHeaders();
        headers.setContentType(org.springframework.http.MediaType.APPLICATION_PDF);
        java.text.SimpleDateFormat sdf = new java.text.SimpleDateFormat("yyyy-MM-dd_HH-mm");
        String filename = "Nota_Medica_" + sdf.format(new java.util.Date()) + ".pdf";

        headers.add(org.springframework.http.HttpHeaders.CONTENT_DISPOSITION,
                "attachment; filename=\"" + filename + "\"");
        headers.add("Access-Control-Expose-Headers", "Content-Disposition");
        headers.setCacheControl("must-revalidate, post-check=0, pre-check=0");

        return ResponseEntity.ok()
                .headers(headers)
                .body(pdfContent);
    }
}