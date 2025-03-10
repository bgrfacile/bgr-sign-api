package com.bgrfacile.bgrsignapi.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class SecureController {

    @GetMapping("/api/secure/hello")
    public ResponseEntity<String> hello() {
        return ResponseEntity.ok("Hello, secured world!");
    }
}
