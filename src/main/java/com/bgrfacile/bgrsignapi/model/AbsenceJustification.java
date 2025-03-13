package com.bgrfacile.bgrsignapi.model;

import com.bgrfacile.bgrsignapi.model.enums.JustificationStatus;
import jakarta.persistence.*;

import java.time.LocalDateTime;

@Entity
@Table(name = "absence_justifications")
public class AbsenceJustification {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @OneToOne
    @JoinColumn(name = "attendance_id")
    private Attendance attendance;

    @Column(nullable = false, columnDefinition = "TEXT")
    private String reason;

    @Column(name = "attachment_path", length = 255)
    private String attachmentPath;

    @ManyToOne
    @JoinColumn(name = "submitted_by")
    private User submittedBy;

    @Enumerated(EnumType.STRING)
    @Column(length = 20)
    private JustificationStatus status = JustificationStatus.pending;

    @ManyToOne
    @JoinColumn(name = "reviewed_by")
    private User reviewedBy;

    @Column(name = "created_at")
    private LocalDateTime createdAt = LocalDateTime.now();

    @Column(name = "updated_at")
    private LocalDateTime updatedAt = LocalDateTime.now();
}
