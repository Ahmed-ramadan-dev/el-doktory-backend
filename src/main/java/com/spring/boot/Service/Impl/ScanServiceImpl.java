package com.spring.boot.Service.Impl;

import com.spring.boot.Dto.CreateScanDto;
import com.spring.boot.Dto.GetScanDto;
import com.spring.boot.Dto.ScanDto;
import com.spring.boot.Exception.*;
import com.spring.boot.Mapper.ScanMapper;
import com.spring.boot.Model.Doctor;
import com.spring.boot.Model.Patients;
import com.spring.boot.Model.Scan;
import com.spring.boot.Model.Specialty;
import com.spring.boot.Repo.DoctorRepo;
import com.spring.boot.Repo.PatientRepo;
import com.spring.boot.Repo.ScanRepo;
import com.spring.boot.Repo.SpecialtyRepo;
import com.spring.boot.Service.ScanService;
import com.spring.boot.Service.ServiceHelp.CloudinaryService;
import com.spring.boot.Service.ServiceHelp.DoctorHelperService;
import com.spring.boot.Service.ServiceHelp.FileHelperService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class ScanServiceImpl implements ScanService {
    private final ScanRepo scanRepo;
    private final DoctorRepo doctorRepo;
    private final PatientRepo patientRepo;
    private final ScanMapper scanMapper;
    private final CloudinaryService cloudinaryService;
    private final SpecialtyRepo specialtyRepo;
    private final DoctorHelperService doctorHelperService;
    private final FileHelperService fileHelperService;

    public ScanServiceImpl(ScanRepo scanRepo, DoctorRepo doctorRepo, PatientRepo patientRepo, ScanMapper scanMapper, CloudinaryService cloudinaryService, SpecialtyRepo specialtyRepo, DoctorHelperService doctorHelperService, FileHelperService fileHelperService) {
        this.scanRepo = scanRepo;
        this.doctorRepo = doctorRepo;
        this.patientRepo = patientRepo;
        this.scanMapper = scanMapper;
        this.cloudinaryService = cloudinaryService;
        this.specialtyRepo = specialtyRepo;
        this.doctorHelperService = doctorHelperService;
        this.fileHelperService = fileHelperService;
    }

    @Override
    public List<ScanDto> getAllScans(String patientId) {
        List<Scan> scans = scanRepo.findByPatient_Id(patientId);
        if (scans == null) {
            scans = new ArrayList<>();
        }
        return scanMapper.toScanDto(scans);
    }


    @Override
    public List<ScanDto> searchScans(String patientId, String keyword, LocalDate date) {
        List<Scan> scans;
        if (date != null) {
            var start = date.atStartOfDay();
            var end = date.atTime(23, 59, 59);
            scans = scanRepo.searchByPatientAndDateAndOptionalKeyword(patientId, start, end, keyword);
        } else {
            scans = scanRepo.findByPatient_IdAndNameContainingIgnoreCaseOrPatient_IdAndDoctor_NameContaining(
                    patientId, keyword, patientId, keyword
            );
        }
        return scans.stream().map(scanMapper::toScanDto).collect(Collectors.toList());
    }

    @Override
    public GetScanDto getScanById(Long id) {
        Scan scan = scanRepo.findById(id)
                .orElseThrow(() -> new AnalysisNotFoundException("الأشعة بالرقم " + id + " غير موجود"));
        return scanMapper.toGetScanDto(scan);
    }

    @Override
    @Transactional
    public void uploadScan(String patientId, MultipartFile[] files, CreateScanDto dto) {

        Patients patient = patientRepo.findById(patientId)
                .orElseThrow(() -> new PatientNotFoundException("المريض غير موجود"));

        Specialty specialty = specialtyRepo.findById(dto.getSpecialtyId())
                .orElseThrow(() -> new SpecialtyNotFoundException("التخصص غير موجود"));

        Doctor doctor = doctorHelperService.getOrCreateDoctor(patient, dto.getDoctorName(), specialty);

        List<String> fileUrls = fileHelperService.uploadFiles(files);

        Scan scan = new Scan();
        scan.setName(dto.getName());
        scan.setDoctor(doctor);
        scan.setPatient(patient);
        scan.setNote(dto.getNote());
        scan.setFileUrls(fileUrls);

        scanRepo.save(scan);
    }

    @Override
    @Transactional
    public void updateScan(Long id, MultipartFile[] newFiles, CreateScanDto dto, List<String> imagesToDelete) {
        Scan scan = scanRepo.findById(id)
                .orElseThrow(() -> new AnalysisNotFoundException("الأشعة بالرقم " + id + " غير موجود"));

        Patients patient = scan.getPatient();
        Specialty specialty = specialtyRepo.findById(dto.getSpecialtyId())
                .orElseThrow(() -> new SpecialtyNotFoundException("التخصص غير موجود"));

        Doctor doctor = doctorHelperService.getOrCreateDoctor(patient, dto.getDoctorName(), specialty);
        scan.setDoctor(doctor);
        scan.setName(dto.getName());
        scan.setNote(dto.getNote());

        List<String> currentFileUrls = scan.getFileUrls() != null ? new ArrayList<>(scan.getFileUrls()) : new ArrayList<>();
        currentFileUrls = fileHelperService.deleteFiles(currentFileUrls, imagesToDelete);

        if (newFiles != null && newFiles.length > 0) {
            currentFileUrls.addAll(fileHelperService.uploadFiles(newFiles));
        }

        scan.setFileUrls(currentFileUrls);
        scanRepo.save(scan);
    }

    @Override
    @Transactional
    public void deleteScan(Long id) {
        Scan scan = scanRepo.findById(id)
                .orElseThrow(() -> new AnalysisNotFoundException("الأشعة بالرقم " + id + " غير موجود"));

        if (scan.getFileUrls() != null && !scan.getFileUrls().isEmpty()) {
            for (String url : scan.getFileUrls()) {
                cloudinaryService.deleteByUrl(url);
            }
        }

        scanRepo.delete(scan);
    }

}






