package com.bgrfacile.bgrsignapi.controller;

import com.bgrfacile.bgrsignapi.dto.TeacherSessionDTO;
import com.bgrfacile.bgrsignapi.model.Teacher;
import com.bgrfacile.bgrsignapi.model.User;
import com.bgrfacile.bgrsignapi.repository.TeacherRepository;
import com.bgrfacile.bgrsignapi.repository.UserRepository;
import com.bgrfacile.bgrsignapi.service.TeacherService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/teacher")
@RequiredArgsConstructor
@Tag(name = "Teacher Controller", description = "Opérations spécifiques aux enseignants.")
public class TeacherController {

    private final TeacherService teacherService;
    private final UserRepository userRepository;
    private final TeacherRepository teacherRepository;

    @GetMapping("/today-sessions")
    @Operation(summary = "Récupérer les sessions du jour pour l'enseignant connecté.")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Sessions récupérées avec succès."),
            @ApiResponse(responseCode = "404", description = "Enseignant non trouvé.")
    })

    public ResponseEntity<List<TeacherSessionDTO>> getTodaySessions() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String email = authentication.getName();

        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("User not found"));

        Teacher teacher = teacherRepository.findById(user.getId())
                .orElseThrow(() -> new RuntimeException("Teacher not found"));

        return ResponseEntity.ok(
                teacherService.getTodaySessions(teacher.getUserId())
        );
    }
}
