package com.bgrfacile.bgrsignapi.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class TodaysClassDTO {
    private Long courseId;
    private String subjectName;
    private LocalDateTime startTime;
    private LocalDateTime endTime;
    private int presentStudents;
    private int totalStudents;
}
