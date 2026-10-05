package com.uv.api_expedientes;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import java.io.File;

@SpringBootApplication
public class ApiExpedientesApplication {

	public static void main(String[] args) {
		// Verificar si el perfil 'desktop' está activo en los argumentos
		boolean isDesktop = false;
		for (String arg : args) {
			if (arg.contains("spring.profiles.active=desktop")) {
				isDesktop = true;
				break;
			}
		}

		if (isDesktop) {
			// 1. Busca un puerto disponible iniciando en 8080
			// Si el 8080 está ocupado, probará 8081, 8082, etc.
			int port = findAvailablePort(8080);

			// 2. Configura la propiedad 'server.port' de Spring Boot con el puerto
			// encontrado
			// Esto le dice a Spring en qué puerto debe iniciar el servidor Tomcat
			System.setProperty("server.port", String.valueOf(port));
		}

		// 3. Inicia la aplicación Spring Boot normalmente
		SpringApplication.run(ApiExpedientesApplication.class, args);
	}

	// Método auxiliar para encontrar un puerto libre
	private static int findAvailablePort(int startPort) {
		int port = startPort;
		while (true) {
			// Intenta abrir un ServerSocket en el puerto 'port'
			// Si tiene éxito (try), significa que el puerto está libre, así que lo
			// retornamos y cerramos el socket.
			try (java.net.ServerSocket socket = new java.net.ServerSocket(port)) {
				return port;
			} catch (java.io.IOException e) {
				// Si falla (catch), significa que el puerto está ocupado ('Address already in
				// use')
				// Incrementamos el puerto y probamos de nuevo en el siguiente ciclo del while
				port++;
			}
		}
	}
}
