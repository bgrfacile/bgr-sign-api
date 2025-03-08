package com.bgrfacile.bgrsignapi.controller;

import com.bgrfacile.bgrsignapi.dto.CreateAttendanceDTO;
import com.bgrfacile.bgrsignapi.dto.TodaysClassDTO;
import com.bgrfacile.bgrsignapi.model.Attendance;
import com.bgrfacile.bgrsignapi.service.AttendanceService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api")
public class AttendanceController {

    @Autowired
    private AttendanceService attendanceService;

    @GetMapping("/attendances")
    public Map<String, List<TodaysClassDTO>> getAttendances() {
        return attendanceService.getTodaysAttendances();
    }

    @PostMapping("/attendances")
    public ResponseEntity<Attendance> createAttendance(@RequestBody CreateAttendanceDTO attendanceDTO) {
        Attendance createdAttendance = attendanceService.markAttendance(attendanceDTO);
        return new ResponseEntity<>(createdAttendance, HttpStatus.CREATED);
    }
}
