package com.airtribe.meditrack.util;

import com.airtribe.meditrack.entity.Appointment;
import com.airtribe.meditrack.entity.Doctor;
import com.airtribe.meditrack.entity.Patient;
import com.airtribe.meditrack.entity.Specialization;
import com.airtribe.meditrack.service.AppointmentService;
import com.airtribe.meditrack.service.DoctorService;
import com.airtribe.meditrack.service.PatientService;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.Scanner;
import java.util.stream.Collectors;

public class AIHelper {

    public Optional<Specialization> recommendSpecialization(String symptom) {
        if (symptom == null) {
            return Optional.empty();
        }
        String lower = symptom.toLowerCase();
        if (lower.contains("heart") || lower.contains("chest")) {
            return Optional.of(Specialization.CARDIOLOGIST);
        } else if (lower.contains("skin") || lower.contains("rash")) {
            return Optional.of(Specialization.DERMATOLOGIST);
        } else if (lower.contains("child") || lower.contains("pediatric")) {
            return Optional.of(Specialization.PEDIATRICIAN);
        } else if (lower.contains("bone") || lower.contains("joint")) {
            return Optional.of(Specialization.ORTHOPEDIC);
        } else if (lower.contains("headache") || lower.contains("neuro")) {
            return Optional.of(Specialization.NEUROLOGIST);
        }
        return Optional.of(Specialization.GENERAL_PHYSICIAN);
    }

    public List<Doctor> recommendDoctors(String symptom, List<Doctor> allDoctors) {
        Optional<Specialization> specializationOpt = recommendSpecialization(symptom);
        return specializationOpt
                .map(spec -> allDoctors.stream()
                        .filter(d -> d.getSpecialization() == spec)
                        .collect(Collectors.toList()))
                .orElseGet(List::of);
    }

    public void handleAiMenu(Scanner scanner,
                             DoctorService doctorService,
                             AppointmentService appointmentService,
                             PatientService patientService) {
        System.out.println("\n=== AI Helper ===");
        System.out.println("1. Recommend doctor by symptom");
        System.out.println("2. Appointment analytics (per doctor)");
        System.out.println("0. Back");
        System.out.print("Enter choice: ");
        String choice = scanner.nextLine();
        switch (choice) {
            case "1" -> handleDoctorRecommendation(scanner, doctorService, appointmentService, patientService);
            case "2" -> handleAnalytics(appointmentService);
            case "0" -> {
            }
            default -> System.out.println("Invalid choice.");
        }
    }

    private void handleDoctorRecommendation(Scanner scanner,
                                            DoctorService doctorService,
                                            AppointmentService appointmentService,
                                            PatientService patientService) {
        System.out.print("Enter patient ID: ");
        String patientId = scanner.nextLine();
        Optional<Patient> patientOpt = patientService.getPatientById(patientId);
        if (patientOpt.isEmpty()) {
            System.out.println("Patient not found.");
            return;
        }

        System.out.print("Describe symptoms: ");
        String symptoms = scanner.nextLine();

        List<Doctor> recommended = recommendDoctors(symptoms, doctorService.getAllDoctors());
        if (recommended.isEmpty()) {
            System.out.println("No suitable doctors found.");
            return;
        }

        System.out.println("Recommended doctors:");
        for (Doctor d : recommended) {
            System.out.println(d.getId() + " - " + d.getName() + " (" + d.getSpecialization() + ")");
        }

        System.out.print("Enter doctor ID to book appointment (or blank to cancel): ");
        String doctorId = scanner.nextLine();
        if (doctorId.isBlank()) {
            return;
        }

        Optional<Doctor> doctorOpt = doctorService.getDoctorById(doctorId);
        if (doctorOpt.isEmpty()) {
            System.out.println("Doctor not found.");
            return;
        }

        System.out.print("Enter appointment date/time (yyyy-MM-dd HH:mm): ");
        String dtStr = scanner.nextLine();
        LocalDateTime dateTime = DateUtil.parse(dtStr);

        Appointment appointment = appointmentService.createAppointment(patientOpt.get(), doctorOpt.get(), dateTime);
        System.out.println("Created appointment: " + appointment);
    }

    private void handleAnalytics(AppointmentService appointmentService) {
        System.out.println("\n=== Appointments per Doctor ===");
        appointmentService.getAppointmentsPerDoctor()
                .forEach((doctor, count) ->
                        System.out.println(doctor.getName() + " (" + doctor.getId() + "): " + count + " appointments"));
    }
}

