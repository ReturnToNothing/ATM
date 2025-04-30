package com.atm;

public enum States
{
    // Model's internal state for the account
    HOME_PAGE,
    WALLET_PAGE,
    PROFILE_PAGE,

    TRANSACTION_PAGE,
    TRANSACTION_CONFIRM,
    TRANSACTION_COMPLETE,
    TRANSACTION_SUMMARY,

    // Model's internal state for notification
    NOTIFY_SHOW,

    // Model's internal state for input
    INPUT_NUMERIC,
    INPUT_STRING_NUMERIC,
    INPUT_BALANCE,
    INPUT_STRING,

    // Model's internal state for tutorial
    TUTORIAL_ONE,
    TUTORIAL_TWO,
    TUTORIAL_THREE,
    LOGIN_FOUR,

    // Model's internal state for log in
    LOGIN_ONE,
    LOGIN_TWO,
    LOGIN_THREE,

    // Model's internal state for sign-in
    SIGN_ONE,
    SIGN_TWO,
    SIGN_THREE,
    SIGN_FOUR,
    SIGN_FIVE,
    SIGN_SIX,

    // Model's internal state for swapping input processes
    PROCESS_CARDNUMBER,
    PROCESS_PHONE,
    PROCESS_DATE,
    PROCESS_CVV,

    PROCESS_FIRST,
    PROCESS_LAST,
    PROCESS_ISSUER,

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
