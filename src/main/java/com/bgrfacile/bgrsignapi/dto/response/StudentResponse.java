package com.bgrfacile.bgrsignapi.dto.response;

import com.bgrfacile.bgrsignapi.model.Student;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class StudentResponse {
    private Long userId;
    private String firstName;
    private String lastName;
    private String dateOfBirth;

    public StudentResponse(Student student) {
        this.userId = student.getUserId();
        this.firstName = student.getFirstName();
        this.lastName = student.getLastName();
        this.dateOfBirth = String.valueOf(student.getDateOfBirth());
    }
}
