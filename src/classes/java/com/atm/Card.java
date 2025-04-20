package com.atm;

public class Card
{
    private String cardSort;
    private String cardNumber;
    private double cardBalance;
    private String cardCompany;
    private Types cardType;

    public Card(String cardSort, String cardNumber, double cardBalance, String cardCompany, Types cardType)
    {
        this.cardSort = cardSort;
        this.cardNumber = cardNumber;
        this.cardBalance = cardBalance;
        this.cardCompany = cardCompany;
        this.cardType = cardType;
    }

    // Getter methods;

    public String getCardSort()
    {
        return this.cardSort;
    }
    public String getCardNumber()
    {
        return this.cardNumber;
    }
    public double getCardBalance()
    {
        return this.cardBalance;
    }
    public String getCardCompany()
    {
        return this.cardCompany;
    }
    public Types getCardType()
    {
        return this.cardType;
    }

    // Setter methods

    public void setCardSort(String cardSort)
    {
        this.cardSort = cardSort;
    }
    public void setCardNumber(String cardNumber)
    {
        this.cardNumber = cardNumber;
    }
    public void setCardBalance(double cardBalance)
    {
        this.cardBalance = cardBalance;
    }
    public void setCompanyName(String companyName)
    {
        this.cardCompany = companyName;
    }
    public void setCardType(Types cardType)
    {
        this.cardType = cardType;
    }
}
