package com.schoolvaccination.controller;

import com.lowagie.text.Document;
import com.lowagie.text.Paragraph;
import com.lowagie.text.Phrase;
import com.lowagie.text.pdf.PdfPCell;
import com.lowagie.text.pdf.PdfPTable;
import com.lowagie.text.pdf.PdfWriter;
import com.schoolvaccination.model.Student;
import com.schoolvaccination.repository.StudentRepository;
import com.schoolvaccination.repository.VaccinationRecordRepository;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVPrinter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.awt.*;
import java.io.IOException;
import java.util.List;
import java.util.stream.Stream;

@RestController
@RequiredArgsConstructor
@RequestMapping("/reports")
public class ReportsController {

    @Autowired
    private StudentRepository studentRepository;

    @Autowired
    private VaccinationRecordRepository vaccinationRecordRepository;

    @GetMapping(value = "/export/csv", produces = "text/csv")
    public void exportCsv(HttpServletResponse response) throws IOException {
        response.setHeader("Content-Disposition", "attachment; filename=students_report.csv");

        List<Student> students = studentRepository.findAll();

        try (CSVPrinter csvPrinter = new CSVPrinter(response.getWriter(),
                CSVFormat.DEFAULT.withHeader("ID", "Name", "Class", "Vaccinated"))) {
            for (Student s : students) {
                boolean vaccinated = !vaccinationRecordRepository.findByStudentId(s.getId()).isEmpty();
                csvPrinter.printRecord(s.getId(), s.getName(), s.getStudentClass(), vaccinated ? "Yes" : "No");
            }
        }
    }

    @GetMapping(value = "/export/pdf", produces = "application/pdf")
    public void exportPdf(HttpServletResponse response) throws IOException {
        response.setHeader("Content-Disposition", "attachment; filename=students_report.pdf");

        List<Student> students = studentRepository.findAll();
        Document document = new Document();
        PdfWriter.getInstance(document, response.getOutputStream());
        document.open();

        document.add(new Paragraph("Student Vaccination Report"));
        document.add(new Paragraph(" "));

        PdfPTable table = new PdfPTable(4);
        Stream.of("ID", "Name", "Class", "Vaccinated")
                .forEach(header -> {
                    PdfPCell cell = new PdfPCell(new Phrase(header));
                    cell.setBackgroundColor(Color.BLACK);
                    table.addCell(cell);
                });

        for (Student s : students) {
            boolean vaccinated = !vaccinationRecordRepository.findByStudentId(s.getId()).isEmpty();
            table.addCell(String.valueOf(s.getId()));
            table.addCell(s.getName());
            table.addCell(s.getStudentClass());
            table.addCell(vaccinated ? "Yes" : "No");
        }

        document.add(table);
        document.close();
    }
}
