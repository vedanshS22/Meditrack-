package com.airtribe.meditrack.entity;

import com.airtribe.meditrack.constants.Constants;

/**
 * Strategy implementation that first applies a percentage discount and
 * then taxes the discounted base.
 */
public class DiscountBillingStrategy implements BillingStrategy {

    private final double discountRate; // e.g. 0.10 = 10% off

    public DiscountBillingStrategy(double discountRate) {
        this.discountRate = discountRate;
    }

    @Override
    public double calculateTotal(double baseAmount) {
        double discounted = baseAmount * (1 - discountRate);
        double tax = discounted * Constants.TAX_RATE;
        return discounted + tax;
    }
}

