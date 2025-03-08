package com.bgrfacile.bgrsignapi.controller;

import com.bgrfacile.bgrsignapi.dto.UserProfileDTO;
import com.bgrfacile.bgrsignapi.dto.request.LoginRequest;
import com.bgrfacile.bgrsignapi.dto.request.RegisterRequest;
import com.bgrfacile.bgrsignapi.dto.response.ApiResponse;
import com.bgrfacile.bgrsignapi.dto.response.JwtAuthenticationResponse;
import com.bgrfacile.bgrsignapi.security.CustomUserDetails;
import com.bgrfacile.bgrsignapi.model.Role;
import com.bgrfacile.bgrsignapi.model.User;
import com.bgrfacile.bgrsignapi.repository.RoleRepository;
import com.bgrfacile.bgrsignapi.repository.UserRepository;
import com.bgrfacile.bgrsignapi.security.JwtTokenProvider;
import com.bgrfacile.bgrsignapi.service.UserService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

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

    @Autowired
    private UserService userService;

    @PostMapping("/login")
    public ResponseEntity<?> authenticateUser(@Valid @RequestBody LoginRequest loginRequest) {
        // Authentifier l'utilisateur
        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        loginRequest.getEmail(),
                        loginRequest.getPassword()
                )
        );

        // Placer l'authentification dans le contexte de sécurité
        SecurityContextHolder.getContext().setAuthentication(authentication);

        // Générer le token JWT
        String jwt = tokenProvider.generateToken(authentication);

        // Récupérer la durée d'expiration du token (en secondes par exemple)
        long expiresIn = tokenProvider.getExpiryDuration(); // Méthode à implémenter dans JwtTokenProvider

        // Retourne la réponse incluant le token et les infos de l'utilisateur
        return ResponseEntity.ok(new JwtAuthenticationResponse(jwt, expiresIn));
    }

    @GetMapping("/me")
    public ResponseEntity<?> getCurrentUser(@AuthenticationPrincipal CustomUserDetails currentUser) {
        if (currentUser == null) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                    .body(new ApiResponse(false, "Utilisateur non authentifié"));
        }
        User user = userRepository.findById(currentUser.getId())
                .orElseThrow(() -> new RuntimeException("Utilisateur non trouvé"));
        UserProfileDTO userProfileDTO = userService.getUserProfile(user);
        return ResponseEntity.ok(userProfileDTO);
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
