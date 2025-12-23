package com.spring.boot.Service.Impl;

import com.spring.boot.Dto.PatientInfoDto;
import com.spring.boot.Exception.PatientNotFoundException;
import com.spring.boot.Mapper.PatientMapper;
import com.spring.boot.Model.Patients;
import com.spring.boot.Repo.PatientRepo;
import com.spring.boot.Service.PatientInfoService;
import org.springframework.stereotype.Service;

@Service
public class PatientInfoServiceImpl implements PatientInfoService {
    private final PatientRepo patientRepo;
    private final PatientMapper patientMapper;
    public PatientInfoServiceImpl(PatientRepo patientRepo, PatientMapper patientMapper) {
        this.patientRepo = patientRepo;
        this.patientMapper = patientMapper;
    }
    @Override
    public PatientInfoDto getPatientInfo(String patientId) {
        Patients patient = patientRepo.findById(patientId)
                .orElseThrow(() -> new PatientNotFoundException("المريض غير موجود"));
        return patientMapper.toPatientInfoDto(patient);
        }

    }

