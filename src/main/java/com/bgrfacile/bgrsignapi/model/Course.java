package com.bgrfacile.bgrsignapi.model;

import jakarta.persistence.*;
import lombok.Data;

import java.time.LocalDateTime;

@Entity
@Table(name = "courses",
        uniqueConstraints = @UniqueConstraint(columnNames = {"teacher_id", "subject_id", "class_id", "schedule"}))
@Data
public class Course {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "teacher_id")
    private Teacher teacher;

    @ManyToOne
    @JoinColumn(name = "subject_id")
    private Subject subject;

    @ManyToOne
    @JoinColumn(name = "class_id")
    private SchoolClass schoolClass;

    @Column(name = "schedule")
    private LocalDateTime schedule;
}
