package com.atm;

public enum States
{
    // Model's internal state
    ACCOUNT_NO,
    PASSWORD_NO,
    LOGGED_IN,

    // Model's internal sub-state
    DEFAULT,
    WITHDRAWAL,
    DEPOSIT,
    BALANCE,
}
