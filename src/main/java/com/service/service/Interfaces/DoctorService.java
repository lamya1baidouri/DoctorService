package com.service.service.Interfaces;

import com.service.service.model.Doctor;
import com.service.service.model.DoctorRequestDTO;

public interface DoctorService {


    void transferPatientToOtherDoctor(String patientId, String doctorId);

    void assignNurseToDoctor(String doctorId, String nurseId);

    void removeNurseFromDoctor(String doctorId, String nurseId);

    void sendMessageToPatient(String patientId, String message);

    void processNotifications();
    Doctor createDoctor(DoctorRequestDTO doctorRequestDTO);
}
