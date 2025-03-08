package com.bgrfacile.bgrsignapi.controller;

import com.bgrfacile.bgrsignapi.dto.request.CreateSessionRequest;
import com.bgrfacile.bgrsignapi.exception.SessionConflictException;
import com.bgrfacile.bgrsignapi.model.Course;
import com.bgrfacile.bgrsignapi.service.SessionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/admin")
public class AdminController {
    @Autowired
    private SessionService sessionService;

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
