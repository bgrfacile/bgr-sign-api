package com.bgrfacile.bgrsignapi.controller;

import com.bgrfacile.bgrsignapi.dto.CreateAttendanceDTO;
import com.bgrfacile.bgrsignapi.dto.TodaysClassDTO;
import com.bgrfacile.bgrsignapi.model.Attendance;
import com.bgrfacile.bgrsignapi.service.AttendanceService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api")
@Tag(name = "Attendance Controller", description = "Gestion des présences")
public class AttendanceController {

    @Autowired
    private AttendanceService attendanceService;

    @GetMapping("/attendances")
    public Map<String, List<TodaysClassDTO>> getAttendances() {
        return attendanceService.getTodaysAttendances();
    }

    @Operation(summary = "Créer une présence", description = "Enregistrer la présence d'un étudiant.")
    @ApiResponses({
            @ApiResponse(responseCode = "201", description = "Présence enregistrée avec succès."),
            @ApiResponse(responseCode = "400", description = "Données invalides fournies.")
    })
    @PostMapping("/attendances")
    public ResponseEntity<Attendance> createAttendance(@RequestBody CreateAttendanceDTO attendanceDTO) {
        Attendance createdAttendance = attendanceService.markAttendance(attendanceDTO);
        return new ResponseEntity<>(createdAttendance, HttpStatus.CREATED);
    }
}
