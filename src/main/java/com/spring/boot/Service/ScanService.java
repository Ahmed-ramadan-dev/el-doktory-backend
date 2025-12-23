package com.spring.boot.Service;

import com.spring.boot.Dto.CreateScanDto;
import com.spring.boot.Dto.GetScanDto;
import com.spring.boot.Dto.ScanDto;
import org.springframework.web.multipart.MultipartFile;

import java.time.LocalDate;
import java.util.List;

public interface ScanService {
    List<ScanDto> getAllScans(String patientId);
    List<ScanDto> searchScans(String patientId, String keyword, LocalDate date);
    GetScanDto getScanById(Long id);
    void uploadScan(String patientId, MultipartFile[] files, CreateScanDto dto);
    void updateScan(Long id, MultipartFile[] newFiles, CreateScanDto dto, List<String> imagesToDelete);
    void deleteScan(Long id);
}
