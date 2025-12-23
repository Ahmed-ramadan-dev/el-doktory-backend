package com.spring.boot.Service;

import com.spring.boot.Dto.DoctorDto;

import java.util.List;

public interface DoctorService {
    List<DoctorDto> searchDoctors(String patientId, String query);
}
