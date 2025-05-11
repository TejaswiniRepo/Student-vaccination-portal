package com.schoolvaccination.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class DashboardSummaryResponse {
    private long totalStudents;
    private long vaccinatedStudents;
    private double vaccinationRate;
    private List<VaccinationDrive> upcomingDrives;
}
