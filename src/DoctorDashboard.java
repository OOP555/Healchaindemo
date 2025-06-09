import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.sql.*;
import java.util.Vector;
import java.awt.event.ActionListener;
import org.mindrot.jbcrypt.BCrypt;

public class DoctorDashboard extends JFrame {
    private JList<String> patientList;
    private DefaultListModel<String> listModel;
    private JTable infoTable;
    private DefaultTableModel tableModel;
    private JTextField newDiagnosisField, newMedField, newTestField;
    private int doctorId;
    private int selectedPatientId = -1;

    private static DoctorDashboard instance;


    public DoctorDashboard(int doctorId) {
        instance = this;
        this.doctorId = doctorId;
        setTitle("Doctor Dashboard");
        setSize(1000, 700);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        initComponents();
        loadPatientsFromDB();
        setVisible(true);
    }

    public static void refreshPatientsTable() {
        if (instance != null) {
            instance.loadPatientsFromDB(); // Reload data
        }
    }

    private void initComponents() {
        JSplitPane splitPane = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT);
        splitPane.setDividerLocation(300);
        splitPane.setBorder(new EmptyBorder(10, 10, 10, 10));

        JPanel leftPanel = new JPanel(new BorderLayout());
        leftPanel.setBorder(BorderFactory.createTitledBorder("Patients"));
        listModel = new DefaultListModel<>();
        patientList = new JList<>(listModel);
        patientList.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        patientList.addListSelectionListener(e -> {
            if (!e.getValueIsAdjusting()) {
                String selectedValue = patientList.getSelectedValue();
                if (selectedValue != null) {
                    selectedPatientId = Integer.parseInt(selectedValue.split(" - ")[0]);
                    loadPatientDetails(selectedPatientId);
                }
            }
        });
        leftPanel.add(new JScrollPane(patientList), BorderLayout.CENTER);

        JButton logoutButton = new JButton("Logout");
        logoutButton.addActionListener(e -> logout());
        leftPanel.add(logoutButton, BorderLayout.SOUTH);

        JPanel rightPanel = new JPanel(new BorderLayout());
        tableModel = new DefaultTableModel(new String[]{"Type", "Details"}, 0);
        infoTable = new JTable(tableModel);
        rightPanel.add(new JScrollPane(infoTable), BorderLayout.CENTER);

        JPanel formPanel = new JPanel(new GridLayout(3, 1, 5, 5));
        newDiagnosisField = new JTextField();
        formPanel.add(createAddPanel("Add Diagnosis:", newDiagnosisField, this::addDiagnosis));
        newMedField = new JTextField();
        formPanel.add(createAddPanel("Add Medication:", newMedField, this::addMedication));
        newTestField = new JTextField();
        formPanel.add(createAddPanel("Add Lab Result:", newTestField, this::addTestResult));
        rightPanel.add(formPanel, BorderLayout.SOUTH);

        splitPane.setLeftComponent(leftPanel);
        splitPane.setRightComponent(rightPanel);
        add(splitPane);
    }

    private void logout() {
        this.dispose(); // Close the dashboard
        new LoginFrame().setVisible(true); // Open login window
    }

    private JPanel createAddPanel(String label, JTextField field, ActionListener action) {
        JPanel panel = new JPanel(new BorderLayout(5, 5));
        panel.add(new JLabel(label), BorderLayout.WEST);
        panel.add(field, BorderLayout.CENTER);
        JButton addButton = new JButton("Add");
        addButton.addActionListener(action);
        panel.add(addButton, BorderLayout.EAST);
        return panel;
    }

    private void loadPatientsFromDB() {
        listModel.clear();
        try (Connection conn = DbHandler.getConnection();
             PreparedStatement stmt = conn.prepareStatement(
                     "SELECT p.patient_id, p.full_name FROM patients p")) {
            ResultSet rs = stmt.executeQuery();
            while (rs.next()) {
                listModel.addElement(rs.getInt("patient_id") + " - " + rs.getString("full_name"));
            }
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(this, "Error loading patients: " + e.getMessage());
        }
    }

    private void loadPatientDetails(int patientId) {
        tableModel.setRowCount(0);
        try (Connection conn = DbHandler.getConnection()) {
            // Load diagnoses (all doctors)
            PreparedStatement diagStmt = conn.prepareStatement(
                    "SELECT d.diagnosis_name, d.description, me.encounter_date, doc.full_name AS doctor_name " +
                            "FROM diagnoses d " +
                            "JOIN medical_encounters me ON d.encounter_id = me.encounter_id " +
                            "JOIN doctors doc ON me.doctor_id = doc.doctor_id " +
                            "WHERE me.patient_id = ? " +
                            "ORDER BY me.encounter_date DESC");
            diagStmt.setInt(1, patientId);
            ResultSet diagRs = diagStmt.executeQuery();
            while (diagRs.next()) {
                tableModel.addRow(new Object[]{"Diagnosis",
                        diagRs.getString("diagnosis_name") + ": " + diagRs.getString("description") +
                                " (Doctor: " + diagRs.getString("doctor_name") +
                                ", Date: " + diagRs.getDate("encounter_date") + ")"});
            }

            // Load medications
            PreparedStatement medStmt = conn.prepareStatement(
                    "SELECT m.medication_name, m.dosage, m.frequency, me.encounter_date, doc.full_name AS doctor_name " +
                            "FROM medications m " +
                            "JOIN medical_encounters me ON m.encounter_id = me.encounter_id " +
                            "JOIN doctors doc ON me.doctor_id = doc.doctor_id " +
                            "WHERE me.patient_id = ? " +
                            "ORDER BY me.encounter_date DESC");
            medStmt.setInt(1, patientId);
            ResultSet medRs = medStmt.executeQuery();
            while (medRs.next()) {
                tableModel.addRow(new Object[]{"Medication",
                        medRs.getString("medication_name") + " - " + medRs.getString("dosage") +
                                " (" + medRs.getString("frequency") + ")" +
                                " (Doctor: " + medRs.getString("doctor_name") +
                                ", Date: " + medRs.getDate("encounter_date") + ")"});
            }

            // Load test results
            PreparedStatement testStmt = conn.prepareStatement(
                    "SELECT t.test_name, t.result_value, t.result_unit, me.encounter_date, doc.full_name AS doctor_name " +
                            "FROM test_results t " +
                            "JOIN medical_encounters me ON t.encounter_id = me.encounter_id " +
                            "JOIN doctors doc ON me.doctor_id = doc.doctor_id " +
                            "WHERE me.patient_id = ? " +
                            "ORDER BY me.encounter_date DESC");
            testStmt.setInt(1, patientId);
            ResultSet testRs = testStmt.executeQuery();
            while (testRs.next()) {
                tableModel.addRow(new Object[]{"Test Result",
                        testRs.getString("test_name") + ": " + testRs.getString("result_value") +
                                " " + testRs.getString("result_unit") +
                                " (Doctor: " + testRs.getString("doctor_name") +
                                ", Date: " + testRs.getDate("encounter_date") + ")"});
            }
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(this, "Error loading patient details: " + e.getMessage());
        }
    }

    private void addDiagnosis(ActionEvent e) {
        addMedicalRecord("diagnoses",
                "INSERT INTO diagnoses (encounter_id, diagnosis_name, description) VALUES (?, ?, ?)",
                newDiagnosisField.getText());
    }

    private void addMedication(ActionEvent e) {
        addMedicalRecord("medications",
                "INSERT INTO medications (encounter_id, medication_name, dosage, frequency) VALUES (?, ?, ?, ?)",
                newMedField.getText());
    }

    private void addTestResult(ActionEvent e) {
        addMedicalRecord("test_results",
                "INSERT INTO test_results (encounter_id, test_name, result_value, result_unit) VALUES (?, ?, ?, ?)",
                newTestField.getText());
    }

    private void addMedicalRecord(String tableName, String sql, String value) {
        if (selectedPatientId == -1) {
            JOptionPane.showMessageDialog(this, "Please select a patient first.");
            return;
        }
        if (value.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Please enter a value.");
            return;
        }

        try (Connection conn = DbHandler.getConnection()) {

            int encounterId = getOrCreateEncounter(conn);


            PreparedStatement stmt = conn.prepareStatement(sql);
            stmt.setInt(1, encounterId);

            if (tableName.equals("diagnoses")) {
                stmt.setString(2, value);
                stmt.setString(3, "");
            } else if (tableName.equals("medications")) {
                String[] parts = value.split(",");
                stmt.setString(2, parts[0].trim());
                stmt.setString(3, parts.length > 1 ? parts[1].trim() : "");
                stmt.setString(4, parts.length > 2 ? parts[2].trim() : "");
            } else if (tableName.equals("test_results")) {
                String[] parts = value.split(",");
                stmt.setString(2, parts[0].trim());
                stmt.setString(3, parts.length > 1 ? parts[1].trim() : "");
                stmt.setString(4, parts.length > 2 ? parts[2].trim() : "");
            }

            stmt.executeUpdate();
            loadPatientDetails(selectedPatientId);
            JOptionPane.showMessageDialog(this, "Record added successfully");

            if (tableName.equals("diagnoses")) newDiagnosisField.setText("");
            else if (tableName.equals("medications")) newMedField.setText("");
            else if (tableName.equals("test_results")) newTestField.setText("");
        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(this, "Error adding record: " + ex.getMessage());
        }
    }

    private int getOrCreateEncounter(Connection conn) throws SQLException {

        String findSql = "SELECT encounter_id FROM medical_encounters " +
                "WHERE patient_id = ? AND doctor_id = ? AND encounter_date = CURRENT_DATE()";
        PreparedStatement findStmt = conn.prepareStatement(findSql);
        findStmt.setInt(1, selectedPatientId);
        findStmt.setInt(2, doctorId);
        ResultSet rs = findStmt.executeQuery();

        if (rs.next()) {
            return rs.getInt("encounter_id");
        }

        // Create new encounter if none exists
        String createSql = "INSERT INTO medical_encounters (patient_id, doctor_id, encounter_date) " +
                "VALUES (?, ?, CURRENT_DATE())";
        PreparedStatement createStmt = conn.prepareStatement(createSql, Statement.RETURN_GENERATED_KEYS);
        createStmt.setInt(1, selectedPatientId);
        createStmt.setInt(2, doctorId);
        createStmt.executeUpdate();

        ResultSet keys = createStmt.getGeneratedKeys();
        if (keys.next()) {
            return keys.getInt(1);
        }

        throw new SQLException("Failed to create encounter");
    }
}