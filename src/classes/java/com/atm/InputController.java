package com.atm;

import javafx.animation.Interpolator;
import javafx.animation.KeyFrame;
import javafx.animation.KeyValue;
import javafx.animation.Timeline;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.StackPane;
import javafx.util.Duration;

public class InputController
{
    @FXML public AnchorPane anchor;
    @FXML public StackPane stackPane;
    @FXML public Button One;
    @FXML public Button Two;
    @FXML public Button Three;
    @FXML public Button Four;
    @FXML public Button Five;
    @FXML public Button Six;
    @FXML public Button Seven;
    @FXML public Button Eight;
    @FXML public Button Nine;
    @FXML public Button Zero;
    @FXML public Button clear;

    private Controller controller;
    private States state;

    public void initialize(Controller controller)
    {
        this.controller = controller;
        this.state = States.DEFAULT;

        One.setId("1");
        Two.setId("2");
        Three.setId("3");
        Four.setId("4");
        Five.setId("5");
        Six.setId("6");
        Seven.setId("7");
        Eight.setId("8");
        Nine.setId("9");
        Zero.setId("0");
        clear.setId("Clear-Input");

        anchor.setTranslateY(310 * 2);

        BindButtons();
    }

    public void BindButtons()
    {
        Button[] buttons = {
                One, Two, Three, Four, Five, Six, Seven, Eight, Nine, Zero, clear
        };

        for (Button button : buttons)
        {
            BindPressed(button);
            hoverButton(button);
        }
    }

    private void BindPressed(Button button)
    {
        //TODO: Add ripple effect
        button.setOnMousePressed(event ->
        {
            this.controller.process(button.getId());
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

    public void slide(States state)
    {
        if (this.state.equals(state))
        {
            return;
        }

        this.state = state;

        double targetY = switch (state)
        {
            case INPUT_DIGIT -> 310;
            default -> 296 * 2;
        };

        double delay = switch (state)
        {
            case INPUT_DIGIT -> 0.5;
            default -> 0;
        };

        System.out.println(state + " InputController");

        Timeline slideAnimation = new Timeline(
                new KeyFrame(Duration.seconds(1),
                        new KeyValue(anchor.translateYProperty(), targetY, new SineInterpolator())
                )
        );

        slideAnimation.setDelay(Duration.seconds(delay));

       slideAnimation.play();
    }
}
