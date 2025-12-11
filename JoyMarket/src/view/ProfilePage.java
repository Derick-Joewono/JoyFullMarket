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
import helper.SessionManager;
import model.Customer;

public class ProfilePage {

    Scene scene;
    BorderPane borderPane;
    VBox card;
    GridPane formPane;

    TextField fullNameField;
    TextField emailField;
    PasswordField newPasswordField;
    TextField phoneField;
    TextField addressField;
    ComboBox<String> genderComboBox;

    Button saveBtn;
    Button backBtn;

    CustomerController customerController;
    Customer currentCustomer;

    private static final String ACCENT_COLOR = "#64748B";

    public void show(Stage stage) {
        currentCustomer = SessionManager.getInstance().getCurrentCustomer();
        if (currentCustomer == null) {
            LoginPage loginPage = new LoginPage();
            loginPage.show(stage);
            return;
        }

        initiate();
        setLayout();
        setEventHandler(stage);

        stage.setScene(scene);
        stage.setTitle("JoyMarket - Profile");
        stage.show();
    }

    private void initiate() {
        borderPane = new BorderPane();
        borderPane.setStyle("-fx-background-color: linear-gradient(to bottom right, #EEF2F6, #F8FAFC);");
        borderPane.setPadding(new Insets(24));

        card = new VBox(18);
        card.setPadding(new Insets(32));
        card.setAlignment(Pos.CENTER);
        card.setMaxWidth(560);
        card.setStyle("-fx-background-color: #FFFFFF; -fx-background-radius: 18; -fx-effect: dropshadow(gaussian, rgba(100,116,139,0.25), 24, 0.12, 0, 8);");

        formPane = new GridPane();
        formPane.setVgap(12);
        formPane.setHgap(12);
        formPane.setAlignment(Pos.CENTER);

        fullNameField = new TextField(currentCustomer.getFull_name());
        fullNameField.setStyle(inputStyle());
        emailField = new TextField(currentCustomer.getEmail());
        emailField.setStyle(inputStyle());
        newPasswordField = new PasswordField();
        newPasswordField.setPromptText("Leave blank to keep current password");
        newPasswordField.setStyle(inputStyle());
        phoneField = new TextField(currentCustomer.getPhone());
        phoneField.setStyle(inputStyle());
        addressField = new TextField(currentCustomer.getAddress());
        addressField.setStyle(inputStyle());
        genderComboBox = new ComboBox<>();
        genderComboBox.getItems().addAll("Male", "Female", "Other");
        genderComboBox.setValue(currentCustomer.getGender());
        genderComboBox.setStyle("-fx-background-radius: 10; -fx-border-radius: 10; -fx-border-color: #CBD5E1; -fx-padding: 6 8;");

        saveBtn = new Button("Save changes");
        saveBtn.setPrefWidth(160);
        saveBtn.setStyle(primaryButtonStyle());

        backBtn = new Button("Back");
        backBtn.setPrefWidth(120);
        backBtn.setStyle(ghostButtonStyle());

        customerController = new CustomerController();

        scene = new Scene(borderPane, 1000, 700);
    }

    private void setLayout() {
        Label heading = new Label("Profile & Settings");
        heading.setStyle("-fx-font-size: 24px; -fx-font-weight: bold; -fx-text-fill: #0F172A;");

        formPane.add(new Label("Full Name") {{ setStyle(labelStyle()); }}, 0, 0);
        formPane.add(fullNameField, 1, 0);

        formPane.add(new Label("Email") {{ setStyle(labelStyle()); }}, 0, 1);
        formPane.add(emailField, 1, 1);

        formPane.add(new Label("New Password") {{ setStyle(labelStyle()); }}, 0, 2);
        formPane.add(newPasswordField, 1, 2);

        formPane.add(new Label("Phone") {{ setStyle(labelStyle()); }}, 0, 3);
        formPane.add(phoneField, 1, 3);

        formPane.add(new Label("Address") {{ setStyle(labelStyle()); }}, 0, 4);
        formPane.add(addressField, 1, 4);

        formPane.add(new Label("Gender") {{ setStyle(labelStyle()); }}, 0, 5);
        formPane.add(genderComboBox, 1, 5);

        HBox actions = new HBox(10);
        actions.setAlignment(Pos.CENTER_RIGHT);
        actions.getChildren().addAll(backBtn, saveBtn);

        card.getChildren().setAll(heading, formPane, actions);
        borderPane.setCenter(card);
        BorderPane.setAlignment(card, Pos.CENTER);
    }

    private void setEventHandler(Stage stage) {
        saveBtn.setOnAction(e -> {
            String fullName = fullNameField.getText().trim();
            String email = emailField.getText().trim();
            String newPassword = newPasswordField.getText();
            String phone = phoneField.getText().trim();
            String address = addressField.getText().trim();
            String gender = genderComboBox.getValue();

            if (fullName.isEmpty() || email.isEmpty() || phone.isEmpty() || address.isEmpty() || gender == null) {
                showAlert(Alert.AlertType.ERROR, "Validation Error", "Please fill in all required fields.");
                return;
            }

            boolean updated = customerController.updateProfile(fullName, email, newPassword, phone, address, gender);
            if (updated) {
                showAlert(Alert.AlertType.INFORMATION, "Profile Updated", "Your profile has been updated.");
                SessionPage sessionPage = new SessionPage();
                sessionPage.show(stage);
            } else {
                showAlert(Alert.AlertType.ERROR, "Update Failed", "Failed to update profile. Please try again.");
            }
        });

        backBtn.setOnAction(e -> {
            SessionPage sessionPage = new SessionPage();
            sessionPage.show(stage);
        });
    }

    private void showAlert(Alert.AlertType alertType, String title, String message) {
        Alert alert = new Alert(alertType);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }

    private String primaryButtonStyle() {
        return "-fx-background-color: " + ACCENT_COLOR + "; -fx-text-fill: white; -fx-font-size: 14px; -fx-font-weight: bold; -fx-background-radius: 10;";
    }

    private String ghostButtonStyle() {
        return "-fx-background-color: transparent; -fx-border-color: " + ACCENT_COLOR + "; -fx-text-fill: " + ACCENT_COLOR + "; -fx-font-size: 14px; -fx-font-weight: bold; -fx-border-radius: 10; -fx-background-radius: 10;";
    }

    private String inputStyle() {
        return "-fx-background-radius: 10; -fx-border-radius: 10; -fx-border-color: #CBD5E1; -fx-padding: 10;";
    }

    private String labelStyle() {
        return "-fx-text-fill: #475569;";
    }
}

