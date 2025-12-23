package com.spring.boot.Repo;

import com.spring.boot.Model.Analysis;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;

import java.time.LocalDateTime;
import java.util.List;

public interface AnalysisRepo extends CrudRepository<Analysis,Long> {
    List<Analysis> findByPatient_Id (String patientId);

    List<Analysis> findByPatient_IdAndNameContainingIgnoreCaseOrPatient_IdAndDoctor_NameContaining(
            String patientId1, String nameKeyword,
            String patientId2, String doctorKeyword
    );

    @Query("SELECT a FROM Analysis a " +
            "WHERE a.patient.id = :patientId " +
            "AND a.createdAt BETWEEN :start AND :end " +
            "AND (:keyword IS NULL OR LOWER(a.name) LIKE LOWER(CONCAT('%', :keyword, '%')) " +
            "     OR LOWER(a.doctor.name) LIKE LOWER(CONCAT('%', :keyword, '%')))")
    List<Analysis> searchByPatientAndDateAndOptionalKeyword(
            @Param("patientId") String patientId,
            @Param("start") LocalDateTime start,
            @Param("end") LocalDateTime end,
            @Param("keyword") String keyword
    );


}
