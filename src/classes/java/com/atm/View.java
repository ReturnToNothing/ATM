package com.atm;

import javafx.fxml.FXMLLoader;
import javafx.scene.image.Image;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.stage.Stage;

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
        this.scene.setFill(Color.TRANSPARENT);

        this.stage.setTitle("EAJ");
        this.stage.getIcons().add(getImage("LunU3.png"));

        this.stage.setHeight(height);
        this.stage.setWidth(width);
        this.stage.centerOnScreen();

        this.stage.setResizable(false);
        this.stage.setScene(this.scene);
        this.stage.show();

        // Initial/Intro scene, might rename later
        IntroController introScene = (IntroController) loadScene("intro-view.fxml", "intro-view");
        introScene.initialize();

        // Notification scene
        NotifyController notifyScene = (NotifyController) loadScene("notify.fxml", "notify");
        notifyScene.initialize(this.controller);

        // Input scenes
        InputController inputScene = (InputController) loadScene("input.fxml", "input");
        inputScene.initialize(this.controller);

        // Account scene
        AccountController accountScene = (AccountController) loadScene("account.fxml", "account");
        accountScene.initialize(this.controller, this.model);

        // SignIn scenes
        SignInController signinScene = (SignInController) loadScene("signin.fxml", "signin");
        signinScene.initialize(this.controller, this.model);

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

    private static Image getImage(String imageName)
    {
        // concatenating the path by appending the specified id before searching the image's URL.
        String imagePath = "/com/atmimages/" + imageName;
        URL URLPath = View.class.getResource(imagePath);

        // adding exception since fetching an invalid path could potentially crash out.
        if (URLPath == null)
        {
            return new Image("");
        }

        // finally, set the image into the imageView before applying it's size.
        return new Image(URLPath.toExternalForm());
    }

    public static Image getCardImage(String companyName)
    {
        // String-to-Image solver, by fetching the company name into their respective brand icons.
        return switch (companyName.toLowerCase())
        {
            case "visa" -> getImage("visa.png");
            case "visa-grey" -> getImage("visa-grey.png");

            case "mastercard" -> getImage("mastercard.png");
            case "mastercard-grey" -> getImage("mastercard-grey.png");

            case "hsbc" -> getImage("hsbc.png");
            case "hsbc-grey" -> getImage("hsbc-grey.png");

            case "profile" -> getImage("profile-white.png");
            case "profile-grey" -> getImage("profile-grey.png");

            default -> getImage("default-blue.png"); // Default logo
        };
    }

    public void update()
    {
        long input = this.model.getInput();
        String title = this.model.getTitle();
        String description = this.model.getDescription();
        Account account = this.model.getAccount();

        States notifyState = this.model.getState(com.atm.Scene.NOTIFY);
        States tutorialState = this.model.getState(com.atm.Scene.TUTORIAL);
        States loginState = this.model.getState(com.atm.Scene.LOGIN);
        States inputState = this.model.getState(com.atm.Scene.INPUT);
        States accountState = this.model.getState(com.atm.Scene.ACCOUNT);

        NotifyController notifyScene = (NotifyController) getController("notify");
        notifyScene.slide(notifyState);
        notifyScene.update(title, description);

        TutorialController tutorialController = (TutorialController) getController("tutorial");
        tutorialController.slide(tutorialState);

        SignInController signinController = (SignInController) getController("signin");
        signinController.update();

        LogInController loginController = (LogInController) getController("login");
        loginController.slide(loginState);
        loginController.update(input, account);

        AccountController accountController = (AccountController) getController("account");
        accountController.slide(accountState);
        accountController.update();

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
