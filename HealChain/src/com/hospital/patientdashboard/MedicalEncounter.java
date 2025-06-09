package com.hospital.patientdashboard;// MedicalEncounter.java (new)

import java.util.Date;

public class MedicalEncounter {
    private int encounterId;
    private int patientId;
    private int doctorId;
    private Date encounterDate;
    private String notes;

    public MedicalEncounter(int encounterId, int patientId, int doctorId, java.sql.Date encounterDate, String notes) {
    }

    public MedicalEncounter() {}

    public MedicalEncounter(int encounterId, int patientId, int doctorId,
                            Date encounterDate, String notes) {
        this.encounterId = encounterId;
        this.patientId = patientId;
        this.doctorId = doctorId;
        this.encounterDate = encounterDate;
        this.notes = notes;
    }

    // Getters and Setters
    public int getEncounterId() {
        return encounterId;
    }

    public void setEncounterId(int encounterId) {
        this.encounterId = encounterId;
    }

    public int getPatientId() {
        return patientId;
    }

    public void setPatientId(int patientId) {
        this.patientId = patientId;
    }

    public int getDoctorId() {
        return doctorId;
    }

    public void setDoctorId(int doctorId) {
        this.doctorId = doctorId;
    }

    public Date getEncounterDate() {
        return encounterDate;
    }

    public void setEncounterDate(Date encounterDate) {
        this.encounterDate = encounterDate;
    }

    public String getNotes() {
        return notes;
    }

    public void setNotes(String notes) {
        this.notes = notes;
    }
}