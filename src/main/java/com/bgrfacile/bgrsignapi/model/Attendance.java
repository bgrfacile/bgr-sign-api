package com.bgrfacile.bgrsignapi.model;

import com.bgrfacile.bgrsignapi.model.enums.AttendanceStatus;
import jakarta.persistence.*;
import lombok.Data;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Entity
@Table(name = "attendances")
@Data
public class Attendance {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "student_id")
    private Student student;

    @ManyToOne
    @JoinColumn(name = "course_id")
    private Course course;

    @Column(name = "date", nullable = false)
    private LocalDate date;

    // Vous pouvez utiliser une énumération pour le statut
    @Enumerated(EnumType.STRING)
    @Column(name = "status", length = 20)
    private AttendanceStatus status;

    @ManyToOne
    @JoinColumn(name = "recorded_by")
    private Teacher recordedBy;

    @Column(name = "recorded_at")
    private LocalDateTime recordedAt = LocalDateTime.now();
}
