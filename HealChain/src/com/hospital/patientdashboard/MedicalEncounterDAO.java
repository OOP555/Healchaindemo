package com.hospital.patientdashboard;

import java.sql.*;

public class MedicalEncounterDAO {
    public static MedicalEncounter getEncounterById(int encounterId) throws SQLException {
        String sql = "SELECT * FROM medical_encounters WHERE encounter_id = ?";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, encounterId);
            ResultSet rs = stmt.executeQuery();

            if (rs.next()) {
                return new MedicalEncounter(
                        rs.getInt("encounter_id"),
                        rs.getInt("patient_id"),
                        rs.getInt("doctor_id"),
                        rs.getDate("encounter_date"),
                        rs.getString("notes")
                );
            }
        }
        return null;
    }
}