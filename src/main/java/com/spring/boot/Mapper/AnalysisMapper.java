package com.spring.boot.Mapper;

import com.spring.boot.Dto.AnalysisDto;
import com.spring.boot.Dto.GetAnalysisDto;
import com.spring.boot.Model.Analysis;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import java.util.List;

@Mapper(componentModel = "spring")
public interface AnalysisMapper {
    @Mapping(source = "doctor.name",target = "doctorName")
    @Mapping(source = "doctor.specialty.name", target = "doctorSpecialty")
    AnalysisDto toAnalysisDto(Analysis analysis);
    List<AnalysisDto> toAnalysisDto(List<Analysis> analysis);
    @Mapping(source = "doctor.name",target = "doctorName")
    @Mapping(source = "doctor.specialty.name", target = "doctorSpecialty")
    GetAnalysisDto toGetAnalysisDto(Analysis analysis);
}
