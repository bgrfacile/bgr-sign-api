package com.bgrfacile.bgrsignapi;

import com.bgrfacile.bgrsignapi.dto.JwtAuthenticationResponse;
import com.bgrfacile.bgrsignapi.dto.LoginRequest;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
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

    // Teste l’endpoint de connexion et vérifie que le token JWT est retourné
    @Test
    public void testLogin_Success() throws Exception {
        LoginRequest loginRequest = new LoginRequest();
        loginRequest.setEmail("testuser@example.com");      // doit exister dans vos données de test
        loginRequest.setPassword("testpassword");

        mockMvc.perform(post("/api/auth/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(loginRequest)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.accessToken").exists())
                .andExpect(jsonPath("$.tokenType").value("Bearer"));
    }

    // Vérifie qu’un endpoint protégé renvoie un 401 Unauthorized lorsqu’aucun token n’est fourni
    @Test
    public void testSecuredEndpoint_Unauthorized() throws Exception {
        mockMvc.perform(get("/api/secure/hello"))
                .andExpect(status().isUnauthorized());
    }

    // Récupère un token via l’endpoint de connexion puis appelle un endpoint protégé avec ce token
    @Test
    public void testSecuredEndpoint_WithValidToken() throws Exception {
        // Obtenir le token via l’endpoint /api/auth/login
        LoginRequest loginRequest = new LoginRequest();
        loginRequest.setEmail("testuser@example.com");
        loginRequest.setPassword("testpassword");

        String loginResponse = mockMvc.perform(post("/api/auth/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(loginRequest)))
                .andExpect(status().isOk())
                .andReturn().getResponse().getContentAsString();

        JwtAuthenticationResponse authResponse = objectMapper.readValue(loginResponse, JwtAuthenticationResponse.class);
        String token = authResponse.getAccessToken();

        // Appeler l’endpoint sécurisé en fournissant le token dans le header Authorization
        mockMvc.perform(get("/api/secure/hello")
                        .header("Authorization", "Bearer " + token))
                .andExpect(status().isOk())
                .andExpect(content().string("Hello, secured world!")); // Par exemple, si votre contrôleur renvoie ce message
    }
}
