package com.airtribe.meditrack;

import com.airtribe.meditrack.entity.*;
import com.airtribe.meditrack.service.AppointmentService;
import com.airtribe.meditrack.service.DoctorService;
import com.airtribe.meditrack.service.PatientService;
import com.airtribe.meditrack.util.DataStore;

import java.time.LocalDateTime;

public class TestRunner {

    public static void main(String[] args) {
        DataStore<Doctor> doctorStore = new DataStore<>();
        DataStore<Patient> patientStore = new DataStore<>();
        DataStore<Appointment> appointmentStore = new DataStore<>();

        DoctorService doctorService = new DoctorService(doctorStore);
        PatientService patientService = new PatientService(patientStore);
        AppointmentService appointmentService = new AppointmentService(appointmentStore, doctorService, patientService);

        Doctor doctor = doctorService.createDoctor("Dr. Smith", 45, "1234567890",
                Specialization.GENERAL_PHYSICIAN, 500);
        Patient patient = patientService.createPatient("John Doe", 30, "0987654321");
        patient.addMedicalHistoryEntry("Allergy: Pollen");

        Appointment appointment = appointmentService.createAppointment(patient, doctor, LocalDateTime.now().plusDays(1));

        System.out.println("=== Core entities ===");
        System.out.println("Doctor: " + doctor);
        System.out.println("Patient: " + patient);
        System.out.println("Appointment: " + appointment);

        // Advanced OOP: deep cloning of Patient (medical history list is copied)
        Patient clonedPatient = patient.clone();
        clonedPatient.addMedicalHistoryEntry("New condition recorded only on clone");

        System.out.println("\n=== Deep copy demo ===");
        System.out.println("Original patient history: " + patient.getMedicalHistory());
        System.out.println("Cloned patient history:   " + clonedPatient.getMedicalHistory());

        // Design patterns: Strategy + Factory used for billing
        Bill standardBill = appointmentService.generateBill(appointment.getId());
        Bill discountedBill = BillFactory.discountedBill("B-DISCOUNT", appointment, 0.10);

        System.out.println("\n=== Billing strategies ===");
        System.out.println("Standard bill total:   " + standardBill.calculateTotalAmount());
        System.out.println("Discounted bill total: " + discountedBill.calculateTotalAmount());
    }
}

