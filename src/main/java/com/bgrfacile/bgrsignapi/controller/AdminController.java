package com.bgrfacile.bgrsignapi.controller;

import com.bgrfacile.bgrsignapi.dto.request.CreateSessionRequest;
import com.bgrfacile.bgrsignapi.exception.SessionConflictException;
import com.bgrfacile.bgrsignapi.model.Course;
import com.bgrfacile.bgrsignapi.service.SessionService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/admin")
@Tag(name = "Admin Controller", description = "Gestion des sessions par l'administrateur")
public class AdminController {
    @Autowired
    private SessionService sessionService;

    @Operation(summary = "Créer une session", description = "Permet à l'administrateur de créer une nouvelle session.")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Session créée avec succès"),
            @ApiResponse(responseCode = "409", description = "Conflit de session détecté")
    })


    @PostMapping("/sessions")
    public ResponseEntity<Course> createSession(@RequestBody CreateSessionRequest request) {
        Course course = sessionService.createSession(request);
        return ResponseEntity.ok(course);
    }

    @ExceptionHandler(SessionConflictException.class)
    public ResponseEntity<String> handleSessionConflictException(SessionConflictException ex) {
        return ResponseEntity.status(HttpStatus.CONFLICT).body(ex.getMessage());
    }
}
