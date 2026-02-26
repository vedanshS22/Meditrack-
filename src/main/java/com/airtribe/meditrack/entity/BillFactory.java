package com.airtribe.meditrack.entity;

/**
 * Design pattern (Factory): centralises creation of {@link Bill} objects
 * and their associated {@link BillingStrategy} variants.
 */
public final class BillFactory {

    private BillFactory() {
    }

    public static Bill standardBill(String billId, Appointment appointment) {
        double baseAmount = appointment.getDoctor().getConsultationFee();
        return new Bill(billId, appointment, baseAmount, new StandardBillingStrategy());
    }

    public static Bill discountedBill(String billId, Appointment appointment, double discountRate) {
        double baseAmount = appointment.getDoctor().getConsultationFee();
        return new Bill(billId, appointment, baseAmount, new DiscountBillingStrategy(discountRate));
    }
}

