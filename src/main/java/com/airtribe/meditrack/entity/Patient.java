package com.airtribe.meditrack.entity;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class Patient extends Person implements Cloneable {

    private final List<String> medicalHistory;

    public Patient(String id, String name, int age, String phone) {
        super(id, name, age, phone);
        this.medicalHistory = new ArrayList<>();
    }

    private Patient(String id, String name, int age, String phone, List<String> medicalHistory) {
        super(id, name, age, phone);
        this.medicalHistory = new ArrayList<>(medicalHistory);
    }

    public List<String> getMedicalHistory() {
        return Collections.unmodifiableList(medicalHistory);
    }

    public void addMedicalHistoryEntry(String entry) {
        if (entry != null && !entry.isBlank()) {
            medicalHistory.add(entry);
        }
    }

    @Override
    public Patient clone() {
        return new Patient(getId(), getName(), getAge(), getPhone(), medicalHistory);
    }
}

