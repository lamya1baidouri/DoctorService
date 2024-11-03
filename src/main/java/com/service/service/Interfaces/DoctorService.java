package com.service.service.Interfaces;

import com.service.service.model.Doctor;
import com.service.service.model.DoctorAuthResponse;
import com.service.service.model.DoctorRequestDTO;
import com.service.service.model.DoctorUpdateDTO;

public interface DoctorService {


    void transferPatientToOtherDoctor(String patientId, String doctorId);

    void assignNurseToDoctor(String doctorId, String nurseId);

    void removeNurseFromDoctor(String doctorId, String nurseId);

    void sendMessageToPatient(String patientId, String message);

    void processNotifications();
    Doctor createDoctor(DoctorRequestDTO doctorRequestDTO);

    // Partie ajoutée pour l'authentification du médecin
    DoctorAuthResponse findDoctorByEmail(String email);

    // Partie ajoutée pour la mise à jour des informations d'un médecin
    void updateDoctor(Long userId, DoctorUpdateDTO doctorUpdateDTO);
}
