package com.airtribe.meditrack.entity;

/**
 * Immutable value object representing a concise bill summary.
 */
public final class BillSummary {

    private final String billId;
    private final double totalAmount;

    public BillSummary(String billId, double totalAmount) {
        this.billId = billId;
        this.totalAmount = totalAmount;
    }

    public String getBillId() {
        return billId;
    }

    public double getTotalAmount() {
        return totalAmount;
    }

    @Override
    public String toString() {
        return "BillSummary{" +
                "billId='" + billId + '\'' +
                ", totalAmount=" + totalAmount +
                '}';
    }
}

