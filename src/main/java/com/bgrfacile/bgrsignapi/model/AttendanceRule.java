package com.bgrfacile.bgrsignapi.model;

import jakarta.persistence.*;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDate;

@Entity
@Table(name = "attendance_rules")
@Data
public class AttendanceRule {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "rule_name", unique = true, nullable = false, length = 100)
    private String ruleName;

    @Column(columnDefinition = "TEXT")
    private String description;

    @Column(name = "justification_deadline_days", nullable = false)
    private Integer justificationDeadlineDays;

    @Column(name = "min_attendance_percentage", precision = 5, scale = 2)
    private BigDecimal minAttendancePercentage;

    @Column(name = "effective_date", nullable = false)
    private LocalDate effectiveDate;
}
