package com.atm;

import javafx.animation.Interpolator;
import javafx.animation.PauseTransition;
import javafx.beans.property.IntegerProperty;
import javafx.beans.property.Property;
import javafx.beans.property.StringProperty;
import javafx.util.Duration;
import javafx.util.Pair;

import java.security.SecureRandom;
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
    private States signinState = States.DEFAULT;
    private States accountState = States.DEFAULT;
    private States transactionState = States.DEFAULT;
    private States processState = States.DEFAULT;

    // Immutable variables for updates and stores.
    private long input = 0;
    private String inputString = "";
    private double inputBalance = 0.00f;

    private int accountNumber = -1;
    private int accountPassword = -1;

    private Card selectedCard;
    private Account selectedPayee;

    private String title = "Title";
    private String description = "description";

    private final PersonalInfo personalInfo = new PersonalInfo();
    private final CardInfo cardInfo = new CardInfo();

    public Model(Bank bank)
    {
        this.bank = bank;
    }

    public PersonalInfo getPersonalInfo()
    {
        return this.personalInfo;
    }

    public CardInfo getCardInfo()
    {
        return this.cardInfo;
    }

    public long getInput()
    {
        return this.input;
    }

    public String getStringInput()
    {
        return this.inputString;
    }

    public double getInputBalance()
    {
        return this.inputBalance / 100.00;
    }

    public Account getAccount()
    {
        if (this.bank.account != null)
        {
            return this.bank.cloneAccount();
        }
        return null;
    }

    public ArrayList<Account> findAccountPartially(long number)
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

    public void setCardIssuer(String cardIssuer)
    {
        if (cardIssuer.isEmpty())
        {
            this.cardIssuer = cardIssuer;
            this.inputString = "";
            return;
        }
        this.cardIssuer = cardIssuer;
        this.inputString = cardIssuer;
    }

    public void setAccountType(String accountType)
    {
        if (accountType.isEmpty())
        {
            this.accountType = accountType;
            this.inputString = "";
            return;
        }
        this.accountType = accountType;
        this.inputString = accountType;
    }

    public void setCardNumber(String cardNumber)
    {
        if (cardNumber.isEmpty())
        {
            this.cardNumber = cardNumber;
            this.input = 0;
            return;
        }
        this.cardNumber = cardNumber;
        this.input = Long.parseLong(cardNumber);
    }

    public void setExpiryDate(String expiryDate)
    {
        if (expiryDate.isEmpty())
        {
            this.expiryDate = expiryDate;
            this.input = 0;
            return;
        }
        this.expiryDate = expiryDate;
        this.input = Integer.parseInt(expiryDate);
    }

    public void setCVVNumber(String cvvNumber)
    {
        if (cvvNumber.isEmpty())
        {
            this.cvvNumber = cvvNumber;
            this.input = 0;
            return;
        }
        this.cvvNumber = cvvNumber;
        this.input = Integer.parseInt(cvvNumber);
    }

    public void setPersonalInfo(Property<?> property)

    // Sets the first name to the provided input and updates the inputString reference.
    public void setFirstName(String input)
    {
        StringProperty firstName = this.personalInfo.getFirstName();
        firstName.set(input);
        this.inputString = firstName.get();
    }

    // Overloaded method: assigns the current firstName value to inputString reference without reassignments.
    public void setFirstName()
    {
        this.inputString = this.personalInfo.getFirstName().get();
    }

    public void setLastName(String input)
    {
        StringProperty lastName = this.personalInfo.getLastName();
        lastName.set(input);
        this.inputString = lastName.get();
    }

    public void setLastName()
    {
        this.inputString = this.personalInfo.getLastName().get();
    }

    public void setNewPIN(int input)
    {
        IntegerProperty newPIN = this.personalInfo.getPIN();
        newPIN.set(input);
        this.input = newPIN.get();
    }

    public void setNewPIN()
    {
        this.input = this.
    }

    public void setPassword(String password)
    {
        System.out.println(password);
        if (password.isEmpty())
        {
            this.accountPassword = 0;
            this.input = 0;
            return;
        }
        this.accountPassword = Integer.parseInt(password);
        this.input = Long.parseLong(password);
    }

    public void setPhoneNumber(String phoneNumber)
    {
        if (phoneNumber.isEmpty())
        {
            this.phoneNumber = phoneNumber;
            this.input = 0;
            return;
        }

        this.phoneNumber = phoneNumber;
        this.input = Long.parseLong(phoneNumber);
    }

    public void setOTPCode(String OTPCode)
    {
        if (OTPCode.isEmpty())
        {
            this.OTPCode = OTPCode;
            this.input = 0;
            return;
        }

        this.OTPCode = OTPCode;
        this.input = Long.parseLong(OTPCode);
    }

    public void setSelectedCard(Card card)
    {
        this.selectedCard = card;

        if (this.bank.account != null)
        {
            this.bank.account.setCard(card);
        }
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
            case SIGNIN ->
                    this.signinState = newState;
            case INPUT ->
                    this.inputState = newState;
            case ACCOUNT ->
                    this.accountState = newState;
            case TRANSACTION ->
                    this.transactionState = newState;
            case PROCESS ->
                    this.processState = newState;
        }
    }

    public States getState(Scene scene)
    {
        return switch (scene)
        {
            case NOTIFY -> this.notifyState;
            case TUTORIAL -> this.tutorialState;
            case LOGIN -> this.loginState;
            case SIGNIN -> this.signinState;
            case INPUT -> this.inputState;
            case ACCOUNT -> this.accountState;
            case TRANSACTION -> this.transactionState;
            case PROCESS -> this.processState;
        };
    }

    public void processState(States state)
    {
        this.setState(Scene.PROCESS, state);
        this.display();
    }

    public void processBalance(String label)
    {
        char character = label.charAt(0);

        this.inputBalance = this.inputBalance * 10 + (character - '0');

        this.display();
    }

    public void processNumber(String label)
    {
        States processState = this.getState(Scene.PROCESS);

        switch (processState)
        {
            case PROCESS_PHONE:
            {
                char character = label.charAt(0);

                if (String.valueOf(this.input).length() >= 10)
                {
                    return;
                }

                this.input = this.input * 10 + character - 48;

                break;
            }
            case PROCESS_DATE:
            {
                char character = label.charAt(0);

                // maximum input is four
                if (String.valueOf(this.input).length() >= 4)
                {
                    return;
                }

                long proposedInput = this.input * 10 + character - 48;

                int length = String.valueOf(proposedInput).length();

                if (length == 1)
                {
                    // The First digit must be 0 or 1 (month constraint)
                    if (character > '1')
                    {
                        return;
                    }
                }
                else if (length == 2)
                {
                    long month = proposedInput;
                    // The Second digit must only be 1/12 based on the months.
                    if (proposedInput < 1 || month > 12) {
                        return;  // Invalid month, reject input.
                    }
                }

                this.input = proposedInput;
                break;
            }
            case PROCESS_CARDNUMBER:
            {
                char character = label.charAt(0);

                // maximum input is below six digits
                if (String.valueOf(this.input).length() >= 16)
                {
                    return;
                }

                this.input = this.input * 10 + character - 48;

                break;
            }
            case PROCESS_CVV:
            {
                char character = label.charAt(0);

                if (String.valueOf(this.input).length() >= 3)
                {
                    return;
                }

                this.input = this.input * 10 + character - 48;

                break;
            }
            default:
            {
                char character = label.charAt(0);

                // maximum input is below six digits
                if (String.valueOf(this.input).length() >= 6)
                {
                    return;
                }

                this.input = this.input * 10 + character - 48;
                break;
            }
        }

        this.display();
    }

    public void processString(String label)
    {
        // Fetch the first character only
        char character = label.charAt(0);

        // Condition needed here
        this.inputString += character;

        this.display();
    }

    public void processClear()
    {
        States inputState = this.getState(Scene.INPUT);

        if (inputState.equals(States.INPUT_DIGIT))
        {
            this.input = 0;
        }
        else if (inputState.equals(States.INPUT_BALANCE))
        {
            this.inputBalance = 0.00f;
        }
        else if (inputState.equals(States.INPUT_STRING)) {
            this.inputString = "";
        }

        this.display();
    }

    public void processRemove()
    {
        States inputState = this.getState(Scene.INPUT);

        if (inputState.equals(States.INPUT_DIGIT))
        {
            if (this.input > 0)
            {
                this.input = this.input / 10;
            }
        }

        if (inputState.equals(States.INPUT_BALANCE))
        {
            // Previously, using the last method could indefinitely divide the balance,
            // despite it's below the 2nd decimals.

            // Instead, we verify if the balance isn't below the 2nd decimal place,
            // otherwise the balance should completely be set as zero.

            // Assume that the balance (10 = 0.10) is larger for acceptance division
            if (this.inputBalance >= 10)
            {
                this.inputBalance = this.inputBalance / 10;
            }
            else
            {
                this.inputBalance = 0.00f;
            }
        }

        if (inputState.equals(States.INPUT_STRING))
        {
            // Removing the last string based on its length - 1
            if (!this.inputString.isEmpty())
            {
                this.inputString = this.inputString.substring(0, this.inputString.length() - 1);
            }
        }

        this.display();
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

    public Pair<Boolean, String> validateLoginInput(long input)
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

    // referenced from: https://simplycalc.com/luhn-source.php
    private boolean isValidLuhn(String number)
    {
        int length = number.length();
        int parity = length % 2;
        int sum = 0;

        // The Number is split into digits, and every second digit is doubled from right to left.
        for (int index = number.length() - 1; index >= 0; index--)
        {
            int digit = number.charAt(index);

            // every two digits, double it
            if (index % 2 == parity)
            {
                digit *= 2;
            }

            // any digits resulted more than 9, subtract it
            if (digit > 9)
            {
                digit -= 9;
            }

            sum += digit;
        }

        // lastly, use modulo operation with the sum
        return (sum % 10 == 0);
    }

    public void processInput(States state)
    {
        this.setState(Scene.INPUT, state);
        this.display();
    }

    public void processTransaction(boolean close)
    {
        if (close)
        {
            this.setState(Scene.TRANSACTION, States.DEFAULT);
            this.display();
            return;
        }

        switch (this.transactionState)
        {
            // If the transaction is closed, clear any input.
            case DEFAULT:
                this.input = 0;
                this.inputBalance = 0;
                this.selectedCard = null;
                this.selectedPayee = null;
                this.setState(Scene.TRANSACTION, States.TRANSACTION_PAGE);
                break;
            case TRANSACTION_PAGE:
                // confirmation section, ensuring that the inputs are filled and verified,
                // revert to the TRANSACTION_PAGE if any isn't verified.

                String title = "Incomplete Transaction";
                String description = "";
                boolean successful = true;

                if (this.selectedCard == null)
                {
                    description += "❌ Must at least select any card.\n\n";
                    successful = false;
                }

                if (this.selectedPayee == null)
                {
                    description += "❌ Must at least select payee.\n\n";
                    successful = false;
                }

                if (inputBalance <= 1)
                {
                    description += String.format("❌ Minimum amount is £0.01. (%s)\n\n", this.getInputBalance());
                    successful = false;
                }

                if (!successful)
                {
                    this.title = title;
                    this.description = description;
                    this.setState(Scene.NOTIFY, States.NOTIFY_SHOW);
                    break;
                }

                // If successful, move onto the confirmation scene
                this.setState(Scene.TRANSACTION, States.TRANSACTION_CONFIRM);
                break;
            case TRANSACTION_CONFIRM:
                // Confirmation
                this.bank.transfer(this.selectedPayee, this.getInputBalance());
                this.setState(Scene.TRANSACTION, States.TRANSACTION_COMPLETE);

                this.input = 0;
                this.inputBalance = 0;
                this.selectedCard = null;
                this.selectedPayee = null;

                // add duration like 1 second
                PauseTransition pause = new PauseTransition(Duration.seconds(1));
                pause.setOnFinished(event -> this.setState(Scene.TRANSACTION, States.DEFAULT));
                pause.play();
                break;
        }

        this.display();
    }

    public void processTransactionSummary(boolean close)
    {
        if (close)
        {
            this.setState(Scene.TRANSACTION, States.DEFAULT);
            this.display();
            return;
        }

        switch (this.transactionState)
        {
            // If the transaction is closed, clear any input.
            case DEFAULT:
                this.input = 0;
                this.inputBalance = 0;
                this.selectedCard = null;
                this.selectedPayee = null;
                this.setState(Scene.TRANSACTION, States.TRANSACTION_SUMMARY);
                break;
            case TRANSACTION_SUMMARY:
                this.setState(Scene.TRANSACTION, States.DEFAULT);
                break;
        }

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

                    // ensuring that the Sign-in scene
                    this.setState(Scene.SIGNIN, States.DEFAULT);

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

                    this.accountNumber = (int) this.input;

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

                    this.accountPassword = (int) this.input;

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

    public void processSignIn(boolean reverse)
    {
        if (reverse)
        {
            switch (this.signinState)
            {
                case SIGN_ONE:
                    this.input = 0;
                    this.inputString = "";
                    this.firstName = "";
                    this.lastName = "";
                    this.phoneNumber = "";
                    this.OTPCode = "";
                    this.expiryDate = "";
                    this.cvvNumber = "";
                    this.setState(Scene.SIGNIN, States.DEFAULT);
                    break;
                case SIGN_TWO:
                    this.input = 0;
                    this.inputString = "";
                    this.setState(Scene.SIGNIN, States.SIGN_ONE);
                    break;
                case SIGN_THREE:
                    this.input = 0;
                    this.inputString = "";
                    this.setState(Scene.SIGNIN, States.SIGN_TWO);
                    break;
                case SIGN_FOUR:
                    this.input = 0;
                    this.inputString = "";
                    this.setState(Scene.SIGNIN, States.SIGN_THREE);
                    break;
                case SIGN_FIVE:
                    this.input = 0;
                    this.inputString = "";
                    this.setState(Scene.SIGNIN, States.SIGN_FOUR);
                    break;
            }
        }
        else
        {
            // The first scene of the sign-in, requiring username and etc
            switch (this.signinState)
            {
                case DEFAULT:
                    this.input = 0;
                    this.accountPassword = 0;
                    this.accountNumber = 0;
                    this.cardIssuer = "";
                    this.accountType = "";
                    this.inputString = "";
                    this.firstName = "";
                    this.lastName = "";
                    this.phoneNumber = "";
                    this.expiryDate = "";
                    this.cvvNumber = "";
                    this.setState(Scene.SIGNIN, States.SIGN_ONE);
                    break;
                case SIGN_ONE:
                    // validate both first and last name, ensuring that these are formally formatted.

                    String title = "Invalid First/Last Name";
                    String description = "";
                    boolean successful = true;

                    if (this.firstName != null)
                    {
                        if (!this.firstName.isEmpty())
                        {
                            int length = this.firstName.length();
                            char firstChar = this.firstName.charAt(0);
                            String theRest = this.firstName.substring(1);

                            if (!this.firstName.matches("[a-zA-Z]+"))
                            {
                                description += "❌ First Name must only contain alphabetic.\n\n";
                                successful = false;
                            }

                            if (length < 5 || length > 15)
                            {
                                description += "❌ First Name is out of range (5-15 characters only).\n\n";
                                successful = false;
                            }

                            if (!Character.isUpperCase(firstChar) || !theRest.equals(theRest.toLowerCase()))
                            {
                                description += "❌ First Name is not using proper Title Case format.\n\n";
                                successful = false;
                            }
                        }
                        else
                        {
                            description += "❌ First Name must be filled.\n\n";
                            successful = false;
                        }
                    }
                    else
                    {
                        description += "❌ First Name must be filled.\n\n";
                        successful = false;
                    }

                    if (this.lastName != null)
                    {
                       if (!this.lastName.isEmpty())
                       {
                           int length = this.lastName.length();
                           char firstChar = this.lastName.charAt(0);
                           String theRest = this.lastName.substring(1);

                           if (!this.lastName.matches("[a-zA-Z]+"))
                           {
                               description += "❌ Last Name must only contain alphabetic.\n\n";
                               successful = false;
                           }

                           if (length < 4 || length > 15)
                           {
                               description += "❌ Last Name is out of range (4-15 characters only).\n\n";
                               successful = false;
                           }

                           if (!Character.isUpperCase(firstChar) || !theRest.equals(theRest.toLowerCase()))
                           {
                               description += "❌ Last Name is not using proper Title Case format.\n\n";
                               successful = false;
                           }
                       }
                       else
                       {
                           description += "❌ Last Name must be filled.\n\n";
                           successful = false;
                       }
                    }
                    else
                    {
                        description += "❌ Last Name must be filled.\n\n";
                        successful = false;
                    }

                    if (this.accountPassword != 0)
                    {
                        if (this.accountPassword < 6)
                        {
                            description += "❌ New Password must at least contain 6 digits.\n\n";
                            successful = false;
                        }
                    }
                    else
                    {
                        description += "❌ New Password must be filled.\n\n";
                        successful = false;
                    }

                    if (!successful)
                    {
                        this.title = title;
                        this.description = description;
                        this.setState(Scene.NOTIFY, States.NOTIFY_SHOW);
                        break;
                    }

                    // If successful, move onto the confirmation scene

                    this.input = 0;
                    this.inputString = "";
                    this.accountNumber = generateAccountNumber();
                    this.setState(Scene.SIGNIN, States.SIGN_TWO);
                    break;
                case SIGN_TWO:
                    // Validate the phone number.

                    title = "Invalid Phone number";
                    description = "";
                    successful = true;

                    if (this.input > 0)
                    {
                        int length = String.valueOf(this.input).length();
                        if (length != 10)
                        {
                            description += "❌ Phone number must have 10 digits.\n\n";
                            successful = false;
                        }
                    }
                    else
                    {
                        description += "❌ Phone number is empty.\n\n";
                        successful = false;
                    }

                    if (!successful)
                    {
                        this.title = title;
                        this.description = description;
                        this.setState(Scene.NOTIFY, States.NOTIFY_SHOW);
                        break;
                    }

                    this.newOPTCode = generateOTP();

                    this.input = 0;
                    this.inputString = "";
                    this.setState(Scene.SIGNIN, States.SIGN_THREE);
                    break;
                case SIGN_THREE:
                    // authenticate the phone number via OTP (One-time Password)

                    title = "OTP incorrect code";
                    description = "";
                    successful = true;

                    if (!this.OTPCode.isEmpty())
                    {
                        if (!this.OTPCode.equals(this.newOPTCode))
                        {
                            description += "❌ OPT Code doesn't match.\n\n";
                            successful = false;
                        }
                    }
                    else
                    {
                        description += "❌ OTP is empty.\n\n";
                        successful = false;
                    }

                    if (!successful)
                    {
                        this.title = title;
                        this.description = description;
                        this.setState(Scene.NOTIFY, States.NOTIFY_SHOW);
                        break;
                    }

                    this.accountNumber = generateAccountNumber();

                    this.input = 0;
                    this.inputString = "";
                    this.setState(Scene.SIGNIN, States.SIGN_FOUR);
                    break;
                case SIGN_FOUR:

                    title = "Invalid credit card";
                    description = "";
                    successful = true;

                    if (!this.cardNumber.isEmpty())
                    {
                        int length = this.cardNumber.length();

                        if (length != 16)
                        {
                            description += "❌ Card Number must have 16 digits.\n\n";
                            successful = false;
                        }

                        if (!isValidLuhn(this.cardNumber))
                        {
                            description += "❌ Card Number is invalid based on Luhn.\n\n";
                            successful = false;
                        }
                    }
                    else
                    {
                        description += "❌ Account Number is not filled.\n\n";
                        successful = false;
                    }

                    if (!this.expiryDate.isEmpty())
                    {
                        int length = this.expiryDate.length();
                        String months = this.expiryDate.substring(0, 2);
                        String years = this.expiryDate.substring(2);

                        if (length != 4)
                        {
                            description += "❌ Expiry Date must have 4 digits.\n\n";
                            successful = false;
                        }

                        if (Integer.parseInt(years) < 25 || Integer.parseInt(years) > 35)
                        {
                            description += "❌ Expiry Date in YY must be between 2025-2035.\n\n";
                            successful = false;
                        }
                    }
                    else
                    {
                        description += "❌ Expiry Date is not filled.\n\n";
                        successful = false;
                    }

                    if (!this.cvvNumber.isEmpty())
                    {
                        int length = this.cvvNumber.length();
                        if (length != 3)
                        {
                            description += "❌ CVV must have 3 digits.\n";
                            successful = false;
                        }
                    }
                    else
                    {
                        description += "❌ CVV is not filled.\n\n";
                        successful = false;
                    }

                    if (!successful)
                    {
                        this.title = title;
                        this.description = description;
                        this.setState(Scene.NOTIFY, States.NOTIFY_SHOW);
                        break;
                    }


                    //    Account newAccount = this.createAccount(name, number, password, type);

                    this.bank.addAccount(
                            this.firstName + " " + this.lastName,
                            this.accountNumber,
                            this.accountPassword,
                            Types.valueOf(this.accountType)
                    );


                    this.input = 0;
                    this.inputString = "";
                    this.setState(Scene.SIGNIN, States.SIGN_FIVE);
                    break;
                case SIGN_FIVE:
                    this.input = 0;
                    this.accountPassword = 0;
                    this.accountNumber = 0;
                    this.cardIssuer = "";
                    this.accountType = "";
                    this.inputString = "";
                    this.firstName = "";
                    this.lastName = "";
                    this.phoneNumber = "";
                    this.expiryDate = "";
                    this.cvvNumber = "";

                    this.setState(Scene.SIGNIN, States.SIGN_SIX);
                    break;
            }
        }

        this.display();
    }

    // A simple 6-digits OPT generator that similarly
    private static String generateOTP()
    {
        // Referenced: https://stackoverflow.com/questions/49460963/generating-otp-through-java
        // Using SecureRandom instead of Random class since the generated code could easily be
        // predicted via LCG.
        SecureRandom random = new SecureRandom();
        int otp = 100000 + random.nextInt(900000);
        return String.valueOf(otp);
    }

    private static int generateAccountNumber()
    {
        SecureRandom random = new SecureRandom();
        int number = 10000000 + random.nextInt(10000000);
        return number;
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
