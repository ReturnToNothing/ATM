package com.atm;

import javafx.animation.Interpolator;
import javafx.animation.KeyFrame;
import javafx.animation.KeyValue;
import javafx.animation.Timeline;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.Region;
import javafx.util.Duration;

public class LoginController {

    @FXML public AnchorPane anchor;
    @FXML public Button registerButton;
    @FXML public Region fadeBackground;

    private Controller controller;

    public void initialize(Controller controller)
    {
        this.controller = controller;

        System.out.println(this.controller);

        BindHover(this.registerButton);

        BindPressed(this.registerButton);
    }

    public void slideLeft()
    {
        Timeline slideAnimation = new Timeline(
                new KeyFrame(Duration.seconds(1),
                        new KeyValue(anchor.translateXProperty(), -360, Interpolator.EASE_BOTH),
                        new KeyValue(fadeBackground.opacityProperty(), 1, Interpolator.EASE_BOTH)
                )
        );

        slideAnimation.play();
    }

    public void slideRight()
    {
        Timeline slideAnimation = new Timeline(
                new KeyFrame(Duration.seconds(1),
                        new KeyValue(anchor.translateXProperty(), 360, Interpolator.EASE_BOTH),
                        new KeyValue(fadeBackground.opacityProperty(), 1, Interpolator.EASE_BOTH)
                )
        );

        slideAnimation.play();
    }

    public void slideIn()
    {
        Timeline slideAnimation = new Timeline(
                new KeyFrame(Duration.seconds(1),
                        new KeyValue(anchor.translateXProperty(), 0, Interpolator.EASE_BOTH),
                        new KeyValue(fadeBackground.opacityProperty(), 0, Interpolator.EASE_BOTH)
                )
        );

        slideAnimation.play();
    }

    private void BindPressed(Button button) {
        //TODO: Add ripple effect
        button.setOnMousePressed(event -> {
            this.controller.process("Log-in");
        });
    }

    private void BindHover(Button button)
    {
        // Initially ensuring that the buttons are unselected (with the greyed-out effect)
        button.setOpacity(0);
        slideRight();

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

        // I had tried to find different solutions, although implementing them are quite complicated,
        // in which I decided to practically create two buttons, where the top one fades in or out.

        // binding the selected button
       button.setOnMouseEntered(event -> {
           hoverAnimation.playFromStart();
       });
       button.setOnMouseExited(event -> {
            exitAnimation.playFromStart();
       });
    }
}
