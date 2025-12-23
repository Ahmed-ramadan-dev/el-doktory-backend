package com.spring.boot.Service;

import com.spring.boot.Dto.CreatePrescriptionDto;
import com.spring.boot.Dto.GetPrescriptionDto;
import com.spring.boot.Dto.PrescriptionDto;
import org.springframework.web.multipart.MultipartFile;

import java.time.LocalDate;
import java.util.List;

public interface PrescriptionService {
    List<PrescriptionDto> getAllPrescriptions(String patientId);
    List<PrescriptionDto> searchPrescriptions(String patientId, String keyword, LocalDate date);
    GetPrescriptionDto getPrescriptionById(Long id);
    void uploadPrescription(String patientId, MultipartFile[] files, CreatePrescriptionDto dto);
    void updatePrescription(Long id, MultipartFile[] newFiles, CreatePrescriptionDto dto, List<String> filesToDelete);
    void deletePrescription(Long id);
}
