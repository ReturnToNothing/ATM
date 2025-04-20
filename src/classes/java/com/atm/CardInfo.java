package com.atm;

import javafx.beans.property.IntegerProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;

public class CardInfo
{
    private final IntegerProperty cardNumber = new SimpleIntegerProperty(0);
    private final IntegerProperty expirationDate = new SimpleIntegerProperty(0);
    private final IntegerProperty cvvCode = new SimpleIntegerProperty(0);
    private final StringProperty cardIssuer = new SimpleStringProperty("");
    private final StringProperty accountType = new SimpleStringProperty("");

    public IntegerProperty getCardNumber()
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
}
