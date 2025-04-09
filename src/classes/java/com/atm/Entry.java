package com.atm;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;

public class Entry extends Application
{
    private String[] names = {
            "John doe",
            "Jane doe",
            "Bob doe"
    };
    private int[] numbers = {
            785432,
            795321,
            785321
    };
    private int[] passwords = {
            785432,
            795321,
            785321
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
            String name = names[index];
            int number = numbers[index];
            int password = passwords[index];
            Types type = types[index];
            bank.addAccount(name, number, password, type);
        }

        Model model = new Model(bank);

        Controller controller = new Controller();
        controller.model = model;

        model.view = new View(stage, controller, model);
        model.display();
    }

}