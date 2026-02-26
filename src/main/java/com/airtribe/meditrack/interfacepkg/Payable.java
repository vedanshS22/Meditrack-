package com.airtribe.meditrack.interfacepkg;

import com.airtribe.meditrack.entity.BillSummary;

/**
 * SOLID (Interface Segregation): tiny interface for billable things,
 * keeping payment-related behaviour decoupled from other responsibilities.
 */
public interface Payable {

    double calculateTotalAmount();

    default BillSummary toBillSummary(String billId) {
        return new BillSummary(billId, calculateTotalAmount());
    }
}

