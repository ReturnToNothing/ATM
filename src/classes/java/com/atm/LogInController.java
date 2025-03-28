package com.atm;

import javafx.animation.KeyFrame;
import javafx.animation.KeyValue;
import javafx.animation.Timeline;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
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

    @FXML public Button loginButton;
    @FXML public Button registerButton;
    @FXML public Button returnButton;

    @FXML public Text firstRule;
    @FXML public Text secondRule;
    @FXML public Text thirdRule;

    private Controller controller;

    public void initialize(Controller controller)
    {
        this.controller = controller;

        loginButton.setId("Start-LogIn");
        registerButton.setId("Start-SignIn");
        returnButton.setId("Return-LogIn");

        anchor.setTranslateX(414);

        BindButtons();
    }

    public void BindButtons()
    {
        Button[] buttons = {
                loginButton, returnButton
        };

        Button[] textButtons = {
                registerButton
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
        double targetX = switch (state)
        {
            case DEFAULT -> 414;
            case LOGIN_ONE -> 0;
            case LOGIN_TWO -> -414;
            case LOGIN_THREE -> -414 * 2;
            default -> 0; // Default position for the welcome page
        };


        // Make the anchor transparent when in DEFAULT state
        // Simply because login overlaps with the tutorial scene
        anchor.setMouseTransparent(state == States.DEFAULT);

       Timeline slideAnimation = new Timeline(
                new KeyFrame(Duration.seconds(1),
                        new KeyValue(anchor.translateXProperty(), targetX, new SineInterpolator())
                )
       );

       System.out.println(state + " " + targetX);

        slideAnimation.play();
    }
}
