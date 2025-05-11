package com.schoolvaccination.controller;

import com.schoolvaccination.model.Student;
import com.schoolvaccination.repository.StudentRepository;
import com.schoolvaccination.services.VaccinationService;
import lombok.RequiredArgsConstructor;
import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVParser;
import org.apache.commons.csv.CSVRecord;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@RestController
@RequiredArgsConstructor
@RequestMapping("/students")
@CrossOrigin(origins = "http://localhost:5173")
public class StudentController {

    @Autowired
    private StudentRepository studentRepository;
    private final VaccinationService vaccinationService;
    private Student student;

    // ✅ Add a new student
    @PostMapping
    public Student addStudent(@RequestBody Student student) {
        return studentRepository.save(student);
    }

    // ✅ Get all students
    @GetMapping
    public List<Student> getAllStudents() {
        return studentRepository.findAll();
    }

    // ✅ Get student by ID
    @GetMapping("/{id}")
    public Student getStudentById(@PathVariable String id) {
        return studentRepository.findById(id).orElse(null);
    }

    // ✅ Update a student
    @PutMapping("/{id}")
    public Student updateStudent(@PathVariable String id, @RequestBody Student updatedStudent) {
        Optional<Student> optionalStudent = studentRepository.findById(id);
        if (optionalStudent.isPresent()) {
            Student student = optionalStudent.get();
            student.setName(updatedStudent.getName());
            student.setStudentId(updatedStudent.getStudentId());
            student.setStudentClass(updatedStudent.getStudentClass());
            student.setVaccinated(updatedStudent.isVaccinated());
            return studentRepository.save(student);
        } else {
            return null;
        }
    }

    // ✅ Delete a student (optional)
    @DeleteMapping("/{id}")
    public void deleteStudent(@PathVariable String id) {
        studentRepository.deleteById(id);
    }

    // ✅ Search students by filters (name, class, ID, status)
    @GetMapping("/search")
    public List<Student> searchStudents(
            @RequestParam(required = false) String name,
            @RequestParam(required = false) String studentClass,
            @RequestParam(required = false) String studentId,
            @RequestParam(required = false) Boolean vaccinated
    ) {
        List<Student> students = studentRepository.findAll();

        return students.stream()
                .filter(s -> (name == null || s.getName().toLowerCase().contains(name.toLowerCase())))
                .filter(s -> (studentClass == null || s.getStudentClass().equalsIgnoreCase(studentClass)))
                .filter(s -> (studentId == null || s.getStudentId().equalsIgnoreCase(studentId)))
                .filter(s -> (vaccinated == null || s.isVaccinated() == vaccinated))
                .toList();
    }

    @PostMapping("/{studentId}/vaccinate/{driveId}")
    public Student markStudentVaccinated(@PathVariable String studentId, @PathVariable String driveId) {
        return vaccinationService.markStudentVaccinated(studentId, driveId);
    }

    @PostMapping("/upload")
    public ResponseEntity<?> uploadStudents(@RequestParam("file") MultipartFile file) {
        if (file.isEmpty()) {
            return ResponseEntity.badRequest().body("File is empty");
        }

        try (BufferedReader reader = new BufferedReader(new InputStreamReader(file.getInputStream()))) {
            CSVParser csvParser = new CSVParser(reader, CSVFormat.DEFAULT.withFirstRecordAsHeader());
            List<Student> students = new ArrayList<>();

            for (CSVRecord record : csvParser) {
                String studentId = record.get("studentId");
                String name = record.get("name");
                String studentClass = record.get("studentClass");

                if (studentRepository.existsById(studentId)) continue; // Skip if already exists

                Student student = new Student();
                student.setId(studentId);
                student.setName(name);
                student.setStudentClass(studentClass);

                students.add(student);
            }

            studentRepository.saveAll(students);
            return ResponseEntity.ok("Uploaded " + students.size() + " new students.");
        } catch (IOException e) {
            return ResponseEntity.status(500).body("Failed to process file.");
        }
    }

}
