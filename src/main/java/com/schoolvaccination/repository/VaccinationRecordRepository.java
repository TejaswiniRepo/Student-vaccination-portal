package com.schoolvaccination.repository;

import com.schoolvaccination.model.VaccinationRecord;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface VaccinationRecordRepository extends MongoRepository<VaccinationRecord, String> {

    long countByDriveId(String id);

    String findByStudentId(String id);
}
