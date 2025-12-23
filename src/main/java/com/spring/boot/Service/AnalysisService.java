package com.spring.boot.Service;

import com.spring.boot.Dto.AnalysisDto;
import com.spring.boot.Dto.CreateAnalysisDto;

import com.spring.boot.Dto.GetAnalysisDto;

import org.springframework.web.multipart.MultipartFile;

import java.time.LocalDate;
import java.util.List;

public interface AnalysisService {
    List<AnalysisDto> getAllAnalysis(String patientId);
    List<AnalysisDto> searchAnalysis(String patientId,String keyword,LocalDate date);
    GetAnalysisDto getAnalysisById( Long id);
    void uploadAnalysis( String patientId, MultipartFile[] files, CreateAnalysisDto dto);
    void updateAnalysis( Long id, MultipartFile[] newFiles, CreateAnalysisDto dto, List<String> imagesToDelete );

    void deleteAnalysis( Long id);

}
