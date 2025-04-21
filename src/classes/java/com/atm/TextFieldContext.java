package com.atm;

import javafx.beans.property.StringProperty;
import javafx.scene.control.TextField;

public record TextFieldContext(TextField textField, String action, Object value)
{

}
