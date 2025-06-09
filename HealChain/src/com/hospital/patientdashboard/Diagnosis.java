package com.hospital.patientdashboard;

public class Diagnosis {
    private int diagnosisId;
    private int encounterId;
    private String diagnosisCode;
    private String diagnosisName;
    private String description;


    public Diagnosis(int diagnosisId, int encounterId, String diagnosisCode,
                     String diagnosisName, String description) {
        this.diagnosisId = diagnosisId;
        this.encounterId = encounterId;
        this.diagnosisCode = diagnosisCode;
        this.diagnosisName = diagnosisName;
        this.description = description;
    }

    // Getters and Setters
    public int getDiagnosisId() {
        return diagnosisId;
    }

    public void setDiagnosisId(int diagnosisId) {
        this.diagnosisId = diagnosisId;
    }

    public int getEncounterId() {
        return encounterId;
    }

    public void setEncounterId(int encounterId) {
        this.encounterId = encounterId;
    }

    public String getDiagnosisCode() {
        return diagnosisCode;
    }

    public void setDiagnosisCode(String diagnosisCode) {
        this.diagnosisCode = diagnosisCode;
    }

    public String getDiagnosisName() {
        return diagnosisName;
    }

    public void setDiagnosisName(String diagnosisName) {
        this.diagnosisName = diagnosisName;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }
}