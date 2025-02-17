package com.bgrfacile.bgrsignapi;

import com.bgrfacile.bgrsignapi.dto.JwtAuthenticationResponse;
import com.bgrfacile.bgrsignapi.dto.LoginRequest;
import com.bgrfacile.bgrsignapi.dto.RegisterRequest;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

@SpringBootTest
@AutoConfigureMockMvc
public class AuthenticationControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    public void testFullWorkflow() throws Exception {
        // Génère un email unique pour éviter le conflit "email déjà utilisé"
        String uniqueEmail = "newuser_" + System.currentTimeMillis() + "@example.com";

        // 1. Inscription
        RegisterRequest registerRequest = new RegisterRequest();
        registerRequest.setEmail(uniqueEmail);
        registerRequest.setPassword("password");
        registerRequest.setRole("student");  // Le rôle doit correspondre à une valeur définie dans ERole

        mockMvc.perform(post("/api/auth/register")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(registerRequest)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.success").value(true));

        // 2. Connexion
        LoginRequest loginRequest = new LoginRequest();
        loginRequest.setEmail(uniqueEmail);
        loginRequest.setPassword("password");

        String loginResponse = mockMvc.perform(post("/api/auth/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(loginRequest)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.accessToken").exists())
                .andReturn().getResponse().getContentAsString();

        JwtAuthenticationResponse authResponse = objectMapper.readValue(loginResponse, JwtAuthenticationResponse.class);
        String token = authResponse.getAccessToken();

        // 3. Accès à un endpoint protégé
        mockMvc.perform(get("/api/secure/hello")
                        .header("Authorization", "Bearer " + token))
                .andExpect(status().isOk())
                .andExpect(content().string("Hello, secured world!"));
    }

    // Vous pouvez garder les autres tests indépendants si vous le souhaitez
    @Test
    public void testLogin_Success() throws Exception {
        LoginRequest loginRequest = new LoginRequest();
        loginRequest.setEmail("teststudent@example.com");  // Doit exister dans vos données de test
        loginRequest.setPassword("password");

        mockMvc.perform(post("/api/auth/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(loginRequest)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.accessToken").exists())
                .andExpect(jsonPath("$.tokenType").value("Bearer"));
    }

    @Test
    public void testSecuredEndpoint_Unauthorized() throws Exception {
        mockMvc.perform(get("/api/secure/hello"))
                .andExpect(status().isUnauthorized());
    }

    @Test
    public void testSecuredEndpoint_WithValidToken() throws Exception {
        // Obtenir le token via l'endpoint /api/auth/login
        LoginRequest loginRequest = new LoginRequest();
        loginRequest.setEmail("teststudent@example.com");
        loginRequest.setPassword("password");

        String loginResponse = mockMvc.perform(post("/api/auth/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(loginRequest)))
                .andExpect(status().isOk())
                .andReturn().getResponse().getContentAsString();

        JwtAuthenticationResponse authResponse = objectMapper.readValue(loginResponse, JwtAuthenticationResponse.class);
        String token = authResponse.getAccessToken();

        // Appeler l'endpoint sécurisé avec le token
        mockMvc.perform(get("/api/secure/hello")
                        .header("Authorization", "Bearer " + token))
                .andExpect(status().isOk())
                .andExpect(content().string("Hello, secured world!"));
    }
}
