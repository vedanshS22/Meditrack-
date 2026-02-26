package com.airtribe.meditrack.entity;

/**
 * Design pattern (Strategy): encapsulates different ways of computing
 * the final bill total so new pricing rules can be added without
 * modifying the {@link Bill} class.
 */
public interface BillingStrategy {

    double calculateTotal(double baseAmount);
}

