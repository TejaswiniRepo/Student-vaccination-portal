package com.schoolvaccination.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.LocalDate;

@Data
@Document(collection = "vaccination_records")
@AllArgsConstructor
@NoArgsConstructor
public class VaccinationRecord {
    @Id
    private String id;
    private String studentId;
    private String driveId;
    private String vaccineName;
    private LocalDate date;
    private boolean vaccinated;
}
