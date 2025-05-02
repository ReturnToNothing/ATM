package com.atm;

import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.layout.AnchorPane;

public record PanelContext(AnchorPane panel, Button pivot, Double start, Double end, Double exit, States state)
{

}
