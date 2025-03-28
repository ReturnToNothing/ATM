package com.atm;

public enum States
{
    // Model's internal state for input
    INPUT_DIGIT,

    // Model's internal state for tutorial
    TUTORIAL_ONE,
    TUTORIAL_TWO,
    TUTORIAL_THREE,

    // Model's internal state for log in
    LOGIN_ONE,
    LOGIN_TWO,
    LOGIN_THREE,

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
