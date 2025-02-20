package com.atm;

public class Account
{
    private final int number;
    private final int password;
    private double balance;

    public Account(int number, int password)
    {
        this.number = number;
        this.password = password;
        this.balance = 0.00f;
    }

    public boolean withdraw(double amount)
    {
        if (this.balance >= amount) {
            this.balance -= amount;
            return true;
        }
        else
        {
            return false;
        }
    }

    public void deposit(int amount)
    {
        this.balance += Math.abs(amount);
    }

    public int getNumber()
    {
        return this.number;
    }

    public int getPassword()
    {
        return this.password;
    }

    public double getBalance()
    {
        return this.balance;
    }
}
