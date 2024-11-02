package com.service.service.service;

import com.service.service.model.Doctor;


import com.service.service.model.DoctorRequestDTO;
import com.service.service.repository.DoctorRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.jupiter.MockitoExtension;


import java.util.Arrays;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
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
    @Test
    public void testCreateDoctor_Success() {

        DoctorRequestDTO doctorRequestDTO = new DoctorRequestDTO();
        doctorRequestDTO.setUsername("Dr. Marie Curie");
        doctorRequestDTO.setPhoneNumber("0987654321");
        doctorRequestDTO.setHospital("Hôpital Sainte-Marie");
        doctorRequestDTO.setEmail("marie.curie@example.com");
        doctorRequestDTO.setPassword("securepassword");
        doctorRequestDTO.setPatientIds(Arrays.asList("patient1", "patient2"));
        doctorRequestDTO.setNurseIds(Arrays.asList("nurse1", "nurse2"));

        // Création de l'objet Doctor attendu après sauvegarde
        Doctor savedDoctor = new Doctor();
        savedDoctor.setId("doctor789");
        savedDoctor.setName("Dr. Marie Curie");
        savedDoctor.setPhoneNumber("0987654321");
        savedDoctor.setHospital("Hôpital Sainte-Marie");
        savedDoctor.setEmail("marie.curie@example.com");
        savedDoctor.setPassword("securepassword");
        savedDoctor.setPatientIds(Arrays.asList("patient1", "patient2"));
        savedDoctor.setNurseIds(Arrays.asList("nurse1", "nurse2"));

        // Configuration du mock pour renvoyer l'objet Doctor sauvegardé
        when(doctorRepository.save(any(Doctor.class))).thenReturn(savedDoctor);

        // Appel de la méthode à tester
        Doctor result = medecinService.createDoctor(doctorRequestDTO);

        // Vérifications
        assertNotNull(result, "Le médecin créé ne doit pas être null");
        assertEquals("doctor789", result.getId(), "L'ID du médecin doit correspondre");
        assertEquals("Dr. Marie Curie", result.getName(), "Le nom du médecin doit correspondre");
        assertEquals("0987654321", result.getPhoneNumber(), "Le numéro de téléphone doit correspondre");
        assertEquals("Hôpital Sainte-Marie", result.getHospital(), "L'hôpital doit correspondre");
        assertEquals("marie.curie@example.com", result.getEmail(), "L'email doit correspondre");
        assertEquals("securepassword", result.getPassword(), "Le mot de passe doit correspondre");
        assertEquals(2, result.getPatientIds().size(), "Le nombre de patients doit correspondre");
        assertTrue(result.getPatientIds().contains("patient1") && result.getPatientIds().contains("patient2"),
                "Les IDs des patients doivent correspondre");
        assertEquals(2, result.getNurseIds().size(), "Le nombre d'infirmières doit correspondre");
        assertTrue(result.getNurseIds().contains("nurse1") && result.getNurseIds().contains("nurse2"),
                "Les IDs des infirmières doivent correspondre");

        // Vérifier que la méthode save a été appelée une fois
        verify(doctorRepository, times(1)).save(any(Doctor.class));
    }
}
