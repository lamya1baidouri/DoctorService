package com.service.service.Entities;



import lombok.Getter;
import lombok.Setter;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.List;
@Getter
@Setter
@Document
public class Doctor {

    @Id
    private String id;
    private String name;


    private List<String> patientIds;  // Liste des patients


    private List<String> nurseIds;  // Liste des infirmières assignées

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


}
