package com.uv.api_expedientes.Auth;

import java.util.Calendar;
import java.util.Date;
import java.util.UUID;

import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.uv.api_expedientes.AccessControl.Roles.Rol;
import com.uv.api_expedientes.AccessControl.Roles.RolRepository;
import com.uv.api_expedientes.Auth.dtos.AuthResponse;
import com.uv.api_expedientes.Auth.dtos.LoginDto;
import com.uv.api_expedientes.Auth.dtos.RefreshTokenRequest;
import com.uv.api_expedientes.Auth.dtos.RegisterUserDto;
import com.uv.api_expedientes.Users.User;
import com.uv.api_expedientes.Users.UserRepository;
import com.uv.api_expedientes.Users.dtos.MatriculaDto;
import com.uv.api_expedientes.jwt.JwtService;

import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class AuthService {

        private final JwtService jwtService;
        private final AuthenticationManager authenticationManager;
        private final UserRepository userRepository;
        private final RolRepository rolRepository;
        private final PasswordEncoder passwordEncoder;

        public AuthResponse login(LoginDto loginDto) {
                // System.out.println("Login attempt for matricula: " + loginDto.getUsername());
                authenticationManager.authenticate(
                                new UsernamePasswordAuthenticationToken(loginDto.getUsername(),
                                                loginDto.getPassword()));
                // System.out.println("Authentication successful for matricula: " +
                // loginDto.getUsername());
                UserDetails user = userRepository.findByUsername(loginDto.getUsername()).orElseThrow();
                String Nombre = userRepository.findByUsername(loginDto.getUsername()).get().getNombre();
                // System.out.println(user.getUsername());
                String token = jwtService.getToken(user);
                String refreshToken = jwtService.generateRefreshToken(user);
                return AuthResponse.builder()
                                .token(token)
                                .nombre(Nombre)
                                .refreshToken(refreshToken)
                                .build();

        }

        public MatriculaDto register(RegisterUserDto registerUserDto) {

                Rol rol = rolRepository.findById(registerUserDto.getRolId())
                                .orElseThrow(() -> new RuntimeException(
                                                "Rol no encontrado con el ID: " + registerUserDto.getRolId()));

                String matricula = MatriculaGenerator.generarMatricula(registerUserDto.getNombre());

                User user = User.builder()
                                .username(matricula)
                                .nombre(registerUserDto.getNombre())
                                .curp(registerUserDto.getCurp())
                                .rfc(registerUserDto.getRfc())
                                .cedulaProfesional(registerUserDto.getCedulaProfesional())
                                .especialidad(registerUserDto.getEspecialidad())
                                .password(passwordEncoder.encode(registerUserDto.getPassword()))
                                .telefono(registerUserDto.getTelefono())
                                .facultad(registerUserDto.getFacultad())
                                .activo(true)
                                .pasante(registerUserDto.isPasante())
                                .fecha_creacion(new Date())
                                .rol(rol)
                                .build();

                userRepository.save(user);
                return MatriculaDto.builder()
                                .matricula(matricula).build();
        }

        public AuthResponse RefreshToken(HttpServletRequest requestToken, RefreshTokenRequest requestRefresh) {
                String refreshToken = requestRefresh.getRefreshToken();
                String AccessToken = jwtService.getTokenFromRequest(requestToken);

                String username = jwtService.getUsernameFromToken(AccessToken);
                String Nombre = userRepository.findByUsername(username).get().getNombre();
                var user = userRepository.findByUsername(username)
                                .orElseThrow(() -> new RuntimeException("Usuario no encontrado"));

                if (jwtService.isTokenValid(AccessToken, user)) {
                        if (jwtService.isTokenValid(refreshToken, user)) {
                                String newAccessToken = jwtService.getToken(user);
                                return AuthResponse.builder()
                                                .token(newAccessToken)
                                                .nombre(Nombre)
                                                .refreshToken(refreshToken)
                                                .build();
                        } else {
                                throw new RuntimeException("Refresh token inválido o expirado");
                        }
                } else {
                        throw new RuntimeException("Access token inválido o expirado");
                }

        }

        public class MatriculaGenerator {

                private static final String PREFIJO = "UV";

                public static String generarMatricula(String username) {

                        int year = Calendar.getInstance().get(Calendar.YEAR);

                        String letras = username.length() >= 3
                                        ? username.substring(0, 3).toUpperCase()
                                        : String.format("%-3s", username).replace(' ', 'X').toUpperCase();

                        String hash = UUID.randomUUID().toString().replace("-", "").substring(0, 4).toUpperCase();

                        return String.format("%s%d%s%s", PREFIJO, year, letras, hash);
                }
        }
}
