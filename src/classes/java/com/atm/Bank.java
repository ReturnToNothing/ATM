package com.atm;

public class Bank  //make it protected
{
    int maxAccounts = 10;
    int numAccounts = 0;

    Account[] accounts;
    Account account;

    public Bank()
    {
        this.accounts = new Account[this.maxAccounts];
        this.account = this.accounts[0];
    }

    private Account createAccount(int number, int password)
    {
        return new Account(number, password);
    }

    private boolean addAccount(Account account)
    {
        if (this.numAccounts < this.maxAccounts)
        {
            this.numAccounts++;
            this.accounts[this.numAccounts] = account;
            return true;
        }
        else
        {
            return false;
        }
    }

    public boolean IsLogged()
    {
        return this.account != null;
    }

    public boolean addAccount(int number, int password)
    {
        Account newAccount = this.createAccount(number, password);
        return this.addAccount(newAccount);
    }

    public boolean login(int number, int password)
    {
        this.logout();

        // Iterates stored accounts to match with the parameters; number and password.
        for (Account account : accounts)
        {
            if (account.getNumber() == number) {
                if (account.getPassword() == password) {
                    this.account = account;
                }
            }
        }
        // Otherwise, if these conditions are not matched;
        // then the account isn't registered or invalid
        return false;
    }

    public void logout()
    {
        if (this.IsLogged())
        {
          this.account = null;
        }
    }

    public boolean deposit(double amount)
    {
        if (this.IsLogged())
        {
            return this.account.withdraw(amount);
        }

        return false;
    }

    public boolean withdraw(double amount)
    {
        if (this.IsLogged())
        {
            return this.account.withdraw(amount);
        }

        return false;
    }

    public double getBalance()
    {
        if (this.IsLogged())
        {
            return this.account.getBalance();
        }

        return -1.00f;
    }
}
