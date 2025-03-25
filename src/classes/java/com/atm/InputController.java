package com.atm;

import javafx.animation.Interpolator;
import javafx.animation.KeyFrame;
import javafx.animation.KeyValue;
import javafx.animation.Timeline;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.layout.AnchorPane;
import javafx.util.Duration;

public class InputController
{
    @FXML public AnchorPane anchor;
    @FXML public Button buttonOne;
    @FXML public Button buttonTwo;
    @FXML public Button buttonThree;
    @FXML public Button buttonFour;
    @FXML public Button buttonFive;
    @FXML public Button buttonSix;
    @FXML public Button buttonSeven;
    @FXML public Button buttonEight;
    @FXML public Button buttonNine;
    @FXML public Button buttonZero;

    private Controller controller;

    public void initialize(Controller controller)
    {
        this.controller = controller;
        bindButtons();
    }

    public void slideOut()
    {
        Timeline slideAnimation = new Timeline(
                new KeyFrame(Duration.seconds(1),
                        new KeyValue(anchor.translateYProperty(), 640, Interpolator.EASE_BOTH),
                        new KeyValue(anchor.opacityProperty(), 0, Interpolator.EASE_BOTH)
                )
        );


        slideAnimation.play();
    }

    public void slideIn()
    {
        Timeline slideAnimation = new Timeline(
                new KeyFrame(Duration.seconds(1),
                        new KeyValue(anchor.translateYProperty(), 0, Interpolator.EASE_BOTH),
                        new KeyValue(anchor.opacityProperty(), 1, Interpolator.EASE_BOTH)
                )
        );

        slideAnimation.setDelay(Duration.seconds(1));
        slideAnimation.play();
    }

    private void bindButtons()
    {
        anchor.setTranslateY(640);

        // Array of declared buttons (although already initialized from fxml)
        Button[] buttons =
                {
                buttonOne, buttonTwo, buttonThree, buttonFour, buttonFive,
                buttonSix, buttonSeven, buttonEight, buttonNine, buttonZero
        };

        // iterate through the array by binding them individually.
        for (Button button : buttons) {
            if (!(button == null))
            {
                BindPressed(button);
                bindHover(button);
            }
        }
    }

    private void BindPressed(Button button) {
        button.setOnMousePressed(event -> {
            this.controller.process(button.getText());
        });
    }

    private void bindHover(Button button)
    {
        // Initially ensuring that the buttons are unselected (with the greyed-out effect)
        button.setOpacity(0);

        // Timelines for hover and exit (transition from gray to blue)
        Timeline hoverAnimation = new Timeline(
                new KeyFrame(Duration.seconds(0.2),
                        new KeyValue(button.opacityProperty(), 1, Interpolator.EASE_BOTH)
                )
        );

        Timeline exitAnimation = new Timeline(
                new KeyFrame(Duration.seconds(0.2),
                        new KeyValue(button.opacityProperty(), 0, Interpolator.EASE_BOTH)
                )
        );

        // binding the selected button
        button.setOnMouseEntered(event -> {
            hoverAnimation.playFromStart();
            System.out.println("entered");
        });
        button.setOnMouseExited(event -> {
            exitAnimation.playFromStart();
        });
    }
}
