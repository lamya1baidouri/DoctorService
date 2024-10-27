package com.service.service.service;


import com.service.service.Interfaces.DoctorService;
import com.service.service.model.Doctor;
import com.service.service.repository.DoctorRepository;
import org.springframework.beans.factory.annotation.Autowired;
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
}
