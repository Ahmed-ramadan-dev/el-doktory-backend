package com.spring.boot.Repo;

import com.spring.boot.Model.Doctor;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface DoctorRepo extends JpaRepository<Doctor,Long> {

    Optional<Doctor> findByName(String name);
}
