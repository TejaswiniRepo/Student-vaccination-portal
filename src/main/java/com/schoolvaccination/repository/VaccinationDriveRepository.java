package com.schoolvaccination.repository;

import com.schoolvaccination.model.VaccinationDrive;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;

@Repository
public interface VaccinationDriveRepository extends MongoRepository<VaccinationDrive, String> {
    List<VaccinationDrive> findByDateOfDriveBetween(LocalDate start, LocalDate end);
    List<VaccinationDrive> findByDateOfDriveAndApplicableClassesIn(LocalDate date, List<String> classes);
}
