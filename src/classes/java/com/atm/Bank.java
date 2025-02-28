package com.atm;

import java.util.ArrayList;

public class Bank  //make it protected
{
    int maxAccounts = 10;

    ArrayList<Account> accounts;
    Account account;

    public Bank()
    {
        this.accounts = new ArrayList<Account>();
        this.account = null;
    }

    private Account createAccount(int number, int password)
    {
        return new Account(number, password);
    }

    public boolean addAccount(int number, int password)
    {
        if (this.accounts.size() < this.maxAccounts)
        {
            Account newAccount = this.createAccount(number, password);
            this.accounts.add(account);
            return true;
        }

        return false;
    }

    public boolean IsLogged()
    {
        return this.account != null;
    }

    public boolean login(int number, int password)
    {
        this.logout();

        // Iterates a list of accounts to match with the parameters and instance variables; number and password.
        for (Account account : accounts)
        {
            if (account != null)
            {
                if (account.getNumber() == number)
                {
                    if (account.getPassword() == password)
                    {
                        this.account = account;
                    }
                }
            }
        }
        // Otherwise, if these conditions are not matched;
        // then the account isn't registered due to unmatched information
        return false;
    }

    public void logout()
    {
        if (!this.IsLogged())
        {
            return;
        }

        this.account = null;
    }

    public void deposit(double amount)
    {
        if (!this.IsLogged())
        {
            return;
        }

        this.account.deposit(amount);
    }

    public boolean withdraw(double amount)
    {
        if (!this.IsLogged())
        {
            return false;
        }

        return this.account.withdraw(amount);
    }

    public double getBalance()
    {
        if (!this.IsLogged())
        {
            return -1.00f;
        }

        return this.account.getBalance();
    }
}
