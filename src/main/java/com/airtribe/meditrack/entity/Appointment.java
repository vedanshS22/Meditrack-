package com.airtribe.meditrack.entity;

import java.time.LocalDateTime;

public class Appointment implements Cloneable {

    private final String id;
    private final Patient patient;
    private final Doctor doctor;
    private final LocalDateTime dateTime;
    private AppointmentStatus status;

    public Appointment(String id, Patient patient, Doctor doctor, LocalDateTime dateTime, AppointmentStatus status) {
        this.id = id;
        this.patient = patient;
        this.doctor = doctor;
        this.dateTime = dateTime;
        this.status = status;
    }

    public String getId() {
        return id;
    }

    public Patient getPatient() {
        return patient;
    }

    public Doctor getDoctor() {
        return doctor;
    }

    public LocalDateTime getDateTime() {
        return dateTime;
    }

    public AppointmentStatus getStatus() {
        return status;
    }

    public void setStatus(AppointmentStatus status) {
        this.status = status;
    }

    @Override
    public Appointment clone() {
        Patient clonedPatient = patient != null ? patient.clone() : null;
        // For doctors we keep the same reference assuming they are shared entities
        return new Appointment(id, clonedPatient, doctor, dateTime, status);
    }

    @Override
    public String toString() {
        return "Appointment{" +
                "id='" + id + '\'' +
                ", patient=" + (patient != null ? patient.getName() : "N/A") +
                ", doctor=" + (doctor != null ? doctor.getName() : "N/A") +
                ", dateTime=" + dateTime +
                ", status=" + status +
                '}';
    }
}

