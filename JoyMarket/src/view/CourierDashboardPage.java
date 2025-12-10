package view;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import controller.CourierController;
import helper.SessionManager;
import model.Courier;
import model.Order;

import java.util.List;

public class CourierDashboardPage {

    private static final String ACCENT_COLOR = "#10B981";

    Scene scene;
    BorderPane borderPane;
    VBox mainContent;

    TableView<Order> orderTable;
    ComboBox<String> statusComboBox;
    Button updateStatusBtn;
    Button refreshBtn;
    Button logoutBtn;
    TextField vehicleTypeField;
    TextField vehiclePlateField;
    Button saveVehicleBtn;

    CourierController courierController;
    Courier currentCourier;

    public void show(Stage stage) {
        currentCourier = SessionManager.getInstance().getCurrentCourier();
        if (currentCourier == null) {
            CourierLoginPage loginPage = new CourierLoginPage();
            loginPage.show(stage);
            return;
        }

        initiate();
        setLayout();
        setEventHandler(stage);
        loadOrders();

        stage.setScene(scene);
        stage.setTitle("JoyMarket - Courier Dashboard");
        stage.show();
    }

    private void initiate() {
        borderPane = new BorderPane();
        borderPane.setStyle("-fx-background-color: linear-gradient(to bottom right, #ECFDF5, #F0FDF4);");
        borderPane.setPadding(new Insets(24));

        mainContent = new VBox(20);
        mainContent.setPadding(new Insets(24));
        mainContent.setAlignment(Pos.TOP_CENTER);
        mainContent.setStyle("-fx-background-color: #FFFFFF; -fx-background-radius: 18; -fx-effect: dropshadow(gaussian, rgba(16,185,129,0.25), 24, 0.12, 0, 8);");

        courierController = new CourierController();

        // Vehicle fields
        vehicleTypeField = new TextField();
        vehicleTypeField.setPromptText("Vehicle type (e.g. Bike/Car)");
        vehicleTypeField.setStyle(inputStyle());

        vehiclePlateField = new TextField();
        vehiclePlateField.setPromptText("Vehicle plate (e.g. H 1234 XX)");
        vehiclePlateField.setStyle(inputStyle());

        saveVehicleBtn = new Button("Save Vehicle Info");
        saveVehicleBtn.setStyle(primaryButtonStyle());

        // Create table
        orderTable = new TableView<>();
        orderTable.setStyle("-fx-background-radius: 10;");
        orderTable.setPrefHeight(300);

        // Create columns
        TableColumn<Order, Integer> idCol = new TableColumn<>("Order ID");
        idCol.setCellValueFactory(new PropertyValueFactory<>("id"));
        idCol.setPrefWidth(80);

        TableColumn<Order, Integer> customerCol = new TableColumn<>("Customer ID");
        customerCol.setCellValueFactory(new PropertyValueFactory<>("customerId"));
        customerCol.setPrefWidth(100);

        TableColumn<Order, Double> totalCol = new TableColumn<>("Total (Rp)");
        totalCol.setCellValueFactory(new PropertyValueFactory<>("total"));
        totalCol.setPrefWidth(120);

        TableColumn<Order, String> statusCol = new TableColumn<>("Order Status");
        statusCol.setCellValueFactory(new PropertyValueFactory<>("status"));
        statusCol.setPrefWidth(100);

        TableColumn<Order, String> deliveryCol = new TableColumn<>("Delivery Status");
        deliveryCol.setCellValueFactory(new PropertyValueFactory<>("deliveryStatus"));
        deliveryCol.setPrefWidth(120);
        deliveryCol.setCellFactory(column -> new TableCell<Order, String>() {
            @Override
            protected void updateItem(String item, boolean empty) {
                super.updateItem(item, empty);
                if (empty || item == null) {
                    setText(null);
                    setStyle("");
                } else {
                    setText(item);
                    switch (item) {
                        case "PENDING":
                            setStyle("-fx-text-fill: #F59E0B; -fx-font-weight: bold;");
                            break;
                        case "IN_PROGRESS":
                            setStyle("-fx-text-fill: #3B82F6; -fx-font-weight: bold;");
                            break;
                        case "DELIVERED":
                            setStyle("-fx-text-fill: #10B981; -fx-font-weight: bold;");
                            break;
                        default:
                            setStyle("");
                    }
                }
            }
        });

        orderTable.getColumns().addAll(idCol, customerCol, totalCol, statusCol, deliveryCol);

        // Status combo box
        statusComboBox = new ComboBox<>();
        statusComboBox.getItems().addAll("PENDING", "IN_PROGRESS", "DELIVERED");
        statusComboBox.setPromptText("Select Status");
        statusComboBox.setStyle("-fx-background-radius: 10; -fx-border-radius: 10; -fx-border-color: #A7F3D0; -fx-padding: 6 8;");

        // Buttons
        updateStatusBtn = new Button("Update Status");
        updateStatusBtn.setStyle(primaryButtonStyle());

        refreshBtn = new Button("Refresh");
        refreshBtn.setStyle(ghostButtonStyle());

        logoutBtn = new Button("Logout");
        logoutBtn.setStyle("-fx-background-color: #EF4444; -fx-text-fill: white; -fx-font-size: 14px; -fx-font-weight: bold; -fx-background-radius: 10;");

        scene = new Scene(borderPane, 800, 650);
    }

    private void setLayout() {
        // Header
        Label welcomeLabel = new Label("Welcome, " + currentCourier.getName() + "! 🚚");
        welcomeLabel.setStyle("-fx-font-size: 24px; -fx-font-weight: bold; -fx-text-fill: #064E3B;");

        Label subtitleLabel = new Label("Manage your assigned deliveries");
        subtitleLabel.setStyle("-fx-font-size: 14px; -fx-text-fill: #065F46;");

        VBox header = new VBox(5);
        header.setAlignment(Pos.CENTER_LEFT);
        header.getChildren().addAll(welcomeLabel, subtitleLabel);

        // Vehicle info section
        Label vehicleTitle = new Label("Vehicle Information");
        vehicleTitle.setStyle("-fx-font-size: 16px; -fx-font-weight: bold; -fx-text-fill: #065F46;");

        if (currentCourier.getVehicleType() != null) {
            vehicleTypeField.setText(currentCourier.getVehicleType());
        }
        if (currentCourier.getVehiclePlate() != null) {
            vehiclePlateField.setText(currentCourier.getVehiclePlate());
        }

        HBox vehicleBox = new HBox(12, vehicleTypeField, vehiclePlateField, saveVehicleBtn);
        vehicleBox.setAlignment(Pos.CENTER_LEFT);

        // Action panel
        Label actionLabel = new Label("Update Delivery Status:");
        actionLabel.setStyle("-fx-text-fill: #065F46; -fx-font-weight: bold;");

        HBox actionBox = new HBox(15);
        actionBox.setAlignment(Pos.CENTER_LEFT);
        actionBox.getChildren().addAll(actionLabel, statusComboBox, updateStatusBtn, refreshBtn);

        // Instructions
        Label instructionLabel = new Label("Select an order from the table, choose a new status, and click 'Update Status'");
        instructionLabel.setStyle("-fx-text-fill: #6B7280; -fx-font-size: 12px;");

        // Logout section
        HBox logoutBox = new HBox();
        logoutBox.setAlignment(Pos.CENTER_RIGHT);
        logoutBox.getChildren().add(logoutBtn);

        // Main layout
        mainContent.getChildren().addAll(header, vehicleTitle, vehicleBox, orderTable, actionBox, instructionLabel, logoutBox);
        borderPane.setCenter(mainContent);
    }

    private void setEventHandler(Stage stage) {
        updateStatusBtn.setOnAction(e -> {
            Order selectedOrder = orderTable.getSelectionModel().getSelectedItem();
            String newStatus = statusComboBox.getValue();

            if (selectedOrder == null) {
                showAlert(Alert.AlertType.WARNING, "No Selection", "Please select an order from the table!");
                return;
            }

            if (newStatus == null || newStatus.isEmpty()) {
                showAlert(Alert.AlertType.WARNING, "No Status", "Please select a delivery status!");
                return;
            }

            boolean success = courierController.updateDeliveryStatus(selectedOrder.getId(), newStatus);

            if (success) {
                showAlert(Alert.AlertType.INFORMATION, "Success",
                    "Delivery status updated to " + newStatus + "!");
                loadOrders();
                statusComboBox.setValue(null);
            } else {
                showAlert(Alert.AlertType.ERROR, "Failed",
                    "Failed to update delivery status. Please try again.");
            }
        });

        saveVehicleBtn.setOnAction(e -> {
            String type = vehicleTypeField.getText().trim();
            String plate = vehiclePlateField.getText().trim();

            if (type.isEmpty() || plate.isEmpty()) {
                showAlert(Alert.AlertType.WARNING, "Validation", "Vehicle type and plate cannot be empty.");
                return;
            }

            boolean success = courierController.updateVehicleInfo(type, plate);
            if (success) {
                showAlert(Alert.AlertType.INFORMATION, "Saved", "Vehicle info updated.");
                currentCourier = courierController.getCurrentCourier(); // refresh reference
            } else {
                showAlert(Alert.AlertType.ERROR, "Failed", "Failed to update vehicle info.");
            }
        });

        refreshBtn.setOnAction(e -> {
            loadOrders();
            showAlert(Alert.AlertType.INFORMATION, "Refreshed", "Order list has been refreshed!");
        });

        logoutBtn.setOnAction(e -> {
            courierController.logout();
            LoginPage loginPage = new LoginPage();
            loginPage.show(stage);
        });
    }

    private void loadOrders() {
        List<Order> orders = courierController.getAssignedOrders();
        ObservableList<Order> orderList = FXCollections.observableArrayList(orders);
        orderTable.setItems(orderList);

        if (orders.isEmpty()) {
            orderTable.setPlaceholder(new Label("No orders assigned to you yet."));
        }
    }

    private String inputStyle() {
        return "-fx-background-radius: 10; -fx-border-radius: 10; -fx-border-color: #A7F3D0; -fx-padding: 8;";
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
}

