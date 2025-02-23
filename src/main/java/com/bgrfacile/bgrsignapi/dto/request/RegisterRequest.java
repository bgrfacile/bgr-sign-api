package com.bgrfacile.bgrsignapi.dto.request;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class RegisterRequest {
    @NotBlank
    @Email
    private String email;

    @NotBlank
    private String password;

    @NotBlank
    private String role; // Par exemple "ADMIN", "TEACHER", "STUDENT", "PARENT"
}
