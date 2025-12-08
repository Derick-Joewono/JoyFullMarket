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
    VBox card;

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
        borderPane.setStyle("-fx-background-color: linear-gradient(to bottom right, #EEF2F6, #F8FAFC);");
        borderPane.setPadding(new Insets(24));

        card = new VBox(18);
        card.setPadding(new Insets(32));
        card.setAlignment(Pos.CENTER);
        card.setMaxWidth(420);
        card.setStyle("-fx-background-color: #FFFFFF; -fx-background-radius: 18; -fx-effect: dropshadow(gaussian, rgba(100,116,139,0.25), 24, 0.12, 0, 8);");

        formPane = new GridPane();
        formPane.setVgap(12);
        formPane.setHgap(12);
        formPane.setAlignment(Pos.CENTER);

        emailField = new TextField();
        emailField.setPromptText("Enter your email");
        emailField.setStyle("-fx-background-radius: 10; -fx-border-radius: 10; -fx-border-color: #CBD5E1; -fx-padding: 10;");
        passwordField = new PasswordField();
        passwordField.setPromptText("Enter your password");
        passwordField.setStyle("-fx-background-radius: 10; -fx-border-radius: 10; -fx-border-color: #CBD5E1; -fx-padding: 10;");

        titleLabel = new Label("JoyMarket Login");
        titleLabel.setStyle("-fx-font-size: 26px; -fx-font-weight: bold; -fx-text-fill: #0F172A;");
        
        emailLabel = new Label("Email:");
        emailLabel.setStyle("-fx-text-fill: #475569;");
        passwordLabel = new Label("Password:");
        passwordLabel.setStyle("-fx-text-fill: #475569;");

        loginBtn = new Button("Login");
        loginBtn.setPrefWidth(150);
        loginBtn.setStyle(primaryButtonStyle());
        
        registerBtn = new Button("Register");
        registerBtn.setPrefWidth(150);
        registerBtn.setStyle(ghostButtonStyle());

        customerController = new CustomerController();

        scene = new Scene(borderPane, 680, 520);
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

        card.getChildren().addAll(titleLabel, formPane);
        borderPane.setCenter(card);
        BorderPane.setAlignment(card, Pos.CENTER);
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

    private String primaryButtonStyle() {
        return "-fx-background-color: #64748B; -fx-text-fill: white; -fx-font-size: 14px; -fx-font-weight: bold; -fx-background-radius: 10;";
    }

    private String ghostButtonStyle() {
        return "-fx-background-color: transparent; -fx-border-color: #64748B; -fx-text-fill: #64748B; -fx-font-size: 14px; -fx-font-weight: bold; -fx-border-radius: 10; -fx-background-radius: 10;";
    }
}






