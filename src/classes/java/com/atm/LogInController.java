package com.atm;

import javafx.animation.KeyFrame;
import javafx.animation.KeyValue;
import javafx.animation.Timeline;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.control.TextField;
import javafx.scene.layout.AnchorPane;
import javafx.scene.text.Text;
import javafx.util.Duration;


public class LogInController
{
    @FXML public AnchorPane anchor;

    @FXML public TextField textField1;
    @FXML public TextField textField2;
    @FXML public TextField textField3;
    @FXML public TextField textField4;
    @FXML public TextField textField5;
    @FXML public TextField textField6;

    @FXML public TextField textField7;
    @FXML public TextField textField8;
    @FXML public TextField textField9;
    @FXML public TextField textField10;
    @FXML public TextField textField11;
    @FXML public TextField textField12;

    @FXML public Button loginButton;
    @FXML public Button registerButton;
    @FXML public Button returnButton;

    @FXML public Button loginButton1;
    @FXML public Button registerButton1;
    @FXML public Button returnButton1;

    @FXML public CheckBox checkBox1;
    @FXML public CheckBox checkBox2;
    @FXML public CheckBox checkBox3;

    @FXML public CheckBox checkBox4;
    @FXML public CheckBox checkBox5;
    @FXML public CheckBox checkBox6;

    @FXML public Button loginButton2;
    @FXML public Button registerButton2;
    @FXML public Text username;

    private Controller controller;
    private States state;

    public void initialize(Controller controller)
    {
        this.state = States.DEFAULT;
        this.controller = controller;

        loginButton.setId("Start-LogIn");
        registerButton.setId("Start-SignIn");
        returnButton.setId("Return-LogIn");

        loginButton1.setId("Start-LogIn");
        registerButton1.setId("Start-SignIn");
        returnButton1.setId("Return-LogIn");

        loginButton2.setId("Start-LogIn");
        registerButton2.setId("Open-Notify");

        anchor.setTranslateX(414);

        BindButtons();
    }

    public void BindButtons()
    {
        Button[] buttons = {
                loginButton, loginButton1, loginButton2, returnButton, returnButton1
        };

        Button[] textButtons = {
                registerButton, registerButton1, registerButton2
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
        //TODO: Add ripple effect
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
            case LOGIN_ONE -> 0;
            case LOGIN_TWO -> -414;
            case LOGIN_THREE -> -414 * 2;
            case LOGIN_FOUR -> -414 * 3;
            default -> 424; // Default position for the welcome page
        };

        // Make the anchor transparent when in DEFAULT state
        // Simply because login overlaps with the tutorial scene

       anchor.setMouseTransparent(state == States.DEFAULT);

       Timeline slideAnimation = new Timeline(
                new KeyFrame(Duration.seconds(1),
                        new KeyValue(anchor.translateXProperty(), targetX, new SineInterpolator())
                )
       );

        slideAnimation.play();
    }

    public void update(int input, Account account)
    {
        if (account != null) {
            this.username.setText(account.getName());
        }

        String inputString = String.valueOf(input);

        if (this.state == States.LOGIN_ONE)
        {
            TextField[] textFields = {
                    textField1, textField2, textField3, textField4, textField5, textField6
            };

            if (inputString.equals("0"))
            {
                inputString = "";
            }

            // Iterating through each TextFields by comparing with the inputString.
            for (int index = 0; index < textFields.length; index++) {
                TextField textField = textFields[index];

                // Remove CSS style of the unlabeled textField
                textField.getStyleClass().remove("pinpoint");
                textField.getStyleClass().remove("pinpoint2");

                if (index < inputString.length())
                {
                    // Set the TextField to the corresponding digit in the input
                    textField.setText(String.valueOf(inputString.charAt(index)));
                }
                else
                {
                    // If there are no digits left to assign, clear the TextField (keep it empty)
                    textField.setText("");
                }

                // Glow the next TextField
                if (index == inputString.length())
                {
                    textField.getStyleClass().add("pinpoint");
                }

                if (index == inputString.length() - 1)
                {
                    textField.getStyleClass().add("pinpoint2");
                }
            }

            // CheckBox conditions.
            checkBox1.setSelected(inputString.length() == 6);

            // Check if only digits (0-9) are allowed.
            checkBox2.setSelected(inputString.matches("\\d+"));

            // Check if there are no sequential or repeating digits.
            checkBox3.setSelected(!hasSequentialOrRepeatingDigits(inputString));
        }
        else if (this.state == States.LOGIN_TWO)
        {
            TextField[] textFields = {
                    textField7, textField8, textField9, textField10, textField11, textField12
            };

            if (inputString.equals("0"))
            {
                inputString = "";
            }

            // Iterating through each TextFields by comparing with the inputString.
            for (int index = 0; index < textFields.length; index++) {
                TextField textField = textFields[index];

                // Remove CSS style of the unlabeled textField
                textField.getStyleClass().remove("pinpoint");
                textField.getStyleClass().remove("pinpoint2");

                if (index < inputString.length())
                {
                    // Set the TextField to the corresponding digit in the input
                    textField.setText(String.valueOf(inputString.charAt(index)));
                }
                else
                {
                    // If there are no digits left to assign, clear the TextField (keep it empty)
                    textField.setText("");
                }

                // Glow the next TextField
                if (index == inputString.length())
                {
                    textField.getStyleClass().add("pinpoint");
                }

                if (index == inputString.length() - 1)
                {
                    textField.getStyleClass().add("pinpoint2");
                }
            }

            // CheckBox conditions.
            checkBox4.setSelected(inputString.length() == 6);

            // Check if only digits (0-9) are allowed.
            checkBox5.setSelected(inputString.matches("\\d+"));

            // Check if there are no sequential or repeating digits.
            checkBox6.setSelected(!hasSequentialOrRepeatingDigits(inputString));
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
}
