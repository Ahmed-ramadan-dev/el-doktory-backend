package com.spring.boot.Service.Impl;
import com.spring.boot.Dto.DoctorDto;
import com.spring.boot.Model.Patients;
import com.spring.boot.Repo.DoctorRepo;
import com.spring.boot.Repo.PatientRepo;
import com.spring.boot.Service.DoctorService;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class DoctorServiceImpl implements DoctorService {

    private final PatientRepo patientRepo;
    private final DoctorRepo doctorRepo;

    public DoctorServiceImpl(PatientRepo patientRepo, DoctorRepo doctorRepo) {
        this.patientRepo = patientRepo;
        this.doctorRepo = doctorRepo;
    }

    @Override
    public List<DoctorDto> searchDoctors(String patientId, String query) {

        Patients patient = patientRepo.findById(patientId)
                .orElseThrow(() -> new RuntimeException("المريض غير موجود"));

        return patient.getDoctors().stream()
                .filter(d -> query == null || d.getName().toLowerCase().contains(query.toLowerCase()))
                .map(d -> new DoctorDto(
                        d.getId(),
                        d.getName(),
                        d.getSpecialty() != null ? d.getSpecialty().getName() : ""
                ))
                .collect(Collectors.toList());
    }
}
