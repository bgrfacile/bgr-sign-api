package com.bgrfacile.bgrsignapi.model;

import jakarta.persistence.*;
import lombok.Data;

import java.time.LocalDate;

@Entity
@Data
@Table(name = "students")
public class Student {
    @Id
    private Long userId;
    private String firstName;
    private String lastName;
    private LocalDate dateOfBirth;

    @ManyToOne
    @JoinColumn(name = "class_id")
    private SchoolClass schoolClass;

    @OneToOne
    @MapsId
    @JoinColumn(name = "user_id")
    private User user;
}
