package com.atm;

import javafx.util.Pair;

import java.util.ArrayList;

public class Model
{
    public View view;
    private Bank bank;

    // Using the State Design Pattern by extending the internal state of the model.
    // By defining two states that alter the logical behavior allows performing different patterns.
    // Currently, the model's internal state linearly extends after the sequence of inserting number/password before login in.
    // Reference:
    // https://onjavahell.blogspot.com/2009/05/simple-example-of-state-design-pattern.html

    // enum-based machine states
    private Scene currentScene = Scene.LOGIN;
    private States notifyState = States.DEFAULT;
    private States inputState = States.DEFAULT;
    private States tutorialState = States.DEFAULT;
    private States loginState = States.DEFAULT;
    private States accountState = States.DEFAULT;
    private States transactionState = States.DEFAULT;

    // Immutable variables for updates and stores.
    private int input = 0;
    private int accountNumber = -1;
    private int accountPassword = -1;

    private Card selectedCard;
    private Account selectedPayee;

    private String title = "Title";
    private String description = "description";

    public Model(Bank bank)
    {
        //restart("Welcome to ATM: Enter your account number");
        this.bank = bank;
    }

    public int getInput()
    {
        return this.input;
    }

    public int getAccountNumber()
    {
        return this.accountNumber;
    }

    public int getAccountPassword()
    {
        return this.accountPassword;
    }

    public Account getAccount()
    {
        if (this.bank.account != null)
        {
            return this.bank.cloneAccount(this.bank.account);
        }
        return null;
    }

    public ArrayList<Account> findAccountPartially(int number)
    {
        return this.bank.findAccountsPartially(number);
    }

    public String getTitle()
    {
        return this.title;
    }

    public String getDescription()
    {
        return this.description;
    }

    public void display()
    {
        this.view.update();
    }

    public void setSelectedCard(Card card)
    {
        this.selectedCard = card;
    }

    public void setSelectedPayee(Account payee)
    {
        this.selectedPayee = payee;
    }

    public void setState(Scene scene, States newState)
    {
        switch (scene)
        {
            case NOTIFY ->
                    this.notifyState = newState;
            case TUTORIAL ->
                    this.tutorialState = newState;
            case LOGIN ->
                    this.loginState = newState;
            case INPUT ->
                    this.inputState = newState;
            case ACCOUNT ->
                    this.accountState = newState;
            case TRANSACTION ->
                    this.transactionState = newState;
        }
    }

    public States getState(Scene scene)
    {
        return switch (scene)
        {
            case NOTIFY -> this.notifyState;
            case TUTORIAL -> this.tutorialState;
            case LOGIN -> this.loginState;
            case INPUT -> this.inputState;
            case ACCOUNT -> this.accountState;
            case TRANSACTION -> this.transactionState;
        };
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

        this.display();
    }

    public void processClear()
    {
        this.input = 0;
        this.display();
    }

    public void processRemove()
    {
        if (this.input > 0)
        {
            this.input = this.input / 10;
            this.display();
        }
    }

    // Helper function for checking any sequential or repeating digits from the input.
    private boolean hasSequentialOrRepeatingDigits(String input)
    {
        // ascending or descending of sequential digits
        String sequentialNumbers = "0123456789012345678909876543210987654321";

        if (sequentialNumbers.contains(input))
        {
            return true;
        }

        // Check for repeating digits (e.g., "111111", "666666")
        return input.matches("(\\d)\\1{2,}"); // Checks for any digit repeating 3 or more times
    }

    public Pair<Boolean, String> validateLoginInput(int input)
    {
        boolean successful = true;
        String message = "";

        String inputString = String.valueOf(input);

        if (inputString.length() != 6)
        {
            message += "❌ 6-digit numeric PIN only.\n";
        }

        if (!inputString.matches("\\d+"))
        {
            message += "❌ Only digits from (0-9) are allowed.\n";
        }

        if (hasSequentialOrRepeatingDigits(inputString) && inputString.length() == 6)
        {
            message += "❌ Repeated or sequential digits are not allowed.\n";
        }

        if (!message.isEmpty())
        {
            successful = false;
        }

        return new Pair<>(successful, message);
    }

    public void processInput(States state)
    {
        this.setState(Scene.INPUT, state);
        this.display();
    }

    public void processTransaction(States state)
    {

        switch (state)
        {
            case DEFAULT, TRANSACTION_PAGE:
                this.input = 0;
                this.selectedCard = null;
                this.selectedPayee = null;
                break;
        }

        System.out.println(selectedCard + " " + selectedPayee);

        this.setState(Scene.TRANSACTION, state);

        this.display();
    }

    public void processAccount(States state)
    {
        this.setState(Scene.ACCOUNT, state);
        this.display();
    }

    public void processNotification(boolean reverse)
    {
        if (reverse)
        {
            switch (this.notifyState)
            {
                case NOTIFY_SHOW:
                    this.setState(Scene.NOTIFY, States.DEFAULT);
                    break;
            }
        }
        else
        {
            // Move forward through tutorial states
            switch (this.notifyState)
            {
                case DEFAULT:
                    this.setState(Scene.NOTIFY, States.NOTIFY_SHOW);
                    break;
            }
        }
        this.display();
    }

    public void processTutorial(boolean reverse) {
        if (reverse)
        {
            switch (this.tutorialState)
            {
                case TUTORIAL_ONE:
                    this.setState(Scene.TUTORIAL, States.DEFAULT);
                    break;
                case TUTORIAL_TWO:
                    this.setState(Scene.TUTORIAL, States.TUTORIAL_TWO);
                    break;
                case TUTORIAL_THREE:
                    this.setState(Scene.TUTORIAL, States.TUTORIAL_THREE);
                    break;
            }
        }
        else
        {
            // Move forward through tutorial states
            switch (this.tutorialState)
            {
                case DEFAULT:
                    this.setState(Scene.TUTORIAL, States.TUTORIAL_ONE);
                    break;
                case TUTORIAL_ONE:
                    this.setState(Scene.TUTORIAL, States.TUTORIAL_TWO);
                    break;
                case TUTORIAL_TWO:
                    this.setState(Scene.TUTORIAL, States.TUTORIAL_THREE);
                    break;
                case TUTORIAL_THREE:
                    this.setState(Scene.TUTORIAL, States.DEFAULT);
                    break;
            }
        }
        this.display();
    }

    public void processLogIn(boolean reverse)
    {
        if (reverse)
        {
            switch (this.loginState)
            {
                case LOGIN_ONE:
                    this.input = 0;
                    this.setState(Scene.LOGIN, States.DEFAULT);
                    this.setState(Scene.INPUT, States.DEFAULT);
                    break;
                case LOGIN_TWO:
                    this.input = 0;
                    this.setState(Scene.INPUT, States.INPUT_DIGIT);
                    this.setState(Scene.LOGIN, States.LOGIN_ONE);
                    break;
                case LOGIN_THREE:
                    this.setState(Scene.LOGIN, States.LOGIN_TWO);
                    break;
            }
        }
        else
        {
            // Move forward through tutorial states
            switch (this.loginState)
            {
                case DEFAULT:
                    this.input = 0;
                    this.setState(Scene.LOGIN, States.LOGIN_ONE);
                    this.setState(Scene.INPUT, States.INPUT_DIGIT); //Pin page
                    break;
                case LOGIN_ONE:

                    // using Pair by supporting multiple values from returning.
                    Pair<Boolean, String> loginResultPIN = validateLoginInput(this.input);

                    // verify if the PIN input
                    if (!loginResultPIN.getKey())
                    {
                        this.input = 0;
                        this.title = "Incorrect PIN";
                        this.description = loginResultPIN.getValue();
                        this.setState(Scene.NOTIFY, States.NOTIFY_SHOW);
                        this.setState(Scene.INPUT, States.INPUT_DIGIT);

                        this.display();
                        return;
                    }

                    this.accountNumber = this.input;

                    this.input = 0;

                    this.setState(Scene.INPUT, States.INPUT_DIGIT);
                    this.setState(Scene.LOGIN, States.LOGIN_TWO);
                    this.setState(Scene.TUTORIAL, States.DEFAULT); //Pass page
                    break;
                case LOGIN_TWO:

                    // using Pair by supporting multiple values from returning.
                    Pair<Boolean, String> loginResultPASS = validateLoginInput(this.input);

                    if (!loginResultPASS.getKey())
                    {
                        this.input = 0;
                        this.title = "Incorrect PASS";
                        this.description = loginResultPASS.getValue();
                        this.setState(Scene.NOTIFY, States.NOTIFY_SHOW);
                        this.setState(Scene.INPUT, States.INPUT_DIGIT);

                        this.display();
                        return;
                    }

                    this.accountPassword = this.input;

                    // verify if the account exists via two stored variables

                    boolean loginAccount = this.bank.login(this.accountNumber, this.accountPassword);

                    if (!loginAccount)
                    {
                        this.input = 0;
                        this.title = "Unregistered Account";
                        this.description = "The matched information that you provided is not registered.\n\nMake sure that your PIN and PASS are correct and try again.";
                        this.setState(Scene.NOTIFY, States.NOTIFY_SHOW);
                        this.setState(Scene.INPUT, States.INPUT_DIGIT);

                        this.display();
                        return;
                    }

                    // if the information matches with an existing account, log with the current account.

                    this.input = 0;
                    this.setState(Scene.LOGIN, States.LOGIN_THREE);
                    this.setState(Scene.INPUT, States.DEFAULT);
                    break;
                case LOGIN_THREE:
                    //Agreement page
                    this.input = 0;
                    this.setState(Scene.ACCOUNT, States.HOME_PAGE);
                    this.setState(Scene.LOGIN, States.LOGIN_FOUR);
                    break;
                case LOGIN_FOUR:
                    this.input = 0;
                    this.setState(Scene.LOGIN, States.DEFAULT);
                    break;
            }
        }
        this.display();
    }

/*
    public void processLogIn2()
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

                //this.view.slideIn(States.DEFAULT);
                break;
            case States.ACCOUNT_NO:
                System.out.println("Currently at PASS page");

                if (!validateInput(this.input))
                {
                    this.input = 0;
                    this.display1 = "";
                    this.display2 = "LogInPASS";
                  //  this.display();
                    return;
                }

                this.input = 0;
                this.display1 = "";
                this.display2 = "LogInPASS";

                this.setState(States.PASSWORD_NO, false);

                //this.view.slideIn(States.ACCOUNT_NO);
                break;
            case States.PASSWORD_NO:

                if (!validateInput(this.input))
                {
                    this.input = 0;
                    this.display1 = "";
                    this.display2 = "LogInPASS";
                  //  this.display();
                    return;
                }

                System.out.println("Currently at Profile page");

                this.input = 0;
                this.display1 = "";
                this.display2 = "Profile";

                this.setState(States.LOGGED_IN, false);

                //this.view.slideIn(States.PASSWORD_NO);
                break;
        }
      //  this.display();
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

                //this.view.slideOut(States.ACCOUNT_NO);
                break;
            case States.PASSWORD_NO:
                System.out.println("Currently at PASS");

                this.input = 0;
                this.display1 = "";
                this.display2 = "LogOut";

                this.setState(States.ACCOUNT_NO, false);

                //this.view.slideOut(States.PASSWORD_NO);
                break;
            case States.LOGGED_IN:
                System.out.println("Currently at the Profile");

                this.input = 0;
                this.display1 = "";
                this.display2 = "Profile";

                this.setState(States.PASSWORD_NO, false);

                //this.view.slideOut(States.LOGGED_IN);
                break;
        }
       // this.display();
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
      //  this.display();
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

       // this.display();
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

       // this.display();
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

        //this.display();
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

      //  this.display();
    }

    //Should be cautious about this method, since it doesn't revert the slides.
    public void processUnknownKey(String action)
    {
        this.restart("Invalid command: " + action);
       // this.display();
    }
*/
}
