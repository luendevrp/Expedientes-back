package com.uv.api_expedientes.jwt;

import java.security.Key;
import java.security.SecureRandom;
import java.util.*;
import java.util.function.Function;

import org.springframework.http.HttpHeaders;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import com.uv.api_expedientes.AccessControl.Permisos.Permiso;
import com.uv.api_expedientes.Users.User;

import io.jsonwebtoken.*;

import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import jakarta.servlet.http.HttpServletRequest;

@Service
public class JwtService {

    private static final String SECRET_KEY = generateSecretKeyBase64();

    // ============================
    // TOKENS PRINCIPALES
    // ============================

    public String getToken(UserDetails userDetails) {
        if (userDetails instanceof User) {
            User user = (User) userDetails;

            // Verificar si el usuario está activo
            if (!user.isEnabled()) {
                throw new RuntimeException("El usuario no está activo");
            }

            Map<String, Object> extraClaims = new HashMap<>();
            extraClaims.put("rol", user.getRol().getNombre());

            // Agrupamos permisos por recurso
            Map<String, List<String>> permisosAgrupados = new HashMap<>();
            if (user.getRol().getPermisos() != null) {
                for (Permiso permiso : user.getRol().getPermisos()) {
                    String recurso = permiso.getRecurso().getNombre();
                    String accion = permiso.getAccion().getNombre();

                    permisosAgrupados.computeIfAbsent(recurso, k -> new ArrayList<>()).add(accion);
                }
            }
            extraClaims.put("permisos", permisosAgrupados);

            return generateToken(extraClaims, userDetails);
        } else {
            throw new RuntimeException("El usuario no es válido");
        }
    }

    private String generateToken(Map<String, Object> extraClaims, UserDetails user) {
        return Jwts.builder()
                // Se añaden los permisos y el rol al token unicamente para ajustar el frontend
                .setClaims(extraClaims)
                .setSubject(user.getUsername())
                .setIssuedAt(new Date(System.currentTimeMillis()))
                .setExpiration(new Date(System.currentTimeMillis() + 1000 * 60 * 30)) // 30 minutos
                .signWith(getKey(), SignatureAlgorithm.HS256)
                .compact();
    }

    // ============================
    // REFRESH TOKEN
    // ============================

    public String generateRefreshToken(UserDetails user) {
        return Jwts.builder()
                .setSubject(user.getUsername())
                .setIssuedAt(new Date(System.currentTimeMillis()))
                .setExpiration(new Date(System.currentTimeMillis() + 1000 * 60 * 60 * 24)) // 24 horas
                .signWith(getKey(), SignatureAlgorithm.HS256)
                .compact();
    }

    private Key getKey() {
        byte[] keyBytes = Decoders.BASE64.decode(SECRET_KEY);
        return Keys.hmacShaKeyFor(keyBytes);
    }

    public String getUsernameFromToken(String token) {
        return getClaim(token, Claims::getSubject);
    }

    public String getTokenFromRequest(HttpServletRequest request) {
        final String authHeader = request.getHeader(HttpHeaders.AUTHORIZATION);

        if (StringUtils.hasText(authHeader) && authHeader.startsWith("Bearer ")) {
            return authHeader.substring(7);
        }
        return null;
    }

    public boolean isTokenValid(String token, UserDetails userDetails) {
        final String username = getUsernameFromToken(token);

        if (!(userDetails instanceof User)) {
            return false;
        }

        User user = (User) userDetails;

        // Bloquear acceso si el usuario está inactivo
        if (!user.isEnabled()) {
            return false;
        }

        return (username.equals(userDetails.getUsername()) &&
                !isTokenExpired(token));
    }

    private Claims getAllClaims(String token) {
        try {
            return Jwts.parserBuilder()
                    .setSigningKey(getKey())
                    .build()
                    .parseClaimsJws(token)
                    .getBody();
        } catch (ExpiredJwtException e) {
            throw new RuntimeException("El token ha expirado");
        } catch (SignatureException e) {
            throw new RuntimeException("Firma JWT inválida");
        } catch (JwtException e) {
            throw new RuntimeException("Token JWT inválido o corrupto");
        }
    }

    public <T> T getClaim(String token, Function<Claims, T> claimsResolver) {
        final Claims claims = getAllClaims(token);
        return claimsResolver.apply(claims);
    }

    private Date getExpiration(String token) {
        return getClaim(token, Claims::getExpiration);
    }

    private boolean isTokenExpired(String token) {
        return getExpiration(token).before(new Date());
    }

    private static String generateSecretKeyBase64() {
        byte[] key = new byte[32]; // 256 bits
        new SecureRandom().nextBytes(key);
        return Base64.getEncoder().encodeToString(key);
    }
}
