package com.service.service.service;

import com.service.service.Interfaces.DoctorService;
import com.service.service.model.Doctor;
import com.service.service.model.DoctorAuthResponse;
import com.service.service.model.DoctorRequestDTO;
import com.service.service.model.DoctorUpdateDTO;
import com.service.service.repository.DoctorRepository;
import jakarta.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

@Service
public class DoctorServiceImpl implements DoctorService {

    @Autowired
    private DoctorRepository doctorRepository;

    @Autowired
    private CommServiceClient commServiceClient;

    @Override
    public void transferPatientToOtherDoctor(String patientId, String doctorId) {
        Doctor currentDoctor = doctorRepository.findDoctorByPatientIdsContaining(patientId);
        currentDoctor.removePatient(patientId);
        doctorRepository.save(currentDoctor);

        Doctor newDoctor = doctorRepository.findById(doctorId)
                .orElseThrow(() -> new RuntimeException("Médecin non trouvé"));
        newDoctor.addPatient(patientId);
        doctorRepository.save(newDoctor);
    }

    @Override
    public void assignNurseToDoctor(String doctorId, String nurseId) {
        Doctor doctor = doctorRepository.findById(doctorId)
                .orElseThrow(() -> new RuntimeException("Médecin non trouvé"));
        doctor.addNurse(nurseId);
        doctorRepository.save(doctor);
    }

    @Override
    public void removeNurseFromDoctor(String doctorId, String nurseId) {
        Doctor doctor = doctorRepository.findById(doctorId)
                .orElseThrow(() -> new RuntimeException("Médecin non trouvé"));
        doctor.removeNurse(nurseId);
        doctorRepository.save(doctor);
    }

    @Override
    public void sendMessageToPatient(String patientId, String message) {
        commServiceClient.sendMessageToPatient(patientId, message);
    }

    @Override
    public void processNotifications() {
        commServiceClient.subscribeToNotifications();
    }

    @Override
    public Doctor createDoctor(DoctorRequestDTO doctorRequestDTO) {
        Doctor doctor = new Doctor();
        doctor.setName(doctorRequestDTO.getUsername());
        doctor.setPatientIds(doctorRequestDTO.getPatientIds());
        doctor.setNurseIds(doctorRequestDTO.getNurseIds());
        return doctorRepository.save(doctor);
    }

    // Partie ajoutée pour l'authentification du médecin
    @Override
    public DoctorAuthResponse findDoctorByEmail(String email) {
        Doctor doctor = doctorRepository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("Médecin non trouvé"));

        // Construire la réponse d'authentification du médecin
        return new DoctorAuthResponse(
                doctor.getEmail(),
                doctor.getPassword(),
                doctor.getRoles()
        );
    }

    // Partie ajoutée pour la mise à jour des informations d'un médecin
    @Override
    public void updateDoctor(Long userId, DoctorUpdateDTO doctorUpdateDTO) {
        Doctor doctor = doctorRepository.findById(String.valueOf(userId))
                .orElseThrow(() -> new RuntimeException("Médecin non trouvé"));

        // Mise à jour des informations du médecin
        doctor.setName(doctorUpdateDTO.getUsername());
        doctor.setNurseIds(doctorUpdateDTO.getNurseIds());
        doctor.setPatientIds(doctorUpdateDTO.getPatientIds());

        doctorRepository.save(doctor);
    }
}
