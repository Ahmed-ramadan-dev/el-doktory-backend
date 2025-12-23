package com.spring.boot.Service.ServiceHelp;

import com.spring.boot.Exception.DoctorAlreadyExistsException;
import com.spring.boot.Model.Doctor;
import com.spring.boot.Model.Patients;
import com.spring.boot.Model.Specialty;
import com.spring.boot.Repo.DoctorRepo;
import com.spring.boot.Repo.PatientRepo;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class DoctorHelperService {

    private final DoctorRepo doctorRepo;
    private final PatientRepo patientRepo;

    public DoctorHelperService(DoctorRepo doctorRepo, PatientRepo patientRepo) {
        this.doctorRepo = doctorRepo;
        this.patientRepo = patientRepo;
    }


    public Doctor getOrCreateDoctor(Patients patient, String doctorName, Specialty specialty) {

        Optional<Doctor> existingDoctorOpt = patient.getDoctors().stream()
                .filter(d -> d.getName().equals(doctorName))
                .findFirst();

        if (existingDoctorOpt.isPresent()) {
            Doctor existingDoctor = existingDoctorOpt.get();
            if (!existingDoctor.getSpecialty().getId().equals(specialty.getId())) {
                throw new DoctorAlreadyExistsException(
                        "اسم الطبيب موجود بالفعل لهذا المريض في تخصص: " + existingDoctor.getSpecialty().getName()
                );
            }
            return existingDoctor;
        }


        Doctor doctor = new Doctor();
        doctor.setName(doctorName);
        doctor.setSpecialty(specialty);
        doctor = doctorRepo.save(doctor);

        patient.getDoctors().add(doctor);
        patientRepo.save(patient);

        return doctor;
    }
}
