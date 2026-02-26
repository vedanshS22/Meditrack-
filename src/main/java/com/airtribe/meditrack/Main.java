package com.airtribe.meditrack;

import com.airtribe.meditrack.entity.Appointment;
import com.airtribe.meditrack.entity.Doctor;
import com.airtribe.meditrack.entity.Patient;
import com.airtribe.meditrack.service.AppointmentService;
import com.airtribe.meditrack.service.DoctorService;
import com.airtribe.meditrack.service.PatientService;
import com.airtribe.meditrack.util.AIHelper;
import com.airtribe.meditrack.util.CSVUtil;
import com.airtribe.meditrack.util.DataStore;

import java.util.List;
import java.util.Scanner;

public class Main {

    public static void main(String[] args) {
        boolean loadData = false;
        for (String arg : args) {
            if ("--loadData".equalsIgnoreCase(arg)) {
                loadData = true;
                break;
            }
        }

        DataStore<Doctor> doctorStore = new DataStore<>();
        DataStore<Patient> patientStore = new DataStore<>();
        DataStore<Appointment> appointmentStore = new DataStore<>();

        DoctorService doctorService = new DoctorService(doctorStore);
        PatientService patientService = new PatientService(patientStore);
        AppointmentService appointmentService = new AppointmentService(appointmentStore, doctorService, patientService);

        if (loadData) {
            CSVUtil.loadInitialData(doctorService, patientService, appointmentService);
        }

        Scanner scanner = new Scanner(System.in);
        AIHelper aiHelper = new AIHelper();

        boolean running = true;
        while (running) {
            printMenu();
            String choice = scanner.nextLine();
            switch (choice) {
                // Menu-driven UI entry points to CRUD/search flows and billing.
                case "1" -> patientService.handlePatientMenu(scanner);          // Patients: CRUD + search
                case "2" -> doctorService.handleDoctorMenu(scanner);            // Doctors: CRUD + search/analytics
                case "3" -> appointmentService.handleAppointmentMenu(scanner);  // Appointments: create/view/cancel + billing
                case "4" -> aiHelper.handleAiMenu(scanner, doctorService, appointmentService, patientService);
                case "0" -> running = false;
                default -> System.out.println("Invalid choice. Please try again.");
            }
        }

        System.out.println("Exiting MediTrack. Goodbye!");
    }

    private static void printMenu() {
        System.out.println("\n=== MediTrack Clinic Management ===");
        System.out.println("1. Manage Patients");
        System.out.println("2. Manage Doctors");
        System.out.println("3. Manage Appointments & Billing");
        System.out.println("4. AI Helper (Recommendations & Analytics)");
        System.out.println("0. Exit");
        System.out.print("Enter your choice: ");
    }
}

