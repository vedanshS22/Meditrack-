package com.airtribe.meditrack.service;

import com.airtribe.meditrack.entity.Doctor;
import com.airtribe.meditrack.entity.Specialization;
import com.airtribe.meditrack.interfacepkg.Searchable;
import com.airtribe.meditrack.util.DataStore;
import com.airtribe.meditrack.util.IdGenerator;
import com.airtribe.meditrack.util.Validator;

import java.util.DoubleSummaryStatistics;
import java.util.List;
import java.util.Optional;
import java.util.Scanner;
import java.util.stream.Collectors;

public class DoctorService implements Searchable<Doctor> {

    // SOLID (Single Responsibility): encapsulates all doctor-specific logic,
    // while relying on the injected DataStore abstraction for persistence.
    private final DataStore<Doctor> store;
    private final IdGenerator idGenerator = IdGenerator.getInstance();

    public DoctorService(DataStore<Doctor> store) {
        this.store = store;
    }

    public Doctor createDoctor(String name, int age, String phone,
                               Specialization specialization, double fee) {
        String id = idGenerator.nextDoctorId();
        return createDoctorWithId(id, name, age, phone, specialization, fee);
    }

    public Doctor createDoctorWithId(String id, String name, int age, String phone,
                                     Specialization specialization, double fee) {
        Validator.requireNonBlank(name, "Name");
        Validator.requirePositive(age, "Age");
        Validator.requireNonBlank(phone, "Phone");
        Validator.requireNonNegative(fee, "Consultation fee");
        Doctor doctor = new Doctor(id, name, age, phone, specialization, fee);
        store.save(id, doctor);
        return doctor;
    }

    public Optional<Doctor> getDoctorById(String id) {
        return store.findById(id);
    }

    public List<Doctor> getAllDoctors() {
        return store.findAll();
    }

    public List<Doctor> filterBySpecialization(Specialization specialization) {
        return getAllDoctors().stream()
                .filter(d -> d.getSpecialization() == specialization)
                .collect(Collectors.toList());
    }

    public DoubleSummaryStatistics feeStatistics() {
        return getAllDoctors().stream()
                .collect(Collectors.summarizingDouble(Doctor::getConsultationFee));
    }

    public boolean deleteDoctor(String id) {
        return store.delete(id);
    }

    @Override
    public List<Doctor> findAll() {
        return getAllDoctors();
    }

    public void handleDoctorMenu(Scanner scanner) {
        boolean back = false;
        while (!back) {
            System.out.println("\n=== Doctor Management ===");
            System.out.println("1. Add doctor");
            System.out.println("2. View all doctors");
            System.out.println("3. Filter by specialization");
            System.out.println("4. Fee statistics");
            System.out.println("5. Delete doctor");
            System.out.println("0. Back");
            System.out.print("Enter choice: ");
            String choice = scanner.nextLine();
            switch (choice) {
                case "1" -> addDoctorFromInput(scanner);
                case "2" -> listDoctors();
                case "3" -> filterBySpecializationFromInput(scanner);
                case "4" -> printFeeStats();
                case "5" -> deleteFromInput(scanner);
                case "0" -> back = true;
                default -> System.out.println("Invalid choice.");
            }
        }
    }

    private void addDoctorFromInput(Scanner scanner) {
        System.out.print("Name: ");
        String name = scanner.nextLine();
        System.out.print("Age: ");
        int age = Integer.parseInt(scanner.nextLine());
        System.out.print("Phone: ");
        String phone = scanner.nextLine();

        System.out.println("Specialization options:");
        for (Specialization s : Specialization.values()) {
            System.out.println("- " + s.name());
        }
        System.out.print("Enter specialization: ");
        Specialization specialization = Specialization.valueOf(scanner.nextLine().toUpperCase());

        System.out.print("Consultation fee: ");
        double fee = Double.parseDouble(scanner.nextLine());

        Doctor doctor = createDoctor(name, age, phone, specialization, fee);
        System.out.println("Created doctor: " + doctor);
    }

    private void listDoctors() {
        getAllDoctors().forEach(System.out::println);
    }

    private void filterBySpecializationFromInput(Scanner scanner) {
        System.out.println("Specialization options:");
        for (Specialization s : Specialization.values()) {
            System.out.println("- " + s.name());
        }
        System.out.print("Enter specialization: ");
        Specialization specialization = Specialization.valueOf(scanner.nextLine().toUpperCase());
        List<Doctor> result = filterBySpecialization(specialization);
        if (result.isEmpty()) {
            System.out.println("No doctors found.");
        } else {
            result.forEach(System.out::println);
        }
    }

    private void printFeeStats() {
        DoubleSummaryStatistics stats = feeStatistics();
        System.out.println("Doctor fee statistics:");
        System.out.println("Count: " + stats.getCount());
        System.out.println("Min: " + stats.getMin());
        System.out.println("Max: " + stats.getMax());
        System.out.println("Average: " + stats.getAverage());
    }

    private void deleteFromInput(Scanner scanner) {
        System.out.print("Enter ID to delete: ");
        String id = scanner.nextLine();
        if (deleteDoctor(id)) {
            System.out.println("Deleted.");
        } else {
            System.out.println("Doctor not found.");
        }
    }
}

