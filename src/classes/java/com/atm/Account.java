package com.atm;

public class Account
{
    private final String name;
    private final int number;
    private final int password;
    private double balance;
    private Types type;

    public Account(String name, int number, int password, Types type)
    {
        this.name = "John doe";
        this.number = number;
        this.password = password;
        this.balance = 100.00f;
        this.type = type;
    }

    public void withdraw(double amount)
    {
        this.balance -= amount;
    }

    public void deposit(double amount)
    {
        this.balance += amount;
    }

    public String getName()
    {
        return this.name;
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

    public Types getType()
    {
        return this.type;
    }
}
