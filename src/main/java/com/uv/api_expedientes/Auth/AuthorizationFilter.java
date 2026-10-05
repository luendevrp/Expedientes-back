package com.uv.api_expedientes.Auth;

import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.springframework.http.HttpStatus;
import org.springframework.lang.NonNull;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.uv.api_expedientes.jwt.JwtService;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;

//Encargado de validar los permisos del usuario para cada solicitud
@Component
@RequiredArgsConstructor
public class AuthorizationFilter extends OncePerRequestFilter {

    private final JwtService jwtService;

    @Override
    protected void doFilterInternal(@NonNull HttpServletRequest request, @NonNull HttpServletResponse response,
            @NonNull FilterChain filterChain)
            throws ServletException, IOException {

        // Excluir el endpoint de refresh del filtro de permisos
        String path = request.getRequestURI();
        if (path.startsWith("/auth/refresh")) {
            filterChain.doFilter(request, response);
            return;
        }

        // Obtener el token del encabezado Authorization
        String token = request.getHeader("Authorization");
        if (token == null || !token.startsWith("Bearer ")) {
            filterChain.doFilter(request, response);
            return;
        }

        token = token.substring(7); // Eliminar "Bearer "

        // Obtener usuario a partir del token
        String username = jwtService.getUsernameFromToken(token);
        if (username == null) {
            filterChain.doFilter(request, response);
            return;
        }

        // Obtener recurso y acción a partir del request
        String recurso = obtenerRecursoDesdeUrl(request.getRequestURI());
        String accion = obtenerAccionDesdeUrl(request.getRequestURI());

        // Validar si recurso y acción son válidos
        if (recurso == null || accion == null) {
            response.setContentType("application/json");
            response.setStatus(HttpStatus.FORBIDDEN.value());
            response.getWriter().write(
                    new ObjectMapper().writeValueAsString(Map.of("error", "Acceso denegado")));
            return;
        }

        // Validar permisos: Verificar si el usuario tiene permiso para el recurso y
        // acción
        UserDetails userDetails = (UserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        Map<String, List<String>> permisos = userDetails.getAuthorities().stream()
                .map(grantedAuthority -> grantedAuthority.getAuthority().split("_"))
                .filter(parts -> parts.length == 2)
                .collect(Collectors.groupingBy(
                        parts -> parts[0].toLowerCase(),
                        Collectors.mapping(parts -> parts[1].toLowerCase(), Collectors.toList())));

        List<String> accionesPermitidas = permisos.get(recurso.toLowerCase());

        // System.out.println("Recurso solicitado: " + recurso.toLowerCase());
        // System.out.println("Acción solicitada: " + accion.toLowerCase());
        // System.out.println("Permisos del usuario: " + permisos);

        if (accionesPermitidas == null || !accionesPermitidas.contains(accion.toLowerCase())) {
            // System.out.println("Acciones permitidas para " + recurso.toLowerCase() + ": "
            // + accionesPermitidas);

            response.setContentType("application/json");
            response.setStatus(HttpStatus.FORBIDDEN.value());
            response.getWriter().write(
                    new ObjectMapper().writeValueAsString(Map.of(
                            "error", "No tienes permiso para esta acción en el recurso " + recurso,
                            "accion_solicitada", accion,
                            "acciones_permitidas", accionesPermitidas)));
            return;
        }

        filterChain.doFilter(request, response);
    }

    // Método para obtener el recurso desde la URL considerando el prefijo /api
    private String obtenerRecursoDesdeUrl(String url) {
        try {
            String[] partes = new URI(url).getPath().split("/");

            // Verificar que la URL tiene el formato esperado: /api/recurso/accion
            if (partes.length > 2 && "api".equals(partes[1])) {
                return partes[2]; // Retorna el recurso después de /api/
            }
            return null; // Si no se encuentra el recurso, devolver null
        } catch (URISyntaxException e) {
            return null;
        }
    }

    // Método para obtener la acción directamente desde la URL considerando el
    // prefijo /api
    private String obtenerAccionDesdeUrl(String url) {
        try {
            String[] partes = new URI(url).getPath().split("/");

            // Verificar que la URL tiene el formato esperado: /api/recurso/accion
            if (partes.length > 3 && "api".equals(partes[1])) {
                return partes[3]; // Retorna la acción después de /api/recurso/
            }
            return null; // Si no hay acción en la URL, devolver null
        } catch (URISyntaxException e) {
            return null;
        }
    }
}