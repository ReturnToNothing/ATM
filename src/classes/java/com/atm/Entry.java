package com.atm;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;

public class Entry extends Application
{
    private int[] numbers = {
            1234,
            5678,
            1111
    };
    private int[] passwords = {
            1234,
            5678,
            2222
    };
    private Types[] types = {
            Types.BASIC,
            Types.PRIME,
            Types.PRIME,
    };

    public static void main(String[] args)
    {
        launch();
    }

    @Override
    public void start(Stage stage) throws IOException {
        Bank bank = new Bank();

        for (int index = 0; index < numbers.length; index++)
        {
            int number = numbers[index];
            int password = passwords[index];
            Types type = types[index];
            bank.addAccount(number, password, type);
        }

        Model model = new Model(bank);

        Controller controller = new Controller();
        controller.model = model;

        View view = new View(stage, controller, model);

        model.view = view;
        model.display();
    }

}