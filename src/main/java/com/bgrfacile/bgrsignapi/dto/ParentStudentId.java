package com.bgrfacile.bgrsignapi.dto;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import lombok.Data;

import java.io.Serializable;

@Embeddable
@Data
public class ParentStudentId implements Serializable {
    @Column(name = "parent_id")
    private Long parentId;

    @Column(name = "student_id")
    private Long studentId;
}
