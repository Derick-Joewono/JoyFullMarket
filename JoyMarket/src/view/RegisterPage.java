package view;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import controller.CustomerController;

public class RegisterPage {

    Scene scene;
    BorderPane borderPane;
    GridPane formPane;
    VBox card;

    TextField full_nameField;
    TextField emailField;
    PasswordField passwordField;
    TextField phoneField;
    TextField addressField;
    ComboBox<String> genderComboBox;

    Label full_nameLabel;
    Label emailLabel;
    Label passwordLabel;
    Label phoneLabel;
    Label addressLabel;
    Label genderLabel;

    Button submitBtn;
    Button backBtn;
    
    CustomerController customerController;

    private void initiate() {
        borderPane = new BorderPane();
        borderPane.setStyle("-fx-background-color: linear-gradient(to bottom right, #EEF2F6, #F8FAFC);");
        borderPane.setPadding(new Insets(24));

        card = new VBox(18);
        card.setPadding(new Insets(32));
        card.setAlignment(Pos.CENTER);
        card.setMaxWidth(520);
        card.setStyle("-fx-background-color: #FFFFFF; -fx-background-radius: 18; -fx-effect: dropshadow(gaussian, rgba(100,116,139,0.25), 24, 0.12, 0, 8);");

        formPane = new GridPane(); // ✅ WAJIB ADA
        formPane.setVgap(12);
        formPane.setHgap(12);
        formPane.setAlignment(Pos.CENTER);

        full_nameField = new TextField();
        emailField = new TextField();
        emailField.setStyle(inputStyle());
        passwordField = new PasswordField();
        passwordField.setStyle(inputStyle());
        phoneField = new TextField();
        phoneField.setStyle(inputStyle());
        addressField = new TextField();
        addressField.setStyle(inputStyle());
        genderComboBox = new ComboBox<>();
        genderComboBox.getItems().addAll("Male", "Female", "Other");
        genderComboBox.setValue("Male");
        genderComboBox.setStyle("-fx-background-radius: 10; -fx-border-radius: 10; -fx-border-color: #CBD5E1; -fx-padding: 6 8;");

        full_nameLabel = new Label("Full Name");
        full_nameLabel.setStyle(labelStyle());
        emailLabel = new Label("Email");
        emailLabel.setStyle(labelStyle());
        passwordLabel = new Label("Password");
        passwordLabel.setStyle(labelStyle());
        phoneLabel = new Label("Phone");
        phoneLabel.setStyle(labelStyle());
        addressLabel = new Label("Address");
        addressLabel.setStyle(labelStyle());
        genderLabel = new Label("Gender");
        genderLabel.setStyle(labelStyle());

        submitBtn = new Button("Register");
        submitBtn.setPrefWidth(160);
        submitBtn.setStyle(primaryButtonStyle());
        backBtn = new Button("Back to Login");
        backBtn.setPrefWidth(160);
        backBtn.setStyle(ghostButtonStyle());
        
        customerController = new CustomerController();

        scene = new Scene(borderPane, 1000, 700);

        borderPane.setCenter(card); // ✅ WAJIB ADA
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

        formPane.add(genderLabel, 0, 5);
        formPane.add(genderComboBox, 1, 5);

        HBox actions = new HBox(10);
        actions.setAlignment(Pos.CENTER_LEFT);
        actions.getChildren().addAll(submitBtn, backBtn);

        formPane.add(actions, 1, 6);

        card.getChildren().setAll(
            new Label("Create your JoyMarket account") {{
                setStyle("-fx-font-size: 24px; -fx-font-weight: bold; -fx-text-fill: #0F172A;");
            }},
            formPane
        );
        BorderPane.setAlignment(card, Pos.CENTER);
        formPane.setAlignment(Pos.CENTER);
    }
    
    private void setEventHandler(Stage stage) {
        submitBtn.setOnAction(e -> {
            String fullName = full_nameField.getText().trim();
            String email = emailField.getText().trim();
            String password = passwordField.getText();
            String phone = phoneField.getText().trim();
            String address = addressField.getText().trim();
            String gender = genderComboBox.getValue();
            
            // Validation
            if (fullName.isEmpty() || email.isEmpty() || password.isEmpty() || 
                phone.isEmpty() || address.isEmpty() || gender == null) {
                showAlert(Alert.AlertType.ERROR, "Validation Error", 
                    "Please fill in all fields!");
                return;
            }
            
            if (!isValidEmail(email)) {
                showAlert(Alert.AlertType.ERROR, "Validation Error", 
                    "Please enter a valid email address!");
                return;
            }
            
            if (password.length() < 6) {
                showAlert(Alert.AlertType.ERROR, "Validation Error", 
                    "Password must be at least 6 characters long!");
                return;
            }
            
            // Register customer
            boolean success = customerController.register(fullName, email, password, phone, address, gender);
            
            if (success) {
                showAlert(Alert.AlertType.INFORMATION, "Success", 
                    "Registration successful! Please login.");
                // Navigate to login page
                LoginPage loginPage = new LoginPage();
                loginPage.show(stage);
            } else {
                showAlert(Alert.AlertType.ERROR, "Registration Failed", 
                    "Failed to register. Email might already be in use.");
            }
        });

        backBtn.setOnAction(e -> {
            LoginPage loginPage = new LoginPage();
            loginPage.show(stage);
        });
    }
    
    private boolean isValidEmail(String email) {
        return email.matches("^[A-Za-z0-9+_.-]+@(.+)$");
    }
    
    private void showAlert(Alert.AlertType alertType, String title, String message) {
        Alert alert = new Alert(alertType);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }
    
    private void clearFields() {
        full_nameField.clear();
        emailField.clear();
        passwordField.clear();
        phoneField.clear();
        addressField.clear();
        genderComboBox.setValue("Male");
    }

    public void show(Stage stage) {
        initiate();
        setLayout(); // ✅ WAJIB DIPANGGIL
        setEventHandler(stage); // ✅ WAJIB DIPANGGIL

        stage.setScene(scene);
        stage.setTitle("JoyMarket Register Page");
        stage.show();
    }

    private String primaryButtonStyle() {
        return "-fx-background-color: #64748B; -fx-text-fill: white; -fx-font-size: 14px; -fx-font-weight: bold; -fx-background-radius: 10;";
    }

    private String inputStyle() {
        return "-fx-background-radius: 10; -fx-border-radius: 10; -fx-border-color: #CBD5E1; -fx-padding: 10;";
    }

    private String labelStyle() {
        return "-fx-text-fill: #475569;";
    }

    private String ghostButtonStyle() {
        return "-fx-background-color: transparent; -fx-border-color: #64748B; -fx-text-fill: #64748B; -fx-font-size: 14px; -fx-font-weight: bold; -fx-border-radius: 10; -fx-background-radius: 10;";
    }
}
