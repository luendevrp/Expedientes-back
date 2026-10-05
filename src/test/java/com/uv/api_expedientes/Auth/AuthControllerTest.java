package com.uv.api_expedientes.Auth;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.uv.api_expedientes.Auth.dtos.AuthResponse;
import com.uv.api_expedientes.Auth.dtos.LoginDto;
import com.uv.api_expedientes.Auth.dtos.RefreshTokenRequest;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import jakarta.servlet.http.HttpServletRequest;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc(addFilters = false)
public class AuthControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    private AuthService authService;

    // Prueba del login exitoso
    @Test
    void testLogin_Returns200AndTokens() throws Exception {

        LoginDto loginDto = new LoginDto();
        loginDto.setUsername("luis123");
        loginDto.setPassword("123");

        AuthResponse fakeResponse = AuthResponse.builder()
                .token("ACCESS123")
                .refreshToken("REFRESH123")
                .nombre("Luis")
                .build();

        Mockito.when(authService.login(Mockito.any(LoginDto.class)))
                .thenReturn(fakeResponse);

        mockMvc.perform(
                post("/auth/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(loginDto)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.token").value("ACCESS123"))
                .andExpect(jsonPath("$.refreshToken").value("REFRESH123"))
                .andExpect(jsonPath("$.nombre").value("Luis"));
    }

    // Prueba del refresh token exitoso
    @Test
    void testRefreshToken_ReturnsNewAccessToken() throws Exception {

        RefreshTokenRequest refreshReq = new RefreshTokenRequest("REFRESH123");

        AuthResponse response = AuthResponse.builder()
                .token("NEW_ACCESS")
                .refreshToken("REFRESH123")
                .nombre("Luis")
                .build();

        Mockito.when(authService.RefreshToken(
                Mockito.any(HttpServletRequest.class),
                Mockito.any(RefreshTokenRequest.class)))
                .thenReturn(response);

        mockMvc.perform(
                post("/auth/refresh")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(refreshReq)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.token").value("NEW_ACCESS"))
                .andExpect(jsonPath("$.refreshToken").value("REFRESH123"))
                .andExpect(jsonPath("$.nombre").value("Luis"));
    }
}
