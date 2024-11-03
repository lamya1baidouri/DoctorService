package com.service.service.model;

import java.util.List;

public class DoctorUpdateDTO {
    private String username;
    private List<String> nurseIds;
    private List<String> patientIds;

    // Getters et setters
    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public List<String> getNurseIds() {
        return nurseIds;
    }

    public void setNurseIds(List<String> nurseIds) {
        this.nurseIds = nurseIds;
    }

    public List<String> getPatientIds() {
        return patientIds;
    }

    public void setPatientIds(List<String> patientIds) {
        this.patientIds = patientIds;
    }
}
