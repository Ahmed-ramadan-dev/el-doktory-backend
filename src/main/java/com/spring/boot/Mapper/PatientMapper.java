package com.spring.boot.Mapper;

import com.spring.boot.Dto.CreatePatientDto;
import com.spring.boot.Dto.PatientInfoDto;
import com.spring.boot.Dto.UpdatePatientDto;
import com.spring.boot.Model.Patients;
import org.mapstruct.Mapper;

import java.util.List;

@Mapper(componentModel = "spring")
public interface PatientMapper {
    Patients toPatients(CreatePatientDto createPatientDto);
    Patients toPatients( UpdatePatientDto updatePatientDto);
    PatientInfoDto toPatientInfoDto(Patients patients);
    List<UpdatePatientDto> toUpdatePatientsList( List<Patients> patientsList);


}

