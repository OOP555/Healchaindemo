package com.hospital.patientdashboard;// MedicalDataService.java


import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class MedicalDataService {
    public static List<Diagnosis> getPatientDiagnoses(int patientId) throws SQLException {
        List<Diagnosis> diagnoses = new ArrayList<>();
        String sql = "SELECT d.* FROM diagnoses d " +
                "JOIN medical_encounters me ON d.encounter_id = me.encounter_id " +
                "WHERE me.patient_id = ?";

        try (Connection conn = DBConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, patientId);
            ResultSet rs = stmt.executeQuery();

            while (rs.next()) {
                diagnoses.add(new Diagnosis(
                        rs.getInt("diagnosis_id"),
                        rs.getInt("encounter_id"),
                        rs.getString("diagnosis_code"),
                        rs.getString("diagnosis_name"),
                        rs.getString("description")
                ));
            }
        }
        return diagnoses;
    }

    public static List<Medication> getPatientMedications(int patientId) throws SQLException {
        List<Medication> medications = new ArrayList<>();
        String sql = "SELECT m.* FROM medications m " +
                "JOIN medical_encounters me ON m.encounter_id = me.encounter_id " +
                "WHERE me.patient_id = ?";

        try (Connection conn = DBConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, patientId);
            ResultSet rs = stmt.executeQuery();

            while (rs.next()) {
                medications.add(new Medication(
                        rs.getInt("medication_id"),
                        rs.getInt("encounter_id"),
                        rs.getString("medication_name"),
                        rs.getString("dosage"),
                        rs.getString("frequency"),
                        rs.getString("duration"),
                        rs.getString("instructions")
                ));
            }
        }
        return medications;
    }

    public static List<TestResult> getPatientTestResults(int patientId) throws SQLException {
        List<TestResult> testResults = new ArrayList<>();
        String sql = "SELECT t.* FROM test_results t " +
                "JOIN medical_encounters me ON t.encounter_id = me.encounter_id " +
                "WHERE me.patient_id = ?";

        try (Connection conn = DBConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, patientId);
            ResultSet rs = stmt.executeQuery();

            while (rs.next()) {
                testResults.add(new TestResult(
                        rs.getInt("test_id"),
                        rs.getInt("encounter_id"),
                        rs.getString("test_name"),
                        rs.getDate("test_date"),
                        rs.getString("result_value"),
                        rs.getString("result_unit"),
                        rs.getString("notes")
                ));
            }
        }
        return testResults;
    }
}