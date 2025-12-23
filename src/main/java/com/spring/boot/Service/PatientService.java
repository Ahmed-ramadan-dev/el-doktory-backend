package com.spring.boot.Service;

import com.spring.boot.Dto.CreatePatientDto;
import com.spring.boot.Dto.UpdatePatientDto;


import java.util.List;
import java.util.Map;

public interface PatientService {
    Map<String, String> createPatientWithQR(CreatePatientDto createPatientDto);
    void updatePatient(UpdatePatientDto updatePatientDto);
    void deletePatient( String id);
    List<UpdatePatientDto> getPatient();
}
