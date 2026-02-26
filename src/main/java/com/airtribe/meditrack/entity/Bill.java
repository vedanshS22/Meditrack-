package com.airtribe.meditrack.entity;

import com.airtribe.meditrack.constants.Constants;
import com.airtribe.meditrack.interfacepkg.Payable;

public class Bill implements Payable {

    private final String id;
    private final Appointment appointment;
    private final double baseAmount;

    public Bill(String id, Appointment appointment, double baseAmount) {
        this.id = id;
        this.appointment = appointment;
        this.baseAmount = baseAmount;
    }

    public String getId() {
        return id;
    }

    public Appointment getAppointment() {
        return appointment;
    }

    public double getBaseAmount() {
        return baseAmount;
    }

    public double getTaxAmount() {
        return baseAmount * Constants.TAX_RATE;
    }

    @Override
    public double calculateTotalAmount() {
        return baseAmount + getTaxAmount();
    }

    public BillSummary toSummary() {
        return new BillSummary(id, calculateTotalAmount());
    }
}

