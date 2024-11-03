package com.service.service.model;

import lombok.Getter;
import lombok.Setter;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.MongoId;

import java.util.ArrayList;
import java.util.List;

@Document
public class Doctor {


    @MongoId
    private String id;
    private String name;
    private String phoneNumber;
    private String hospital;
    private String email;
    private String password;
    private List<String> roles;
    private List<String> patientIds = new ArrayList<>();


    private List<String> nurseIds = new ArrayList<>();

    public void addPatient(String patientId) {
        this.patientIds.add(patientId);
    }

    public void removePatient(String patientId) {
        this.patientIds.remove(patientId);
    }

    public void addNurse(String nurseId) {
        this.nurseIds.add(nurseId);
    }

    public void removeNurse(String nurseId) {
        this.nurseIds.remove(nurseId);
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

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

    public List<String> getPatientIds() {
        return patientIds;
    }

    public void setPatientIds(List<String> patientIds) {
        this.patientIds = patientIds;
    }

    public List<String> getNurseIds() {
        return nurseIds;
    }

    public void setNurseIds(List<String> nurseIds) {
        this.nurseIds = nurseIds;
    }

    public List<String> getRoles() {
        return roles;
    }
    public  void setRoles(List<String> roles) {
        this.roles = roles;
    }
}
