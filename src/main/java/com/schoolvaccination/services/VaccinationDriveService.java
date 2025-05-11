package com.schoolvaccination.services;

import com.schoolvaccination.model.VaccinationDrive;
import com.schoolvaccination.repository.VaccinationDriveRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;

@Service
@RequiredArgsConstructor
public class VaccinationDriveService {

    private final VaccinationDriveRepository driveRepository;

    public VaccinationDrive createDrive(VaccinationDrive drive) {
        LocalDate now = LocalDate.now();

        if (drive.getDateOfDrive().isBefore(now.plusDays(15))) {
            throw new RuntimeException("Drive must be scheduled at least 15 days in advance");
        }

        List<VaccinationDrive> conflictingDrives =
                driveRepository.findByDateOfDriveAndApplicableClassesIn(drive.getDateOfDrive(), drive.getApplicableClasses());

        if (!conflictingDrives.isEmpty()) {
            throw new RuntimeException("A drive is already scheduled on this date for one or more of the same classes.");
        }

        return driveRepository.save(drive);
    }

    public List<VaccinationDrive> getAllDrives() {
        return driveRepository.findAll();
    }

    public List<VaccinationDrive> getUpcomingDrives() {
        LocalDate today = LocalDate.now();
        LocalDate in30Days = today.plusDays(30);
        return driveRepository.findByDateOfDriveBetween(today, in30Days);
    }

    public VaccinationDrive updateDrive(String id, VaccinationDrive updatedDrive) {
        VaccinationDrive existing = driveRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Drive not found"));

        if (existing.getDateOfDrive().isBefore(LocalDate.now())) {
            throw new RuntimeException("Cannot edit past drives.");
        }

        existing.setVaccineName(updatedDrive.getVaccineName());
        existing.setDateOfDrive(updatedDrive.getDateOfDrive());
        existing.setAvailableDoses(updatedDrive.getAvailableDoses());
        existing.setApplicableClasses(updatedDrive.getApplicableClasses());

        return driveRepository.save(existing);
    }

    public VaccinationDrive getDriveById(String id) {
        return driveRepository.findById(id).orElse(null);
    }
}

