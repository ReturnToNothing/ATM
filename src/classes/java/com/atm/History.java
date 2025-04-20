package com.atm;

public record History(Account payee, Card card, double amount, String date, String time)
{
}