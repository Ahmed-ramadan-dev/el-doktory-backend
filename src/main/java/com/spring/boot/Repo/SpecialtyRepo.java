package com.spring.boot.Repo;

import com.spring.boot.Model.Specialty;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface SpecialtyRepo extends JpaRepository<Specialty, Long> {

}
