package view;

import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;
import javafx.stage.Stage;
import controller.CustomerController;

public class RegisterPage {

    Scene scene;
    BorderPane borderPane;
    GridPane formPane;

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
    
    CustomerController customerController;

    private void initiate() {
        borderPane = new BorderPane();
        formPane = new GridPane(); // ✅ WAJIB ADA

        formPane.setVgap(10);
        formPane.setHgap(10);

        full_nameField = new TextField();
        emailField = new TextField();
        passwordField = new PasswordField();
        phoneField = new TextField();
        addressField = new TextField();
        genderComboBox = new ComboBox<>();
        genderComboBox.getItems().addAll("Male", "Female", "Other");
        genderComboBox.setValue("Male");

        full_nameLabel = new Label("Full Name");
        emailLabel = new Label("Email");
        passwordLabel = new Label("Password");
        phoneLabel = new Label("Phone");
        addressLabel = new Label("Address");
        genderLabel = new Label("Gender");

        submitBtn = new Button("Register");
        
        customerController = new CustomerController();

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

        formPane.add(genderLabel, 0, 5);
        formPane.add(genderComboBox, 1, 5);

        formPane.add(submitBtn, 1, 6);
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
}
