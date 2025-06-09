package com.hospital.patientdashboard;

import javax.swing.*;
import java.awt.*;
import java.sql.SQLException;
import java.util.List;

import static com.hospital.patientdashboard.DBConnection.getConnection;

public class PatientDashboard {
    // Constructor establishes a DB connection – do *not* remove
    public PatientDashboard() throws SQLException {
        getConnection();
    }


    private static Patient currentPatient;

    public static void main(String[] args) throws SQLException {
        // Launch the login UI first
        showLoginScreen();
        new PatientDashboard();
    }


    private static void showLoginScreen() {
        JFrame loginFrame = new JFrame("Patient Login");
        loginFrame.setSize(800, 500);
        loginFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        loginFrame.setLayout(new BorderLayout());

        /* ---------- Left branding panel ---------- */
        JPanel leftPanel = new JPanel(new GridBagLayout());
        leftPanel.setPreferredSize(new Dimension(350, 500));
        leftPanel.setBackground(new Color(137, 207, 240)); // #89CFF0

        GridBagConstraints leftGbc = new GridBagConstraints();
        leftGbc.insets = new Insets(10, 10, 10, 10);
        leftGbc.anchor = GridBagConstraints.CENTER;

        JLabel logoLabel = new JLabel("HEALCHAIN");
        logoLabel.setFont(new Font("Arial", Font.BOLD, 36));
        logoLabel.setForeground(Color.WHITE);
        leftPanel.add(logoLabel, leftGbc);

        /* ---------- Right form panel ---------- */
        JPanel formPanel = new JPanel(new GridBagLayout());
        formPanel.setBackground(Color.WHITE);
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(10, 10, 10, 10);

        JLabel titleLabel = new JLabel("Patient Portal Login");
        titleLabel.setFont(new Font("Arial", Font.BOLD, 24));
        titleLabel.setForeground(new Color(137, 207, 240));
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.gridwidth = 2;
        formPanel.add(titleLabel, gbc);

        JLabel userLabel = new JLabel("Username:");
        gbc.gridx = 0;
        gbc.gridy = 1;
        gbc.gridwidth = 1;
        formPanel.add(userLabel, gbc);

        JTextField userText = new JTextField(15);
        gbc.gridx = 1;
        formPanel.add(userText, gbc);

        JLabel passwordLabel = new JLabel("Password:");
        gbc.gridx = 0;
        gbc.gridy = 2;
        formPanel.add(passwordLabel, gbc);

        JPasswordField passwordText = new JPasswordField(15);
        gbc.gridx = 1;
        formPanel.add(passwordText, gbc);

        JButton loginButton = new JButton("Login");
        loginButton.setPreferredSize(new Dimension(130, 40));
        loginButton.setBackground(new Color(137, 207, 240));
        loginButton.setForeground(Color.WHITE);
        loginButton.setFont(new Font("Arial", Font.BOLD, 14));
        loginButton.setFocusPainted(false);
        loginButton.setBorderPainted(false);
        gbc.gridx = 0;
        gbc.gridy = 3;
        gbc.gridwidth = 2;
        formPanel.add(loginButton, gbc);

        // ** Keep the original authentication logic intact **
        loginButton.addActionListener(e -> {
            try {
                int userId = AuthService.authenticate(
                        userText.getText(),
                        new String(passwordText.getPassword())
                );

                if (userId > 0) {
                    currentPatient = PatientDAO.getPatientByUserId(userId);
                    if (currentPatient != null) {
                        loginFrame.dispose();
                        showDashboard();
                    }
                } else {
                    JOptionPane.showMessageDialog(loginFrame,
                            "Invalid credentials", "Error", JOptionPane.ERROR_MESSAGE);
                }
            } catch (SQLException ex) {
                ex.printStackTrace();
                JOptionPane.showMessageDialog(loginFrame,
                        "Database error", "Error", JOptionPane.ERROR_MESSAGE);
            }
        });

        loginFrame.add(leftPanel, BorderLayout.WEST);
        loginFrame.add(formPanel, BorderLayout.CENTER);

        loginFrame.setLocationRelativeTo(null);
        loginFrame.setVisible(true);
    }


    private static void showDashboard() {
        JFrame dashboardFrame = new JFrame("Patient Dashboard");
        dashboardFrame.setSize(900, 700);
        dashboardFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        dashboardFrame.setLayout(new BorderLayout());

        /* ---------- Header ---------- */
        JPanel headerPanel = new JPanel(new BorderLayout());
        headerPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        headerPanel.setBackground(new Color(137, 207, 240));

        JLabel welcomeLabel = new JLabel("Welcome, " + currentPatient.getFullName());
        welcomeLabel.setFont(new Font("Arial", Font.BOLD, 20));
        welcomeLabel.setForeground(Color.WHITE);
        headerPanel.add(welcomeLabel, BorderLayout.WEST);

        JButton logoutBtn = new JButton("Logout");
        logoutBtn.setPreferredSize(new Dimension(100, 30));
        logoutBtn.setBackground(new Color(200, 0, 0)); // Red color for logout
        logoutBtn.setForeground(Color.WHITE);
        logoutBtn.setFocusPainted(false);
        logoutBtn.setBorderPainted(false);
        logoutBtn.addActionListener(e -> {
            dashboardFrame.dispose();
            showLoginScreen();
        });

        JPanel logoutPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        logoutPanel.setOpaque(false);
        logoutPanel.add(logoutBtn);
        headerPanel.add(logoutPanel, BorderLayout.EAST);

        dashboardFrame.add(headerPanel, BorderLayout.NORTH);

        dashboardFrame.add(headerPanel, BorderLayout.NORTH);

        /* ---------- Main area (menu + content) ---------- */
        JPanel mainPanel = new JPanel(new BorderLayout());

        // --- Left menu ---
        JPanel menuPanel = new JPanel();
        menuPanel.setLayout(new BoxLayout(menuPanel, BoxLayout.Y_AXIS));
        menuPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        menuPanel.setPreferredSize(new Dimension(200, 0));

        JButton profileBtn = new JButton("Profile Info");
        JButton diagnosisBtn = new JButton("Diagnosis");
        JButton medicationBtn = new JButton("Medication");
        JButton testResultsBtn = new JButton("Test Results");

        for (JButton btn : new JButton[]{profileBtn, diagnosisBtn, medicationBtn, testResultsBtn}) {
            btn.setAlignmentX(Component.LEFT_ALIGNMENT);
            btn.setMaximumSize(new Dimension(180, 40));
            btn.setMargin(new Insets(10, 10, 10, 10));
        }

        menuPanel.add(Box.createVerticalStrut(20));
        menuPanel.add(profileBtn);
        menuPanel.add(Box.createVerticalStrut(10));
        menuPanel.add(diagnosisBtn);
        menuPanel.add(Box.createVerticalStrut(10));
        menuPanel.add(medicationBtn);
        menuPanel.add(Box.createVerticalStrut(10));
        menuPanel.add(testResultsBtn);

        // --- Content area ---
        JPanel profilePanel = createProfilePanel();
        JPanel contentPanel = new JPanel(new BorderLayout());
        contentPanel.add(profilePanel, BorderLayout.NORTH);

        JPanel tablePanel = new JPanel(new BorderLayout());
        JTable dataTable = new JTable();
        JScrollPane scrollPane = new JScrollPane(dataTable);
        tablePanel.add(scrollPane, BorderLayout.CENTER);
        contentPanel.add(tablePanel, BorderLayout.CENTER);


        /* ---------- Button wiring ---------- */
        profileBtn.addActionListener(e -> {
            contentPanel.removeAll();
            contentPanel.add(profilePanel, BorderLayout.NORTH);
            contentPanel.add(new JPanel(), BorderLayout.CENTER); // Empty spacer
            contentPanel.revalidate();
            contentPanel.repaint();
        });

        diagnosisBtn.addActionListener(e -> {
            updateTable(dataTable, "Diagnosis");
            contentPanel.removeAll();
            contentPanel.add(tablePanel, BorderLayout.CENTER);
            contentPanel.revalidate();
            contentPanel.repaint();
        });

        medicationBtn.addActionListener(e -> {
            updateTable(dataTable, "Medication");
            contentPanel.removeAll();
            contentPanel.add(tablePanel, BorderLayout.CENTER);
            contentPanel.revalidate();
            contentPanel.repaint();
        });

        testResultsBtn.addActionListener(e -> {
            updateTable(dataTable, "Test Results");
            contentPanel.removeAll();
            contentPanel.add(tablePanel, BorderLayout.CENTER);
            contentPanel.revalidate();
            contentPanel.repaint();
        });

        mainPanel.add(menuPanel, BorderLayout.WEST);
        mainPanel.add(contentPanel, BorderLayout.CENTER);

        dashboardFrame.add(mainPanel, BorderLayout.CENTER);
        dashboardFrame.setLocationRelativeTo(null);
        dashboardFrame.setVisible(true);
    }


    private static JPanel createProfilePanel() {
        JPanel panel = new JPanel(new GridLayout(0, 2, 10, 5));
        panel.setBorder(BorderFactory.createTitledBorder("Profile Information"));
        panel.setPreferredSize(new Dimension(0, 150));

        panel.add(new JLabel("Patient ID:"));
        panel.add(new JLabel(String.valueOf(currentPatient.getPatientId())));

        panel.add(new JLabel("Full Name:"));
        panel.add(new JLabel(currentPatient.getFullName()));

        panel.add(new JLabel("Date of Birth:"));
        panel.add(new JLabel(currentPatient.getDob()));

        panel.add(new JLabel("Gender:"));
        panel.add(new JLabel(currentPatient.getGender()));

        panel.add(new JLabel("Contact:"));
        panel.add(new JLabel(currentPatient.getContact()));

        panel.add(new JLabel("Address:"));
        panel.add(new JLabel(currentPatient.getAddress()));

        return panel;
    }

    private static void updateTable(JTable table, String viewType) {
        String[] columnNames;
        Object[][] data;

        try {
            switch (viewType) {
                case "Diagnosis":
                    List<Diagnosis> diagnoses = MedicalDataService.getPatientDiagnoses(currentPatient.getPatientId());
                    columnNames = new String[]{"Diagnosis ID", "Encounter Date", "Diagnosis Name", "Description"};
                    data = new Object[diagnoses.size()][4];

                    for (int i = 0; i < diagnoses.size(); i++) {
                        Diagnosis d = diagnoses.get(i);
                        MedicalEncounter encounter = MedicalEncounterDAO.getEncounterById(d.getEncounterId());
                        data[i] = new Object[]{
                                d.getDiagnosisId(),
                                encounter.getEncounterDate(),
                                d.getDiagnosisName(),
                                d.getDescription()
                        };
                    }
                    break;

                case "Medication":
                    List<Medication> medications = MedicalDataService.getPatientMedications(currentPatient.getPatientId());
                    columnNames = new String[]{"Medication ID", "Medication Name", "Dosage", "Instructions"};
                    data = new Object[medications.size()][4];

                    for (int i = 0; i < medications.size(); i++) {
                        Medication m = medications.get(i);
                        data[i] = new Object[]{
                                m.getMedicationId(),
                                m.getMedicationName(),
                                m.getDosage(),
                                m.getInstructions()
                        };
                    }
                    break;

                case "Test Results":
                    List<TestResult> testResults = MedicalDataService.getPatientTestResults(currentPatient.getPatientId());
                    columnNames = new String[]{"Test ID", "Test Name", "Result", "Date"};
                    data = new Object[testResults.size()][4];

                    for (int i = 0; i < testResults.size(); i++) {
                        TestResult t = testResults.get(i);
                        data[i] = new Object[]{
                                t.getTestId(),
                                t.getTestName(),
                                t.getResultValue() + " " + t.getResultUnit(),
                                t.getTestDate()
                        };
                    }
                    break;

                default:
                    columnNames = new String[]{};
                    data = new Object[][]{};
            }

            table.setModel(new javax.swing.table.DefaultTableModel(data, columnNames) {
                @Override
                public boolean isCellEditable(int row, int column) {
                    return false; // Keep table non-editable
                }
            });
        } catch (SQLException ex) {
            ex.printStackTrace();
            JOptionPane.showMessageDialog(null, "Error loading data: " + ex.getMessage(),
                    "Database Error", JOptionPane.ERROR_MESSAGE);
        }
    }
}