package com.atm;

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

    public void processString(String action, String string)
    {
        switch (action)
        {
            case "FirstName" -> this.model.setFirstName(string);
            case "LastName" -> this.model.setLastName(string);
            case "CardNumber" -> {
                this.model.setState(Scene.PROCESS, States.PROCESS_CARDNUMBER);
                this.model.setCardNumber(string);
            }
            case "PhoneNumber" -> {
                this.model.setState(Scene.PROCESS, States.PROCESS_PHONE);
                this.model.setPhoneNumber(string);
            }
            case "ExpiryDate" -> {
                this.model.setState(Scene.PROCESS, States.PROCESS_DATE);
                this.model.setExpiryDate(string);
            }
            case "CVVNumber" -> {
                this.model.setState(Scene.PROCESS, States.PROCESS_CVV);
                this.model.setCVVNumber(string);
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
            case "Open-Input":
                this.model.processInput(States.INPUT_DIGIT);
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
