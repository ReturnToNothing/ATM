package com.atm;

import javafx.animation.KeyFrame;
import javafx.animation.KeyValue;
import javafx.animation.Timeline;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.TextField;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.HBox;
import javafx.scene.text.Text;
import javafx.util.Duration;


public class SignInController
{
    @FXML public AnchorPane anchor;
    @FXML public AnchorPane anchorRight;

    @FXML public CheckBox checkBox1;
    @FXML public CheckBox checkBox2;
    @FXML public CheckBox checkBox3;
    @FXML public CheckBox checkBox4;
    @FXML public CheckBox checkBox5;
    @FXML public CheckBox checkBox6;
    @FXML public CheckBox checkBox7;
    @FXML public CheckBox checkBox8;
    @FXML public CheckBox checkBox9;

    @FXML public Button returnButton;
    @FXML public Button returnButton1;
    @FXML public Button returnButton2;
    @FXML public Button returnButton3;
    @FXML public Button signInButton;
    @FXML public Button signInButton1;
    @FXML public Button signInButton2;
    @FXML public Button loginButton;

    @FXML public TextField firstTextField;
    @FXML public TextField lastTextField;
    @FXML public TextField phoneTextField;

    @FXML public TextField cardNumberTextField;
    @FXML public TextField expiryDateTextField;
    @FXML public TextField cvvTextField;

    @FXML public Text type;
    @FXML public Text username;
    @FXML public Text company;
    @FXML public Text number;
    @FXML public ImageView companyIcon;

    @FXML public ChoiceBox<String> companyDropBox;

    private TextFieldContext selectedTextField;
    private TextFieldContext firstTextFieldContext;
    private TextFieldContext lastTextFieldContext;
    private TextFieldContext phoneTextFieldContext;

    private TextFieldContext cardNumberTextFieldContext;
    private TextFieldContext expiryDateTextFieldContext;
    private TextFieldContext cvvTextFieldContext;

    private StringProperty firstName;
    private StringProperty lastName;
    private StringProperty phoneNumber;
    private StringProperty OTPNumber;

    private StringProperty cardNumber;
    private StringProperty expiryDate;
    private StringProperty cvvNumber;

    private StringProperty cardCompany;
    private StringProperty cardType;

    private Model model;
    private Controller controller;

    private States inputState;
    private States state;

    public void initialize(Controller controller, Model model)
    {
        this.state = States.DEFAULT;
        this.inputState = States.DEFAULT;
        this.controller = controller;
        this.model = model;

        this.firstName = new SimpleStringProperty("");
        this.lastName = new SimpleStringProperty("");
        this.phoneNumber = new SimpleStringProperty("");
        this.OTPNumber = new SimpleStringProperty("");
        this.cardNumber = new SimpleStringProperty("");
        this.expiryDate = new SimpleStringProperty("");
        this.cvvNumber = new SimpleStringProperty("");
        this.cardCompany = new SimpleStringProperty("");
        this.cardType = new SimpleStringProperty("");

        returnButton.setId("Return-SignIn");
        loginButton.setId("Start-LogIn");
        signInButton.setId("Start-SignIn");
        signInButton1.setId("Start-SignIn");
        signInButton2.setId("Start-SignIn");

        firstTextField.setId("Open-Input-String");
        lastTextField.setId("Open-Input-String");
        phoneTextField.setId("Open-Input");

        cardNumberTextField.setId("Open-Input");
        expiryDateTextField.setId("Open-Input");
        cvvTextField.setId("Open-Input");

        this.firstTextFieldContext = new TextFieldContext(
                this.firstTextField,
                "FirstName",
                this.firstName
        );
        this.lastTextFieldContext = new TextFieldContext(
                this.lastTextField,
                "LastName",
                this.lastName
        );
        this.phoneTextFieldContext = new TextFieldContext(
                this.phoneTextField,
                "PhoneNumber",
                this.phoneNumber
        );
        this.cardNumberTextFieldContext = new TextFieldContext(
                this.cardNumberTextField,
                "CardNumber",
                this.cardNumber
        );
        this.expiryDateTextFieldContext = new TextFieldContext(
                this.expiryDateTextField,
                "ExpiryDate",
                this.expiryDate
        );
        this.cvvTextFieldContext = new TextFieldContext(
                this.cvvTextField,
                "CVVNumber",
                this.cvvNumber
        );

        // despite the choiceBox using a listener for choosing an item should trigger the same
        // event when the TextField are selected via Mouse.
        this.companyDropBox.getItems().addAll("Visa", "Mastercard", "HSBC");
        this.companyDropBox.getSelectionModel().selectedItemProperty().addListener((observable, oldValue, newValue) -> {
          this.controller.process("Update");
        });

        anchor.setTranslateX(414);

        BindButtons();
        BindTextFields();
    }

    public void BindButtons()
    {
        Button[] buttons = {
                returnButton, signInButton, signInButton1, signInButton2
        };

        Button[] textButtons = {
                loginButton
        };

        for (Button button : buttons) {
            hoverButton(button);
            BindPressed(button);
        }

        for (Button textButton : textButtons)
        {
            hoverButtonText(textButton);
            BindPressed(textButton);
        }
    }

    public void BindTextFields()
    {
        TextFieldContext[] textFieldContexts = {
                firstTextFieldContext, lastTextFieldContext, phoneTextFieldContext,
                cardNumberTextFieldContext, expiryDateTextFieldContext, cvvTextFieldContext
        };

        for (TextFieldContext textFieldContext : textFieldContexts)
        {
            registerTextField(textFieldContext);
        }
    }

    private void registerTextField(TextFieldContext textFieldContext)
    {
        textFieldContext.textField().setOnMouseClicked(event ->
        {
            this.selectedTextField = textFieldContext;
            this.controller.process(textFieldContext.textField().getId());
        });
    }

    private void hoverButton(Button button)
    {
        // Initially ensuring that the buttons are unselected (with the greyed-out effect)
        button.setOpacity(0);

        // Timelines for hover and exit (transition from gray to blue)
        Timeline hoverAnimation = new Timeline(
                new KeyFrame(Duration.seconds(.2),
                        new KeyValue(button.opacityProperty(), 1, new SineInterpolator())
                )
        );

        Timeline exitAnimation = new Timeline(
                new KeyFrame(Duration.seconds(.2),
                        new KeyValue(button.opacityProperty(), 0, new SineInterpolator())
                )
        );

        // I had tried to find different solutions, although implementing them are quite complicated,
        // in which I decided to practically create two buttons, where the top one fades in or out.

        button.setOnMouseEntered(event -> {
            hoverAnimation.playFromStart();
        });
        button.setOnMouseExited(event -> {
            exitAnimation.playFromStart();
        });
    }

    private void hoverButtonText(Button button)
    {
        // Timelines for hover and exit (transition from gray to blue)
        Timeline hoverAnimation = new Timeline(
                new KeyFrame(Duration.seconds(.2),
                        new KeyValue(button.opacityProperty(), .6, new SineInterpolator())
                )
        );

        Timeline exitAnimation = new Timeline(
                new KeyFrame(Duration.seconds(.2),
                        new KeyValue(button.opacityProperty(), 1, new SineInterpolator())
                )
        );

        // I had tried to find different solutions, although implementing them are quite complicated,
        // in which I decided to practically create two buttons, where the top one fades in or out.

        button.setOnMouseEntered(event -> {
            hoverAnimation.playFromStart();
        });
        button.setOnMouseExited(event -> {
            exitAnimation.playFromStart();
        });
    }

    private void BindPressed(Button button) {
        button.setOnMousePressed(event -> {
            this.controller.process(button.getId());
        });
    }

    public void slide(States state)
    {
        this.state = state;

        double targetX = switch (state)
        {
            case DEFAULT -> 424;
            case SIGN_ONE -> 0;
            case SIGN_TWO -> -414;
            case SIGN_THREE -> -414 * 2;
            case SIGN_FOUR -> -414 * 3;
            default -> 424; // Default position for the welcome page
        };

        double nextTargetX = switch (state)
        {
            case DEFAULT -> 0;
            case SIGN_ONE -> 414;
            case SIGN_TWO -> 414 * 2;
            case SIGN_THREE -> 414 * 3;
            case SIGN_FOUR -> 414 * 4;
            default -> 424; // Default position for the welcome page
        };

        // Make the anchor transparent when in DEFAULT state
        // Simply because login overlaps with the tutorial scene

       anchor.setMouseTransparent(state == States.DEFAULT);

       Timeline slideAnimation = new Timeline(
                new KeyFrame(Duration.seconds(1),
                        new KeyValue(anchor.translateXProperty(), targetX, new SineInterpolator())
                ),
               new KeyFrame(Duration.seconds(1.2),
                       new KeyValue(anchorRight.translateXProperty(), nextTargetX - 20, new SineInterpolator())
               )
       );

        slideAnimation.play();
    }

    public void updateThird(States inputState)
    {
        if (this.selectedTextField == null)
        {
            return;
        }

        this.username.setText(this.firstName.get() + " " + this.lastName.get());

        if (this.companyDropBox.getValue() != null)
        {
            String companyName = this.companyDropBox.getValue();
            this.company.setText(companyName);
            this.companyIcon.setImage(View.getCardImage(companyName.toLowerCase()));
        }

        TextField currentTextField = this.selectedTextField.textField();
        String currentAction = this.selectedTextField.action();
        StringProperty currentVariable = this.selectedTextField.variable();

        this.cardCompany.set(this.companyDropBox.getValue());

        if (inputState.equals(States.INPUT_DIGIT))
        {
            applyTextFieldStyle(currentTextField);

            if (!this.inputState.equals(inputState))
            {
                this.controller.processString(currentAction, currentVariable.get());
            }

            String inputString = String.valueOf(this.model.getInput());

            if (currentTextField.equals(cardNumberTextField))
            {
                currentTextField.setText(formatCardNumber(inputString, false));
                currentVariable.set(formatCardNumber(currentTextField.getText(), true));
            }
            else if (currentTextField.equals(expiryDateTextField))
            {
                currentTextField.setText(formatExpiryDate(inputString, false));
                currentVariable.set(formatExpiryDate(currentTextField.getText(), true));
            }
            else
            {
                currentTextField.setText(inputString);
                currentVariable.set(currentTextField.getText());
            }

            validateCard(checkBox7, checkBox8, checkBox9);
        }
        else
        {
            resetTextFieldStyle(currentTextField);

            this.controller.processString(currentAction, currentVariable.get());
            this.selectedTextField = null;
        }

        this.inputState = inputState;
    }

    public void updateSecond(States inputState)
    {
        if (this.selectedTextField == null)
        {
            return;
        }

        TextField currentTextField = this.selectedTextField.textField();
        String currentAction = this.selectedTextField.action();
        StringProperty currentVariable = this.selectedTextField.variable();

        if (inputState.equals(States.INPUT_DIGIT))
        {
            applyTextFieldStyle(currentTextField);

           if (!this.inputState.equals(inputState))
            {
                this.controller.processString(currentAction, currentVariable.get());
            }

            long input = this.model.getInput();

            currentTextField.setText(formatPhoneNumber(String.valueOf(input), false));

            if (currentTextField.equals(phoneTextField))
            {
                currentVariable.set(formatPhoneNumber(currentTextField.getText(), true));
            }

            validatePhoneInput(checkBox4, checkBox5, checkBox6);
        }
        else
        {
            resetTextFieldStyle(currentTextField);
            this.controller.processString(currentAction, currentVariable.get());
            this.selectedTextField = null;
        }

        this.inputState = inputState;
    }

    public void updateFirst(States inputState)
    {
        if (this.selectedTextField == null)
        {
            return;
        }

        /* Using a context-like object that encapsulates multi-data that is easily
           accessible, bindable, and greatly reduced the previous duplication. */
        TextField currentTextField = this.selectedTextField.textField();
        String currentAction = this.selectedTextField.action();
        StringProperty currentVariable = this.selectedTextField.variable();

        if (inputState.equals(States.INPUT_STRING))
        {
            applyTextFieldStyle(currentTextField);

            // Swap the inputString based on selected TextField
            // This is only updated once when TextField is deselected, preventing the input from repeating.
            // Once deselected, the first or last name is updated.
            if (!this.inputState.equals(inputState))
            {
                this.controller.processString(currentAction, currentVariable.get());
            }

            String stringInput = this.model.getStringInput();
            stringInput = toTitleCase(stringInput);

            currentTextField.setText(stringInput);

            currentVariable.set(currentTextField.getText());

            validateInput(stringInput, checkBox1, checkBox2, checkBox3);
        }
        else
        {
            resetTextFieldStyle(currentTextField);
            this.controller.processString(currentAction, currentVariable.get());
            this.selectedTextField = null;
        }

        this.inputState = inputState;
    }

    public void update()
    {
        States inputState = this.model.getState(Scene.INPUT);
        States signingState = this.model.getState(Scene.SIGNIN);

        slide(signingState);

        if (signingState.equals(States.SIGN_ONE))
        {
            updateFirst(inputState);
        }
        if (signingState.equals(States.SIGN_TWO))
        {
            updateSecond(inputState);
        }
        if (signingState.equals(States.SIGN_THREE))
        {
            updateThird(inputState);
        }

        if (signingState.equals(States.DEFAULT))
        {
            this.selectedTextField = null;

            // might actually remove these from view into the model instead.
            this.firstName.set("");
            this.lastName.set("");
            this.phoneNumber.set("");
            this.cardNumber.set("");
            this.expiryDate.set("");
            this.cvvNumber.set("");
        }
    }

    // Convert any string into a Titlecase sensitivity
    private static String toTitleCase(String input)
    {
        if (input.isEmpty())
        {
            return input;
        }

        String[] words = input.toLowerCase().split("\\s+");
        StringBuilder titleCase = new StringBuilder();

        for (String word : words)
        {
            if (!word.isEmpty())
            {
                titleCase.append(Character.toUpperCase(word.charAt(0)))
                        .append(word.substring(1))
                        .append(" ");
            }
        }

        return titleCase.toString().trim();
    }

    // Helper function for validating the input, checking if any of these conditions are met.
    // Move this at the bottom, since it's a private method
    private void validateInput(String input, CheckBox checkAlphabet, CheckBox checkLength, CheckBox checkFormat)
    {
        if (input == null)
        {
            return;
        }
        // Only alphabetic characters allowed.
        if (checkAlphabet != null)
        {
            checkAlphabet.setSelected(input.matches("[a-zA-Z]+") && !input.isEmpty());
        }

        // Minimum and maximum length from 5-12.
        if (checkLength != null)
        {
            int length = input.length();
            checkLength.setSelected(length > 5 && length < 15);
        }

        // Proper case sensitivity and formatting.
        if (checkFormat != null) {
            if (input.isEmpty()) {
                checkFormat.setSelected(false);
            } else {
                char firstChar = input.charAt(0);
                String theRest = input.substring(1);

                checkFormat.setSelected(Character.isUpperCase(firstChar) || theRest.equals(theRest.toLowerCase()));
            }
        }
    }

    private void validatePhoneInput(CheckBox checkDigits, CheckBox checkLength, CheckBox checkFormat)
    {
        if (!this.phoneNumber.get().isEmpty())
        {
            long phoneNumber = Long.parseLong(this.phoneNumber.get());

            // Only Digits Allowed--always true since the input always be a digit
            checkDigits.setSelected(phoneNumber > 0);

            // maximum length from 10 digits.
            if (checkLength != null)
            {
                int length = String.valueOf(phoneNumber).length();

                checkLength.setSelected(length == 10);
            }

            // always true despite that the input is automatically formatted.
            checkFormat.setSelected(true);
        }
    }

    private void validateCard(CheckBox checkAll, CheckBox checkExpiry, CheckBox checksum)
    {
        // All fields must be filled.
        if (checkAll != null)
        {
            checkAll.setSelected(
                    !this.cardNumber.get().isEmpty() &
                    !this.expiryDate.get().isEmpty() &
                    !this.cvvNumber.get().isEmpty() &
                    this.companyDropBox.getValue() != null
            );
        }

        // The Expiry date must be between 2025–2035.
        if (checkExpiry != null)
        {
            if (this.expiryDate.get().length() >= 4)
            {
                int year = Integer.parseInt(this.expiryDate.get().substring(2));
                checkExpiry.setSelected(year > 24 && year < 35);
            }
            else
                checkExpiry.setSelected(false);
        }

        // Card number must be valid, according to the Luhn sum.
        checksum.setSelected(isValidLuhn(this.cardNumber.get()));
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
/*

    */

    // Helper method for formatting string with a country code
    private String formatPhoneNumber(String input, boolean unformat)
    {
        if (unformat)
        {
            // Remove the leading "+44 " (4 characters in total (including the space))
            // Had to verify if it initially contains these characters without causing an error
            if (input.startsWith("+44 "))
            {
                return input.substring(4);  // then remove "+44 "  for unformatting
            }

            return input;
        }

        // clean input to remove existing spaces or hyphens (digits only)
        input = input.replaceAll("\\D", "");

        // format phone number by prepending with a UK's country code
        if (!input.isEmpty())
        {
            return "+44 " + input;
        }

        // Return the input as is if invalid or empty
        return input;
    }

    // Helper method for formatting string into 4 groups of 4 digits
    private String formatCardNumber(String input, boolean unformat)
    {
        if (unformat)
        {
            // Remove all spaces and hyphens
            return input.replaceAll("[\\s-]+", "");
        }

        // clean input to remove existing spaces or hyphens
        input = input.replaceAll("\\s+", "");

        StringBuilder formatted = new StringBuilder();

        // iterate each digit, and append with a hyphen after each 4 digits
        for (int index = 0; index < input.length(); index++)
        {
            if (index > 0 && index % 4 == 0)
            {
                formatted.append("-");
            }
            formatted.append(input.charAt(index));
        }

        return formatted.toString();
    }

    // Helper method for formatting string with a slash between 2 digits.
    private String formatExpiryDate(String input, boolean unformat)
    {
        if (unformat)
        {
            // Remove the slash between the two digits
            return input.replaceAll("[/\\s]+", "");
        }

        // clean input to remove existing spaces or hyphens
        input = input.replaceAll("\\D", "");

        if (input.length() >= 2)
        {
            return input.substring(0, 2) + (input.length() > 2 ? "/" + input.substring(2, Math.min(4, input.length())) : "");
        }

        return input;
    }

    private void applyTextFieldStyle(TextField textField)
    {
        if (this.selectedTextField == null)
        {
            return;
        }

        ObservableList<String> styleClasses = textField.getStyleClass();
        if (!styleClasses.isEmpty())
        {
            int lastIndex = styleClasses.size() - 1;
            if (styleClasses.get(lastIndex).equals("baseTextField"))
            {
                styleClasses.set(lastIndex, "baseTextField-pinpoint");
            }
            if (styleClasses.get(lastIndex).equals("baseTextFieldHalf"))
            {
                styleClasses.set(lastIndex, "baseTextFieldHalf-pinpoint");
            }
        }
    }

    private void resetTextFieldStyle(TextField textField)
    {
        if (this.selectedTextField == null)
        {
            return;
        }

        ObservableList<String> styleClasses = textField.getStyleClass();
        if (!styleClasses.isEmpty())
        {
            int lastIndex = styleClasses.size() - 1;
            if (styleClasses.get(lastIndex).equals("baseTextField-pinpoint"))
            {
                styleClasses.set(lastIndex, "baseTextField");
            }
            if (styleClasses.get(lastIndex).equals("baseTextFieldHalf-pinpoint"))
            {
                styleClasses.set(lastIndex, "baseTextFieldHalf");
            }
        }
    }
}
