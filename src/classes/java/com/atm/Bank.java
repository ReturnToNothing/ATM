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

    private Account createAccount(int number, int password, States type)
    {
        return new Account(number, password, type);
    }

    public boolean addAccount(int number, int password, States type)
    {
        if (this.accounts.size() < this.maxAccounts)
        {
            Account newAccount = this.createAccount(number, password, type);
            this.accounts.add(newAccount);
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
            System.out.println(account);
            if (account != null)
            {
                if (account.getNumber() == number)
                {
                    if (account.getPassword() == password)
                    {
                        this.account = account;
                        return true;
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

    public boolean deposit(double amount)
    {
        if (!this.IsLogged())
        {
            return false;
        }

        return this.account.deposit(amount);
    }

    public Status withdraw(double amount)
    {
        if (!this.IsLogged())
        {
            return Status.UNSUCCESSFUL;
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
