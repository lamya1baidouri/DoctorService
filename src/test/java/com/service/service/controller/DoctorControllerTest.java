package com.service.service.controller;

import com.service.service.Interfaces.DoctorService;
import com.service.service.config.SecurityConfig; // Importer la classe SecurityConfig
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.MockitoAnnotations;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import; // Importer l'annotation Import
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;

import static org.mockito.Mockito.*;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;

@WebMvcTest(DoctorController.class)
@Import(SecurityConfig.class) // Importer SecurityConfig
public class DoctorControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private DoctorService doctorService;

    @BeforeEach
    public void setup() {
        MockitoAnnotations.openMocks(this);
    }

    /**
     * Test pour transférer un patient à un autre médecin
     */
    @Test
    @WithMockUser(roles = "DOCTOR")
    public void testTransferPatientToOtherDoctor() throws Exception {
        String patientId = "patient123";
        String newDoctorId = "doctor456";

        doNothing().when(doctorService).transferPatientToOtherDoctor(patientId, newDoctorId);

        mockMvc.perform(put("/api/doctors/patients/transfer")
                        .with(csrf()) // Ajout du token CSRF
                        .param("patientId", patientId)
                        .param("newDoctorId", newDoctorId))
                .andDo(print()) // Ajout des logs
                .andExpect(status().isOk());

        verify(doctorService, times(1)).transferPatientToOtherDoctor(patientId, newDoctorId);
    }

    /**
     * Test pour assigner une infirmière à un médecin
     */
    @Test
    @WithMockUser(roles = "DOCTOR")
    public void testAssignNurseToDoctor() throws Exception {
        String doctorId = "doctor123";
        String nurseId = "nurse456";

        doNothing().when(doctorService).assignNurseToDoctor(doctorId, nurseId);

        mockMvc.perform(post("/api/doctors/{doctorId}/nurses/{nurseId}", doctorId, nurseId)
                        .with(csrf())) // Ajout du token CSRF
                .andDo(print()) // Ajout des logs
                .andExpect(status().isOk());

        verify(doctorService, times(1)).assignNurseToDoctor(doctorId, nurseId);
    }

    /**
     * Test pour retirer une infirmière d'un médecin
     */
    @Test
    @WithMockUser(roles = "DOCTOR")
    public void testRemoveNurseFromDoctor() throws Exception {
        String doctorId = "doctor123";
        String nurseId = "nurse456";

        doNothing().when(doctorService).removeNurseFromDoctor(doctorId, nurseId);

        mockMvc.perform(delete("/api/doctors/{doctorId}/nurses/{nurseId}", doctorId, nurseId)
                        .with(csrf())) // Ajout du token CSRF
                .andDo(print()) // Ajout des logs
                .andExpect(status().isOk());

        verify(doctorService, times(1)).removeNurseFromDoctor(doctorId, nurseId);
    }

    /**
     * Test pour envoyer un message à un patient
     */
    @Test
    @WithMockUser(roles = "DOCTOR")
    public void testSendMessageToPatient() throws Exception {
        String patientId = "patient123";
        String message = "Bonjour, comment allez-vous ?";

        doNothing().when(doctorService).sendMessageToPatient(patientId, message);

        mockMvc.perform(post("/api/doctors/patients/{patientId}/message", patientId)
                        .with(csrf()) // Ajout du token CSRF
                        .param("message", message))
                .andDo(print()) // Ajout des logs
                .andExpect(status().isOk());

        verify(doctorService, times(1)).sendMessageToPatient(patientId, message);
    }

    /**
     * Test pour le processus de gestion des notifications pour un docteur
     */
    @Test
    @WithMockUser(roles = "DOCTOR")
    public void testProcessNotifications() throws Exception {

        doNothing().when(doctorService).processNotifications();

        mockMvc.perform(post("/api/doctors/notifications/process")
                        .with(csrf())) // Ajout du token CSRF
                .andDo(print()) // Ajout des logs
                .andExpect(status().isOk());

        verify(doctorService, times(1)).processNotifications();
    }

    /**
     * Test pour vérifier l'accès non autorisé (sans le rôle DOCTOR)
     */
    @Test
    @WithMockUser(roles = "NURSE")
    public void testAccessDeniedForNonDoctorRole() throws Exception {
        mockMvc.perform(put("/api/doctors/patients/transfer")
                        .with(csrf()) // Ajout du token CSRF
                        .param("patientId", "patient123")
                        .param("newDoctorId", "doctor456"))
                .andDo(print()) // Ajout des logs
                .andExpect(status().isForbidden());

        mockMvc.perform(post("/api/doctors/{doctorId}/nurses/{nurseId}", "doctor123", "nurse456")
                        .with(csrf())) // Ajout du token CSRF
                .andDo(print()) // Ajout des logs
                .andExpect(status().isForbidden());

        mockMvc.perform(delete("/api/doctors/{doctorId}/nurses/{nurseId}", "doctor123", "nurse456")
                        .with(csrf())) // Ajout du token CSRF
                .andDo(print()) // Ajout des logs
                .andExpect(status().isForbidden());

        mockMvc.perform(post("/api/doctors/patients/{patientId}/message", "patient123")
                        .with(csrf()) // Ajout du token CSRF
                        .param("message", "Hello"))
                .andDo(print()) // Ajout des logs
                .andExpect(status().isForbidden());

        mockMvc.perform(post("/api/doctors/notifications/process")
                        .with(csrf())) // Ajout du token CSRF
                .andDo(print()) // Ajout des logs
                .andExpect(status().isForbidden());

        verify(doctorService, never()).transferPatientToOtherDoctor(anyString(), anyString());
        verify(doctorService, never()).assignNurseToDoctor(anyString(), anyString());
        verify(doctorService, never()).removeNurseFromDoctor(anyString(), anyString());
        verify(doctorService, never()).sendMessageToPatient(anyString(), anyString());
        verify(doctorService, never()).processNotifications();
    }

    /**
     * Test pour vérifier l'accès non authentifié
     */
    @Test
    public void testAccessDeniedForUnauthenticated() throws Exception {
        mockMvc.perform(put("/api/doctors/patients/transfer")
                        .with(csrf()) // Ajout du token CSRF
                        .param("patientId", "patient123")
                        .param("newDoctorId", "doctor456"))
                .andDo(print()) // Ajout des logs
                .andExpect(status().isUnauthorized());

        mockMvc.perform(post("/api/doctors/{doctorId}/nurses/{nurseId}", "doctor123", "nurse456")
                        .with(csrf())) // Ajout du token CSRF
                .andDo(print()) // Ajout des logs
                .andExpect(status().isUnauthorized());

        mockMvc.perform(delete("/api/doctors/{doctorId}/nurses/{nurseId}", "doctor123", "nurse456")
                        .with(csrf())) // Ajout du token CSRF
                .andDo(print()) // Ajout des logs
                .andExpect(status().isUnauthorized());

        mockMvc.perform(post("/api/doctors/patients/{patientId}/message", "patient123")
                        .with(csrf()) // Ajout du token CSRF
                        .param("message", "Hello"))
                .andDo(print()) // Ajout des logs
                .andExpect(status().isUnauthorized());

        mockMvc.perform(post("/api/doctors/notifications/process")
                        .with(csrf())) // Ajout du token CSRF
                .andDo(print()) // Ajout des logs
                .andExpect(status().isUnauthorized());

        verify(doctorService, never()).transferPatientToOtherDoctor(anyString(), anyString());
        verify(doctorService, never()).assignNurseToDoctor(anyString(), anyString());
        verify(doctorService, never()).removeNurseFromDoctor(anyString(), anyString());
        verify(doctorService, never()).sendMessageToPatient(anyString(), anyString());
        verify(doctorService, never()).processNotifications();
    }
}
