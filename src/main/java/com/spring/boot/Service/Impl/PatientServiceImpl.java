package com.spring.boot.Service.Impl;

import com.spring.boot.Config.QRCodeGenerator;
import com.spring.boot.Dto.CreatePatientDto;
import com.spring.boot.Dto.UpdatePatientDto;
import com.spring.boot.Exception.PatientNotFoundException;
import com.spring.boot.Mapper.PatientMapper;
import com.spring.boot.Model.Patients;
import com.spring.boot.Repo.PatientRepo;
import com.spring.boot.Service.PatientService;

import org.springframework.stereotype.Service;


import java.util.List;
import java.util.Map;

@Service
public class PatientServiceImpl implements PatientService {
    private final PatientRepo patientRepo;
    private final PatientMapper patientMapper;
    public PatientServiceImpl(PatientRepo patientRepo, PatientMapper patientMapper) {
        this.patientRepo = patientRepo;
        this.patientMapper = patientMapper;
    }
    @Override
    public Map<String, String> createPatientWithQR(CreatePatientDto dto) {
        Patients patient = patientMapper.toPatients(dto);
        patientRepo.save(patient);

        String frontendUrl = "https://el-doctory.vercel.app/" + patient.getId();

        String qrBase64;
        try {
            qrBase64 = QRCodeGenerator.generateQRCodeBase64(frontendUrl, 300, 300);
        } catch (Exception e) {
            throw new RuntimeException("فشل في توليد رمز الاستجابة السريعة (QR Code)", e);
        }

        return Map.of(
                "patientId", patient.getId(),
                "frontendUrl", frontendUrl,
                "qrCodeBase64", qrBase64
        );
    }

    @Override
    public void updatePatient(UpdatePatientDto updatePatientDto) {
    if (!patientRepo.findById(updatePatientDto.getId()).isPresent()){
        throw new PatientNotFoundException("المريض غير موجود");
    }
    Patients patient = patientMapper.toPatients(updatePatientDto);
    patientRepo.save(patient);
    }

    @Override
    public void deletePatient(String id) {
        if (!patientRepo.findById(id).isPresent()){
            throw new PatientNotFoundException("المريض غير موجود");

    }
        patientRepo.deleteById(id);
}

    @Override
    public List<UpdatePatientDto> getPatient() {
       List<Patients> patients = patientRepo.findAll();
       List<UpdatePatientDto> updatePatientDtoList = patientMapper.toUpdatePatientsList(patients);
        return updatePatientDtoList;
    }
}
