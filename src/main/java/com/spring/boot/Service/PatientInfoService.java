package com.spring.boot.Service;

import com.spring.boot.Dto.PatientInfoDto;
import com.spring.boot.Dto.UpdatePatientDto;


public interface PatientInfoService {
    PatientInfoDto getPatientInfo(String patientId);
}
