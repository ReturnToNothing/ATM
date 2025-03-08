package com.atm;

public enum Status
{
    // Bank's internal basic status for each method.
    UNSUCCESSFUL,
    SUCCESSFUL,

    // Bank's internal status based on deposit.
    EXCEEDS_DEPOSIT,

    // Bank's internal status based on withdrawal.
    INSUFFICIENT_FUNDS,
    EXCEEDS_WITHDRAWAL,
}
