package com.atm;

public class Account
{
    private final int number;
    private final int password;
    private double balance;
    private States type;

    public Account(int number, int password, States type)
    {
        this.number = number;
        this.password = password;
        this.balance = 100.00f;
        this.type = type;
    }

    public Status withdraw(double amount)
    {
        double limit = 100;
        boolean overdraft = false;

        amount = Math.abs(amount);

        if (this.type.equals(States.PRIME))
        {
            limit = 1000;
            overdraft = true;
        }

        // Check if the requested withdrawal exceeds the limit.
        if (amount > limit)
        {
            return Status.EXCEEDS_LIMIT; // Withdrawal amount exceeds the allowed limit.
        }

        // Verify if the withdrawal can be processed, bypassed if overdraft is allowed.
        if (this.balance >= amount || overdraft)
        {
            this.balance -= amount;
            return Status.SUCCESSFUL;
        }
        else
        {
            return Status.INSUFFICIENT_FUNDS;
        }
    }

    public boolean deposit(double amount)
    {
        amount = Math.abs(amount);
        if (amount <= 1000)
        {
            this.balance += amount;
            return false;
        }
        else
        {
            return false;
        }
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
