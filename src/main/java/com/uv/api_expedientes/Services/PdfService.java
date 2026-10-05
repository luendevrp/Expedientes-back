package com.uv.api_expedientes.Services;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;

import org.springframework.stereotype.Service;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.Context;

import com.openhtmltopdf.pdfboxout.PdfRendererBuilder;
import com.uv.api_expedientes.Pacientes.dtos.SatisticsPacienteDto;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class PdfService {

    private final TemplateEngine templateEngine;

    public byte[] generarEstadisticasPdf(SatisticsPacienteDto stats, Date startDate, Date endDate) throws IOException {
        Context context = new Context();
        context.setVariable("stats", stats);

        String dateRangeText;
        if (startDate != null && endDate != null) {
            SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
            dateRangeText = "Rango: " + sdf.format(startDate) + " - " + sdf.format(endDate);
        } else {
            dateRangeText = "Reporte Histórico Completo";
        }
        context.setVariable("dateRange", dateRangeText);

        SimpleDateFormat sdfGen = new SimpleDateFormat("dd/MM/yyyy HH:mm");
        context.setVariable("generationDate", sdfGen.format(new Date()));

        String html = templateEngine.process("reporte-pacientes/pdf_stats", context);

        try (ByteArrayOutputStream os = new ByteArrayOutputStream()) {
            PdfRendererBuilder builder = new PdfRendererBuilder();
            builder.useFastMode();
            builder.withHtmlContent(html, getClass().getResource("/templates/reporte-pacientes/").toString());
            builder.toStream(os);
            builder.run();
            return os.toByteArray();
        } catch (Exception e) {
            throw new IOException("Error creating PDF", e);
        }
    }

    public byte[] generarNotaMedicaPdf(com.uv.api_expedientes.Notas.NotaEvolucion.NotaEvolucion nota)
            throws IOException {
        Context context = new Context();
        context.setVariable("nota", nota);
        context.setVariable("paciente", nota.getPaciente());
        context.setVariable("medico", nota.getUsuario());

        SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy HH:mm");
        context.setVariable("fechaHora",
                sdf.format(nota.getFecha_creacion() != null ? nota.getFecha_creacion() : new Date()));

        String html = templateEngine.process("nota-medica/nota-medica", context);

        try (ByteArrayOutputStream os = new ByteArrayOutputStream()) {
            PdfRendererBuilder builder = new PdfRendererBuilder();
            builder.useFastMode();
            // Important: Base URI must point to where the CSS/Images are for this template
            builder.withHtmlContent(html, getClass().getResource("/templates/nota-medica/").toString());
            builder.toStream(os);
            builder.run();
            return os.toByteArray();
        } catch (Exception e) {
            throw new IOException("Error creating PDF", e);
        }
    }

    public byte[] generarHistoriaClinicaPdf(com.uv.api_expedientes.Notas.HistoriaClinica.HistoriaClinica historia)
            throws IOException {
        Context context = new Context();
        context.setVariable("nota", historia);

        context.setVariable("historia", historia);
        context.setVariable("paciente", historia.getPaciente());
        context.setVariable("medico", historia.getUsuario());

        SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy HH:mm");
        context.setVariable("fechaHora",
                sdf.format(historia.getFecha_creacion() != null ? historia.getFecha_creacion() : new Date()));

        String html = templateEngine.process("historia-clinica/historia-clinica", context);

        try (ByteArrayOutputStream os = new ByteArrayOutputStream()) {
            PdfRendererBuilder builder = new PdfRendererBuilder();
            builder.useFastMode();
            builder.withHtmlContent(html, getClass().getResource("/templates/historia-clinica/").toString());
            builder.toStream(os);
            builder.run();
            return os.toByteArray();
        } catch (Exception e) {
            throw new IOException("Error creating PDF", e);
        }
    }
}
