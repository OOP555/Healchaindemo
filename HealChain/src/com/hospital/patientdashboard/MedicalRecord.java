package com.hospital.patientdashboard;

class MedicalRecord {
    String recordId;
    String date;
    String hospital;
    String doctor;
    String diagnosis;
    String prescription;
    String testType;
    String testResult;

    public MedicalRecord(String recordId, String date, String hospital, String doctor,
                         String diagnosis, String prescription, String testType, String testResult) {
        this.recordId = recordId;
        this.date = date;
        this.hospital = hospital;
        this.doctor = doctor;
        this.diagnosis = diagnosis;
        this.prescription = prescription;
        this.testType = testType;
        this.testResult = testResult;
    }
}