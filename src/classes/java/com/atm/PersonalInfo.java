package com.atm;

import javafx.beans.property.*;

public class PersonalInfo
{
    private final StringProperty firstName = new SimpleStringProperty("");
    private final StringProperty lastName = new SimpleStringProperty("");
    private final LongProperty phoneNumber = new SimpleLongProperty(0);
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

    public LongProperty getPhoneNumber()
    {
        return this.phoneNumber;
    }

    public IntegerProperty getOTPCode()
    {
        return this.optCode;
    }

    public IntegerProperty getGeneratedOPTCode()
    {
        return this.generatedOPTCode;
    }

    public void clear()
    {
        this.firstName.set("");
        this.lastName.set("");
        this.pin.set(0);
        this.phoneNumber.set(0);
        this.optCode.set(0);
        this.generatedOPTCode.set(0);
    }
}
