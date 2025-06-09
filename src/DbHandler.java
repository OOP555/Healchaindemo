import java.sql.*;
import org.mindrot.jbcrypt.BCrypt;
public class DbHandler {
    private static final String DB_URL = "jdbc:mysql://localhost:3306/heal_chain_db";
    private static final String DB_USER = "root";
    private static final String DB_PASSWORD = "";

    public static Connection getConnection() throws SQLException {
        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
            Connection conn = DriverManager.getConnection(DB_URL, DB_USER, DB_PASSWORD);
            conn.setCatalog("heal_chain_db");
            return conn;
        } catch (ClassNotFoundException e) {
            throw new SQLException("Database driver not found");
        }
    }

    public static boolean validateAdmin(String username, String password) {
        // Hardcoded admin credentials (replace with secure method in production)
        return "admin".equals(username) && "admin123".equals(password);
    }
}