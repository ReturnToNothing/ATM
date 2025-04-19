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

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

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
    @FXML public Button remove;

    private Controller controller;
    private States state;

    private final Map<Button, String[]> letterMap = new HashMap<>();

    private Map<Button, Integer> letterIndex = new HashMap<>();
    private boolean hasCycled = false;

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
        remove.setId("Remove-Input");

        letterMap.put(One, new String[]{"A", "B", "C"});
        letterMap.put(Two, new String[]{"D", "E", "F"});
        letterMap.put(Three, new String[]{"G", "H", "I"});
        letterMap.put(Four, new String[]{"J", "K", "L"});
        letterMap.put(Five, new String[]{"M", "N", "O"});
        letterMap.put(Six, new String[]{"P", "Q", "R"});
        letterMap.put(Seven, new String[]{"S", "T", "U"});
        letterMap.put(Eight, new String[]{"V", "W", "X"});
        letterMap.put(Nine, new String[]{"Y", "Z"});
        letterMap.put(Zero, new String[]{" "});

        anchor.setTranslateY(896);

        BindButtons();
        BindClickAway();
    }

    public void BindClickAway()
    {
        // ref; https://www.npmjs.com/package/react-click-away-listener
        // Click-away listener for closing this scene whenever the end-user
        // clicks outside its boundary.
        anchor.setOnMouseClicked(event ->
        {
            // Check if click happened outside the input anchor,
            // we can track the end-user clicks position by extracting the event
            if (!stackPane.getBoundsInParent().contains(event.getX(), event.getY()))
            {
                controller.process("Close-Input");
            }
        });
    }

    public void BindButtons()
    {
        Button[] buttons = {
                One, Two, Three, Four, Five, Six, Seven, Eight, Nine, Zero, clear, remove
        };

        for (Button button : buttons)
        {
            BindPressed(button);
        }
    }

    private void BindPressed(Button button)
    {
        //TODO: Add ripple effect
        button.setOnMousePressed(event ->
        {
            String action = button.getId();

            if (Objects.equals(action, "Clear-Input") || Objects.equals(action, "Remove-Input"))
            {
                this.controller.process(action);
                return;
            }

            switch (this.state)
            {
                case INPUT_DIGIT ->
                {
                    System.out.println("action " + "prcessed onced");
                    this.controller.process(action);
                }
                case INPUT_BALANCE ->
                {
                    this.controller.processBalance(action);
                }
                case INPUT_STRING ->
                {
                    Timeline holdCycle = new Timeline(
                            new KeyFrame(Duration.seconds(0.5),
                                    cycleEvent -> cycleLetter(button))
                    );
                    holdCycle.setCycleCount(Timeline.INDEFINITE);
                    holdCycle.playFromStart();

                    button.setUserData(holdCycle);
                }
            }
        });

        button.setOnMouseReleased(event ->
        {
            if (this.state.equals(States.INPUT_STRING))
            {
                Timeline holdCycle = (Timeline) button.getUserData();

                if (holdCycle != null)
                {
                    holdCycle.stop();
                }

                // The Cycle is active once the end-user held the button for less than a second.
                // If the cycle isn't active, then its initial letter is sent. (A)
                // Otherwise, releasing the held button should send the cycled letter (A -> B)
                if (!hasCycled)
                {
                    selectFirstLetter(button);
                }
                else
                {
                    selectCycledLetter(button);
                }

                hasCycled = false; // reset for next press
            }
        });

        HoverButton(button);
    }

    private void HoverButton(Button button)
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
            case INPUT_DIGIT, INPUT_BALANCE, INPUT_STRING -> 0;
            default -> 896;
        };

        double delay = switch (state)
        {
            case INPUT_DIGIT, INPUT_BALANCE, INPUT_STRING -> 0;
            default -> 0;
        };

        anchor.setMouseTransparent(state == States.DEFAULT);

        Timeline slideAnimation = new Timeline(
                new KeyFrame(Duration.seconds(1),
                        new KeyValue(anchor.translateYProperty(), targetY, new SineInterpolator())
                )
        );

       // slideAnimation.setDelay(Duration.seconds(delay));

       slideAnimation.playFromStart();
    }

    private void cycleLetter(Button button)
    {
        String[] letters = letterMap.get(button);

        int index = letterIndex.getOrDefault(button, 0);
        index = (index + 1) % letters.length;

        letterIndex.put(button, index);

        System.out.println("Cycling: " + letters[index]);

        hasCycled = true;
    }

    private void selectFirstLetter(Button button)
    {
        String[] letters = letterMap.get(button);

        if (letters != null && letters.length > 0)
        {
            String selectedLetter = letters[0];

            System.out.println("Selected: " + selectedLetter);

            this.controller.processString(selectedLetter);
        }
    }

    private void selectCycledLetter(Button button)
    {
        String[] letters = letterMap.get(button);
        int index = letterIndex.getOrDefault(button, 0);

        if (letters != null && letters.length > 0)
        {
            String selectedLetter = letters[index];
            System.out.println("Cycle Selected: " + selectedLetter);

            this.controller.processString(selectedLetter);
        }
    }
}
