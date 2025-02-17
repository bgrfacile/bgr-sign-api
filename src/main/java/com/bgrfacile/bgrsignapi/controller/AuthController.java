package com.bgrfacile.bgrsignapi.controller;

import com.bgrfacile.bgrsignapi.dto.ApiResponse;
import com.bgrfacile.bgrsignapi.dto.JwtAuthenticationResponse;
import com.bgrfacile.bgrsignapi.dto.LoginRequest;
import com.bgrfacile.bgrsignapi.dto.RegisterRequest;
import com.bgrfacile.bgrsignapi.model.Role;
import com.bgrfacile.bgrsignapi.model.User;
import com.bgrfacile.bgrsignapi.repository.RoleRepository;
import com.bgrfacile.bgrsignapi.repository.UserRepository;
import com.bgrfacile.bgrsignapi.security.JwtTokenProvider;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Collections;

@RestController
@RequestMapping("/api/auth")
public class AuthController {

    @Autowired
    private AuthenticationManager authenticationManager;

    @Autowired
    private JwtTokenProvider tokenProvider;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private RoleRepository roleRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @PostMapping("/login")
    public ResponseEntity<?> authenticateUser(@Valid @RequestBody LoginRequest loginRequest) {
        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        loginRequest.getEmail(),
                        loginRequest.getPassword()
                )
        );
        SecurityContextHolder.getContext().setAuthentication(authentication);
        String jwt = tokenProvider.generateToken(authentication);
        return ResponseEntity.ok(new JwtAuthenticationResponse(jwt));
    }

    @PostMapping("/register")
    public ResponseEntity<?> registerUser(@Valid @RequestBody RegisterRequest registerRequest) {
        // Vérifie si l'email existe déjà
        if (userRepository.existsByEmail(registerRequest.getEmail())) {
            return ResponseEntity.badRequest().body(new ApiResponse(false, "L'adresse email est déjà utilisée !"));
        }

        // Crée un nouvel utilisateur
        User user = new User();
        user.setEmail(registerRequest.getEmail());
        user.setPassword(passwordEncoder.encode(registerRequest.getPassword()));

        // Assigne un rôle par défaut, par exemple "ROLE_USER"
        Role userRole = roleRepository.findByName(registerRequest.getRole())
                .orElseThrow(() -> new RuntimeException("Erreur: le rôle " + registerRequest.getRole() + " n'existe pas en base."));

        user.setRoles(Collections.singleton(userRole));

        // Sauvegarde l'utilisateur
        userRepository.save(user);

        return ResponseEntity.ok(new ApiResponse(true, "Utilisateur enregistré avec succès !"));
    }
}
