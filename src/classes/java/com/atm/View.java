package com.atm;

import javafx.stage.Stage;

import javafx.event.ActionEvent;

import javafx.scene.Scene;
import javafx.scene.Node;

import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.TextField;
import javafx.scene.text.Text;

import javafx.scene.layout.GridPane;
import javafx.scene.layout.TilePane;

public class View
{
    public Controller controller;
    public Model model;

    private final int height = 500;
    private final int width = 250;

    private GridPane grid;
    private TilePane tile;
    private Label title;
    private TextField message;
    private TextField reply;
    private ScrollPane scrollPane;

    public View(Stage stage)
    {

        stage.setTitle("ATM");

        stage.setHeight(height);
        stage.setWidth(width);
        stage.centerOnScreen();

        String[][] labels = {
                {"7", "8", "9", "", "Dep", ""},
                {"4", "5", "6", "", "W/D", ""},
                {"1", "2", "3", "", "Bal", "Fin"},
                {"CLR", "0", "", "", "", "Ent"}
        };

        this.title = createLabel("Title");
        this.message = createTextField("Message", 0, false);
        this.reply = createTextField("Reply", 0, false);
        this.scrollPane = createScrollPane("ScrollPane", this.reply);

        Button[][] buttons = createButtonGrid(labels);

        this.tile = createTilePane("Buttons", buttons);
        this.grid = createGridPane("Layout", this.title, this.message, this.scrollPane, this.tile);

        Scene scene = new Scene(this.grid, width, height);
        stage.setScene(scene);
        stage.show();
    }

    private GridPane createGridPane(String id, Node... instances)
    {
        GridPane grid = new GridPane();
        grid.setId(id);

        /*
        Instances within the node are sequentially added within the GridPane, especially
        incrementally applying their position in columns and rows.
         */
        int currentRow = 0;
        for (Node instance : instances)
        {
            grid.add(instance, 0, currentRow++);
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

    private ScrollPane createScrollPane(String id, Node content)
    {
        ScrollPane scrollPane = new ScrollPane();
        scrollPane.setId(id);

        scrollPane.setContent(content);
        scrollPane.setFitToWidth(true);
        scrollPane.setFitToHeight(true);

        return scrollPane;
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
                    Button newButton = new Button(label);
                    newButton.setId(label);
                    newButton.setOnAction(this::buttonClicked);
                    buttons[row][col] = newButton;
                }
                else
                {
                    Text newText = new Text();
                    newText.setId("empty" + row + col);
                    buttons[row][col] = null;
                }
            }
        }
        return buttons;
    }

    private void buttonClicked(ActionEvent event)
    {
        Button button = (Button) event.getSource();
        if (this.controller != null)
        {
            String label = button.getText();
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
