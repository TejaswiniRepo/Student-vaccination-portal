package com.schoolvaccination.services;

import com.schoolvaccination.model.Student;
import com.schoolvaccination.model.VaccinationDrive;
import com.schoolvaccination.model.VaccinationRecord;
import com.schoolvaccination.repository.StudentRepository;
import com.schoolvaccination.repository.VaccinationDriveRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDate;

@Service
@RequiredArgsConstructor
public class VaccinationService {

    private final StudentRepository studentRepository;
    private final VaccinationDriveRepository driveRepository;

    public Student markStudentVaccinated(String studentId, String driveId) {
        Student student = studentRepository.findById(studentId)
                .orElseThrow(() -> new RuntimeException("Student not found"));

        VaccinationDrive drive = driveRepository.findById(driveId)
                .orElseThrow(() -> new RuntimeException("Drive not found"));

        if (drive.getDateOfDrive().isBefore(LocalDate.now())) {
            throw new RuntimeException("Drive already expired");
        }

        boolean alreadyVaccinatedForThisVaccine = student.getVaccinationRecords().stream()
                .anyMatch(record -> record.getVaccineName().equalsIgnoreCase(drive.getVaccineName()));

        if (alreadyVaccinatedForThisVaccine) {
            throw new RuntimeException("Student already vaccinated for this vaccine");
        }

        VaccinationRecord record = new VaccinationRecord(drive.getVaccineName(), studentId, driveId, drive.getVaccineName(), drive.getDateOfDrive(), true);
        student.getVaccinationRecords().add(record);

        student.setVaccinated(true);

        return studentRepository.save(student);
    }
}
