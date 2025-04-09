package com.atm;

public class Controller
{
    public Model model;

    public Controller()
    {
    }

    // Had to use overloading methods for accepting a different type of parameters

    public void process(String action, Card card)
    {
        System.out.println(action);
        switch (action)
        {
            case "Transaction-page-select":
                this.model.setSelectedCard(card);
                break;
            default:
                //  this.model.processUnknownKey(action);
        }
    }

    public void process(String action, Account account)
    {
        System.out.println(action);
        switch (action)
        {
            case "Transaction-page-select":
                this.model.setSelectedPayee(account);
                break;
            default:
                //  this.model.processUnknownKey(action);
        }
    }

    public void process(String action)
    {

        System.out.println(action);
        // Using switch statement to evaluate each keys assigned within the UI.
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
            case "Clear-Input":
                this.model.processClear();
                break;
            case "Remove-Input":
                this.model.processRemove();
                break;
            case "enter":
                //this.model.processEnter();
                break;
            case "W/D":
              //  this.model.processWithdraw();
                break;
            case "Dep":
               // this.model.processDeposit();
                break;
            case "Bal":
               // this.model.processBalance();
                break;
            case "cancel":
                //this.model.processFinish();
                break;
            case "LogIn":
                //this.model.processLogIn();
                break;
            case "LogOut":
              //  this.model.processLogout();
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
            case "Home-page":
                this.model.processAccount(States.HOME_PAGE);
                break;
            case "Wallet-page":
                this.model.processAccount(States.WALLET_PAGE);
                break;
            case "Profile-page":
                this.model.processAccount(States.PROFILE_PAGE);
                break;
            case "Close-page":
                this.model.processTransaction(States.DEFAULT);
                break;
            case "Transaction-page":
                this.model.processTransaction(States.TRANSACTION_PAGE);
                break;
            default:
              //  this.model.processUnknownKey(action);
        }
    }
}
