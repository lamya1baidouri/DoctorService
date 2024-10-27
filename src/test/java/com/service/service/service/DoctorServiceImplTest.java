package com.service.service.service;

import com.service.service.model.Doctor;


import com.service.service.repository.DoctorRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.jupiter.MockitoExtension;


import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class DoctorServiceImplTest {

    @Mock
    private DoctorRepository doctorRepository;

    @Mock
    private CommServiceClient commServiceClient;

    @InjectMocks
    private DoctorServiceImpl medecinService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    public void testTransferPatientToOtherDoctor() {
        String patientId = "123";
        String doctorId = "456";
        Doctor currentDoctor = new Doctor();
        currentDoctor.setId("001");
        currentDoctor.setPhoneNumber("1234567890");
        currentDoctor.setHospital("General Hospital");
        currentDoctor.setEmail("doctor1@example.com");
        currentDoctor.setPassword("password123");
        currentDoctor.addPatient(patientId);

        Doctor newDoctor = new Doctor();
        newDoctor.setId(doctorId);
        newDoctor.setPhoneNumber("0987654321");
        newDoctor.setHospital("Central Hospital");
        newDoctor.setEmail("doctor2@example.com");
        newDoctor.setPassword("securepass");

        when(doctorRepository.findDoctorByPatientIdsContaining(patientId)).thenReturn(currentDoctor);
        when(doctorRepository.findById(doctorId)).thenReturn(Optional.of(newDoctor));

        medecinService.transferPatientToOtherDoctor(patientId, doctorId);

        verify(doctorRepository, times(1)).save(currentDoctor);
        verify(doctorRepository, times(1)).save(newDoctor);
        assertEquals(0, currentDoctor.getPatientIds().size());
        assertEquals(1, newDoctor.getPatientIds().size());
        assertEquals(patientId, newDoctor.getPatientIds().get(0));
    }

    @Test
    public void testAssignNurseToDoctor() {
        String doctorId = "456";
        String nurseId = "789";
        Doctor doctor = new Doctor();
        doctor.setId(doctorId);
        doctor.setPhoneNumber("1234567890");
        doctor.setHospital("General Hospital");
        doctor.setEmail("doctor1@example.com");
        doctor.setPassword("password123");

        when(doctorRepository.findById(doctorId)).thenReturn(Optional.of(doctor));

        medecinService.assignNurseToDoctor(doctorId, nurseId);

        verify(doctorRepository, times(1)).save(doctor);
        assertEquals(1, doctor.getNurseIds().size());
        assertEquals(nurseId, doctor.getNurseIds().get(0));
    }

    @Test
    public void testRemoveNurseFromDoctor() {
        String doctorId = "456";
        String nurseId = "789";
        Doctor doctor = new Doctor();
        doctor.setId(doctorId);
        doctor.setPhoneNumber("1234567890");
        doctor.setHospital("General Hospital");
        doctor.setEmail("doctor1@example.com");
        doctor.setPassword("password123");

        doctor.addNurse(nurseId);

        when(doctorRepository.findById(doctorId)).thenReturn(Optional.of(doctor));

        medecinService.removeNurseFromDoctor(doctorId, nurseId);

        verify(doctorRepository, times(1)).save(doctor);
        assertEquals(0, doctor.getNurseIds().size());
    }

    @Test
    public void testSendMessageToPatient() {
        String patientId = "123";
        String message = "Hello, this is a test message.";

        medecinService.sendMessageToPatient(patientId, message);

        verify(commServiceClient, times(1)).sendMessageToPatient(patientId, message);
    }
/**
    @Test
    public void testProcessNotifications() {
        medecinService.processNotifications();

        verify(commServiceClient, times(1)).subscribeToNotifications();
    }**/

    @Test
    public void testTransferPatientToOtherDoctorDoctorNotFound() {
        String patientId = "123";
        String doctorId = "456";
        Doctor currentDoctor = new Doctor();
        currentDoctor.setId("001");
        currentDoctor.addPatient(patientId);

        when(doctorRepository.findDoctorByPatientIdsContaining(patientId)).thenReturn(currentDoctor);
        when(doctorRepository.findById(doctorId)).thenReturn(Optional.empty());

        Exception exception = assertThrows(RuntimeException.class, () -> {
            medecinService.transferPatientToOtherDoctor(patientId, doctorId);
        });

        assertEquals("Médecin non trouvé", exception.getMessage());
    }
}
