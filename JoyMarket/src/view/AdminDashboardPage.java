package view;

import controller.AdminController;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import model.Admin;

public class AdminDashboardPage {

    private static final String ACCENT_COLOR = "#EF4444"; // Red for admin theme

    private Admin admin;
    private AdminController controller;
    
    Scene scene;
    BorderPane borderPane;
    VBox card;
    
    Label titleLabel;
    Button btnEditStock;
    Button btnAssignCourier;
    Button btnLogout;

    public AdminDashboardPage(Admin admin) {
        this.admin = admin;
    }

    private void initiate() {
        controller = new AdminController();
        
        borderPane = new BorderPane();
        borderPane.setStyle("-fx-background-color: linear-gradient(to bottom right, #FEF2F2, #FEE2E2);");
        borderPane.setPadding(new Insets(24));

        card = new VBox(20);
        card.setPadding(new Insets(40));
        card.setAlignment(Pos.CENTER);
        card.setMaxWidth(480);
        card.setStyle("-fx-background-color: #FFFFFF; -fx-background-radius: 18; -fx-effect: dropshadow(gaussian, rgba(239,68,68,0.25), 24, 0.12, 0, 8);");

        titleLabel = new Label("Admin Dashboard");
        titleLabel.setStyle("-fx-font-size: 28px; -fx-font-weight: bold; -fx-text-fill: #0F172A;");

        Label welcomeLabel = new Label("Welcome, " + admin.getName() + "!");
        welcomeLabel.setStyle("-fx-font-size: 16px; -fx-text-fill: #64748B; -fx-padding: 0 0 10 0;");

        btnEditStock = new Button("Edit Product Stock & Price");
        btnEditStock.setPrefWidth(280);
        btnEditStock.setPrefHeight(45);
        btnEditStock.setStyle(primaryButtonStyle());

        btnAssignCourier = new Button("Assign Courier to Orders");
        btnAssignCourier.setPrefWidth(280);
        btnAssignCourier.setPrefHeight(45);
        btnAssignCourier.setStyle(primaryButtonStyle());

        btnLogout = new Button("Logout");
        btnLogout.setPrefWidth(280);
        btnLogout.setPrefHeight(45);
        btnLogout.setStyle(logoutButtonStyle());

        scene = new Scene(borderPane, 1000, 700);
    }

    private void setLayout() {
        // Admin badge
        Label adminBadge = new Label("👤");
        adminBadge.setStyle("-fx-font-size: 48px;");

        card.getChildren().addAll(adminBadge, titleLabel, new Label("Welcome, " + admin.getName() + "!") {{
            setStyle("-fx-font-size: 16px; -fx-text-fill: #64748B; -fx-padding: 0 0 20 0;");
        }}, btnEditStock, btnAssignCourier, btnLogout);
        
        borderPane.setCenter(card);
        BorderPane.setAlignment(card, Pos.CENTER);
    }

    private void setEventHandler(Stage stage) {
        btnEditStock.setOnAction(e -> {
            AdminEditStockPage editStockPage = new AdminEditStockPage();
            editStockPage.show(stage);
        });

        btnAssignCourier.setOnAction(e -> {
            AssignCourierPage assignPage = new AssignCourierPage();
            assignPage.show(stage);
        });

        btnLogout.setOnAction(e -> {
            controller.logout();
            showAlert(Alert.AlertType.INFORMATION, "Logged Out", "You have been logged out successfully.");
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
        stage.setTitle("JoyMarket - Admin Dashboard");
        stage.show();
    }

    private String primaryButtonStyle() {
        return "-fx-background-color: " + ACCENT_COLOR + "; -fx-text-fill: white; -fx-font-size: 14px; -fx-font-weight: bold; -fx-background-radius: 10;";
    }

    private String logoutButtonStyle() {
        return "-fx-background-color: #64748B; -fx-text-fill: white; -fx-font-size: 14px; -fx-font-weight: bold; -fx-background-radius: 10;";
    }
}
