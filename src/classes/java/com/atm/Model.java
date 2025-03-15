package com.atm;

import java.util.Objects;

public class Model
{
    public View view;
    private Bank bank;

    // Using the State Design Pattern by extending the internal state of the model.
    // By defining two states that alters the logical behavior allows to perform different patterns.
    // Currently, the model's internal state linearly extends after the sequence of inserting number/password before login in.
    // Reference:
    // https://onjavahell.blogspot.com/2009/05/simple-example-of-state-design-pattern.html
    States state = States.DEFAULT;
    States subState = States.DEFAULT;

    int input = 0;
    int accountNumber = -1;
    int accountPassword = -1;

    String title = "Bank ATM";
    String display1 = null;
    String display2 = null;

    public Model(Bank bank)
    {
        restart("Welcome to ATM: Enter your account number");
        this.bank = bank;
    }

    public void display()
    {
        this.view.update();
    }

    private void setState(States newState, boolean extended)
    {
        if (extended) {
            if (!this.subState.equals(newState)) {
                this.subState = newState;
            }
        }
        else
        {
            if (!this.state.equals(newState)) {
                this.state = newState;
            }
        }
    }

    private void setSubState(States newState)
    {
        if (!this.subState.equals(newState))
        {
            //States oldState = this.subState;  currently unused.
            this.subState = newState;
        }
    }

    private void restart(String message)
    {
        this.setState(States.DEFAULT, false);
        this.setState(States.DEFAULT, true);
        this.input = 0;
        this.display1 = message;
        this.display2 = "Enter your account number\nFollowed by \"ENTER\"";
    }

    public void processNumber(String label)
    {
        char character = label.charAt(0);

        // maximum input is below six digits
        if (String.valueOf(this.input).length() >= 6)
        {
           return;
        }

        this.input = this.input * 10 + character - 48;
        this.display1 = "" + this.input;

        this.display();
    }

    public void processClear()
    {
        this.input = 0;
        this.display1 = "0";
        this.display();
    }

    public boolean validateInput(int input)
    {
        String inputString = String.valueOf(input);

        if (inputString.length() < 6)
        {
            System.out.println("❌ Input requires six digits: "+ input);
            return false;
        }

        return true;
    }

    public void processLogIn()
    {
        switch (this.state)
        {
            // Each state's transition should at least reset the display and the input.
            case States.DEFAULT:
                System.out.println("Opening the PIN page");

                this.input = 0;
                this.display1 = "";
                this.display2 = "LogInPIN";

                this.setState(States.ACCOUNT_NO, false);

                this.view.slideIn(States.DEFAULT);
                break;
            case States.ACCOUNT_NO:
                System.out.println("Currently at PASS page");

                if (!validateInput(this.input))
                {
                    this.input = 0;
                    this.display1 = "";
                    this.display2 = "LogInPASS";
                    this.display();
                    return;
                }

                this.input = 0;
                this.display1 = "";
                this.display2 = "LogInPASS";

                this.setState(States.PASSWORD_NO, false);

                this.view.slideIn(States.ACCOUNT_NO);
                break;
        }
        this.display();
    }

    public void processLogout()
    {
        switch (this.state)
        {
            case States.ACCOUNT_NO:
                System.out.println("Closing the PIN page");

                this.input = 0;
                this.display1 = "";
                this.display2 = "LogOut";

                this.setState(States.DEFAULT, false);

                this.view.slideOut(States.ACCOUNT_NO);
                break;
            case States.PASSWORD_NO:
                System.out.println("Currently at PASS");

                this.input = 0;
                this.display1 = "";
                this.display2 = "LogOut";

                this.setState(States.ACCOUNT_NO, false);

                this.view.slideOut(States.PASSWORD_NO);
                break;
        }
        this.display();
    }

    public void processEnter()
    {
        switch (this.state)
        {
            case States.ACCOUNT_NO:
                if (this.input <= 0)
                {
                    this.input = 0;
                    this.display1 = "0";
                    this.display2 = "Please enter a number greater than zero.\nEnter your account number\nFollowed by \"ENTER\"";
                    break;
                }

                this.accountNumber = this.input;
                this.input = 0;

                this.setState(States.PASSWORD_NO, false);

                this.display1 = "0";
                this.display2 = "Now enter your password\nFollowed by \"Ent\"";
                break;
            case States.PASSWORD_NO:
                if (this.input <= 0)
                {
                    this.input = 0;
                    this.display1 = "0";
                    this.display2 = "Please enter a number greater than zero.\nNow enter your password\nFollowed by \"ENTER\"";
                    break;
                }

                this.accountPassword = this.input;
                this.input = 0;
                this.display1 = "0";

                if (this.bank.login(this.accountNumber, this.accountPassword))
                {
                    this.display2 = "Accepted\n\nNow enter the transaction you require:\nWithdraw followed by \"W/D\"\nDeposit followed by \"Dep\"\nCheck balance followed by \"Bal\"";
                    this.setState(States.LOGGED_IN, false);
                }
                else
                {
                    this.restart("Unknown account/password");
                }
            case States.LOGGED_IN:
                switch (this.subState) {
                    case States.WITHDRAWAL:

                        // input is specified as the amount of cash to withdraw.

                        Status withdrawStatus = this.bank.withdraw(this.input);

                        System.out.println(withdrawStatus);

                         switch (withdrawStatus)
                         {
                             case Status.UNSUCCESSFUL:
                                 this.display2 = "Unexpected state alteration occurred; this account is currently logged off.";
                                 this.setState(States.DEFAULT, true);
                                 break;
                             case Status.SUCCESSFUL:
                                 this.display2 = "You have Withdrawn: " + this.input + "\n\nNow enter the additional transaction you require:\nWithdraw followed by \"W/D\"\nDeposit followed by \"Dep\"\nCheck balance followed by \"Bal\"\nLogout followed by \"Fin\"";
                                 this.setState(States.DEFAULT, true);
                                 break;
                             case Status.INSUFFICIENT_FUNDS:
                                 this.display2 = "Insufficient funds occurred.\nNow try again followed by \"Ent\"";
                                 break;
                             case Status.EXCEEDS_WITHDRAWAL:
                                 this.display2 = "Withdrawal has exceeded its threshold limit.\nBasic Accounts exceeds under 100\nPrime Accounts exceeds under 1000\nNow try again followed by \"Ent\"";
                                 break;
                         }

                        this.input = 0;
                        this.display1 = "";
                        break;
                    case States.DEPOSIT:

                        // input is specified as the amount of cash to deposit.

                        Status depositStatus = this.bank.deposit(this.input);

                        System.out.println(depositStatus);

                        switch (depositStatus)
                        {
                            case Status.UNSUCCESSFUL:
                                this.display2 = "Unexpected state alteration occurred; this account is currently logged off.";
                                this.setState(States.DEFAULT, true);
                                break;
                            case Status.SUCCESSFUL:
                                this.display2 = "You have deposited: " + this.input + "\n\nNow enter the additional transaction you require:\nWithdraw followed by \"W/D\"\nDeposit followed by \"Dep\"\nCheck balance followed by \"Bal\"\nLogout followed by \"Fin\"";
                                this.setState(States.DEFAULT, true);
                                break;
                            case Status.EXCEEDS_DEPOSIT:
                                this.display2 = "Deposit has exceeded its threshold limit.\nNow try again followed by \"Ent\"";
                                break;
                        }

                        this.input = 0;
                        this.display1 = "";
                        break;
                    case States.BALANCE:

                        // input isn't specified as it's currently unused for now.

                        double balance = this.bank.getBalance();

                        if (balance > 0)
                        {
                            this.display2 = "Your balance is currently: " + balance + "\n\nNow enter the additional transaction you require:\nWithdraw followed by \"W/D\"\nDeposit followed by \"Dep\"\nCheck balance followed by \"Bal\"\nLogout followed by \"Fin\"";
                        }
                        else
                        {
                            this.display2 = "Your balance is currently: " + balance + "\nWhich has previously been overdrawn\nNow enter the additional transaction you require:\nWithdraw followed by \"W/D\"\nDeposit followed by \"Dep\"\nCheck balance followed by \"Bal\"\nLogout followed by \"Fin\"";
                        }

                        this.setState(States.DEFAULT, true);

                        this.input = 0;
                        this.display1 = "";
                        break;
                }
        }
        this.display();
    }

    public void processWithdraw()
    {
        if (this.state.equals(States.LOGGED_IN))
        {
            if (!this.subState.equals(States.WITHDRAWAL))
            {
                this.input = 0;
                this.display1 = "";
                this.display2 = "How much do you want to Withdraw?\nNow enter the amount followed by \"Ent\"";
                this.setState(States.WITHDRAWAL, true);
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
            if (!this.subState.equals(States.DEPOSIT))
            {
                this.input = 0;
                this.display1 = "";
                this.display2 = "How much do you want to deposit?\nNow enter the amount followed by \"Ent\"";
                this.setState(States.DEPOSIT, true);
            }
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
            if (!this.subState.equals(States.BALANCE))
            {
                this.input = 0;
                this.display1 = "";
                this.display2 = "Do you want a summary of your balance? (input isn't required for now)\nNow enter the amount followed by \"Ent\"";
                this.setState(States.BALANCE, true);
            }
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
            this.setState(States.ACCOUNT_NO, false);
            this.setState(States.DEFAULT, true);
            this.input = 0;
            this.display1 = "0";
            this.display2 = "Welcome to ATM: Enter your account number";
            this.bank.logout();
        }
        else
        {
            this.restart("You are not logged in");
        }

        this.display();
    }

    //Should be cautious about this method, since it doesn't revert the slides.
    public void processUnknownKey(String action)
    {
        this.restart("Invalid command: " + action);
        this.display();
    }

    private record equals() {
    }
}
