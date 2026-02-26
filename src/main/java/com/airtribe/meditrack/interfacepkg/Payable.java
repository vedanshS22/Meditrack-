package com.airtribe.meditrack.interfacepkg;

import com.airtribe.meditrack.entity.BillSummary;

public interface Payable {

    double calculateTotalAmount();

    default BillSummary toBillSummary(String billId) {
        return new BillSummary(billId, calculateTotalAmount());
    }
}

