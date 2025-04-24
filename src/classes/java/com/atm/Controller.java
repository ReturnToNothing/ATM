package com.atm;

import javafx.beans.property.IntegerProperty;
import javafx.beans.property.LongProperty;
import javafx.beans.property.StringProperty;

import java.util.Set;

public class Controller
{
    public Model model;
    
    public Controller()
    {
    }

    // Had to use overloading methods for accepting a different type of parameters
    public void process(String action, Card card)
    {
        switch (action)
        {
            case "Transaction-page-select":
                this.model.setSelectedCard(card);
                this.model.display();
                break;
            default:
                //  this.model.processUnknownKey(action);
        }
    }

    public void process(String action, Account account)
    {
        switch (action)
        {
            case "Transaction-page-select":
                this.model.setSelectedPayee(account);
                this.model.display();
                break;
            default:
                //  this.model.processUnknownKey(action);
        }
    }

    public void processBalance(String action)
    {
        // Using a switch statement to evaluate each key assigned within the UI.
        switch (action) {
            case "1":
            case "2":
            case "3":
            case "4":
            case "5":
            case "6":
            case "7":
            case "8":
            case "9":
            case "0":
                this.model.processBalance(action);
                break;
            case "Clear-Input":
                this.model.processClear();
                break;
            case "Remove-Input":
                this.model.processRemove();
                break;
            default:
                //  this.model.processUnknownKey(action);
        }
    }

    public void processString(String action)
    {
        Set<String> validInputs = Set.of(
                "A", "B", "C", "D", "E", "F", "G", "H", "I",
                "J", "K", "L", "M", "N", "O", "P", "Q", "R",
                "S", "T", "U", "V", "W", "X", "Y", "Z"
        );
        switch (action)
        {
            case "Clear-Input" -> this.model.processClear();
            case "Remove-Input" -> this.model.processRemove();
            default ->
            {
                if (validInputs.contains(action))
                {
                    this.model.processString(action);
                }
                else
                {
                    // this.model.processUnknownKey(action);
                }
            }
        }
    }

    public void processNumericalString(String action)
    {
        switch (action)
        {
            case "1":
            case "2":
            case "3":
            case "4":
            case "5":
            case "6":
            case "7":
            case "8":
            case "9":
            case "0":
                this.model.processString(action);
                break;
            case "Clear-Input":
                this.model.processClear();
                break;
            case "Remove-Input":
                this.model.processRemove();
                break;
            default:
                //  this.model.processUnknownKey(action);
        }
    }

    public void processSet(String action)
    {
        PersonalInfo personalInfo = this.model.getPersonalInfo();
        CardInfo cardInfo = this.model.getCardInfo();
        switch (action)
        {
            case "FirstName" ->
            {
                StringProperty firstName = personalInfo.getFirstName();
                this.model.setInfo(firstName);
                this.model.setState(Scene.PROCESS, States.PROCESS_FIRST);
            }
            case "LastName" ->
            {
                StringProperty lastName = personalInfo.getLastName();;
                this.model.setInfo(lastName);
                this.model.setState(Scene.PROCESS, States.PROCESS_LAST);
            }
            case "PIN" ->
            {
                IntegerProperty PIN = personalInfo.getPIN();
                this.model.setInfo(PIN);
                this.model.setState(Scene.PROCESS, States.DEFAULT);
            }
            case "PhoneNumber" ->
            {
                LongProperty phoneNumber = personalInfo.getPhoneNumber();
                this.model.setInfo(phoneNumber);
                this.model.setState(Scene.PROCESS, States.PROCESS_PHONE);
            }
            case "OPT" ->
            {
                IntegerProperty OPT = personalInfo.getOTPCode();
                this.model.setInfo(OPT);
                this.model.setState(Scene.PROCESS, States.DEFAULT);
            }
            case "CardNumber" ->
            {
                LongProperty cardNumber = cardInfo.getCardNumber();
                this.model.setInfo(cardNumber);
                this.model.setState(Scene.PROCESS, States.PROCESS_CARDNUMBER);
            }
            case "ExpirationDate" ->
            {
                StringProperty expirationDate = cardInfo.getExpirationDate();
                this.model.setInfo(expirationDate);
                this.model.setState(Scene.PROCESS, States.PROCESS_DATE);
            }
            case "CVV" ->
            {
                IntegerProperty CVV = cardInfo.getCVVCode();
                this.model.setInfo(CVV);
                this.model.setState(Scene.PROCESS, States.PROCESS_CVV);
            }
            case "Issuer" ->
            {
                StringProperty issuer = cardInfo.getCardIssuer();
                this.model.setInfo(issuer);
                this.model.setState(Scene.PROCESS, States.PROCESS_ISSUER);
            }
            case "Type" ->
            {
                StringProperty type = cardInfo.getAccountType();
                this.model.setInfo(type);
                this.model.setState(Scene.PROCESS, States.DEFAULT);
            }
            default ->
            {
                // this.model.processUnknownKey(action);
            }
        }
    }

    public void processSet(String action, Object value)
    {
        PersonalInfo personalInfo = this.model.getPersonalInfo();
        CardInfo cardInfo = this.model.getCardInfo();
        switch (action)
        {
            case "FirstName" ->
            {
                StringProperty firstName = personalInfo.getFirstName();
                this.model.setInfo(firstName, value);
            }
            case "LastName" ->
            {
                StringProperty lastName = personalInfo.getLastName();;
                this.model.setInfo(lastName, value);
            }
            case "PIN" ->
            {
                IntegerProperty PIN = personalInfo.getPIN();
                this.model.setInfo(PIN, value);
            }
            case "PhoneNumber" ->
            {
                LongProperty phoneNumber = personalInfo.getPhoneNumber();
                this.model.setInfo(phoneNumber, value);
            }
            case "OPT" ->
            {
                IntegerProperty OPT = personalInfo.getOTPCode();
                this.model.setInfo(OPT, value);
            }
            case "CardNumber" ->
            {
                LongProperty cardNumber = cardInfo.getCardNumber();
                this.model.setInfo(cardNumber, value);
            }
            case "ExpirationDate" ->
            {
                StringProperty expirationDate = cardInfo.getExpirationDate();
                this.model.setInfo(expirationDate, value);
            }
            case "CVV" ->
            {
                IntegerProperty CVV = cardInfo.getCVVCode();
                this.model.setInfo(CVV, value);
            }
            case "Issuer" ->
            {
                StringProperty issuer = cardInfo.getCardIssuer();
                this.model.setInfo(issuer, value);
                this.model.display();
            }
            case "Type" ->
            {
                StringProperty type = cardInfo.getAccountType();
                this.model.setInfo(type, value);
                this.model.display();
            }
            default ->
            {
                // this.model.processUnknownKey(action);
            }
        }
    }

    public void process(String action)
    {
        // Using a switch statement to evaluate each key assigned within the UI.
        switch (action)
        {
            case "1":
            case "2":
            case "3":
            case "4":
            case "5":
            case "6":
            case "7":
            case "8":
            case "9":
            case "0":
                this.model.processNumber(action);
                break;
            case "Update":
                this.model.display();
                break;
            case "Clear-Input":
                this.model.processClear();
                break;
            case "Remove-Input":
                this.model.processRemove();
                break;
            case "Open-Input-Balance":
                this.model.processInput(States.INPUT_BALANCE);
                break;
            case "Open-Input-String":
                this.model.processInput(States.INPUT_STRING);
                break;
            case "Open-Input-Numeric-String":
                this.model.processInput(States.INPUT_STRING_NUMERIC);
                break;
            case "Open-Input":
                this.model.processInput(States.INPUT_NUMERIC);
                break;
            case "Close-Input":
                this.model.processInput(States.DEFAULT);
                break;
            case "Open-Notify":
                this.model.processNotification(false);
                break;
            case "Close-Notify":
                this.model.processNotification(true);
                break;
            case "Start-Tutorial":
                this.model.processTutorial(false);
                break;
            case "Return-Tutorial":
                this.model.processTutorial(true);
                break;
            case "Start-LogIn":
                this.model.processLogIn(false);
                break;
            case "Return-LogIn":
                this.model.processLogIn(true);
                break;
            case "Start-SignIn":
                this.model.processSignIn(false);
                break;
            case "Return-SignIn":
                this.model.processSignIn(true);
                break;
            case "Home-page":
                this.model.processAccount(States.HOME_PAGE);
                break;
            case "Wallet-page":
                this.model.processAccount(States.WALLET_PAGE);
                break;
            case "Profile-page":
                this.model.processAccount(States.PROFILE_PAGE);
                break;
            case "Transaction-page-close":
                this.model.processTransaction(true);
                break;
            case "Transaction-page":
                this.model.processTransaction(false);
                break;
            case "Transaction-summary-close":
                this.model.processTransactionSummary(true);
                break;
            case "Transaction-summary":
                this.model.processTransactionSummary(false);
                break;
            default:
              //  this.model.processUnknownKey(action);
        }
    }
}
