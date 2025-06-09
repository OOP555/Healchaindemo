import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.*;
import org.mindrot.jbcrypt.BCrypt;


public class LoginFrame extends JFrame {
    private JTextField usernameField;
    private JPasswordField passwordField;
    private JComboBox<UserType> userTypeComboBox;

    public LoginFrame() {
        setTitle("HealChain Login");
        setSize(800, 500);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        initComponents();
    }


    private void initComponents() {
        JPanel mainPanel = new JPanel(new BorderLayout());

        /* ---------- Left branding panel ---------- */
        JPanel leftPanel = new JPanel(new GridBagLayout());
        leftPanel.setPreferredSize(new Dimension(350, 500));
        leftPanel.setBackground(new Color(53, 94, 59)); // deep green

        GridBagConstraints leftGbc = new GridBagConstraints();
        leftGbc.gridwidth = GridBagConstraints.REMAINDER;
        leftGbc.insets = new Insets(10, 0, 10, 0);
        leftGbc.anchor = GridBagConstraints.CENTER;

        JLabel logoLabel = new JLabel("HEALCHAIN");
        logoLabel.setFont(new Font("Arial", Font.BOLD, 36));
        logoLabel.setForeground(Color.WHITE);
        leftPanel.add(logoLabel, leftGbc);

        /* ---------- Right login form panel ---------- */
        JPanel rightPanel = new JPanel(new GridBagLayout());
        rightPanel.setBackground(Color.WHITE);

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(10, 10, 10, 10);
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.anchor = GridBagConstraints.CENTER;

        // Title label
        JLabel titleLabel = new JLabel("Login to System");
        titleLabel.setFont(new Font("Arial", Font.BOLD, 24));
        titleLabel.setForeground(new Color(53, 94, 59));
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.gridwidth = 2;
        rightPanel.add(titleLabel, gbc);

        gbc.gridwidth = 1;
        gbc.anchor = GridBagConstraints.WEST;

        // Username
        gbc.gridx = 0;
        gbc.gridy = 1;
        rightPanel.add(new JLabel("Username:"), gbc);
        gbc.gridx = 1;
        usernameField = new JTextField(15);
        rightPanel.add(usernameField, gbc);

        // Password
        gbc.gridx = 0;
        gbc.gridy = 2;
        rightPanel.add(new JLabel("Password:"), gbc);
        gbc.gridx = 1;
        passwordField = new JPasswordField(15);
        rightPanel.add(passwordField, gbc);

        // User type selector
        gbc.gridx = 0;
        gbc.gridy = 3;
        rightPanel.add(new JLabel("User Type:"), gbc);
        gbc.gridx = 1;
        userTypeComboBox = new JComboBox<>(UserType.values());
        rightPanel.add(userTypeComboBox, gbc);

        // Login button (wiring preserved)
        gbc.gridx = 0;
        gbc.gridy = 4;
        gbc.gridwidth = 2;
        gbc.anchor = GridBagConstraints.CENTER;
        JButton loginButton = new JButton("Login");
        loginButton.setPreferredSize(new Dimension(130, 40));
        loginButton.setBackground(new Color(53, 94, 59));
        loginButton.setForeground(Color.WHITE);
        loginButton.setFont(new Font("Arial", Font.BOLD, 14));
        loginButton.setFocusPainted(false);
        loginButton.setBorderPainted(false);
        loginButton.addActionListener(this::handleLogin);
        rightPanel.add(loginButton, gbc);

        /* ---------- Assemble ---------- */
        mainPanel.add(leftPanel, BorderLayout.WEST);
        mainPanel.add(rightPanel, BorderLayout.CENTER);
        add(mainPanel);
    }


    private void handleLogin(ActionEvent e) {
        String username = usernameField.getText();
        String inputHash = new String(passwordField.getPassword());
        UserType userType = (UserType) userTypeComboBox.getSelectedItem();

        // Basic form validation
        if (username.isEmpty() || inputHash.isEmpty()) {
            JOptionPane.showMessageDialog(this,
                    "Username and password hash are required!",
                    "Error",
                    JOptionPane.ERROR_MESSAGE);
            return;
        }

        // Hard‑coded receptionist
        if (userType == UserType.REGISTRATION &&
                username.equals("Ms.margot") &&
                inputHash.equals("r0000")) {
            new RegistrationPanel().setVisible(true);
            dispose();
            return;
        }

        try (Connection conn = DbHandler.getConnection()) {
            String sql = "SELECT u.user_id, u.hash_pw, u.role, " +
                    "CASE WHEN u.role = 'DOCTOR' THEN d.doctor_id ELSE NULL END AS doctor_id, " +
                    "CASE WHEN u.role = 'PATIENT' THEN p.patient_id ELSE NULL END AS patient_id " +
                    "FROM users u " +
                    "LEFT JOIN doctors d ON u.user_id = d.user_id " +
                    "LEFT JOIN patients p ON u.user_id = p.user_id " +
                    "WHERE u.username = ?";

            PreparedStatement stmt = conn.prepareStatement(sql);
            stmt.setString(1, username);
            ResultSet rs = stmt.executeQuery();

            if (rs.next()) {
                String storedHash = rs.getString("hash_pw");

                if (inputHash.equals(storedHash)) {
                    int userId = rs.getInt("user_id");
                    String role = rs.getString("role");

                    if ((userType == UserType.DOCTOR && "DOCTOR".equals(role)) ||
                            (userType == UserType.REGISTRATION && "PATIENT".equals(role))) {

                        int doctorId = rs.getInt("doctor_id");
                        if (userType == UserType.DOCTOR) {
                            new DoctorDashboard(doctorId).setVisible(true);
                        } else {
                            new RegistrationPanel().setVisible(true);
                        }
                        dispose();
                    } else {
                        JOptionPane.showMessageDialog(this,
                                "Account type doesn't match selected role.",
                                "Role Mismatch",
                                JOptionPane.ERROR_MESSAGE);
                    }
                } else {
                    JOptionPane.showMessageDialog(this,
                            "Invalid password hash.",
                            "Authentication Failed",
                            JOptionPane.ERROR_MESSAGE);
                }
            } else {
                JOptionPane.showMessageDialog(this,
                        "User not found.",
                        "Authentication Failed",
                        JOptionPane.ERROR_MESSAGE);
            }
        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(this,
                    "Database error: " + ex.getMessage(),
                    "System Error",
                    JOptionPane.ERROR_MESSAGE);
            ex.printStackTrace();
        }
    }
}
