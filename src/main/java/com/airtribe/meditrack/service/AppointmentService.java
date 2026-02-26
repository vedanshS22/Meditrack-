package com.airtribe.meditrack.service;

import com.airtribe.meditrack.entity.*;
import com.airtribe.meditrack.exception.AppointmentNotFoundException;
import com.airtribe.meditrack.util.DataStore;
import com.airtribe.meditrack.util.DateUtil;
import com.airtribe.meditrack.util.IdGenerator;

import java.time.LocalDateTime;
import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;

public class AppointmentService {

    private final DataStore<Appointment> store;
    private final DoctorService doctorService;
    private final PatientService patientService;
    private final IdGenerator idGenerator = IdGenerator.getInstance();

    public AppointmentService(DataStore<Appointment> store,
                              DoctorService doctorService,
                              PatientService patientService) {
        this.store = store;
        this.doctorService = doctorService;
        this.patientService = patientService;
    }

    public Appointment createAppointment(Patient patient, Doctor doctor, LocalDateTime dateTime) {
        String id = idGenerator.nextAppointmentId();
        return createAppointmentWithId(id, patient, doctor, dateTime, AppointmentStatus.CONFIRMED);
    }

    public Appointment createAppointmentWithId(String id, Patient patient, Doctor doctor,
                                               LocalDateTime dateTime, AppointmentStatus status) {
        Appointment appointment = new Appointment(id, patient, doctor, dateTime, status);
        store.save(id, appointment);
        return appointment;
    }

    public Optional<Appointment> getAppointmentById(String id) {
        return store.findById(id);
    }

    public List<Appointment> getAllAppointments() {
        return store.findAll();
    }

    public void cancelAppointment(String id) {
        Appointment appointment = getAppointmentById(id)
                .orElseThrow(() -> new AppointmentNotFoundException("Appointment not found: " + id));
        appointment.setStatus(AppointmentStatus.CANCELLED);
    }

    public Bill generateBill(String appointmentId) {
        Appointment appointment = getAppointmentById(appointmentId)
                .orElseThrow(() -> new AppointmentNotFoundException("Appointment not found: " + appointmentId));
        String billId = idGenerator.nextBillId();
        // Design pattern (Factory): delegates to BillFactory, which in turn
        // selects the appropriate BillingStrategy for this bill.
        return BillFactory.standardBill(billId, appointment);
    }

    public Map<Doctor, Long> getAppointmentsPerDoctor() {
        return getAllAppointments().stream()
                .collect(Collectors.groupingBy(Appointment::getDoctor, Collectors.counting()));
    }

    public void handleAppointmentMenu(Scanner scanner) {
        boolean back = false;
        while (!back) {
            System.out.println("\n=== Appointment & Billing ===");
            System.out.println("1. Create appointment");
            System.out.println("2. View all appointments");
            System.out.println("3. Cancel appointment");
            System.out.println("4. Generate bill");
            System.out.println("0. Back");
            System.out.print("Enter choice: ");
            String choice = scanner.nextLine();
            switch (choice) {
                case "1" -> createAppointmentFromInput(scanner);
                case "2" -> listAppointments();
                case "3" -> cancelFromInput(scanner);
                case "4" -> billFromInput(scanner);
                case "0" -> back = true;
                default -> System.out.println("Invalid choice.");
            }
        }
    }

    private void createAppointmentFromInput(Scanner scanner) {
        System.out.print("Enter patient ID: ");
        String patientId = scanner.nextLine();
        Optional<Patient> patientOpt = patientService.getPatientById(patientId);
        if (patientOpt.isEmpty()) {
            System.out.println("Patient not found.");
            return;
        }

        System.out.print("Enter doctor ID: ");
        String doctorId = scanner.nextLine();
        Optional<Doctor> doctorOpt = doctorService.getDoctorById(doctorId);
        if (doctorOpt.isEmpty()) {
            System.out.println("Doctor not found.");
            return;
        }

        System.out.print("Enter appointment date/time (yyyy-MM-dd HH:mm): ");
        String dtStr = scanner.nextLine();
        LocalDateTime dateTime = DateUtil.parse(dtStr);

        Appointment appointment = createAppointment(patientOpt.get(), doctorOpt.get(), dateTime);
        System.out.println("Created appointment: " + appointment);
    }

    private void listAppointments() {
        getAllAppointments().forEach(System.out::println);
    }

    private void cancelFromInput(Scanner scanner) {
        System.out.print("Enter appointment ID: ");
        String id = scanner.nextLine();
        try {
            cancelAppointment(id);
            System.out.println("Appointment cancelled.");
        } catch (AppointmentNotFoundException ex) {
            System.out.println(ex.getMessage());
        }
    }

    private void billFromInput(Scanner scanner) {
        System.out.print("Enter appointment ID: ");
        String id = scanner.nextLine();
        try {
            Bill bill = generateBill(id);
            System.out.println("Bill total: " + bill.calculateTotalAmount());
            System.out.println("Summary: " + bill.toSummary());
        } catch (AppointmentNotFoundException ex) {
            System.out.println(ex.getMessage());
        }
    }
}

