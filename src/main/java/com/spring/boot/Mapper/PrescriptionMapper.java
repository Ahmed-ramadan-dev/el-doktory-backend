package com.spring.boot.Mapper;

import com.spring.boot.Dto.GetPrescriptionDto;
import com.spring.boot.Dto.PrescriptionDto;
import com.spring.boot.Model.Prescription;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import java.util.List;

@Mapper(componentModel = "spring")
public interface PrescriptionMapper {
    @Mapping(source = "doctor.name",target = "doctorName")
    @Mapping(source = "doctor.specialty.name", target = "doctorSpecialty")
    PrescriptionDto toPrescriptionDto(Prescription Prescription);
    List<PrescriptionDto> toPrescriptionDto(List<Prescription> Prescription);
    @Mapping(source = "doctor.name",target = "doctorName")
    @Mapping(source = "doctor.specialty.name", target = "doctorSpecialty")
    GetPrescriptionDto toGetPrescriptionDto(Prescription Prescription);
}
