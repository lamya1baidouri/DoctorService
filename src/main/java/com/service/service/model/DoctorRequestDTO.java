package com.service.service.model;



import java.util.List;

public class DoctorRequestDTO {

    private String phoneNumber;
    private String hospital;
    private String email;
    private String password;
    private String username;
    private List<String> nurseIds;
    private List<String> patientIds;

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public String getHospital() {
        return hospital;
    }

    public void setHospital(String hospital) {
        this.hospital = hospital;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

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
