package com.atm;

import javafx.animation.Interpolator;
import javafx.animation.KeyFrame;
import javafx.animation.KeyValue;
import javafx.animation.Timeline;
import javafx.fxml.FXML;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.Region;
import javafx.scene.text.Text;
import javafx.util.Duration;

public class IntroController
{
    // declared elements that are automatically injected from the FXML document.
    @FXML public AnchorPane anchor;
    @FXML public Text title;
    @FXML public Text title1;
    @FXML public ImageView logo;
    @FXML public ImageView background;
    @FXML public Region regionBlue;

    public void initialize()
    {
        startAnimation();
    }

    private void startAnimation()
    {
        // Ensure the backgroundPane's size is available
        double maxRadius = Math.max(360, 640);

        // Initially applying values
        background.setOpacity(0);
        regionBlue.setOpacity(0);
        title.setOpacity(0);
        title1.setOpacity(0);

        Timeline fadeOutAnimation = new Timeline(
                new KeyFrame(Duration.seconds(1), new KeyValue(anchor.opacityProperty(), 0, Interpolator.EASE_BOTH))
        );

        Timeline fadeAnimation = new Timeline(
                new KeyFrame(Duration.seconds(2), new KeyValue(logo.scaleYProperty(), 10, Interpolator.EASE_BOTH)),
                new KeyFrame(Duration.seconds(2), new KeyValue(logo.scaleXProperty(), 10, Interpolator.EASE_BOTH)),
                new KeyFrame(Duration.seconds(1), new KeyValue(regionBlue.opacityProperty(), 0, Interpolator.EASE_BOTH))
        );

        Timeline disappearAnimation = new Timeline(
                new KeyFrame(Duration.seconds(2), new KeyValue(background.opacityProperty(), 0, Interpolator.EASE_BOTH)),
                new KeyFrame(Duration.seconds(1), new KeyValue(title1.opacityProperty(), 0, Interpolator.EASE_BOTH)),
                new KeyFrame(Duration.seconds(1), new KeyValue(title.opacityProperty(), 0, Interpolator.EASE_BOTH))
        );

        Timeline appearAnimation = new Timeline(
                new KeyFrame(Duration.seconds(2), new KeyValue(background.opacityProperty(), 1, Interpolator.EASE_BOTH)),
                new KeyFrame(Duration.seconds(2), new KeyValue(regionBlue.opacityProperty(), 1, Interpolator.EASE_BOTH))
        );

        Timeline appearTitleAnimation = new Timeline(
                new KeyFrame(Duration.seconds(1), new KeyValue(title1.opacityProperty(), 1, Interpolator.EASE_BOTH))
        );

        Timeline appearTextAnimation = new Timeline(
                new KeyFrame(Duration.seconds(1), new KeyValue(title.opacityProperty(), 1, Interpolator.EASE_BOTH))
        );

        appearTextAnimation.setOnFinished(event -> {
            disappearAnimation.play();
        });

        appearTitleAnimation.setOnFinished(event -> {
            appearTextAnimation.play();
        });

        appearAnimation.setOnFinished(event -> {
            appearTitleAnimation.play();
        });

        disappearAnimation.setOnFinished(event -> {
            fadeAnimation.play();
        });

        fadeAnimation.setOnFinished(event -> {
           // anchor.setOpacity(0);
            title.setOpacity(0);
            title1.setOpacity(0);
            logo.setOpacity(0);
            background.setOpacity(0);
            regionBlue.setOpacity(0);
            fadeOutAnimation.play();
        });

        disappearAnimation.setDelay(Duration.seconds(1));
        appearAnimation.setDelay(Duration.seconds(1));

        appearAnimation.play();
    }
}
