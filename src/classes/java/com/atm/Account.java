package com.atm;

import java.util.ArrayList;
import java.util.List;

public class Account implements Cloneable
{
    private final String name;
    private final int number;
    private final int password;
    private final Types type;

    private final List<Card> cards;
    private final List<History> history;
    private Card currentCard;

    public Account(String name, int number, int password, Types type)
    {
        this.name = name;
        this.number = number;
        this.password = password;
        this.type = type;

        this.history = new ArrayList<>();
        this.cards = new ArrayList<>();

        this.addCard(new Card("53-23-65", "1124513", 1000.99, "Mastercard", Types.Physical));
        this.addCard(new Card("20-71-69","737612", 199.50, "Visa", Types.Virtual)); // visa
        this.addCard(new Card("92-13-95","257346", 10.00, "HSBC", Types.Debit));
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

    public Types getType()
    {
        return this.type;
    }

    public void addHistory(History history)
    {
        this.history.add(history);
    }

    public List<History> getHistory()
    {
        return this.history;
    }

    public void addCard(Card card)
    {
        this.currentCard = card;
        this.cards.add(card);
    }

    public Card getCard()
    {
        return this.currentCard;
    }

    public void setCard(Card card)
    {
        this.currentCard = card;
    }

    public List<Card> getCards()
    {
        return this.cards;
    }

    @Override
    public Account clone()
    {
        try
        {
            // TODO: copy mutable state here, so the clone can't change the internals of the original
            return (Account) super.clone();
        }
        catch (CloneNotSupportedException event)
        {
            throw new AssertionError();
        }
    }
}
