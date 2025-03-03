package com.atm;

public enum Status
{
    UNSUCCESSFUL,
    // Bank's internal status based on withdrawal.
    SUCCESSFUL,                // Withdrawal was successful
    INSUFFICIENT_FUNDS,     // Not enough balance, and no overdraft allowed
    EXCEEDS_LIMIT,          // Requested withdrawal amount exceeds the allowed limit
}
