package view;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.Separator;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import controller.CustomerController;
import helper.SessionManager;
import model.Customer;

public class SessionPage {

    Scene scene;
    BorderPane borderPane;
    VBox mainContainer;
    VBox infoContainer;
    HBox headerBox;
    HBox buttonBox;

    Label welcomeLabel;
    Label nameLabel;
    Label emailLabel;
    Label phoneLabel;
    Label addressLabel;
    Label genderLabel;
    Label titleLabel;

    Button logoutBtn;
    Button viewProfileBtn;
    Button shopBtn;

    CustomerController customerController;

    private void initiate() {
        borderPane = new BorderPane();
        mainContainer = new VBox(20);
        infoContainer = new VBox(15);
        headerBox = new HBox();
        buttonBox = new HBox(15);

        mainContainer.setPadding(new Insets(30));
        mainContainer.setAlignment(Pos.CENTER);
        infoContainer.setPadding(new Insets(20));
        infoContainer.setAlignment(Pos.CENTER_LEFT);
        headerBox.setAlignment(Pos.CENTER_RIGHT);
        headerBox.setPadding(new Insets(10));
        buttonBox.setAlignment(Pos.CENTER);
        buttonBox.setSpacing(15);

        customerController = new CustomerController();

        Customer currentCustomer = SessionManager.getInstance().getCurrentCustomer();
        
        if (currentCustomer == null) {
            // If no session, redirect to login
            return;
        }

        titleLabel = new Label("JoyMarket Dashboard");
        titleLabel.setStyle("-fx-font-size: 28px; -fx-font-weight: bold;");

        welcomeLabel = new Label("Welcome, " + currentCustomer.getFull_name() + "!");
        welcomeLabel.setStyle("-fx-font-size: 20px; -fx-font-weight: bold;");

        nameLabel = new Label("Name: " + currentCustomer.getFull_name());
        nameLabel.setStyle("-fx-font-size: 14px;");
        
        emailLabel = new Label("Email: " + currentCustomer.getEmail());
        emailLabel.setStyle("-fx-font-size: 14px;");
        
        phoneLabel = new Label("Phone: " + currentCustomer.getPhone());
        phoneLabel.setStyle("-fx-font-size: 14px;");
        
        addressLabel = new Label("Address: " + currentCustomer.getAddress());
        addressLabel.setStyle("-fx-font-size: 14px;");
        
        genderLabel = new Label("Gender: " + currentCustomer.getGender());
        genderLabel.setStyle("-fx-font-size: 14px;");

        logoutBtn = new Button("Logout");
        logoutBtn.setPrefWidth(120);
        logoutBtn.setStyle("-fx-font-size: 14px; -fx-background-color: #dc3545; -fx-text-fill: white;");
        
        viewProfileBtn = new Button("View Profile");
        viewProfileBtn.setPrefWidth(120);
        viewProfileBtn.setStyle("-fx-font-size: 14px;");
        
        shopBtn = new Button("Shop Now");
        shopBtn.setPrefWidth(120);
        shopBtn.setStyle("-fx-font-size: 14px; -fx-background-color: #28a745; -fx-text-fill: white;");

        scene = new Scene(borderPane, 700, 500);
    }

    private void setLayout() {
        Customer currentCustomer = SessionManager.getInstance().getCurrentCustomer();
        
        if (currentCustomer == null) {
            return;
        }

        infoContainer.getChildren().addAll(
            welcomeLabel,
            new Separator(),
            nameLabel,
            emailLabel,
            phoneLabel,
            addressLabel,
            genderLabel
        );

        buttonBox.getChildren().addAll(shopBtn, viewProfileBtn, logoutBtn);
        headerBox.getChildren().add(logoutBtn);

        mainContainer.getChildren().addAll(
            titleLabel,
            infoContainer,
            new Separator(),
            buttonBox
        );

        borderPane.setTop(headerBox);
        borderPane.setCenter(mainContainer);
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
            showAlert(Alert.AlertType.INFORMATION, "Profile", 
                "Profile feature coming soon!");
        });

        shopBtn.setOnAction(e -> {
            showAlert(Alert.AlertType.INFORMATION, "Shop", 
                "Shopping feature coming soon!");
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






