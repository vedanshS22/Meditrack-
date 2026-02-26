package com.airtribe.meditrack.util;

import java.util.concurrent.atomic.AtomicInteger;

public final class IdGenerator {

    private static final IdGenerator INSTANCE = new IdGenerator();

    private final AtomicInteger doctorCounter = new AtomicInteger(1);
    private final AtomicInteger patientCounter = new AtomicInteger(1);
    private final AtomicInteger appointmentCounter = new AtomicInteger(1);
    private final AtomicInteger billCounter = new AtomicInteger(1);

    private IdGenerator() {
    }

    public static IdGenerator getInstance() {
        return INSTANCE;
    }

    public String nextDoctorId() {
        return "D" + doctorCounter.getAndIncrement();
    }

    public String nextPatientId() {
        return "P" + patientCounter.getAndIncrement();
    }

    public String nextAppointmentId() {
        return "A" + appointmentCounter.getAndIncrement();
    }

    public String nextBillId() {
        return "B" + billCounter.getAndIncrement();
    }
}

