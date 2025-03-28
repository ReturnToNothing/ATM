package com.atm;

import javafx.fxml.FXMLLoader;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.layout.*;
import javafx.stage.Stage;

import javafx.event.ActionEvent;

import javafx.scene.Scene;
import javafx.scene.Node;

import java.io.IOException;
import java.net.URL;
import java.util.*;

public class View
{
    public Controller controller;
    public Model model;

    private final double height = 926;
    private final double width = 414;

    private Stage stage;
    private Scene scene;
    private StackPane root;

    private Map<String, Node> scenes = new HashMap<>();
    private Map<String, Object> controllers = new HashMap<>();

    public View(Stage stage, Controller controller, Model model) throws IOException
    {
        this.controller = controller;
        this.model = model;

        this.stage = stage;
        this.root = new StackPane();
        this.scene = new Scene(root, width, height);

        this.stage.setTitle("EAJ");
        this.stage.getIcons().add(fetchImage("LunU3.png"));

        this.stage.setHeight(height);
        this.stage.setWidth(width);
        this.stage.centerOnScreen();

        this.stage.setResizable(false);
        this.stage.setScene(this.scene);
        this.stage.show();

        // Initial/Intro scene, might rename later
        IntroController introScene = (IntroController) loadScene("intro-view.fxml", "intro-view");
        introScene.initialize();

        // Input scenes
        InputController inputScene = (InputController) loadScene("input.fxml", "input");
        inputScene.initialize(this.controller);

        // Login scenes
        LogInController loginScene = (LogInController) loadScene("login.fxml", "login");
        loginScene.initialize(this.controller);

        // Welcome scenes
        TutorialController tutorialScene = (TutorialController) loadScene("tutorial.fxml", "tutorial");
        tutorialScene.initialize(this.controller);

    }

    public Object loadScene(String fxmlFile, String fxmlName) throws IOException
    {
        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/com/atmfxml/" + fxmlFile));
        Node sceneNode = fxmlLoader.load();

        root.getChildren().addFirst(sceneNode); // load the fxml file into the root.

        controllers.put(fxmlName, fxmlLoader.getController());
        scenes.put(fxmlName, sceneNode);

        return fxmlLoader.getController();
    }

    public void unloadScene(String fxmlName) throws IOException
    {
        Object controller = controllers.get(fxmlName);
        if (controller != null)
        {
            controllers.remove(fxmlName);
        }
    }

    public Object getController(String fxmlName)
    {
        return controllers.get(fxmlName);
    }

    public void initializeScene(String fxmlName, Node... nodes)
    {
        Node sceneNode = scenes.get(fxmlName);

        if (sceneNode == null) return;
        //TODO: use the vargs later..
        switch (fxmlName)
        {
            case "intro-view":
                // Another reference for retrieving elements from the scene.
                // https://stackoverflow.com/questions/42781401/javafx-is-it-more-efficient-to-select-node-using-lookup-or-link-to-controller-t

                IntroController introController = (IntroController) getController("intro-view");
                if (introController != null)
                {
                    introController.initialize();
                }

                break;
            case "welcome-view":
                WelcomeController welcomeController = (WelcomeController) getController("welcome-view");
                if (welcomeController != null)
                {
                  //  welcomeController.initialize();
                }

                break;
        }
    }

    private Image fetchImage(String imageFile)
    {
        // concatenating the path by appending the specified id before searching the image's URL.
        String imagePath = "/com/atmimages/" + imageFile;
        URL URLPath = getClass().getResource(imagePath);

        // adding exception since fetching an invalid path could potentially crash out.
        if (URLPath == null)
        {
            return new Image("");
        }

        // finally, set the image into the imageView before applying it's size.
        return new Image(URLPath.toExternalForm());
    }

    private void buttonClicked(ActionEvent event, String input)
    {
        Button button = (Button)event.getSource();
        if (this.controller != null)
        {
            this.controller.process(input);
        }
    }

    public void update()
    {

        States tutorialState = this.model.getState(com.atm.Scene.TUTORIAL);
        States loginState = this.model.getState(com.atm.Scene.LOGIN);
        States inputState = this.model.getState(com.atm.Scene.INPUT);

        TutorialController tutorialController = (TutorialController) getController("tutorial");
        tutorialController.slide(tutorialState);

        LogInController loginController = (LogInController) getController("login");
        loginController.slide(loginState);

        InputController inputController = (InputController) getController("input");
        inputController.slide(inputState);

        /*
            // fetch both internal states of the model
            States state = this.model.state;
            States prevState = this.model.prevState;

            if (Objects.equals(type, "tutorial")) {
                System.out.println("Current State: " + state + " | Previous State: " + prevState);

                WelcomeController welcomeController = (WelcomeController) getController("welcome-view");
                TutorialController tutorialOneController = (TutorialController) getController("tutorialOne-view");
                TutorialController tutorialTwoController = (TutorialController) getController("tutorialTwo-view");
                TutorialController tutorialThreeController = (TutorialController) getController("tutorialThree-view");

                LoginController loginController = (LoginController) getController("login-view");
                InputController inputController = (InputController) getController("input-view");

                if (state.equals(States.DEFAULT))
                {
                    if (prevState.equals(States.TUTORIAL_ONE)) {
                        welcomeController.slideIn();
                        tutorialOneController.slideRight();
                    }
                    if (prevState.equals(States.TUTORIAL_THREE))
                    {
                        loginController.slideIn();
                        inputController.slideIn();
                        tutorialThreeController.slideLeft();
                    }
                }

                if (state.equals(States.TUTORIAL_ONE))
                {
                    if (prevState.equals(States.TUTORIAL_TWO)) {
                        tutorialTwoController.slideRight();
                    }
                    welcomeController.slideOut();
                    tutorialOneController.slideIn();
                }

                if (state.equals(States.TUTORIAL_TWO))
                {
                    if (prevState.equals(States.TUTORIAL_THREE)) {
                        tutorialThreeController.slideRight();
                    }
                    tutorialOneController.slideLeft();
                    tutorialTwoController.slideIn();
                }

                if (state.equals(States.TUTORIAL_THREE))
                {
                    tutorialTwoController.slideLeft();
                    tutorialThreeController.slideIn();
                }
            }

             */
    }
}
