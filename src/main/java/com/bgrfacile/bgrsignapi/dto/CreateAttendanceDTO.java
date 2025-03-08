package com.bgrfacile.bgrsignapi.dto;

import com.bgrfacile.bgrsignapi.model.enums.AttendanceStatus;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class CreateAttendanceDTO {
    private Long studentId;
    private Long courseId;
    private LocalDate date;
    private AttendanceStatus status;
    private Long teacherId;

}
