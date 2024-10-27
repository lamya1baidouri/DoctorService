package com.service.service.repository;

import com.service.service.model.Doctor;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface DoctorRepository extends MongoRepository<Doctor, String> {

    Doctor findDoctorByPatientIdsContaining(String patientId);

    Optional<Doctor> findById(String doctorId);
}



