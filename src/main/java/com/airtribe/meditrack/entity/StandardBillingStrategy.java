package com.airtribe.meditrack.entity;

import com.airtribe.meditrack.constants.Constants;

/**
 * Strategy implementation that applies a fixed tax rate.
 */
public class StandardBillingStrategy implements BillingStrategy {

    @Override
    public double calculateTotal(double baseAmount) {
        double tax = baseAmount * Constants.TAX_RATE;
        return baseAmount + tax;
    }
}

