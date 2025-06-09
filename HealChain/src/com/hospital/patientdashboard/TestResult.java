package com.hospital.patientdashboard;

import java.util.Date;

public class TestResult {
    private int testId;
    private int encounterId;
    private String testName;
    private Date testDate;
    private String resultValue;
    private String resultUnit;
    private String notes;

    // Constructors
    public TestResult() {}

    public TestResult(int testId, int encounterId, String testName, Date testDate,
                      String resultValue, String resultUnit, String notes) {
        this.testId = testId;
        this.encounterId = encounterId;
        this.testName = testName;
        this.testDate = testDate;
        this.resultValue = resultValue;
        this.resultUnit = resultUnit;
        this.notes = notes;
    }

    // Getters and Setters
    public int getTestId() {
        return testId;
    }

    public void setTestId(int testId) {
        this.testId = testId;
    }

    public int getEncounterId() {
        return encounterId;
    }

    public void setEncounterId(int encounterId) {
        this.encounterId = encounterId;
    }

    public String getTestName() {
        return testName;
    }

    public void setTestName(String testName) {
        this.testName = testName;
    }

    public Date getTestDate() {
        return testDate;
    }

    public void setTestDate(Date testDate) {
        this.testDate = testDate;
    }

    public String getResultValue() {
        return resultValue;
    }

    public void setResultValue(String resultValue) {
        this.resultValue = resultValue;
    }

    public String getResultUnit() {
        return resultUnit;
    }

    public void setResultUnit(String resultUnit) {
        this.resultUnit = resultUnit;
    }

    public String getNotes() {
        return notes;
    }

    public void setNotes(String notes) {
        this.notes = notes;
    }
}