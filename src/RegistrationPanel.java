import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.sql.*;
import org.mindrot.jbcrypt.BCrypt;

public class RegistrationPanel extends JFrame {
    private JTextField fullNameField, dobField, genderField, contactField, addressField;
    private JPasswordField passwordField;
//    private final int doctorId;

    public RegistrationPanel(){

        setTitle("Patient Registration");
        setSize(600, 400);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(DISPOSE_ON_CLOSE);
        initComponents();
    }


    private void initComponents() {
        JPanel panel = new JPanel(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(5, 5, 5, 5);
        gbc.anchor = GridBagConstraints.WEST;

        JLabel titleLabel = new JLabel("Patient Registration");
        titleLabel.setFont(new Font("Arial", Font.BOLD, 16));
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.gridwidth = 2;
        panel.add(titleLabel, gbc);

        gbc.gridwidth = 1;
        gbc.gridy = 1;
        panel.add(new JLabel("Full Name:"), gbc);
        gbc.gridx = 1;
        fullNameField = new JTextField(20);
        panel.add(fullNameField, gbc);

        gbc.gridx = 0;
        gbc.gridy = 2;
        panel.add(new JLabel("Date of Birth (YYYY-MM-DD):"), gbc);
        gbc.gridx = 1;
        dobField = new JTextField(20);
        panel.add(dobField, gbc);

        gbc.gridx = 0;
        gbc.gridy = 3;
        panel.add(new JLabel("Gender:"), gbc);
        gbc.gridx = 1;
        genderField = new JTextField(20);
        panel.add(genderField, gbc);

        gbc.gridx = 0;
        gbc.gridy = 4;
        panel.add(new JLabel("Contact:"), gbc);
        gbc.gridx = 1;
        contactField = new JTextField(20);
        panel.add(contactField, gbc);

        gbc.gridx = 0;
        gbc.gridy = 5;
        panel.add(new JLabel("Address:"), gbc);
        gbc.gridx = 1;
        addressField = new JTextField(20);
        panel.add(addressField, gbc);

        gbc.gridx = 0;
        gbc.gridy = 6;
        panel.add(new JLabel("Temporary Password:"), gbc);
        gbc.gridx = 1;
        passwordField = new JPasswordField(20);
        panel.add(passwordField, gbc);

        gbc.gridx = 0;
        gbc.gridy = 7;
        gbc.gridwidth = 2;
        JButton saveButton = new JButton("Register Patient");
        saveButton.addActionListener(this::registerPatient);
        panel.add(saveButton, gbc);

        gbc.gridy = 8;
        JButton logoutButton = new JButton("Logout");
        logoutButton.addActionListener(e -> logout());
        panel.add(logoutButton, gbc);

        add(panel);
    }

    private void logout() {
        this.dispose(); // Close the registration panel
        new LoginFrame().setVisible(true); // Open login window
    }

    private void registerPatient(ActionEvent e) {
        String name = fullNameField.getText().trim();
        try {
            Connection conn = DbHandler.getConnection();
            conn.setAutoCommit(false);


            String userSql = "INSERT INTO users (username, hash_pw, role, default_pwd) VALUES (?, ?, 'PATIENT', 1)";
            PreparedStatement userStmt = conn.prepareStatement(userSql, Statement.RETURN_GENERATED_KEYS);

            String username = name.toLowerCase().replaceAll("\\s+", "");
            String password = new String(passwordField.getPassword());

            userStmt.setString(1, username);
            userStmt.setString(2, password);
            userStmt.executeUpdate();

            ResultSet rs = userStmt.getGeneratedKeys();
            int userId = 0;
            if (rs.next()) {
                userId = rs.getInt(1);
            }

            //  create patient record
            String patientSql = "INSERT INTO patients (user_id, full_name, dob, gender, contact, address) " +
                    "VALUES (?, ?, ?, ?, ?, ?)";
            PreparedStatement patientStmt = conn.prepareStatement(patientSql);
            patientStmt.setInt(1, userId);
            patientStmt.setString(2, fullNameField.getText());
            patientStmt.setDate(3, Date.valueOf(dobField.getText()));
            patientStmt.setString(4, genderField.getText());
            patientStmt.setString(5, contactField.getText());
            patientStmt.setString(6, addressField.getText());
            patientStmt.executeUpdate();



            conn.commit();
            JOptionPane.showMessageDialog(this, "Patient registered successfully!\nTemporary username: " + username);
            dispose();
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this, "Error: " + ex.getMessage());
            ex.printStackTrace();

        }
        DoctorDashboard.refreshPatientsTable();

    }

}