package com.atm;

import javafx.geometry.Pos;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.stage.Stage;

import javafx.event.ActionEvent;

import javafx.scene.Scene;
import javafx.scene.Node;

import javafx.scene.layout.GridPane;
import javafx.scene.layout.TilePane;

import java.net.URL;
import java.util.Objects;

public class View
{
    public Controller controller;
    public Model model;

    private final int height = 420;
    private final int width = 500;

    private GridPane grid;
    private GridPane grid2;
    private TilePane buttonTile;
    private TilePane actionTile;
    private Label title;
    private TextField message;
    private TextArea reply;
    private ScrollPane scrollPane;

    public View(Stage stage)
    {

        stage.setTitle("ATM");

        stage.setHeight(height);
        stage.setWidth(width);
        stage.centerOnScreen();

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
         */

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
        scene.getStylesheets().add("atm.css");
        stage.setScene(scene);
        stage.show();
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

    private ImageView fetchButtonImage(String id)
    {
        // concatenating the path by appending the specified id before searching the image's URL.
        String imagePath = "/com/atm/" + id + ".png";
        URL URLPath = getClass().getResource(imagePath);

        // adding exception since fetching invalid path could potentially crash out.
        if (URLPath == null)
        {
            return new ImageView();
        }

        // finally, set the image into the imageView before applying it's size.
        Image newImage = new Image(URLPath.toExternalForm());
        ImageView newImageView = new ImageView(newImage);
        newImageView.setPreserveRatio(true);
        newImageView.setFitHeight(60f);
        newImageView.setFitWidth(60f);

        return newImageView;
    }

    private void styleButtons(Button[][] instances)
    {
        // Iterating through the nested table containing buttons.
        for (Button[] row : instances)
        {
            for (Button button : row)
            {
                // Filter nullable instance within the table; ignoring empty labels.
                if (button != null)
                {
                    String id = button.getId();
                    ImageView imageId = fetchButtonImage(id);
                    button.setGraphic(imageId);
                }
            }
        }
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
                    Button newButton = new Button();
                    newButton.setId(label);
                    newButton.setOnAction(this::buttonClicked);

                    buttons[row][col] = newButton;
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

    private void buttonClicked(ActionEvent event)
    {
        Button button = (Button)event.getSource();
        if (this.controller != null)
        {
            String label = button.getId();
            this.controller.process(label);
        }
    }

    public void update()
    {
        if (this.model != null)
        {
            String message1 = this.model.title;
            this.title.setText(message1);
            String message2 = this.model.display1;
            this.message.setText(message2);
            String message3 = this.model.display2;
            this.reply.setText(message3);
        }
    }
}
