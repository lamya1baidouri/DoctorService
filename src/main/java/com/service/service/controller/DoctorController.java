package com.service.service.controller;

import com.service.service.Interfaces.DoctorService;
import com.service.service.model.Doctor;
import com.service.service.model.DoctorRequestDTO;
import com.service.service.model.MessageRequestDTO;
import com.service.service.model.DoctorAuthResponse;
import com.service.service.model.DoctorUpdateDTO;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

/**
 * Contrôleur pour la gestion des médecins.
 */
@RestController
@RequestMapping("/api/doctors")
@Tag(name = "Doctor Management", description = "Gestion des opérations liées aux médecins")
public class DoctorController {

    @Autowired
    private DoctorService doctorService;

    /**
     * Créer un nouveau médecin.
     *
     * @param doctorRequestDTO Détails du médecin à créer.
     * @return ResponseEntity avec le médecin créé et le statut HTTP approprié.
     */
    @Operation(summary = "Create a new doctor", description = "Permet de créer un nouveau médecin en fournissant les détails nécessaires.")
    @PostMapping
    @PreAuthorize("hasRole('ADMIN')") // Par exemple, seule une personne avec le rôle ADMIN peut créer un médecin
    public ResponseEntity<Doctor> createDoctor(
            @Parameter(description = "Détails du médecin à créer") @RequestBody DoctorRequestDTO doctorRequestDTO) {
        try {
            Doctor createdDoctor = doctorService.createDoctor(doctorRequestDTO);
            return new ResponseEntity<>(createdDoctor, HttpStatus.CREATED);
        } catch (Exception e) {
            return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    /**
     * Authentifier un médecin par email.
     *
     * @param email Email du médecin.
     * @return ResponseEntity avec les détails du médecin et le statut HTTP approprié.
     */
    @Operation(summary = "Authenticate a doctor by email", description = "Permet d'authentifier un médecin en utilisant son email.")
    @GetMapping("/getDoctorByemail")
    public ResponseEntity<DoctorAuthResponse> getDoctorByEmail(
            @Parameter(description = "Email du médecin", example = "doctor@example.com") @RequestParam String email) {
        DoctorAuthResponse response = doctorService.findDoctorByEmail(email);
        if (response == null) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(response);
    }

    /**
     * Mettre à jour les informations d'un médecin.
     *
     * @param userId ID du médecin.
     * @param doctorUpdateDTO Détails de la mise à jour.
     * @return ResponseEntity avec le statut HTTP approprié.
     */
    @Operation(summary = "Update doctor information", description = "Permet de mettre à jour les détails d'un médecin.")
    @PutMapping("/{userId}")
    @PreAuthorize("hasRole('DOCTOR')")
    public ResponseEntity<String> updateDoctor(
            @Parameter(description = "ID du médecin", example = "123") @PathVariable Long userId,
            @RequestBody DoctorUpdateDTO doctorUpdateDTO) {
        doctorService.updateDoctor(userId, doctorUpdateDTO);
        return ResponseEntity.ok("Médecin mis à jour avec succès");
    }

    /**
     * Transférer un patient à un autre médecin.
     *
     * @param patientId ID du patient à transférer.
     * @param newDoctorId ID du nouveau médecin.
     * @return ResponseEntity avec le statut HTTP approprié.
     */
    @Operation(summary = "Transfer a patient to another doctor", description = "Permet à un docteur de transférer un patient vers un autre docteur en fournissant les IDs des deux docteurs.")
    @PutMapping("/patients/transfer")
    @PreAuthorize("hasRole('DOCTOR')")
    public ResponseEntity<Void> transferPatientToOtherDoctor(
            @Parameter(description = "ID du patient à transférer", example = "patient123") @RequestParam String patientId,
            @Parameter(description = "ID du nouveau médecin", example = "doctor456") @RequestParam String newDoctorId) {
        doctorService.transferPatientToOtherDoctor(patientId, newDoctorId);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    /**
     * Assigner une infirmière à un médecin.
     *
     * @param doctorId ID du médecin.
     * @param nurseId ID de l'infirmière.
     * @return ResponseEntity avec le statut HTTP approprié.
     */
    @Operation(summary = "Assign a nurse to a doctor", description = "Assigne une infirmière à un médecin spécifique en utilisant leurs IDs.")
    @PostMapping("/{doctorId}/nurses/{nurseId}")
    @PreAuthorize("hasRole('DOCTOR')")
    public ResponseEntity<Void> assignNurseToDoctor(
            @Parameter(description = "ID du médecin", example = "doctor123") @PathVariable String doctorId,
            @Parameter(description = "ID de l'infirmière", example = "nurse456") @PathVariable String nurseId) {
        doctorService.assignNurseToDoctor(doctorId, nurseId);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    /**
     * Retirer une infirmière d'un médecin.
     *
     * @param doctorId ID du médecin.
     * @param nurseId ID de l'infirmière.
     * @return ResponseEntity avec le statut HTTP approprié.
     */
    @Operation(summary = "Remove a nurse from a doctor", description = "Supprime l'affectation d'une infirmière à un médecin en fournissant leurs IDs.")
    @DeleteMapping("/{doctorId}/nurses/{nurseId}")
    @PreAuthorize("hasRole('DOCTOR')")
    public ResponseEntity<Void> removeNurseFromDoctor(
            @Parameter(description = "ID du médecin", example = "doctor123") @PathVariable String doctorId,
            @Parameter(description = "ID de l'infirmière", example = "nurse456") @PathVariable String nurseId) {
        doctorService.removeNurseFromDoctor(doctorId, nurseId);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    /**
     * Envoyer un message à un patient.
     *
     * @param patientId ID du patient.
     * @param message Message à envoyer.
     * @return ResponseEntity avec le statut HTTP approprié.
     */
    @Operation(summary = "Send a message to a patient", description = "Envoie un message à un patient spécifique en utilisant son ID.")
    @PostMapping("/patients/{patientId}/message")
    @PreAuthorize("hasRole('DOCTOR')")
    public ResponseEntity<Void> sendMessageToPatient(
            @Parameter(description = "ID du patient", example = "patient123") @PathVariable String patientId,
            @RequestBody MessageRequestDTO message) {
        doctorService.sendMessageToPatient(patientId, message.getMessage());
        return new ResponseEntity<>(HttpStatus.OK);
    }

    /**
     * Processus de gestion des notifications pour un docteur.
     *
     * @return ResponseEntity avec le statut HTTP approprié.
     */
    @Operation(summary = "Process notifications for a doctor", description = "Permet à un médecin de traiter ses notifications.")
    @PostMapping("/notifications/process")
    @PreAuthorize("hasRole('DOCTOR')")
    public ResponseEntity<Void> processNotifications() {
        doctorService.processNotifications();
        return new ResponseEntity<>(HttpStatus.OK);
    }
}
