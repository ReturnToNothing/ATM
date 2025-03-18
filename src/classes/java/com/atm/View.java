package com.atm;

import javafx.animation.*;
import javafx.geometry.Pos;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.*;
import javafx.stage.Stage;

import javafx.event.ActionEvent;
import javafx.util.Duration;

import javafx.scene.Scene;
import javafx.scene.Node;

import java.net.URL;
import java.util.ArrayList;
import java.util.List;

public class View
{
    public Controller controller;
    public Model model;

    private final int height = 640;
    private final int width = 360;

    private GridPane grid;
    private GridPane grid2;
    private TilePane buttonTile;
    private TilePane actionTile;
    private Label title;
    private TextField message;
    private TextArea reply;
    private ScrollPane scrollPane;

    private BorderPane reloadLayer;
    private BorderPane introLayer;
    private BorderPane firstLayer;
    private HBox secondLayer;
    private HBox thirdLayer;
    private HBox fourthLayer;
    private GridPane inputLayer;

    public View(Stage stage)
    {
        stage.setTitle("ATM");
        stage.getIcons().add(fetchImage("Icon.png", 256, 256));

        stage.setHeight(height);
        stage.setWidth(width);
        stage.centerOnScreen();

        reloadLayer = addReloadLayer();
        introLayer = addIntroLayer();
        firstLayer = addFirstLayer();
        secondLayer = addSecondLayer();
        thirdLayer = addThirdLayer();
        fourthLayer = addProfileLayer();
        inputLayer = addInputLayer();

        // Each layer is stacked from the opposite order
        StackPane root = new StackPane(firstLayer, secondLayer, thirdLayer, fourthLayer, inputLayer, reloadLayer, introLayer);
        Scene scene = new Scene(root, width, height);
        scene.getStylesheets().add(getClass().getResource("/com/atm/atm.css").toExternalForm());

        // Play the intro of the application before fading away.
        FadeIn();

        /*
        // numerical numbers ranging from 1-9
        String[][] digits = {
                {"1", "2", "3",},
                {"4", "5", "6",},
                {"7", "8", "9"}
        };

        // action buttons
        String[][] actions = {
                {"enter",},
                {"clear",},
                {"cancel"}
        };

        /*
        String[][] labels = {
                {"1", "2", "3", "", "Dep", ""},
                {"4", "5", "6", "", "W/D", ""},
                {"7", "8", "9", "", "Bal", "Fin"},
                {"CLR", "0", "00", "", "", "Ent"}
        };


        this.title = createLabel("Title");
        this.message = createTextField("Message", 0, false);
        this.reply = createTextArea("Reply", 0, false);
        this.scrollPane = createScrollPane("ScrollPane", this.reply);

        Button[][] digitButtons = createButtonGrid(digits);
        Button[][] actionButtons = createButtonGrid(actions);

        styleButtons(digitButtons);
        styleButtons(actionButtons);

        this.buttonTile = createTilePane("Buttons", digitButtons);
        this.actionTile = createTilePane("Actions", actionButtons);

        this.grid2 = createGridPane("Layout", false, this.buttonTile, this.actionTile);
        this.grid = createGridPane("Layout", true, this.title, this.message, this.scrollPane, this.grid2);
        this.grid.setAlignment(Pos.CENTER);

        Scene scene = new Scene(this.grid, width, height);
*/
        stage.setResizable(false);
        stage.setScene(scene);
        stage.show();
    }

    public void Load(boolean load, double duration)
    {
        KeyValue LoadInKey = new KeyValue(reloadLayer.opacityProperty(), 0.5, Interpolator.EASE_BOTH);
        KeyValue LoadOutKey = new KeyValue(reloadLayer.opacityProperty(), 0, Interpolator.EASE_BOTH);

        Timeline LoadIn = new Timeline(
                new KeyFrame(Duration.seconds(duration), LoadInKey)
        );
        Timeline LoadOut = new Timeline(
                new KeyFrame(Duration.seconds(duration), LoadOutKey)
        );

       LoadOut.setOnFinished(event -> reloadLayer.setTranslateY(-height));

        if (load)
        {
            reloadLayer.setTranslateY(0);
            LoadIn.play();
        }
        else
        {
            LoadOut.play();
        }
    }

    public void FadeIn()
    {
        KeyValue fadeInKey = new KeyValue(introLayer.opacityProperty(), 0, Interpolator.EASE_BOTH);
        PauseTransition delayFade = new PauseTransition(Duration.seconds(3));

        Timeline fadeIn = new Timeline(
                new KeyFrame(Duration.seconds(2), fadeInKey)
        );

        fadeIn.setOnFinished(event ->
        {
            introLayer.setTranslateY(height);
            Load(false, 1f);
        });

        delayFade.setOnFinished(event -> fadeIn.play());
        delayFade.play();
    }

    public void slideIn(States state)
    {
        PauseTransition loadDelay = new PauseTransition(Duration.seconds(1));

        KeyValue slideInput = new KeyValue(inputLayer.translateYProperty(), 0, Interpolator.EASE_BOTH);
        KeyValue slideSecondLayer = new KeyValue(secondLayer.translateXProperty(), -50, Interpolator.EASE_BOTH);
        KeyValue slideThirdLayer = new KeyValue(thirdLayer.translateXProperty(), -50, Interpolator.EASE_BOTH);
        KeyValue slideFourthLayer = new KeyValue(fourthLayer.translateXProperty(), -50, Interpolator.EASE_BOTH);

        // A slide animation for the secondLayer, including enabling the InputLayer.
        Timeline slideInPIN = new Timeline(
                new KeyFrame(Duration.seconds(1), slideInput),
                new KeyFrame(Duration.seconds(0.7), slideSecondLayer)
        );

        Timeline slideInPASS = new Timeline(
                new KeyFrame(Duration.seconds(1), slideInput),
                new KeyFrame(Duration.seconds(0.7), slideThirdLayer)
        );

        //Todo: have the inputLayer as a separate animation, since specific layers doesn't require any input yet.
        Timeline slideInProfile = new Timeline(
                new KeyFrame(Duration.seconds(1), slideInput),
                new KeyFrame(Duration.seconds(0.7), slideFourthLayer)
        );

        switch (state)
        {
            case States.DEFAULT:
                Load(true, 1);

                loadDelay.setOnFinished(event -> {
                    Load(false, 1);
                    slideInPIN.playFromStart();
                });

                loadDelay.play();
                break;
            case States.ACCOUNT_NO:
                Load(true, 1);

                loadDelay.setOnFinished(event -> {
                    Load(false, 1);
                    slideInPASS.playFromStart();
                });
                loadDelay.play();
                break;
            case States.PASSWORD_NO:
                Load(true, 1);

                loadDelay.setOnFinished(event -> {
                    Load(false, 1);
                    slideInProfile.playFromStart();
                });
                loadDelay.play();
                break;
        }
    }

    public void slideOut(States state)
    {
        PauseTransition loadDelay = new PauseTransition(Duration.seconds(0.5));

        KeyValue slideInput = new KeyValue(inputLayer.translateYProperty(), height, Interpolator.EASE_BOTH);
        KeyValue slideSecondLayer = new KeyValue(secondLayer.translateXProperty(), width, Interpolator.EASE_BOTH);
        KeyValue slideThirdLayer = new KeyValue(thirdLayer.translateXProperty(), width, Interpolator.EASE_BOTH);
        KeyValue slideFourthLayer = new KeyValue(fourthLayer.translateXProperty(), width, Interpolator.EASE_BOTH);

        // A slide animation for the secondLayer, including enabling the InputLayer.
        Timeline slideOutPIN = new Timeline(
                new KeyFrame(Duration.seconds(1), slideInput),
                new KeyFrame(Duration.seconds(0.5), slideSecondLayer)
        );

        Timeline slideOutPASS = new Timeline(
                new KeyFrame(Duration.seconds(0.5), slideThirdLayer)
        );

        Timeline slideOutProfile = new Timeline(
                new KeyFrame(Duration.seconds(1), slideInput),
                new KeyFrame(Duration.seconds(0.7), slideFourthLayer)
        );

        switch (state)
        {
            case States.ACCOUNT_NO:

                Load(true, 0.5f);

                loadDelay.setOnFinished(event -> {
                    Load(false, 0.5f);
                    slideOutPIN.playFromStart();
                });

                loadDelay.play();
                break;
            case States.PASSWORD_NO:
                Load(true, 0.5f);

                loadDelay.setOnFinished(event -> {
                    Load(false, 0.5f);
                    slideOutPASS.playFromStart();
                });

                loadDelay.play();
                break;
            case States.LOGGED_IN:
                Load(true, 0.5f);

                loadDelay.setOnFinished(event -> {
                    Load(false, 0.5f);
                    slideOutProfile.playFromStart();
                });

                loadDelay.play();
                break;
        }
    }

    private BorderPane addIntroLayer()
    {
        BorderPane introLayer = new BorderPane();
        introLayer.setId("introLayer");
        introLayer.setPrefSize(width, height);

        return introLayer;
    }

    private BorderPane addReloadLayer()
    {
        ImageView reloadView = new ImageView(fetchImage("loading_icon.gif", 441, 291));
        reloadView.setId("reloadView");
        reloadView.setPreserveRatio(true);

        //gif taken from https://commons.wikimedia.org/wiki/File:Loading_icon.gif
        BorderPane reloadLayer = new BorderPane();
        reloadLayer.setCenter(reloadView);
        reloadLayer.setId("reloadLayer");
        reloadLayer.setPrefSize(width, height);
        reloadLayer.setOpacity(0.5f);

        return reloadLayer;
    }

    private BorderPane addFirstLayer()
    {
        ImageView introView = new ImageView(fetchImage("poster.png", 290, 360));
        introView.setId("introView");
        introView.setPreserveRatio(true);

        Label introLabel = new Label("Welcome to the ATM");
        introLabel.setId("introLabel");

        Button startButton = new Button("Log In");
        startButton.setId("startButton");
        startButton.setOnAction(event -> buttonClicked(event, "LogIn"));

        Button createButton = new Button("Sign In");
        createButton.setId("createButton");

        Button optionButton = new Button("...");
        optionButton.setId("optionButton");

        Button lastButton = new Button("Create Account");
        lastButton.setId("lastButton");

        Separator separator = new Separator();
        separator.setId("separator");

        Separator separator1 = new Separator();
        separator1.setId("separator");

        Separator separator2 = new Separator();
        separator2.setId("separator");

        StackPane topStack = new StackPane(introLabel, optionButton);
        topStack.setId("topStack");
        topStack.setPrefSize(width, 50);
        StackPane.setAlignment(introLabel, Pos.CENTER_LEFT);
        StackPane.setAlignment(optionButton, Pos.CENTER_RIGHT);

        VBox topContainer = new VBox(10, topStack, separator, startButton, createButton, separator1);
        topContainer.setId("topContainer");
        topContainer.setAlignment(Pos.CENTER);

        VBox middleContainer = new VBox(introView);
        middleContainer.setId("middleContainer");
        middleContainer.setAlignment(Pos.CENTER);

        VBox bottomContainer = new VBox(10, separator2, lastButton);
        bottomContainer.setId("bottomContainer");
        bottomContainer.setAlignment(Pos.BOTTOM_CENTER);

        BorderPane firstLayer = new BorderPane();
        firstLayer.setId("firstLayer");
        firstLayer.setTop(topContainer);
        firstLayer.setCenter(middleContainer);
        firstLayer.setBottom(bottomContainer);
        firstLayer.setPrefSize(width, height);

        return firstLayer;
    }

    private HBox addSecondLayer()
    {
        Button backButton = new Button("<");
        backButton.setId("backButton");
        backButton.setOnAction(event -> buttonClicked(event, "LogOut"));

        Label loginLabel = new Label("Log In");
        loginLabel.setId("loginLabel");

        Label messageLabel = new Label("Enter your Account PIN");
        messageLabel.setId("messageLabel");

        Separator separator = new Separator();
        separator.setId("separator");

        Separator separator1 = new Separator();
        separator1.setId("separator");

        Separator separator2 = new Separator();
        separator2.setId("separator");

        Separator separator3 = new Separator();
        separator3.setId("separator");

        Button enterButton = new Button("Enter with your current PIN");
        enterButton.setId("enterButton");
        enterButton.setOnAction(event -> buttonClicked(event, "LogIn"));

        StackPane messageStack = new StackPane(messageLabel);
        messageStack.setId("loginStack");
        messageStack.setAlignment(Pos.CENTER_LEFT);

        // Generating Six textboxes in a row before inserting them into a Hbox, these acts as inputs.
        TextField[] pinFields = new TextField[6];
        for (int index = 0; index < 6; index++)
        {
            pinFields[index] = new TextField();
            pinFields[index].setId("pinField");
            pinFields[index].setPrefWidth(60);
            pinFields[index].setPrefHeight(60);
            pinFields[index].setEditable(false);
        }

        HBox pinHBox = new HBox(20, pinFields);
        pinHBox.setId("pinHBox");

        StackPane topStack = new StackPane(loginLabel, backButton);
        topStack.setId("topStack");
        topStack.setPrefSize(width, 50);
        StackPane.setAlignment(loginLabel, Pos.CENTER_LEFT);
        StackPane.setAlignment(backButton, Pos.CENTER_RIGHT);

        VBox topContainer = new VBox(10, topStack, separator1, messageStack, pinHBox, enterButton, separator2);
        topContainer.setId("topContainer");
        topContainer.setAlignment(Pos.CENTER);

        VBox bottomContainer = new VBox(separator3);
        bottomContainer.setId("bottomContainer");
        bottomContainer.setAlignment(Pos.TOP_CENTER);

        BorderPane secondLayer = new BorderPane();
        secondLayer.setId("secondLayer");
        secondLayer.setTop(topContainer);
        secondLayer.setBottom(bottomContainer);
        secondLayer.setPrefSize(width, height);
        secondLayer.setTranslateX(50);

        HBox gradientLayer = new HBox(secondLayer);
        gradientLayer.setId("gradientLayer");

        // Initially position secondLayer off-screen to the left side.
        gradientLayer.setTranslateX(width);

        return gradientLayer;
    }

    private HBox addThirdLayer()
    {
        // Exactly similar to the secondLayer, except we simply configure the labels.
        Button backButton = new Button("<");
        backButton.setId("backButton");
        backButton.setOnAction(event -> buttonClicked(event, "LogOut"));

        Label loginLabel = new Label("Log In");
        loginLabel.setId("loginLabel");

        Label messageLabel = new Label("Enter your Account PASS");
        messageLabel.setId("messageLabel");

        Separator separator = new Separator();
        separator.setId("separator");

        Separator separator1 = new Separator();
        separator1.setId("separator");

        Separator separator2 = new Separator();
        separator2.setId("separator");

        Separator separator3 = new Separator();
        separator3.setId("separator");

        Button enterButton = new Button("Enter with your current PASS");
        enterButton.setId("enterButton");
        enterButton.setOnAction(event -> buttonClicked(event, "LogIn"));

        StackPane messageStack = new StackPane(messageLabel);
        messageStack.setId("loginStack");
        messageStack.setAlignment(Pos.CENTER_LEFT);

        // Generating Six textboxes in a row before inserting them into a Hbox, these acts as inputs.
        TextField[] pinFields = new TextField[6];
        for (int index = 0; index < 6; index++)
        {
            pinFields[index] = new TextField();
            pinFields[index].setId("pinField");
            pinFields[index].setPrefWidth(60);
            pinFields[index].setPrefHeight(60);
            pinFields[index].setEditable(false);
        }

        HBox pinHBox = new HBox(20, pinFields);
        pinHBox.setId("pinHBox");

        StackPane topStack = new StackPane(loginLabel, backButton);
        topStack.setId("topStack");
        topStack.setPrefSize(width, 50);
        StackPane.setAlignment(loginLabel, Pos.CENTER_LEFT);
        StackPane.setAlignment(backButton, Pos.CENTER_RIGHT);

        VBox topContainer = new VBox(10, topStack, separator1, messageStack, pinHBox, enterButton, separator2);
        topContainer.setId("topContainer");
        topContainer.setAlignment(Pos.CENTER);

        VBox bottomContainer = new VBox(separator3);
        bottomContainer.setId("bottomContainer");
        bottomContainer.setAlignment(Pos.TOP_CENTER);

        BorderPane thirdLayer = new BorderPane();
        thirdLayer.setId("secondLayer");
        thirdLayer.setTop(topContainer);
        thirdLayer.setBottom(bottomContainer);
        thirdLayer.setPrefSize(width, height);
        thirdLayer.setTranslateX(50);

        HBox gradientLayer = new HBox(thirdLayer);
        gradientLayer.setId("gradientLayer");

        // Initially position thirdLayer off-screen to the left side.
        gradientLayer.setTranslateX(width);

        return gradientLayer;
    }

    private HBox addProfileLayer()
    {
        Button backButton = new Button("<");
        backButton.setId("backButton");
        backButton.setOnAction(event -> buttonClicked(event, "LogOut"));

        Label loginLabel = new Label("Log In");
        loginLabel.setId("loginLabel");

        Label messageLabel = new Label("Enter your Account PASS");
        messageLabel.setId("messageLabel");

        Separator separator = new Separator();
        separator.setId("separator");

        Separator separator1 = new Separator();
        separator1.setId("separator");

        Separator separator2 = new Separator();
        separator2.setId("separator");

        Separator separator3 = new Separator();
        separator3.setId("separator");

        Button enterButton = new Button("Enter with your current PASS");
        enterButton.setId("enterButton");
        enterButton.setOnAction(event -> buttonClicked(event, "LogIn"));

        StackPane messageStack = new StackPane(messageLabel);
        messageStack.setId("loginStack");
        messageStack.setAlignment(Pos.CENTER_LEFT);

        // Generating Six textboxes in a row before inserting them into a Hbox, these acts as inputs.
        TextField[] pinFields = new TextField[6];
        for (int index = 0; index < 6; index++)
        {
            pinFields[index] = new TextField();
            pinFields[index].setId("pinField");
            pinFields[index].setPrefWidth(60);
            pinFields[index].setPrefHeight(60);
            pinFields[index].setEditable(false);
        }

        HBox pinHBox = new HBox(20, pinFields);
        pinHBox.setId("pinHBox");

        StackPane topStack = new StackPane(loginLabel, backButton);
        topStack.setId("topStack");
        topStack.setPrefSize(width, 80);
        StackPane.setAlignment(loginLabel, Pos.CENTER_LEFT);
        StackPane.setAlignment(backButton, Pos.CENTER_RIGHT);

        VBox topContainer = new VBox(10, topStack, separator1, messageStack);
        topContainer.setId("topContainer");
        topContainer.setAlignment(Pos.CENTER);

        VBox bottomContainer = new VBox(separator3);
        bottomContainer.setId("bottomContainer");
        bottomContainer.setAlignment(Pos.TOP_CENTER);

        BorderPane thirdLayer = new BorderPane();
        thirdLayer.setId("secondLayer");
        thirdLayer.setTop(topContainer);
        thirdLayer.setBottom(bottomContainer);
        thirdLayer.setPrefSize(width, height);
        thirdLayer.setTranslateX(50);

        HBox gradientLayer = new HBox(thirdLayer);
        gradientLayer.setId("gradientLayer");

        // Initially position thirdLayer off-screen to the left side.
        gradientLayer.setTranslateX(width);

        return gradientLayer;
    }

    private GridPane addInputLayer()
    {
        Separator separator = new Separator();
        separator.setId("separator");

        // numerical numbers ranging from 1-9
        String[][] digits = {
                {"1", "2", "3",},
                {"4", "5", "6",},
                {"7", "8", "9",},
                {" ", "0", "Clear"}
        };

        Button[][] digitButtons = createButtonGrid(digits);

        TilePane buttonTile = new TilePane();
        buttonTile.setAlignment(Pos.CENTER);
        buttonTile.setId("Buttons");

        for (Button[] row : digitButtons)
        {
            for (Button button : row)
            {
                // Filter nullable instance within the table; ignoring empty labels.
                if (button != null)
                {
                    buttonTile.getChildren().add(button);
                }
            }
        }

        Region backgroundLayer = new Region();
        backgroundLayer.setId("backgroundLayer");
        backgroundLayer.setPrefSize(width, 200);

        StackPane inputContainer = new StackPane(backgroundLayer, buttonTile);
        inputContainer.setAlignment(Pos.CENTER);

        GridPane inputLayer = createGridPane("inputLayer", false, inputContainer);
        inputLayer.setAlignment(Pos.BOTTOM_CENTER);
        inputLayer.setPickOnBounds(false);

        //-fx-translate-y: 10px; should work in CSS

        inputLayer.setTranslateY(height);

        return inputLayer;
    }

    private GridPane createGridPane(String id, boolean row, Node... instances)
    {
        GridPane grid = new GridPane();
        grid.setId(id);

        /*
        Instances within the node are sequentially added within the GridPane, especially
        incrementally applying their position in columns and rows.
         */
        int currentRow = 0;
        int currentCol = 0;
        for (Node instance : instances)
        {
            if (row) {
                grid.add(instance, 0, ++currentRow);
            }
            else
            {
                grid.add(instance, ++currentCol, 0);
            };
        }

        return grid;
    }

    private TilePane createTilePane(String id, Button[][] instances)
    {
        TilePane tile = new TilePane();
        tile.setId(id);

        // Iterating through the nested table containing buttons.
        for (Button[] row : instances)
        {
            for (Button button : row)
            {
                // Filter nullable instance within the table; ignoring empty labels.
                if (button != null)
                {
                    tile.getChildren().add(button);
                }
            }
        }

        return tile;
    }

    private Label createLabel(String id)
    {
        Label label = new Label(id);
        label.setId(id);

        return label;
    }

    private TextField createTextField(String id, int columns, boolean editable)
    {
        TextField textField = new TextField();

        textField.setPrefColumnCount(columns);
        textField.setText(id);
        textField.setId(id);

        textField.setEditable(editable);

        return textField;
    }

    private TextArea createTextArea(String id, int columns, boolean editable)
    {
        TextArea textArea = new TextArea();

        textArea.setPrefColumnCount(columns);
        textArea.setText(id);
        textArea.setId(id);

        textArea.setEditable(editable);

        return textArea;
    }

    private ScrollPane createScrollPane(String id, Node content)
    {
        ScrollPane scrollPane = new ScrollPane();
        scrollPane.setId(id);

        scrollPane.setContent(content);
        scrollPane.setFitToWidth(true);
        scrollPane.setFitToHeight(true);

        return scrollPane;
    }

    private Image fetchImage(String id, double height, double width)
    {
        // concatenating the path by appending the specified id before searching the image's URL.
        String imagePath = "/com/atm/" + id;
        URL URLPath = getClass().getResource(imagePath);

        // adding exception since fetching invalid path could potentially crash out.
        if (URLPath == null)
        {
            return new Image("");
        }

        // finally, set the image into the imageView before applying it's size.
        return new Image(URLPath.toExternalForm());
    }

    private Button[][] createButtonGrid(String[][] labels)
    {
        int rows = labels.length;
        int cols = labels[0].length;
        Button[][] buttons = new Button[rows][cols];

        for (int row = 0; row < rows; row++)
        {
            for (int col = 0; col < cols; col++)
            {
                // Finds the next label within the next row and column.
                String label = labels[row][col];

                // label's length condition, accepting non-empty labels.
                if (!label.isEmpty())
                {
                    Button button = new Button(label);
                    button.setId("button");
                    button.setOnAction(event -> buttonClicked(event, label));
                    buttons[row][col] = button;
                }
                else
                {
                    Button newText = new Button(" ");
                    newText.setId("empty");
                    buttons[row][col] = newText;
                }
            }
        }
        return buttons;
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
        if (this.model != null)
        {
            States state = this.model.state;

            if (state.equals(States.ACCOUNT_NO) || state.equals(States.PASSWORD_NO))
            { // Update the Login's PIN
                int input = this.model.input;
                HBox selectedLayer = (state.equals(States.ACCOUNT_NO)) ? this.secondLayer : this.thirdLayer;

                // Initiating a list for accommodating TextFields.
                List<TextField> textFields = new ArrayList<TextField>(4);

                // Luckily, we can easily fetch the TextField via their target from css.
                selectedLayer.lookupAll("#pinField").forEach(node -> {
                    textFields.add((TextField) node);
                });

                String inputString = String.valueOf(input);

                if (inputString.equals("0")) {
                    inputString = "";
                }

                // Iterating through each TextFields by comparing with the inputString.
                for (int index = 0; index < textFields.size(); index++) {
                    TextField textField = textFields.get(index);

                    textField.getStyleClass().remove("glowing");

                    if (index < inputString.length()) {
                        // Set the TextField to the corresponding digit in the input
                        textField.setText(String.valueOf(inputString.charAt(index)));
                    } else {
                        // If there are no digits left to assign, clear the TextField (keep it empty)
                        textField.setText("");
                    }

                    // Glow the next TextField
                    if (index == inputString.length()) {
                        textField.getStyleClass().add("glowing");
                    }
                }
            }
        }
    }
}
