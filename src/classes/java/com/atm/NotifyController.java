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

public class NotifyController
{
    @FXML public AnchorPane anchor;
    @FXML public Text title;
    @FXML public Text description;
    @FXML public Button closeButton;

    private Controller controller;
    private States state;

    public void initialize(Controller controller)
    {
        this.state = States.DEFAULT;
        this.controller = controller;

        closeButton.setId("Close-Notify");

        anchor.setOpacity(0);

        BindButtons();
    }

    public void BindButtons()
    {
        Button[] buttons = {
                closeButton
        };

        for (Button button : buttons) {
            hoverButton(button);
            BindPressed(button);
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

    private void BindPressed(Button button) {
        //TODO: Add ripple effect
        button.setOnMousePressed(event -> {
            this.controller.process(button.getId());
        });
    }

    public void slide(States state)
    {
        if (this.state.equals(state))
        {
            return;
        }

        this.state = state;

        double targetX = switch (state)
        {
            case DEFAULT -> 0;
            case NOTIFY_SHOW -> 1;
            default -> 0; // Default position for the welcome page
        };

        // Make the anchor transparent when in DEFAULT state
        // Simply because login overlaps with the tutorial scene

       anchor.setMouseTransparent(state == States.DEFAULT);

       Timeline slideAnimation = new Timeline(
                new KeyFrame(Duration.seconds(.5),
                        new KeyValue(anchor.opacityProperty(), targetX, new SineInterpolator())
                )
       );

        slideAnimation.play();
    }

    public void update(String title, String description)
    {
        this.title.setText(title);
        this.description.setText(description);
    }
}
