package com.atm;

import javafx.beans.property.IntegerProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;

public class PersonalInfo
{
    private final StringProperty firstName = new SimpleStringProperty("");
    private final StringProperty lastName = new SimpleStringProperty("");
    private final IntegerProperty phoneNumber = new SimpleIntegerProperty(0);
    private final IntegerProperty pin = new SimpleIntegerProperty(0);
    private final IntegerProperty optCode = new SimpleIntegerProperty(0);
    private final IntegerProperty generatedOPTCode = new SimpleIntegerProperty(0);

    public StringProperty getFirstName()
    {
        return this.firstName;
    }

    public StringProperty getLastName()
    {
        return this.lastName;
    }

    public IntegerProperty getPIN()
    {
        return this.pin;
    }

    public IntegerProperty getPhoneNumber()
    {
        return this.phoneNumber;
    }

    public IntegerProperty getOTPCode()
    {
        return this.optCode;
    }
}
