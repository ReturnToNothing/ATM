package com.atm;

import javafx.animation.KeyFrame;
import javafx.animation.KeyValue;
import javafx.animation.Timeline;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.layout.AnchorPane;
import javafx.util.Duration;

public class TutorialController
{
    @FXML public AnchorPane anchor;
    @FXML public AnchorPane anchorRight;
    @FXML public AnchorPane anchorLeft;
    @FXML public Button logButton;
    @FXML public Button startButton;
    @FXML public Button registerButton;
    @FXML public Button continueButton;
    @FXML public Button logButton1;
    @FXML public Button continueButton1;
    @FXML public Button logButton2;
    @FXML public Button logButton4;
    @FXML  public Button returnButton;

    private Controller controller;
    private View view;

    public void initialize(Controller controller)
    {
        //this.view = view;
        this.controller = controller;

        logButton.setId("Start-LogIn");
        logButton1.setId("Start-LogIn");
        logButton2.setId("Start-LogIn");
        logButton4.setId("Start-LogIn");

        registerButton.setId("Start-SignIn");

        startButton.setId("Start-Tutorial");
        continueButton.setId("Start-Tutorial");
        continueButton1.setId("Start-Tutorial");
        returnButton.setId("Start-Tutorial");

        BindButtons();
    }

    public void BindButtons()
    {
        Button[] buttons = {
                logButton, startButton,
                continueButton, continueButton1, logButton4
        };

        Button[] textButtons = {
                registerButton, logButton1, logButton2, returnButton
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
            case DEFAULT -> 0;
            case TUTORIAL_ONE -> -414;
            case TUTORIAL_TWO -> -414 * 2;
            case TUTORIAL_THREE -> -414 * 3;
            default -> 0; // Default position for the welcome page
        };

        double nextTargetX = switch (state)
        {
            case DEFAULT -> 0;
            case TUTORIAL_ONE -> 414;
            case TUTORIAL_TWO -> 414 * 2;
            case TUTORIAL_THREE -> 414 * 3;
            default -> 0; // Default position for the welcome page
        };

       Timeline slideAnimation = new Timeline(
               new KeyFrame(Duration.seconds(1),
                        new KeyValue(anchor.translateXProperty(), targetX, new SineInterpolator())
               ),
               new KeyFrame(Duration.seconds(1.2),
                        new KeyValue(anchorRight.translateXProperty(), nextTargetX - 20, new SineInterpolator())
               ),
               new KeyFrame(Duration.seconds(1.2),
                       new KeyValue(anchorLeft.translateXProperty(), nextTargetX + 20, new SineInterpolator())
               )
       );

       slideAnimation.play();
    }
}
