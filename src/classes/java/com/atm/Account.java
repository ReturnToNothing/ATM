package com.atm;

import java.util.ArrayList;
import java.util.List;

public class Account
{
    private final String name;
    private final int number;
    private final int password;
    private final Types type;
    private double balance;

    private final List<Card> cards;
    private Card currentCard;

    public Account(String name, int number, int password, Types type)
    {
        this.name = name;
        this.number = number;
        this.password = password;
        this.type = type;

        this.balance = 100.00f;
        this.cards = new ArrayList<>();

        this.addCard(new Card("53-23-65", "125634", 1000.99, "Mastercard", Types.Physical));
        this.addCard(new Card("20-71-69","737612", 199.50, "Visa", Types.Virtual)); // visa
        this.addCard(new Card("92-13-95","257346", 10.00, "HSBC", Types.Debit));
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

    public void addCard(Card card)
    {
        this.cards.add(card);
        currentCard = card;
    }

    public Card getCard()
    {
        return this.currentCard;
    }

    public List<Card> getCards()
    {
        return this.cards;
    }
}
