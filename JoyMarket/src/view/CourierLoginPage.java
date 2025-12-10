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
import controller.CourierController;
import helper.SessionManager;

public class CourierLoginPage {

    private static final String ACCENT_COLOR = "#10B981"; // Green for courier theme

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
    Button backBtn;

    CourierController courierController;

    private void initiate() {
        borderPane = new BorderPane();
        borderPane.setStyle("-fx-background-color: linear-gradient(to bottom right, #ECFDF5, #F0FDF4);");
        borderPane.setPadding(new Insets(24));

        card = new VBox(18);
        card.setPadding(new Insets(32));
        card.setAlignment(Pos.CENTER);
        card.setMaxWidth(420);
        card.setStyle("-fx-background-color: #FFFFFF; -fx-background-radius: 18; -fx-effect: dropshadow(gaussian, rgba(16,185,129,0.25), 24, 0.12, 0, 8);");

        formPane = new GridPane();
        formPane.setVgap(12);
        formPane.setHgap(12);
        formPane.setAlignment(Pos.CENTER);

        emailField = new TextField();
        emailField.setPromptText("Enter your email");
        emailField.setStyle(inputStyle());
        passwordField = new PasswordField();
        passwordField.setPromptText("Enter your password");
        passwordField.setStyle(inputStyle());

        titleLabel = new Label("Courier Login");
        titleLabel.setStyle("-fx-font-size: 26px; -fx-font-weight: bold; -fx-text-fill: #064E3B;");

        emailLabel = new Label("Email:");
        emailLabel.setStyle(labelStyle());
        passwordLabel = new Label("Password:");
        passwordLabel.setStyle(labelStyle());

        loginBtn = new Button("Login");
        loginBtn.setPrefWidth(150);
        loginBtn.setStyle(primaryButtonStyle());

        backBtn = new Button("Back");
        backBtn.setPrefWidth(150);
        backBtn.setStyle(ghostButtonStyle());

        courierController = new CourierController();

        scene = new Scene(borderPane, 680, 520);
    }

    private void setLayout() {
        formPane.add(emailLabel, 0, 0);
        formPane.add(emailField, 1, 0);

        formPane.add(passwordLabel, 0, 1);
        formPane.add(passwordField, 1, 1);

        HBox buttonBox = new HBox(10);
        buttonBox.setAlignment(Pos.CENTER);
        buttonBox.getChildren().addAll(loginBtn, backBtn);

        formPane.add(buttonBox, 0, 2, 2, 1);
        GridPane.setHalignment(buttonBox, javafx.geometry.HPos.CENTER);

        // Add courier icon/badge
        Label courierBadge = new Label("🚚");
        courierBadge.setStyle("-fx-font-size: 48px;");

        card.getChildren().addAll(courierBadge, titleLabel, formPane);
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
            boolean success = courierController.login(email, password);

            if (success) {
                showAlert(Alert.AlertType.INFORMATION, "Success",
                    "Login successful! Welcome, " + SessionManager.getInstance().getCurrentCourierName() + "!");

                // Navigate to courier dashboard
                CourierDashboardPage dashboard = new CourierDashboardPage();
                dashboard.show(stage);
            } else {
                showAlert(Alert.AlertType.ERROR, "Login Failed",
                    "Invalid phone or password, or account is inactive!");
            }
        });

        backBtn.setOnAction(e -> {
            LoginPage loginPage = new LoginPage();
            loginPage.show(stage);
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
        stage.setTitle("JoyMarket - Courier Login");
        stage.show();
    }

    private String primaryButtonStyle() {
        return "-fx-background-color: " + ACCENT_COLOR + "; -fx-text-fill: white; -fx-font-size: 14px; -fx-font-weight: bold; -fx-background-radius: 10;";
    }

    private String ghostButtonStyle() {
        return "-fx-background-color: transparent; -fx-border-color: " + ACCENT_COLOR + "; -fx-text-fill: " + ACCENT_COLOR + "; -fx-font-size: 14px; -fx-font-weight: bold; -fx-border-radius: 10; -fx-background-radius: 10;";
    }

    private String inputStyle() {
        return "-fx-background-radius: 10; -fx-border-radius: 10; -fx-border-color: #A7F3D0; -fx-padding: 10;";
    }

    private String labelStyle() {
        return "-fx-text-fill: #065F46;";
    }
}

