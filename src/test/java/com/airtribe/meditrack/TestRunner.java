package com.airtribe.meditrack;

import com.airtribe.meditrack.entity.Appointment;
import com.airtribe.meditrack.entity.Doctor;
import com.airtribe.meditrack.entity.Patient;
import com.airtribe.meditrack.entity.Specialization;
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
        Appointment appointment = appointmentService.createAppointment(patient, doctor, LocalDateTime.now().plusDays(1));

        System.out.println("Doctor: " + doctor);
        System.out.println("Patient: " + patient);
        System.out.println("Appointment: " + appointment);
    }
}

