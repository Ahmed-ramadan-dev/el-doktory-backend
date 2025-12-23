package com.spring.boot.Mapper;


import com.spring.boot.Dto.GetScanDto;
import com.spring.boot.Dto.ScanDto;

import com.spring.boot.Model.Scan;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import java.util.List;

@Mapper(componentModel = "spring")
public interface ScanMapper {
    @Mapping(source = "doctor.name",target = "doctorName")
    @Mapping(source = "doctor.specialty.name", target = "doctorSpecialty")
    ScanDto toScanDto(Scan scan);
    List<ScanDto> toScanDto(List<Scan> scan);
    @Mapping(source = "doctor.name",target = "doctorName")
    @Mapping(source = "doctor.specialty.name", target = "doctorSpecialty")
    GetScanDto toGetScanDto(Scan scan);

}
