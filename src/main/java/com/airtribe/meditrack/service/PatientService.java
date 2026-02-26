package com.airtribe.meditrack.service;

import com.airtribe.meditrack.entity.Patient;
import com.airtribe.meditrack.interfacepkg.Searchable;
import com.airtribe.meditrack.util.DataStore;
import com.airtribe.meditrack.util.IdGenerator;
import com.airtribe.meditrack.util.Validator;

import java.util.List;
import java.util.Optional;
import java.util.Scanner;
import java.util.stream.Collectors;

public class PatientService implements Searchable<Patient> {

    private final DataStore<Patient> store;
    private final IdGenerator idGenerator = IdGenerator.getInstance();

    public PatientService(DataStore<Patient> store) {
        this.store = store;
    }

    public Patient createPatient(String name, int age, String phone) {
        String id = idGenerator.nextPatientId();
        return createPatientWithId(id, name, age, phone);
    }

    public Patient createPatientWithId(String id, String name, int age, String phone) {
        Validator.requireNonBlank(name, "Name");
        Validator.requirePositive(age, "Age");
        Validator.requireNonBlank(phone, "Phone");
        Patient patient = new Patient(id, name, age, phone);
        store.save(id, patient);
        return patient;
    }

    public Optional<Patient> getPatientById(String id) {
        return store.findById(id);
    }

    public List<Patient> getAllPatients() {
        return store.findAll();
    }

    // Overloaded search methods for polymorphism demonstration
    public Optional<Patient> searchPatient(String id) {
        return getPatientById(id);
    }

    public List<Patient> searchPatientByName(String name) {
        return getAllPatients().stream()
                .filter(p -> p.getName() != null && p.getName().equalsIgnoreCase(name))
                .collect(Collectors.toList());
    }

    public List<Patient> searchPatient(int age) {
        return getAllPatients().stream()
                .filter(p -> p.getAge() == age)
                .collect(Collectors.toList());
    }

    public boolean deletePatient(String id) {
        return store.delete(id);
    }

    @Override
    public List<Patient> findAll() {
        return getAllPatients();
    }

    public void handlePatientMenu(Scanner scanner) {
        boolean back = false;
        while (!back) {
            System.out.println("\n=== Patient Management ===");
            System.out.println("1. Add patient");
            System.out.println("2. View all patients");
            System.out.println("3. Search patient by ID");
            System.out.println("4. Search patient by name");
            System.out.println("5. Search patient by age");
            System.out.println("6. Delete patient");
            System.out.println("0. Back");
            System.out.print("Enter choice: ");
            String choice = scanner.nextLine();
            switch (choice) {
                case "1" -> addPatientFromInput(scanner);
                case "2" -> listPatients();
                case "3" -> searchByIdFromInput(scanner);
                case "4" -> searchByNameFromInput(scanner);
                case "5" -> searchByAgeFromInput(scanner);
                case "6" -> deleteFromInput(scanner);
                case "0" -> back = true;
                default -> System.out.println("Invalid choice.");
            }
        }
    }

    private void addPatientFromInput(Scanner scanner) {
        System.out.print("Name: ");
        String name = scanner.nextLine();
        System.out.print("Age: ");
        int age = Integer.parseInt(scanner.nextLine());
        System.out.print("Phone: ");
        String phone = scanner.nextLine();
        Patient patient = createPatient(name, age, phone);
        System.out.println("Created patient: " + patient);
    }

    private void listPatients() {
        getAllPatients().forEach(System.out::println);
    }

    private void searchByIdFromInput(Scanner scanner) {
        System.out.print("Enter ID: ");
        String id = scanner.nextLine();
        searchPatient(id).ifPresentOrElse(
                System.out::println,
                () -> System.out.println("Patient not found.")
        );
    }

    private void searchByNameFromInput(Scanner scanner) {
        System.out.print("Enter name: ");
        String name = scanner.nextLine();
        List<Patient> result = searchPatientByName(name);
        if (result.isEmpty()) {
            System.out.println("No patients found.");
        } else {
            result.forEach(System.out::println);
        }
    }

    private void searchByAgeFromInput(Scanner scanner) {
        System.out.print("Enter age: ");
        int age = Integer.parseInt(scanner.nextLine());
        List<Patient> result = searchPatient(age);
        if (result.isEmpty()) {
            System.out.println("No patients found.");
        } else {
            result.forEach(System.out::println);
        }
    }

    private void deleteFromInput(Scanner scanner) {
        System.out.print("Enter ID to delete: ");
        String id = scanner.nextLine();
        if (deletePatient(id)) {
            System.out.println("Deleted.");
        } else {
            System.out.println("Patient not found.");
        }
    }
}

