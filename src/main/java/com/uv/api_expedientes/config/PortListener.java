package com.uv.api_expedientes.config;

import org.springframework.boot.web.servlet.context.ServletWebServerInitializedEvent;
import org.springframework.context.ApplicationListener;
import org.springframework.stereotype.Component;

import org.springframework.lang.NonNull;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Profile;

import java.io.File;
import java.io.IOException;

@Component
@Profile("desktop")
public class PortListener implements ApplicationListener<ServletWebServerInitializedEvent> {

    @Value("${app.config.path}")
    private String configPath;

    // Este evento se dispara cuando el servidor web (Tomcat) ha sido inicializado
    // completamente
    @Override
    public void onApplicationEvent(@NonNull ServletWebServerInitializedEvent event) {
        // 1. Obtiene el puerto real en el que el servidor terminó escuchando
        int port = event.getWebServer().getPort();

        // 2. Actualiza el archivo de configuración JSON con el nuevo puerto y URL base
        try {
            ObjectMapper mapper = new ObjectMapper();
            File configFile = new File(configPath);

            if (configFile.exists()) {
                ObjectNode rootNode = (ObjectNode) mapper.readTree(configFile);

                // Actualizar ApiPort
                rootNode.put("ApiPort", port);

                // Actualizar apiBaseUrl (asumiendo localhost y http)
                rootNode.put("apiBaseUrl", "http://localhost:" + port);

                // Escribir los cambios de vuelta al archivo
                mapper.writerWithDefaultPrettyPrinter().writeValue(configFile, rootNode);

                System.out.println("Configuración actualizada en " + configPath + ": Puerto=" + port);
            } else {
                System.err.println("No se encontró el archivo de configuración en: " + configPath);
            }

        } catch (IOException e) {
            System.err.println("Error al actualizar la configuración del puerto: " + e.getMessage());
            e.printStackTrace();
        }
    }
}
