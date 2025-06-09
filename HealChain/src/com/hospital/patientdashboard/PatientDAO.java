package com.hospital.patientdashboard;// PatientDAO.java
import java.sql.*;

public class PatientDAO {
    public static Patient getPatientByUserId(int userId) throws SQLException {
        String sql = "SELECT * FROM patients WHERE user_id = ?";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, userId);
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                return new Patient(
                        rs.getInt("patient_id"),
                        rs.getInt("user_id"),
                        rs.getString("full_name"),
                        rs.getString("dob"),
                        rs.getString("gender"),
                        rs.getString("contact"),
                        rs.getString("address")
                );
            }
        }
        return null;
    }
}