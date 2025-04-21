package com.atm;

import javafx.beans.property.*;

public class CardInfo
{
    private final LongProperty cardNumber = new SimpleLongProperty(0);
    private final IntegerProperty expirationDate = new SimpleIntegerProperty(0);
    private final IntegerProperty cvvCode = new SimpleIntegerProperty(0);
    private final StringProperty cardIssuer = new SimpleStringProperty("");
    private final StringProperty accountType = new SimpleStringProperty("");

    public LongProperty getCardNumber()
    {
        return this.cardNumber;
    }

    public IntegerProperty getExpirationDate()
    {
        return this.expirationDate;
    }

    public IntegerProperty getCVVCode()
    {
        return this.cvvCode;
    }

    public StringProperty getCardIssuer()
    {
        return this.cardIssuer;
    }

    public StringProperty getAccountType()
    {
        return this.accountType;
    }

    public void clear()
    {
        this.cardNumber.set(0);
        this.expirationDate.set(0);
        this.cvvCode.set(0);
        this.cardIssuer.set("");
        this.accountType.set("");
    }
}
