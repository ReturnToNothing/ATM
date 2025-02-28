package com.atm;

import java.util.Objects;

public class Model
{
    public View view;
    private Bank bank;

    States state = States.ACCOUNT_NO;
    States subState = States.WITHDRAWAL;

    int input = 0;
    int accountNumber = -1;
    int accountPassword = -1;

    String title = "Bank ATM";
    String display1 = null;
    String display2 = null;

    public Model(Bank bank)
    {
        restart("Welcome to ATM");
        this.bank = bank;
    }

    public void display()
    {
        this.view.update();
    }

    private void setState(States newState)
    {
        if (!this.state.equals(newState))
        {
            States oldState = this.state;
            this.state = newState;
        }
    }

    private void setSubState(States newState)
    {
        if (!this.subState.equals(newState))
        {
            States oldState = this.subState;
            this.subState = newState;
        }
    }

    private void restart(String message)
    {
        this.setState(States.ACCOUNT_NO);
        this.setSubState(States.DEFAULT);
        this.input = 0;
        this.display1 = message;
        this.display2 = "Enter your account number\nFollowed by \"Ent\"";
    }

    public void processNumber(String label)
    {
        char character = label.charAt(0);

        this.input = this.input * 10 + character - 48;
        this.display1 = "" + this.input;

        this.display();
    }

    public void processClear()
    {
        this.input = 0;
        this.display1 = "";
        this.display();
    }

    public void processEnter()
    {
        switch (this.state)
        {
            case States.ACCOUNT_NO:
                this.accountNumber = this.input;
                this.input = 0;

                this.setState(States.PASSWORD_NO);

                this.display1 = "";
                this.display2 = "Now enter your password\nFollowed by \"Ent\"";
                break;
            case States.PASSWORD_NO:
                this.accountPassword = this.input;
                this.input = 0;
                this.display1 = "";

                if (this.bank.login(this.accountNumber, this.accountPassword))
                {
                    this.setState(States.LOGGED_IN);
                    this.display2 = "Accepted\n\nNow enter the transaction you require:\nWithdraw followed by \"W/D\"\nDeposit followed by \"Dep\"\nCheck balance followed by \"Bal\"";
                }
                else
                {
                    this.restart("Unknown account/password");
                }
            case States.LOGGED_IN:
                if (this.subState.equals(States.WITHDRAWAL))
                {
                    if (this.input > 0)
                    {
                        if (this.bank.withdraw(this.input))
                        {
                            this.display2 = "You have Withdrawn: " + this.input + "\n\nNow enter the additional transaction you require:\nWithdraw followed by \"W/D\"\nDeposit followed by \"Dep\"\nCheck balance followed by \"Bal\"";
                            this.setSubState(States.DEFAULT);
                        }
                        else
                        {
                            this.display2 = "Insufficient funds.\nNow try again followed by \"Ent\"";
                        }
                    }
                    else
                    {
                        this.display2 = "negative/zero value is unacceptable.\nNow try again followed by \"Ent\"";
                    }

                    this.input = 0;
                    this.display1 = "";
                }
        }
        this.display();
    }

    public void processWithdraw()
    {
        if (this.state.equals(States.LOGGED_IN))
        {
            if (this.subState.equals(States.WITHDRAWAL))
            {
                this.input = 0;
                this.display1 = "";
                this.display2 = "How much do you want to Withdraw?\nNow enter the amount followed by \"Ent\"";
                this.setSubState(States.WITHDRAWAL);
            }
        }
        else
        {
            this.restart("You are not logged in");
        }

        this.display();
    }

    public void processDeposit()
    {
        if (this.state.equals(States.LOGGED_IN))
        {
            this.bank.deposit(this.input);
            this.display1 = "";
            this.display2 = "Deposited: " + this.input;
            this.input = 0;
        }
        else
        {
            this.restart("You are not logged in");
        }

        this.display();
    }

    public void processBalance()
    {
        if (this.state.equals(States.LOGGED_IN))
        {
            this.input = 0;
            this.display2 = "Your balance is: " + this.bank.getBalance();
        }
        else
        {
            this.restart("You are not logged in");
        }

        this.display();
    }

    public void processFinish()
    {
        if (this.state.equals(States.LOGGED_IN))
        {
            this.setState(States.ACCOUNT_NO);
            this.input = 0;
            this.display2 = "Welcome: Enter your account number";
            this.bank.logout();
        }
        else
        {
            this.restart("You are not logged in");
        }

        this.display();
    }

    public void processUnknownKey(String action)
    {
        this.restart("Invalid command");
        this.display();
    }
}
