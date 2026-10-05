package com.uv.api_expedientes.Auth;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import static org.junit.jupiter.api.Assertions.*;

import java.util.Date;
import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.*;
import org.mockito.junit.jupiter.MockitoExtension;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;

import com.uv.api_expedientes.AccessControl.Roles.Rol;
import com.uv.api_expedientes.AccessControl.Roles.RolRepository;
import com.uv.api_expedientes.Auth.dtos.AuthResponse;
import com.uv.api_expedientes.Auth.dtos.LoginDto;
import com.uv.api_expedientes.Auth.dtos.RefreshTokenRequest;
import com.uv.api_expedientes.Auth.dtos.RegisterUserDto;
import com.uv.api_expedientes.Users.UserRepository;
import com.uv.api_expedientes.Users.dtos.MatriculaDto;
import com.uv.api_expedientes.jwt.JwtService;

import jakarta.servlet.http.HttpServletRequest;

@ExtendWith(MockitoExtension.class)
public class AuthServiceTest {

    @Mock
    private JwtService jwtService;
    @Mock
    private AuthenticationManager authenticationManager;
    @Mock
    private UserRepository userRepository;
    @Mock
    private RolRepository rolRepository;
    @Mock
    private PasswordEncoder passwordEncoder;

    @InjectMocks
    private AuthService authService;

    private com.uv.api_expedientes.Users.User userEntity;

    @BeforeEach
    void setup() {

        Rol rolMock = Rol.builder()
                .id(1)
                .nombre("Admin")
                .descripcion("Rol administrador")
                .activo(true)
                .fecha_creacion(new Date())
                .permisos(List.of())
                .build();

        userEntity = com.uv.api_expedientes.Users.User.builder()
                .id(1)
                .username("luis123")
                .nombre("Luis")
                .password("123")
                .activo(true)
                .rol(rolMock)
                .fecha_creacion(new Date())
                .build();
    }

    // Verifica que el login retorne tokens correctamente
    @Test
    void testLogin_ReturnsTokensSuccessfully() {

        LoginDto loginDto = new LoginDto();
        loginDto.setUsername("luis123");
        loginDto.setPassword("123");

        when(authenticationManager.authenticate(any(UsernamePasswordAuthenticationToken.class)))
                .thenReturn(new UsernamePasswordAuthenticationToken("luis123", "123"));

        when(userRepository.findByUsername("luis123")).thenReturn(Optional.of(userEntity));

        when(jwtService.getToken(any(UserDetails.class))).thenReturn("ACCESS123");
        when(jwtService.generateRefreshToken(any(UserDetails.class))).thenReturn("REFRESH123");

        AuthResponse response = authService.login(loginDto);

        assertEquals("ACCESS123", response.getToken());
        assertEquals("REFRESH123", response.getRefreshToken());
        assertEquals("Luis", response.getNombre());

        System.out.println("[TEST OK] testLogin_ReturnsTokensSuccessfully");
    }

    // Verifica que el registro guarde el usuario correctamente
    @Test
    void testRegister_SavesUserSuccessfully() {

        RegisterUserDto dto = new RegisterUserDto();
        dto.setNombre("Luis");
        dto.setCurp("CURP123");
        dto.setRfc("RFC123");
        dto.setEspecialidad("Medicina");
        dto.setTelefono("12345");
        dto.setPassword("123");
        dto.setFacultad("UV");
        dto.setRolId(1);

        Rol rol = Rol.builder()
                .id(1)
                .nombre("Admin")
                .descripcion("Rol administrador")
                .activo(true)
                .fecha_creacion(new Date())
                .permisos(List.of())
                .build();

        when(rolRepository.findById(1)).thenReturn(Optional.of(rol));
        when(passwordEncoder.encode("123")).thenReturn("ENC123");

        MatriculaDto result = authService.register(dto);

        verify(userRepository, times(1)).save(any(com.uv.api_expedientes.Users.User.class));
        assertNotNull(result.getMatricula());

        System.out.println("[TEST OK] testRegister_SavesUserSuccessfully");
    }

    // Verifica que el refresh token funcione correctamente
    @Test
    void testRefreshToken_Success() {

        HttpServletRequest request = mock(HttpServletRequest.class);
        RefreshTokenRequest refresh = new RefreshTokenRequest("REFRESH123");

        when(jwtService.getTokenFromRequest(request)).thenReturn("ACCESS123");
        when(jwtService.getUsernameFromToken("ACCESS123")).thenReturn("luis123");
        when(userRepository.findByUsername("luis123")).thenReturn(Optional.of(userEntity));

        when(jwtService.isTokenValid("ACCESS123", userEntity)).thenReturn(true);
        when(jwtService.isTokenValid("REFRESH123", userEntity)).thenReturn(true);
        when(jwtService.getToken(userEntity)).thenReturn("NEW_ACCESS");

        AuthResponse response = authService.RefreshToken(request, refresh);

        assertEquals("NEW_ACCESS", response.getToken());
        assertEquals("REFRESH123", response.getRefreshToken());
        assertEquals("Luis", response.getNombre());

        System.out.println("[TEST OK] testRefreshToken_Success");
    }

    // Verifica que se lance excepción si el refresh token es inválido
    @Test
    void testRefreshToken_RefreshInvalid_ThrowsException() {

        HttpServletRequest request = mock(HttpServletRequest.class);
        RefreshTokenRequest refresh = new RefreshTokenRequest("REF_INVALID");

        when(jwtService.getTokenFromRequest(request)).thenReturn("ACCESS123");
        when(jwtService.getUsernameFromToken("ACCESS123")).thenReturn("luis123");
        when(userRepository.findByUsername("luis123")).thenReturn(Optional.of(userEntity));

        when(jwtService.isTokenValid("ACCESS123", userEntity)).thenReturn(true);
        when(jwtService.isTokenValid("REF_INVALID", userEntity)).thenReturn(false);

        RuntimeException ex = assertThrows(RuntimeException.class,
                () -> authService.RefreshToken(request, refresh));

        assertEquals("Refresh token inválido o expirado", ex.getMessage());

        System.out.println("[TEST OK] testRefreshToken_RefreshInvalid_ThrowsException");
    }
}
