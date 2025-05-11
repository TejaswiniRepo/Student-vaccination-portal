package com.schoolvaccination.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.LocalDate;
import java.util.List;

@Data
@Document(collection = "vaccination_drives")
@AllArgsConstructor
@NoArgsConstructor
public class VaccinationDrive {
    @Id
    private String id;
    private String vaccineName;
    private LocalDate dateOfDrive;
    private int availableDoses;
    private List<String> applicableClasses;
}
