package com.airtribe.meditrack.entity;

import com.airtribe.meditrack.interfacepkg.Payable;

/**
 * SOLID (Open/Closed) + Strategy pattern:
 * delegates total calculation to a pluggable {@link BillingStrategy}
 * so new pricing rules do not require modifying this class.
 */
public class Bill implements Payable {

    private final String id;
    private final Appointment appointment;
    private final double baseAmount;
    private final BillingStrategy billingStrategy;

    public Bill(String id, Appointment appointment, double baseAmount, BillingStrategy billingStrategy) {
        this.id = id;
        this.appointment = appointment;
        this.baseAmount = baseAmount;
        this.billingStrategy = billingStrategy;
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

    @Override
    public double calculateTotalAmount() {
        return billingStrategy.calculateTotal(baseAmount);
    }

    public BillSummary toSummary() {
        return new BillSummary(id, calculateTotalAmount());
    }
}

