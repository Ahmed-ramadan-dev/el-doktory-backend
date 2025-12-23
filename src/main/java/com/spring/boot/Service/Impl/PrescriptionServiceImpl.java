package com.spring.boot.Service.Impl;

import com.spring.boot.Dto.CreatePrescriptionDto;
import com.spring.boot.Dto.GetPrescriptionDto;
import com.spring.boot.Dto.PrescriptionDto;
import com.spring.boot.Exception.*;
import com.spring.boot.Mapper.PrescriptionMapper;
import com.spring.boot.Model.Doctor;
import com.spring.boot.Model.Patients;
import com.spring.boot.Model.Prescription;
import com.spring.boot.Model.Specialty;
import com.spring.boot.Repo.DoctorRepo;
import com.spring.boot.Repo.PatientRepo;
import com.spring.boot.Repo.PrescriptionRepo;
import com.spring.boot.Repo.SpecialtyRepo;
import com.spring.boot.Service.PrescriptionService;
import com.spring.boot.Service.ServiceHelp.CloudinaryService;
import com.spring.boot.Service.ServiceHelp.DoctorHelperService;
import com.spring.boot.Service.ServiceHelp.FileHelperService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class PrescriptionServiceImpl implements PrescriptionService {

    private final PrescriptionRepo prescriptionRepo;
    private final DoctorRepo doctorRepo;
    private final PatientRepo patientRepo;
    private final PrescriptionMapper prescriptionMapper;
    private final CloudinaryService cloudinaryService;
    private final SpecialtyRepo specialtyRepo;
    private final DoctorHelperService doctorHelperService;
    private final FileHelperService fileHelperService;

    public PrescriptionServiceImpl(PrescriptionRepo prescriptionRepo, DoctorRepo doctorRepo,
                                   PatientRepo patientRepo, PrescriptionMapper prescriptionMapper,
                                   CloudinaryService cloudinaryService, SpecialtyRepo specialtyRepo, DoctorHelperService doctorHelperService, FileHelperService fileHelperService) {
        this.prescriptionRepo = prescriptionRepo;
        this.doctorRepo = doctorRepo;
        this.patientRepo = patientRepo;
        this.prescriptionMapper = prescriptionMapper;
        this.cloudinaryService = cloudinaryService;
        this.specialtyRepo = specialtyRepo;
        this.doctorHelperService = doctorHelperService;
        this.fileHelperService = fileHelperService;
    }

    @Override
    public List<PrescriptionDto> getAllPrescriptions(String patientId) {
        List<Prescription> prescriptions = prescriptionRepo.findByPatient_Id(patientId);
        if (prescriptions == null) prescriptions = new ArrayList<>();
        return prescriptionMapper.toPrescriptionDto(prescriptions);
    }

    @Override
    public List<PrescriptionDto> searchPrescriptions(String patientId, String keyword, LocalDate date) {
        List<Prescription> prescriptions;
        if (date != null) {
            LocalDateTime start = date.atStartOfDay();
            LocalDateTime end = date.atTime(23, 59, 59);
            prescriptions = prescriptionRepo.searchByPatientAndDateAndOptionalKeyword(patientId, start, end, keyword);
        } else {
            prescriptions = prescriptionRepo.findByPatient_IdAndNameContainingIgnoreCaseOrPatient_IdAndDoctor_NameContaining(
                    patientId, keyword, patientId, keyword
            );
        }
        return prescriptions.stream().map(prescriptionMapper::toPrescriptionDto).collect(Collectors.toList());
    }

    @Override
    public GetPrescriptionDto getPrescriptionById(Long id) {
        Prescription prescription = prescriptionRepo.findById(id)
                .orElseThrow(() -> new AnalysisNotFoundException("الوصفة الطبية بالرقم " + id + " غير موجود"));
        return prescriptionMapper.toGetPrescriptionDto(prescription);
    }

    @Override
    @Transactional
    public void uploadPrescription(String patientId, MultipartFile[] files, CreatePrescriptionDto dto) {
        Patients patient = patientRepo.findById(patientId)
                .orElseThrow(() -> new PatientNotFoundException("المريض غير موجود"));

        Specialty specialty = specialtyRepo.findById(dto.getSpecialtyId())
                .orElseThrow(() -> new SpecialtyNotFoundException("التخصص غير موجود"));

        Doctor doctor = doctorHelperService.getOrCreateDoctor(patient, dto.getDoctorName(), specialty);

        List<String> fileUrls = fileHelperService.uploadFiles(files);

        Prescription prescription = new Prescription();
        prescription.setName(dto.getName());
        prescription.setDoctor(doctor);
        prescription.setPatient(patient);
        prescription.setNote(dto.getNote());
        prescription.setFileUrls(fileUrls);

        prescriptionRepo.save(prescription);
    }

    @Override
    @Transactional
    public void updatePrescription(Long id, MultipartFile[] newFiles, CreatePrescriptionDto dto, List<String> filesToDelete) {
        Prescription prescription = prescriptionRepo.findById(id)
                .orElseThrow(() -> new AnalysisNotFoundException("الوصفة الطبية بالرقم " + id + " غير موجود"));

        Patients patient = prescription.getPatient();
        Specialty specialty = specialtyRepo.findById(dto.getSpecialtyId())
                .orElseThrow(() -> new SpecialtyNotFoundException("التخصص غير موجود"));

        Doctor doctor = doctorHelperService.getOrCreateDoctor(patient, dto.getDoctorName(), specialty);
        prescription.setDoctor(doctor);
        prescription.setName(dto.getName());
        prescription.setNote(dto.getNote());

        List<String> currentFiles = prescription.getFileUrls() != null ? new ArrayList<>(prescription.getFileUrls()) : new ArrayList<>();

        currentFiles = fileHelperService.deleteFiles(currentFiles, filesToDelete);

        if (newFiles != null && newFiles.length > 0) {
            currentFiles.addAll(fileHelperService.uploadFiles(newFiles));
        }

        prescription.setFileUrls(currentFiles);
        prescriptionRepo.save(prescription);
    }

    @Override
    @Transactional
    public void deletePrescription(Long id) {
        Prescription prescription = prescriptionRepo.findById(id)
                .orElseThrow(() -> new AnalysisNotFoundException("الوصفة الطبية بالرقم " + id + " غير موجود"));

        if (prescription.getFileUrls() != null) {
            for (String url : prescription.getFileUrls()) {
                cloudinaryService.deleteByUrl(url);
            }
        }

        prescriptionRepo.delete(prescription);
    }

}
