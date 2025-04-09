package com.atm;

public record Card(String cardSort, String cardNumber, double cardBalance, String companyName, Types cardType)
{
}