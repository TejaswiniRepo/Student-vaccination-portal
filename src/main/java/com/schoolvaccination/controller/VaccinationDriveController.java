package com.schoolvaccination.controller;

import com.schoolvaccination.model.VaccinationDrive;
import com.schoolvaccination.services.VaccinationDriveService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/drives")
@RequiredArgsConstructor
@CrossOrigin(origins = "http://localhost:5173")
public class VaccinationDriveController {

    private final VaccinationDriveService driveService;

    // ✅ Create a drive
    @PostMapping
    public VaccinationDrive createDrive(@RequestBody VaccinationDrive drive) {
        return driveService.createDrive(drive);
    }

    // ✅ Get all drives
    @GetMapping
    public List<VaccinationDrive> getAllDrives() {
        return driveService.getAllDrives();
    }

    // ✅ Get upcoming drives (next 30 days)
    @GetMapping("/upcoming")
    public List<VaccinationDrive> getUpcomingDrives() {
        return driveService.getUpcomingDrives();
    }

    // ✅ Get drive by ID
    @GetMapping("/{id}")
    public VaccinationDrive getDriveById(@PathVariable String id) {
        return driveService.getDriveById(id);
    }

    // ✅ Update drive
    @PutMapping("/{id}")
    public VaccinationDrive updateDrive(@PathVariable String id, @RequestBody VaccinationDrive drive) {
        return driveService.updateDrive(id, drive);
    }
}
