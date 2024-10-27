package com.service.service.Interfaces;

public interface DoctorService {


    void transferPatientToOtherDoctor(String patientId, String doctorId);

    void assignNurseToDoctor(String doctorId, String nurseId);

    void removeNurseFromDoctor(String doctorId, String nurseId);

    void sendMessageToPatient(String patientId, String message);

    void processNotifications();
}
