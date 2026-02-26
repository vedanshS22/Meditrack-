package com.airtribe.meditrack.util;

import com.airtribe.meditrack.constants.Constants;
import com.airtribe.meditrack.entity.*;
import com.airtribe.meditrack.service.AppointmentService;
import com.airtribe.meditrack.service.DoctorService;
import com.airtribe.meditrack.service.PatientService;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.time.LocalDateTime;

public final class CSVUtil {

    private CSVUtil() {
    }

    public static void savePatients(PatientService patientService) {
        Path path = Path.of(Constants.PATIENT_CSV);
        ensureParentDir(path);
        try (BufferedWriter writer = Files.newBufferedWriter(path)) {
            for (Patient p : patientService.getAllPatients()) {
                writer.write(String.join(",",
                        p.getId(),
                        escape(p.getName()),
                        String.valueOf(p.getAge()),
                        escape(p.getPhone())
                ));
                writer.newLine();
            }
        } catch (IOException e) {
            System.err.println("Failed to save patients: " + e.getMessage());
        }
    }

    public static void saveDoctors(DoctorService doctorService) {
        Path path = Path.of(Constants.DOCTOR_CSV);
        ensureParentDir(path);
        try (BufferedWriter writer = Files.newBufferedWriter(path)) {
            for (Doctor d : doctorService.getAllDoctors()) {
                writer.write(String.join(",",
                        d.getId(),
                        escape(d.getName()),
                        String.valueOf(d.getAge()),
                        escape(d.getPhone()),
                        d.getSpecialization().name(),
                        String.valueOf(d.getConsultationFee())
                ));
                writer.newLine();
            }
        } catch (IOException e) {
            System.err.println("Failed to save doctors: " + e.getMessage());
        }
    }

    public static void saveAppointments(AppointmentService appointmentService) {
        Path path = Path.of(Constants.APPOINTMENT_CSV);
        ensureParentDir(path);
        try (BufferedWriter writer = Files.newBufferedWriter(path)) {
            for (Appointment a : appointmentService.getAllAppointments()) {
                writer.write(String.join(",",
                        a.getId(),
                        a.getPatient().getId(),
                        a.getDoctor().getId(),
                        DateUtil.format(a.getDateTime()),
                        a.getStatus().name()
                ));
                writer.newLine();
            }
        } catch (IOException e) {
            System.err.println("Failed to save appointments: " + e.getMessage());
        }
    }

    public static void loadInitialData(DoctorService doctorService,
                                       PatientService patientService,
                                       AppointmentService appointmentService) {
        loadPatients(patientService);
        loadDoctors(doctorService);
        loadAppointments(doctorService, patientService, appointmentService);
    }

    private static void loadPatients(PatientService patientService) {
        Path path = Path.of(Constants.PATIENT_CSV);
        if (!Files.exists(path)) {
            return;
        }
        try (BufferedReader reader = Files.newBufferedReader(path)) {
            String line;
            while ((line = reader.readLine()) != null) {
                String[] parts = line.split(",");
                if (parts.length >= 4) {
                    String id = parts[0];
                    String name = unescape(parts[1]);
                    int age = Integer.parseInt(parts[2]);
                    String phone = unescape(parts[3]);
                    patientService.createPatientWithId(id, name, age, phone);
                }
            }
        } catch (IOException e) {
            System.err.println("Failed to load patients: " + e.getMessage());
        }
    }

    private static void loadDoctors(DoctorService doctorService) {
        Path path = Path.of(Constants.DOCTOR_CSV);
        if (!Files.exists(path)) {
            return;
        }
        try (BufferedReader reader = Files.newBufferedReader(path)) {
            String line;
            while ((line = reader.readLine()) != null) {
                String[] parts = line.split(",");
                if (parts.length >= 6) {
                    String id = parts[0];
                    String name = unescape(parts[1]);
                    int age = Integer.parseInt(parts[2]);
                    String phone = unescape(parts[3]);
                    Specialization specialization = Specialization.valueOf(parts[4]);
                    double fee = Double.parseDouble(parts[5]);
                    doctorService.createDoctorWithId(id, name, age, phone, specialization, fee);
                }
            }
        } catch (IOException e) {
            System.err.println("Failed to load doctors: " + e.getMessage());
        }
    }

    private static void loadAppointments(DoctorService doctorService,
                                         PatientService patientService,
                                         AppointmentService appointmentService) {
        Path path = Path.of(Constants.APPOINTMENT_CSV);
        if (!Files.exists(path)) {
            return;
        }
        try (BufferedReader reader = Files.newBufferedReader(path)) {
            String line;
            while ((line = reader.readLine()) != null) {
                String[] parts = line.split(",");
                if (parts.length >= 5) {
                    String id = parts[0];
                    String patientId = parts[1];
                    String doctorId = parts[2];
                    LocalDateTime dateTime = DateUtil.parse(parts[3]);
                    AppointmentStatus status = AppointmentStatus.valueOf(parts[4]);

                    patientService.getPatientById(patientId).ifPresent(patient ->
                            doctorService.getDoctorById(doctorId).ifPresent(doctor ->
                                    appointmentService.createAppointmentWithId(id, patient, doctor, dateTime, status)
                            )
                    );
                }
            }
        } catch (IOException e) {
            System.err.println("Failed to load appointments: " + e.getMessage());
        }
    }

    private static void ensureParentDir(Path path) {
        try {
            if (path.getParent() != null) {
                Files.createDirectories(path.getParent());
            }
        } catch (IOException e) {
            System.err.println("Failed to create data directory: " + e.getMessage());
        }
    }

    private static String escape(String value) {
        return value.replace(",", "\\,");
    }

    private static String unescape(String value) {
        return value.replace("\\,", ",");
    }
}

