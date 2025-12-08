package view;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import controller.CustomerController;
import helper.SessionManager;

public class LoginPage {

    Scene scene;
    BorderPane borderPane;
    GridPane formPane;
    VBox mainContainer;

    TextField emailField;
    PasswordField passwordField;

    Label emailLabel;
    Label passwordLabel;
    Label titleLabel;

    Button loginBtn;
    Button registerBtn;

    CustomerController customerController;

    private void initiate() {
        borderPane = new BorderPane();
        mainContainer = new VBox(20);
        formPane = new GridPane();

        formPane.setVgap(15);
        formPane.setHgap(15);
        formPane.setPadding(new Insets(20));
        mainContainer.setPadding(new Insets(40));
        mainContainer.setAlignment(Pos.CENTER);

        emailField = new TextField();
        emailField.setPromptText("Enter your email");
        passwordField = new PasswordField();
        passwordField.setPromptText("Enter your password");

        titleLabel = new Label("JoyMarket Login");
        titleLabel.setStyle("-fx-font-size: 24px; -fx-font-weight: bold;");
        
        emailLabel = new Label("Email:");
        passwordLabel = new Label("Password:");

        loginBtn = new Button("Login");
        loginBtn.setPrefWidth(150);
        loginBtn.setStyle("-fx-font-size: 14px;");
        
        registerBtn = new Button("Register");
        registerBtn.setPrefWidth(150);
        registerBtn.setStyle("-fx-font-size: 14px;");

        customerController = new CustomerController();

        scene = new Scene(borderPane, 500, 400);
    }

    private void setLayout() {
        formPane.add(emailLabel, 0, 0);
        formPane.add(emailField, 1, 0);

        formPane.add(passwordLabel, 0, 1);
        formPane.add(passwordField, 1, 1);

        HBox buttonBox = new HBox(10);
        buttonBox.setAlignment(Pos.CENTER);
        buttonBox.getChildren().addAll(loginBtn, registerBtn);

        formPane.add(buttonBox, 0, 2, 2, 1);
        GridPane.setHalignment(buttonBox, javafx.geometry.HPos.CENTER);

        mainContainer.getChildren().addAll(titleLabel, formPane);
        borderPane.setCenter(mainContainer);
    }

    private void setEventHandler(Stage stage) {
        loginBtn.setOnAction(e -> {
            String email = emailField.getText().trim();
            String password = passwordField.getText();

            // Validation
            if (email.isEmpty() || password.isEmpty()) {
                showAlert(Alert.AlertType.ERROR, "Validation Error", 
                    "Please fill in all fields!");
                return;
            }

            // Attempt login
            boolean success = customerController.login(email, password);

            if (success) {
                showAlert(Alert.AlertType.INFORMATION, "Success", 
                    "Login successful! Welcome, " + SessionManager.getInstance().getCurrentCustomerName() + "!");
                
                // Navigate to session page
                SessionPage sessionPage = new SessionPage();
                sessionPage.show(stage);
            } else {
                showAlert(Alert.AlertType.ERROR, "Login Failed", 
                    "Invalid email or password!");
            }
        });

        registerBtn.setOnAction(e -> {
            RegisterPage registerPage = new RegisterPage();
            registerPage.show(stage);
        });
    }

    private void showAlert(Alert.AlertType alertType, String title, String message) {
        Alert alert = new Alert(alertType);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }

    public void show(Stage stage) {
        initiate();
        setLayout();
        setEventHandler(stage);

        stage.setScene(scene);
        stage.setTitle("JoyMarket - Login");
        stage.show();
    }
}






