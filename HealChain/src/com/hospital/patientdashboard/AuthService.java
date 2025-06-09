package com.hospital.patientdashboard;// AuthService.java

import java.sql.*;

public class AuthService {
    public static int authenticate(String username, String password) throws SQLException {
        String sql = "SELECT user_id, hash_pw FROM users WHERE username = ?";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, username);
            ResultSet rs = stmt.executeQuery();

            if (rs.next() && password.equals(rs.getString("hash_pw"))) {
                return rs.getInt("user_id");
            }
        }
        return -1;
    }


}