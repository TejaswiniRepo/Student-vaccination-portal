package com.schoolvaccination.repository;

import com.schoolvaccination.model.Student;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface StudentRepository extends MongoRepository<Student, String> {

    List<Student> findByVaccinated(boolean value);
    long countByStudentClassIn(List<String> value);
    boolean existsById(String id);

}
