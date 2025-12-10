package view;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import controller.CustomerBalanceController;
import controller.CustomerController;
import helper.SessionManager;

import model.Customer;

public class SessionPage {

    Scene scene;
    BorderPane borderPane;
    VBox card;
    VBox infoBox;
    HBox actionBox;

    Label titleLabel;
    Label subtitleLabel;

    Button logoutBtn;
    Button viewProfileBtn;
    Button shopBtn;
    Button topUpBtn;

    //fixed controller
    CustomerController customerController;
    Customer currentCustomer;

    CustomerBalanceController balanceController;
    double currentBalance;

    private static final String ACCENT_COLOR = "#64748B";

    private void initiate() {
        borderPane = new BorderPane();
        borderPane.setPadding(new Insets(24));
        borderPane.setStyle("-fx-background-color: linear-gradient(to bottom right, #EEF2F6, #F8FAFC);");

        card = new VBox(18);
        card.setPadding(new Insets(28));
        card.setAlignment(Pos.TOP_LEFT);
        card.setStyle("-fx-background-color: #FFFFFF; -fx-background-radius: 18; -fx-effect: dropshadow(gaussian, rgba(100,116,139,0.25), 24, 0.12, 0, 8);");

        infoBox = new VBox(10);
        actionBox = new HBox(12);
        actionBox.setAlignment(Pos.CENTER_LEFT);

        customerController = new CustomerController();
        currentCustomer = SessionManager.getInstance().getCurrentCustomer();

        scene = new Scene(borderPane, 900, 640);
        
        customerController = new CustomerController();
        currentCustomer = SessionManager.getInstance().getCurrentCustomer();

        balanceController = new CustomerBalanceController();
        currentCustomer = SessionManager.getInstance().getCurrentCustomer();

        // Ambil saldo via controller, bukan repository
        currentBalance = balanceController.getBalance(currentCustomer.getId());


    }

    private void setLayout() {
        if (currentCustomer == null) {
            return;
        }

        infoBox.getChildren().clear();
        actionBox.getChildren().clear();

        titleLabel = new Label("JoyMarket Dashboard");
        titleLabel.setStyle("-fx-font-size: 26px; -fx-font-weight: bold; -fx-text-fill: #0F172A;");

        subtitleLabel = new Label("Secure session active. Your profile and quick actions are below.");
        subtitleLabel.setStyle("-fx-text-fill: #475569;");

        infoBox.getChildren().addAll(
            infoRow("Name", currentCustomer.getFull_name()),
            infoRow("Email", currentCustomer.getEmail()),
            infoRow("Phone", currentCustomer.getPhone()),
            infoRow("Address", currentCustomer.getAddress()),
            infoRow("Gender", currentCustomer.getGender()),
            infoRow("Balance", "Rp " + currentBalance)
        );

        shopBtn = createAccentButton("Shop now", ACCENT_COLOR);
        viewProfileBtn = createAccentButton("View profile", "#334155");
        logoutBtn = createAccentButton("Logout", "#E11D48");
        topUpBtn = createAccentButton("Top Up Balance", "#16A34A");

        shopBtn.setPrefWidth(130);
        viewProfileBtn.setPrefWidth(130);
        logoutBtn.setPrefWidth(110);
        topUpBtn.setPrefWidth(130);

        actionBox.getChildren().addAll(shopBtn, viewProfileBtn, logoutBtn, topUpBtn);

        card.getChildren().setAll(titleLabel, subtitleLabel, infoBox, actionBox);

        borderPane.setCenter(card);
    }

    private void setEventHandler(Stage stage) {
        logoutBtn.setOnAction(e -> {
            customerController.logout();
            showAlert(Alert.AlertType.INFORMATION, "Logged Out", 
                "You have been logged out successfully!");
            
            LoginPage loginPage = new LoginPage();
            loginPage.show(stage);
        });

        viewProfileBtn.setOnAction(e -> {
            ProfilePage profilePage = new ProfilePage();
            profilePage.show(stage);
        });

        shopBtn.setOnAction(e -> {
            ProductListPage productListPage = new ProductListPage();
            productListPage.show(stage);
        });

        
        topUpBtn.setOnAction(e -> {
            TopUpPage topUpPage = new TopUpPage();
            topUpPage.show(stage);
        });

    }

    private void showAlert(Alert.AlertType alertType, String title, String message) {
        Alert alert = new Alert(alertType);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }

    private HBox infoRow(String labelText, String valueText) {
        HBox row = new HBox(8);
        row.setAlignment(Pos.CENTER_LEFT);

        Label label = new Label(labelText + ":");
        label.setStyle("-fx-font-weight: bold; -fx-text-fill: #0F172A;");

        Label value = new Label(valueText);
        value.setStyle("-fx-text-fill: #475569;");

        row.getChildren().addAll(label, value);
        return row;
    }

    private Button createAccentButton(String text, String backgroundColor) {
        Button button = new Button(text);
        button.setStyle("-fx-background-color: " + backgroundColor + "; -fx-text-fill: white; -fx-background-radius: 10; -fx-font-size: 13px; -fx-font-weight: bold;");
        button.setPrefHeight(40);
        return button;
    }

    public void show(Stage stage) {
        if (!SessionManager.getInstance().isLoggedIn()) {
            // Redirect to login if not logged in
            LoginPage loginPage = new LoginPage();
            loginPage.show(stage);
            return;
        }

        initiate();
        setLayout();
        setEventHandler(stage);

        stage.setScene(scene);
        stage.setTitle("JoyMarket - Dashboard");
        stage.show();
    }
}






