package com.spring.boot.Repo;

import com.spring.boot.Model.Scan;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface ScanRepo extends JpaRepository<Scan,Long> {
    List<Scan> findByPatient_Id(String patientId);

    List<Scan> findByPatient_IdAndNameContainingIgnoreCaseOrPatient_IdAndDoctor_NameContaining(
            String patientId1, String nameKeyword,
            String patientId2, String doctorKeyword
    );

    @Query("SELECT s FROM Scan s " +
            "WHERE s.patient.id = :patientId " +
            "AND s.createdAt BETWEEN :start AND :end " +
            "AND (:keyword IS NULL OR LOWER(s.name) LIKE LOWER(CONCAT('%', :keyword, '%')) " +
            "     OR LOWER(s.doctor.name) LIKE LOWER(CONCAT('%', :keyword, '%')))")
    List<Scan> searchByPatientAndDateAndOptionalKeyword(
            @Param("patientId") String patientId,
            @Param("start") LocalDateTime start,
            @Param("end") LocalDateTime end,
            @Param("keyword") String keyword
    );
}
