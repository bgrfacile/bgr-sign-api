package com.bgrfacile.bgrsignapi.model;

import jakarta.persistence.*;
import lombok.Data;

import java.time.LocalDate;

@Entity
@Data
@Table(name = "teachers")
public class Teacher {
    @Id
    private Long userId;
    private String firstName;
    private String lastName;
    private LocalDate hireDate;
    private String specialization;

    @OneToOne
    @MapsId
    @JoinColumn(name = "user_id")
    private User user;
}
