package com.spring.boot.Service.Impl;


import com.spring.boot.Dto.AnalysisDto;
import com.spring.boot.Dto.CreateAnalysisDto;
import com.spring.boot.Dto.GetAnalysisDto;
import com.spring.boot.Exception.*;
import com.spring.boot.Mapper.AnalysisMapper;
import com.spring.boot.Model.Analysis;
import com.spring.boot.Model.Doctor;
import com.spring.boot.Model.Patients;
import com.spring.boot.Model.Specialty;
import com.spring.boot.Repo.AnalysisRepo;
import com.spring.boot.Repo.DoctorRepo;
import com.spring.boot.Repo.PatientRepo;
import com.spring.boot.Repo.SpecialtyRepo;
import com.spring.boot.Service.AnalysisService;
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
public class AnalysisServiceImpl implements AnalysisService {

    private final DoctorRepo doctorRepo;
    private final PatientRepo patientRepo;
    private final AnalysisMapper analysisMapper;
    private final AnalysisRepo analysisRepo;
    private final CloudinaryService cloudinaryService;
    private final SpecialtyRepo specialtyRepo;
    private final DoctorHelperService doctorHelperService;
    private final FileHelperService fileHelperService;

    public AnalysisServiceImpl(
            DoctorRepo doctorRepo,
            PatientRepo patientRepo,
            AnalysisMapper analysisMapper,
            AnalysisRepo analysisRepo,
            CloudinaryService cloudinaryService, SpecialtyRepo specialtyRepo, DoctorHelperService doctorHelperService, FileHelperService fileHelperService
    ) {
        this.doctorRepo = doctorRepo;
        this.patientRepo = patientRepo;
        this.analysisMapper = analysisMapper;
        this.analysisRepo = analysisRepo;
        this.cloudinaryService = cloudinaryService;
        this.specialtyRepo = specialtyRepo;
        this.doctorHelperService = doctorHelperService;
        this.fileHelperService = fileHelperService;
    }

    @Override
    public List<AnalysisDto> getAllAnalysis(String patientId) {

        List<Analysis> analyses = analysisRepo.findByPatient_Id(patientId);
        if (analyses == null) analyses = new ArrayList<>();
        return analysisMapper.toAnalysisDto(analyses);
    }


    @Override
    public List<AnalysisDto> searchAnalysis(String patientId, String keyword, LocalDate date) {

        List<Analysis> analyses;

        if (date != null) {
            LocalDateTime start = date.atStartOfDay();
            LocalDateTime end = date.atTime(23, 59, 59);

            analyses = analysisRepo.searchByPatientAndDateAndOptionalKeyword(
                    patientId, start, end, keyword
            );
        } else {
            analyses = analysisRepo
                    .findByPatient_IdAndNameContainingIgnoreCaseOrPatient_IdAndDoctor_NameContaining(
                            patientId, keyword, patientId, keyword
                    );
        }

        return analyses.stream()
                .map(analysisMapper::toAnalysisDto)
                .collect(Collectors.toList());
    }

    @Override
    public GetAnalysisDto getAnalysisById(Long id) {

        Analysis analysis = analysisRepo.findById(id)
                .orElseThrow(() ->
                        new AnalysisNotFoundException("لم يتم العثور على التحليل بالرقم " + id));

        return analysisMapper.toGetAnalysisDto(analysis);
    }

    @Override
    @Transactional
    public void uploadAnalysis(String patientId, MultipartFile[] files, CreateAnalysisDto dto) {
        Patients patient = patientRepo.findById(patientId)
                .orElseThrow(() -> new PatientNotFoundException("المريض غير موجود"));

        Specialty specialty = specialtyRepo.findById(dto.getSpecialtyId())
                .orElseThrow(() -> new SpecialtyNotFoundException("التخصص غير موجود"));

        Doctor doctor = doctorHelperService.getOrCreateDoctor(patient, dto.getDoctorName(), specialty);

        List<String> fileUrls = fileHelperService.uploadFiles(files);

        Analysis analysis = new Analysis();
        analysis.setName(dto.getName());
        analysis.setDoctor(doctor);
        analysis.setPatient(patient);
        analysis.setNote(dto.getNote());
        analysis.setFileUrls(fileUrls);

        analysisRepo.save(analysis);
    }


    @Override
    @Transactional
    public void updateAnalysis(
            Long id,
            MultipartFile[] newFiles,
            CreateAnalysisDto dto,
            List<String> imagesToDelete
    ) {

        Analysis analysis = analysisRepo.findById(id)
                .orElseThrow(() -> new AnalysisNotFoundException("لم يتم العثور على التحليل بالمعرّف " + id));

        Patients patient = analysis.getPatient();
        Specialty specialty = specialtyRepo.findById(dto.getSpecialtyId())
                .orElseThrow(() -> new SpecialtyNotFoundException("التخصص غير موجود"));

        Doctor doctor = doctorHelperService.getOrCreateDoctor(patient, dto.getDoctorName(), specialty);
        analysis.setDoctor(doctor);
        analysis.setName(dto.getName());
        analysis.setNote(dto.getNote());

        List<String> currentFileUrls = analysis.getFileUrls() != null ? new ArrayList<>(analysis.getFileUrls()) : new ArrayList<>();
        currentFileUrls = fileHelperService.deleteFiles(currentFileUrls, imagesToDelete);

        if (newFiles != null && newFiles.length > 0) {
            currentFileUrls.addAll(fileHelperService.uploadFiles(newFiles));
        }

        analysis.setFileUrls(currentFileUrls);
        analysisRepo.save(analysis);
    }


    @Override
    @Transactional
    public void deleteAnalysis(Long id) {

        Analysis analysis = analysisRepo.findById(id)
                .orElseThrow(() ->
                        new AnalysisNotFoundException("لم يتم العثور على التحليل بالمعرّف " + id));

        if (analysis.getFileUrls() != null && !analysis.getFileUrls().isEmpty()) {
            for (String url : analysis.getFileUrls()) {
                cloudinaryService.deleteByUrl(url);
            }
        }

        analysisRepo.delete(analysis);
    }
}

