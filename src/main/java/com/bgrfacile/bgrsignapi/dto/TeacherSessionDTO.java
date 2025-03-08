package com.bgrfacile.bgrsignapi.dto;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class TeacherSessionDTO {
    private String time; // Format "9:00 AM"
    private String subjectName;
    private int presentCount;
    private int totalStudents;
}
