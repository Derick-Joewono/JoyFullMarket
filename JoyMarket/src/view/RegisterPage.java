package view;

import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;
import javafx.stage.Stage;

public class RegisterPage {

    Scene scene;
    BorderPane borderPane;
    GridPane formPane;

    TextField full_nameField;
    TextField emailField;
    TextField passwordField;
    TextField phoneField;
    TextField addressField;

    Label full_nameLabel;
    Label emailLabel;
    Label passwordLabel;
    Label phoneLabel;
    Label addressLabel;

    Button submitBtn;

    private void initiate() {
        borderPane = new BorderPane();
        formPane = new GridPane(); // ✅ WAJIB ADA

        formPane.setVgap(10);
        formPane.setHgap(10);

        full_nameField = new TextField();
        emailField = new TextField();
        passwordField = new TextField();
        phoneField = new TextField();
        addressField = new TextField();

        full_nameLabel = new Label("Full Name");
        emailLabel = new Label("Email");
        passwordLabel = new Label("Password");
        phoneLabel = new Label("Phone");
        addressLabel = new Label("Address");

        submitBtn = new Button("Register");

        scene = new Scene(borderPane, 500, 400);

        borderPane.setCenter(formPane); // ✅ WAJIB ADA
    }

    private void setLayout() {
        formPane.add(full_nameLabel, 0, 0);
        formPane.add(full_nameField, 1, 0);

        formPane.add(emailLabel, 0, 1);
        formPane.add(emailField, 1, 1);

        formPane.add(passwordLabel, 0, 2);
        formPane.add(passwordField, 1, 2);

        formPane.add(phoneLabel, 0, 3);
        formPane.add(phoneField, 1, 3);

        formPane.add(addressLabel, 0, 4);
        formPane.add(addressField, 1, 4);

        formPane.add(submitBtn, 1, 5);
    }

    public void show(Stage stage) {
        initiate();
        setLayout(); // ✅ WAJIB DIPANGGIL

        stage.setScene(scene);
        stage.setTitle("JoyMarket Register Page");
        stage.show();
    }
}
