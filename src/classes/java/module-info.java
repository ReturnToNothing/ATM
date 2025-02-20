module com.atm.atm {
    requires javafx.controls;
    requires javafx.fxml;

    requires org.controlsfx.controls;

    opens com.atm to javafx.fxml;
    exports com.atm;
}