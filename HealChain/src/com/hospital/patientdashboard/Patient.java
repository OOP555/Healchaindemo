package com.hospital.patientdashboard;

public class Patient {
    private int patientId;
    private int userId;
    private String fullName;
    private String dob;
    private String gender;
    private String contact;
    private String address;

    // Constructors
    public Patient() {}

    public Patient(int patientId, int userId, String fullName, String dob,
                   String gender, String contact, String address) {
        this.patientId = patientId;
        this.userId = userId;
        this.fullName = fullName;
        this.dob = dob;
        this.gender = gender;
        this.contact = contact;
        this.address = address;
    }

    public int getPatientId() {
        return patientId;
    }

    public void setPatientId(int patientId) {
        this.patientId = patientId;
    }

    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }

    public String getFullName() {
        return fullName;
    }

    public void setFullName(String fullName) {
        this.fullName = fullName;
    }

    public String getDob() {
        return dob;
    }

    public void setDob(String dob) {
        this.dob = dob;
    }

    public String getGender() {
        return gender;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    public String getContact() {
        return contact;
    }

    public void setContact(String contact) {
        this.contact = contact;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }
}
