package com.schoolvaccination.controller;

import com.schoolvaccination.model.DashboardSummaryResponse;
import com.schoolvaccination.model.VaccinationDrive;
import com.schoolvaccination.repository.StudentRepository;
import com.schoolvaccination.repository.VaccinationDriveRepository;
import com.schoolvaccination.repository.VaccinationRecordRepository;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/dashboard")
@CrossOrigin(origins = "http://localhost:5173")
public class DashboardController {

    private final StudentRepository studentRepository;
    private final VaccinationDriveRepository driveRepository;
    private final VaccinationRecordRepository recordRepository;

    public DashboardController(StudentRepository studentRepository, VaccinationDriveRepository driveRepository,
                               VaccinationRecordRepository recordRepository) {
        this.studentRepository = studentRepository;
        this.driveRepository = driveRepository;
        this.recordRepository = recordRepository;
    }
    @GetMapping("/summary")
    public DashboardSummaryResponse getDashboardSummary() {
        long total = studentRepository.count();
        long vaccinated = studentRepository.findByVaccinated(true).size();
        double percentage = total == 0 ? 0 : ((double) vaccinated / total) * 100;

        LocalDate now = LocalDate.now();
        LocalDate in30Days = now.plusDays(30);
        List<VaccinationDrive> upcomingDrives = driveRepository.findByDateOfDriveBetween(now, in30Days);

        return new DashboardSummaryResponse(total, vaccinated, percentage, upcomingDrives);
    }

    @GetMapping("/vaccination-progress")
    public ResponseEntity<List<Map<String, Object>>> getVaccinationProgress() {
        List<VaccinationDrive> drives = driveRepository.findAll();
        List<Map<String, Object>> result = new ArrayList<>();

        for (VaccinationDrive drive : drives) {
            long totalEligible = studentRepository.countByStudentClassIn(drive
                    .getApplicableClasses());
            long vaccinated = recordRepository.countByDriveId(drive.getId());

            Map<String, Object> entry = new HashMap<>();
            entry.put("date", drive.getDateOfDrive());
            entry.put("vaccine", drive.getVaccineName());
            entry.put("percentage", totalEligible == 0 ? 0 : (vaccinated * 100.0 / totalEligible));
            result.add(entry);
        }

        return ResponseEntity.ok(result);
    }

}
