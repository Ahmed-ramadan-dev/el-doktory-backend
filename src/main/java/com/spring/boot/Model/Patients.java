package com.spring.boot.Model;
import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.*;
import org.apache.commons.lang3.RandomStringUtils;
import com.fasterxml.jackson.annotation.JsonManagedReference;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Patients {

    @Id
    private String id;
    private String name;
    private int age;
    private String phoneNumber;
    private String address;

    @OneToMany(mappedBy = "patient", cascade = CascadeType.ALL, orphanRemoval = true)
    @JsonManagedReference
    private List<Analysis> analyses;

    @OneToMany(mappedBy = "patient", cascade = CascadeType.ALL, orphanRemoval = true)
    @JsonManagedReference
    private List<Scan> scans;

    @OneToMany(mappedBy = "patient", cascade = CascadeType.ALL, orphanRemoval = true)
    @JsonManagedReference
    private List<Prescription> prescriptions;
    @ManyToMany
    @JoinTable(
            name = "patient_doctor",
            joinColumns = @JoinColumn(name = "patient_id"),
            inverseJoinColumns = @JoinColumn(name = "doctor_id")
    )
    @JsonIgnore
    private Set<Doctor> doctors = new HashSet<>();

    public Patients(String name, int age) {
        this.name = name;
        this.age = age;
    }


    @PrePersist
    public void ensureId() {
        if (id == null || id.isEmpty()) {
            id = RandomStringUtils.random(8, "ABCDEFGHIJKLMNOPQRSTUVWXYZ0123456789");
        }
    }

}
