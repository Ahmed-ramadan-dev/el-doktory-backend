package com.spring.boot.Repo;

import com.spring.boot.Model.Prescription;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDateTime;
import java.util.List;

public interface PrescriptionRepo extends JpaRepository<Prescription,Long> {
    List<Prescription> findByPatient_Id(String patientId);

    List<Prescription> findByPatient_IdAndNameContainingIgnoreCaseOrPatient_IdAndDoctor_NameContaining(
            String patientId1, String nameKeyword,
            String patientId2, String doctorKeyword
    );

    @Query("SELECT p FROM Prescription p " +
            "WHERE p.patient.id = :patientId " +
            "AND p.createdAt BETWEEN :start AND :end " +
            "AND (:keyword IS NULL OR LOWER(p.name) LIKE LOWER(CONCAT('%', :keyword, '%')) " +
            "     OR LOWER(p.doctor.name) LIKE LOWER(CONCAT('%', :keyword, '%')))")
    List<Prescription> searchByPatientAndDateAndOptionalKeyword(
            @Param("patientId") String patientId,
            @Param("start") LocalDateTime start,
            @Param("end") LocalDateTime end,
            @Param("keyword") String keyword
    );
}
