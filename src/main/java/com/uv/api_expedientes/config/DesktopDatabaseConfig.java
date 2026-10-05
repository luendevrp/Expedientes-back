package com.uv.api_expedientes.config;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.jdbc.DataSourceBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;

import javax.sql.DataSource;
import java.io.File;
import java.io.IOException;
import java.util.Map;

//Configurar backend para iniciar con el archivo config.json obteniendo la ruta al momento de ejecutar el jar unicamente en modo desktop
@Configuration
@Profile("desktop")
public class DesktopDatabaseConfig {

    @Value("${app.config.path}")
    private String configPath;

    @Value("${spring.datasource.driver-class-name}")
    private String driverClassName;

    @Bean
    public DataSource dataSource() throws IOException {
        ObjectMapper mapper = new ObjectMapper();
        File configFile = new File(configPath);

        // Reading the config into a Map
        Map<String, Object> config = mapper.readValue(configFile, Map.class);

        String dbHost = (String) config.get("DBhost");
        Integer dbPort = (Integer) config.get("DBport");
        String dbName = (String) config.get("DBname");
        String dbUser = (String) config.get("DBuser");
        String dbPassword = (String) config.get("DBpassword");

        String url = String.format("jdbc:postgresql://%s:%d/%s", dbHost, dbPort, dbName);

        return DataSourceBuilder.create()
                .driverClassName(driverClassName)
                .url(url)
                .username(dbUser)
                .password(dbPassword)
                .build();
    }
}
