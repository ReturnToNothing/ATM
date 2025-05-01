package com.atm;

import javafx.util.Pair;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Objects;

public class Bank  //make it protected
{
    int maxAccounts = 10;

    ArrayList<Account> accounts;
    Account account;
    Account foundAccount;

    public Bank()
    {
        this.accounts = new ArrayList<Account>();
        this.account = null;
        this.foundAccount = null;
    }

    private Account createAccount(String name, int number, int password, Types type)
    {
        return new Account(name, number, password, type);
    }

    // Another method that creates an immutable copy from a legitimate account.
    public Account cloneAccount()
    {
        if (this.IsLogged())
        {
            return null;
        }

        return this.account.clone();
    }

    public boolean addAccount(String name, int number, int password, Types type)
    {
        if (this.accounts.size() < this.maxAccounts)
        {
            Account newAccount = this.createAccount(name, number, password, type);

            System.out.println(newAccount);
            this.accounts.add(newAccount);
            return true;
        }

        return false;
    }

    public boolean IsLogged()
    {
        return this.account == null;
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
                        return true;
                    }
                }
            }
        }
        // Otherwise, if these conditions are not matched;
        // then the account isn't registered due to unmatched information
        return false;
    }

    public Card findCard(String cardNumber)
    {
        if (this.IsLogged())
        {
            return null;
        }

        for (Card card : account.getCards())
        {
            if (card != null)
            {
                if (Objects.equals(card.getCardNumber(), cardNumber))
                {
                    return card;
                }
            }
        }

        return null;
    }

    public ArrayList<Account> findAccountsPartially(long PIN)
    {
        ArrayList<Account> matches = new ArrayList<>();
        String partialPin = String.valueOf(PIN);

        for (Account account : accounts)
        {
            if (account != null)
            {
                String accountPIN = String.valueOf(account.getNumber());

                if (accountPIN.startsWith(partialPin))
                {
                    matches.add(account);
                }
            }
        }

        return matches;
    }

    public void logout()
    {
        if (this.IsLogged())
        {
            return;
        }

        this.account = null;
    }

    public Status setCard(Card card)
    {
        if (this.IsLogged())
        {
            return Status.UNSUCCESSFUL;
        }

        this.account.setCard(card);

        return Status.SUCCESSFUL;
    }

    public Status transfer(Account payee, double amount)
    {
        if (this.IsLogged())
        {
            return Status.UNSUCCESSFUL;
        }

        amount = Math.abs(amount);

        // Fetches the primary card of the payer and the payee
        Card payerCard = this.account.getCard();
        Card payeeCard = payee.getCard();

        double payerBalance = payerCard.getCardBalance();
        double payeeBalance = payeeCard.getCardBalance();

        // Verifying if the card's balance is enough,
        // hence 1 exchanged as 0.01
        if (payerBalance > amount)
        {
            payerCard.setCardBalance(payerBalance - amount);
            payeeCard.setCardBalance(payeeBalance + amount);

            Pair<String, String> time = TimeFormatter.getTime();

            History transferHistory = new History(payee, payerCard, amount, time.getKey(), time.getValue());

            this.account.addHistory(transferHistory);
        }
        else
        {
            return Status.INSUFFICIENT_FUNDS;
        }

        return Status.SUCCESSFUL;
    }
/*
    public Status deposit(double amount)
    {
        if (this.IsLogged())
        {
            return Status.UNSUCCESSFUL;
        }

        amount = Math.abs(amount);

        if (amount <= 1000)
        {
            this.account.deposit(amount);
            return Status.SUCCESSFUL;
        }
        else
        {
            return Status.EXCEEDS_DEPOSIT;
        }
    }

    public Status withdraw(double amount)
    {
        if (this.IsLogged())
        {
            return Status.UNSUCCESSFUL;
        }

        amount = Math.abs(amount);

        double balance = this.account.getBalance();
        Types type = this.account.getType();

        double limit = 100;
        boolean overdraft = false;

        if (type.equals(Types.PRIME))
        {
            limit = 1000;
            overdraft = true;
        }

        // Check if the requested withdrawal exceeds the limit.
        if (amount > limit)
        {
            return Status.EXCEEDS_WITHDRAWAL; // The Withdrawal amount exceeds the allowed limit.
        }

        // Verify if the withdrawal can be processed, bypassed if overdraft is allowed.
        if (balance >= amount || overdraft)
        {
            this.account.withdraw(amount);
            return Status.SUCCESSFUL;
        }
        else
        {
            return Status.INSUFFICIENT_FUNDS;
        }
    }

    public double getBalance() {
        if (this.IsLogged()) {
            return -1.00f;
        }

        return this.account.getBalance();
    }

 */
}
