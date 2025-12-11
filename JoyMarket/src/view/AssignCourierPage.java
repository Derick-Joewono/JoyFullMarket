package view;

import controller.AdminController;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.*;
import javafx.stage.Stage;
import model.Courier;
import model.Order;
import model.OrderDetail;
import repository.CourierRepository;
import repository.OrderDetailRepository;

import java.util.List;

public class AssignCourierPage {

    private static final String ACCENT_COLOR = "#EF4444";

    private AdminController controller = new AdminController();
    private CourierRepository courierRepo = new CourierRepository();
    private OrderDetailRepository orderDetailRepo = new OrderDetailRepository();

    private BorderPane borderPane;
    private VBox card;
    private TableView<Order> orderTable;
    private ComboBox<Courier> courierComboBox;
    private Button assignBtn;
    private Button backBtn;
    private Button refreshBtn;
    private Button viewDetailsBtn;

    private Scene scene;

    private void initiate() {
        borderPane = new BorderPane();
        borderPane.setStyle("-fx-background-color: linear-gradient(to bottom right, #FEF2F2, #FEE2E2);");
        borderPane.setPadding(new Insets(24));

        card = new VBox(20);
        card.setPadding(new Insets(32));
        card.setAlignment(Pos.TOP_CENTER);
        card.setStyle("-fx-background-color: #FFFFFF; -fx-background-radius: 18; -fx-effect: dropshadow(gaussian, rgba(239,68,68,0.25), 24, 0.12, 0, 8);");

        orderTable = new TableView<>();
        orderTable.setStyle("-fx-background-radius: 10;");
        orderTable.setPrefHeight(350);

        courierComboBox = new ComboBox<>();
        courierComboBox.setPromptText("Select Courier");
        courierComboBox.setPrefWidth(250);
        courierComboBox.setStyle("-fx-background-radius: 10; -fx-border-radius: 10; -fx-border-color: #FCA5A5; -fx-padding: 6 8;");
        courierComboBox.setCellFactory(lv -> new ListCell<Courier>() {
            @Override
            public void updateItem(Courier item, boolean empty) {
                super.updateItem(item, empty);
                if (empty || item == null) {
                    setText(null);
                } else {
                    setText(item.getName() + " (" + item.getPhone() + ")");
                }
            }
        });
        courierComboBox.setButtonCell(new ListCell<Courier>() {
            @Override
            public void updateItem(Courier item, boolean empty) {
                super.updateItem(item, empty);
                setText(empty || item == null ? "Select Courier" : item.getName() + " (" + item.getPhone() + ")");
            }
        });

        assignBtn = new Button("Assign Courier");
        assignBtn.setStyle(primaryButtonStyle());
        assignBtn.setPrefWidth(150);

        refreshBtn = new Button("Refresh");
        refreshBtn.setStyle(ghostButtonStyle());

        viewDetailsBtn = new Button("View Order Details");
        viewDetailsBtn.setStyle(ghostButtonStyle());

        backBtn = new Button("Back");
        backBtn.setStyle(ghostButtonStyle());

        loadCouriers();
    }

    private void setLayout() {
        Label heading = new Label("Assign Courier to Orders");
        heading.setStyle("-fx-font-size: 24px; -fx-font-weight: bold; -fx-text-fill: #0F172A;");

        // Table Columns
        TableColumn<Order, Integer> idCol = new TableColumn<>("Order ID");
        idCol.setCellValueFactory(new PropertyValueFactory<>("id"));
        idCol.setPrefWidth(80);

        TableColumn<Order, Integer> customerCol = new TableColumn<>("Customer ID");
        customerCol.setCellValueFactory(new PropertyValueFactory<>("customerId"));
        customerCol.setPrefWidth(100);

        TableColumn<Order, Double> totalCol = new TableColumn<>("Total (Rp)");
        totalCol.setCellValueFactory(new PropertyValueFactory<>("total"));
        totalCol.setPrefWidth(150);
        totalCol.setCellFactory(column -> new TableCell<Order, Double>() {
            @Override
            protected void updateItem(Double item, boolean empty) {
                super.updateItem(item, empty);
                if (empty || item == null) {
                    setText(null);
                } else {
                    setText(String.format("%,.0f", item));
                }
            }
        });

        TableColumn<Order, String> statusCol = new TableColumn<>("Order Status");
        statusCol.setCellValueFactory(new PropertyValueFactory<>("status"));
        statusCol.setPrefWidth(120);

        TableColumn<Order, Integer> courierCol = new TableColumn<>("Courier ID");
        courierCol.setCellValueFactory(new PropertyValueFactory<>("courierId"));
        courierCol.setPrefWidth(100);
        courierCol.setCellFactory(column -> new TableCell<Order, Integer>() {
            @Override
            protected void updateItem(Integer item, boolean empty) {
                super.updateItem(item, empty);
                if (empty || item == null) {
                    setText("Not Assigned");
                    setStyle("-fx-text-fill: #94A3B8;");
                } else {
                    Courier courier = courierRepo.getCourierById(item);
                    if (courier != null) {
                        setText(courier.getName());
                        setStyle("-fx-text-fill: #10B981; -fx-font-weight: bold;");
                    } else {
                        setText("ID: " + item);
                        setStyle("-fx-text-fill: #64748B;");
                    }
                }
            }
        });

        TableColumn<Order, String> deliveryCol = new TableColumn<>("Delivery Status");
        deliveryCol.setCellValueFactory(new PropertyValueFactory<>("deliveryStatus"));
        deliveryCol.setPrefWidth(130);
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

        orderTable.getColumns().addAll(idCol, customerCol, totalCol, statusCol, courierCol, deliveryCol);
        orderTable.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);

        Label instructionLabel = new Label("Select an order from the table, choose a courier, and click 'Assign Courier'");
        instructionLabel.setStyle("-fx-text-fill: #6B7280; -fx-font-size: 12px;");

        HBox assignBox = new HBox(12);
        assignBox.setAlignment(Pos.CENTER_LEFT);
        assignBox.getChildren().addAll(
            new Label("Select Courier:") {{ setStyle("-fx-text-fill: #475569; -fx-font-weight: bold;"); }},
            courierComboBox,
            assignBtn
        );

        HBox actionButtons = new HBox(10);
        actionButtons.setAlignment(Pos.CENTER);
        actionButtons.getChildren().addAll(viewDetailsBtn, refreshBtn, backBtn);

        card.getChildren().addAll(heading, orderTable, assignBox, instructionLabel, actionButtons);
        borderPane.setCenter(card);
        BorderPane.setAlignment(card, Pos.CENTER);
    }

    private void setEventHandler(Stage stage) {
        assignBtn.setOnAction(e -> {
            Order selectedOrder = orderTable.getSelectionModel().getSelectedItem();
            Courier selectedCourier = courierComboBox.getValue();

            if (selectedOrder == null) {
                showAlert(Alert.AlertType.WARNING, "No Selection", "Please select an order from the table!");
                return;
            }

            if (selectedCourier == null) {
                showAlert(Alert.AlertType.WARNING, "No Courier Selected", "Please select a courier!");
                return;
            }

            if (selectedOrder.getCourierId() != null) {
                Alert confirmAlert = new Alert(Alert.AlertType.CONFIRMATION);
                confirmAlert.setTitle("Confirm Reassignment");
                confirmAlert.setHeaderText("Order Already Assigned");
                confirmAlert.setContentText("This order is already assigned to a courier. Do you want to reassign it to " + selectedCourier.getName() + "?");

                confirmAlert.showAndWait().ifPresent(buttonType -> {
                    if (buttonType != ButtonType.OK) {
                        return;
                    }
                    performAssignment(selectedOrder, selectedCourier);
                });
            } else {
                performAssignment(selectedOrder, selectedCourier);
            }
        });

        viewDetailsBtn.setOnAction(e -> {
            Order selectedOrder = orderTable.getSelectionModel().getSelectedItem();
            if (selectedOrder == null) {
                showAlert(Alert.AlertType.WARNING, "No Selection", "Please select an order from the table!");
                return;
            }
            showOrderDetailsDialog(selectedOrder);
        });

        refreshBtn.setOnAction(e -> {
            loadOrders();
            loadCouriers();
            showAlert(Alert.AlertType.INFORMATION, "Refreshed", "Order and courier list has been refreshed!");
        });

        backBtn.setOnAction(e -> {
            AdminDashboardPage dashboard = new AdminDashboardPage(controller.getCurrentAdmin());
            dashboard.show(stage);
        });
    }

    private void performAssignment(Order order, Courier courier) {
        boolean ok = controller.assignCourier(order.getId(), courier.getCourierId());

        if (ok) {
            showAlert(Alert.AlertType.INFORMATION, "Success", "Courier " + courier.getName() + " assigned to Order #" + order.getId() + " successfully!");
            loadOrders();
            courierComboBox.setValue(null);
        } else {
            showAlert(Alert.AlertType.ERROR, "Failed", "Assignment failed. Please try again.");
        }
    }

    private void loadOrders() {
        List<Order> orders = controller.listOpenOrders();
        ObservableList<Order> orderList = FXCollections.observableArrayList(orders);
        orderTable.setItems(orderList);

        if (orders.isEmpty()) {
            orderTable.setPlaceholder(new Label("No orders available"));
        }
    }

    private void loadCouriers() {
        List<Courier> couriers = controller.listCouriers();
        ObservableList<Courier> courierList = FXCollections.observableArrayList(couriers);
        courierComboBox.setItems(courierList);

        if (couriers.isEmpty()) {
            courierComboBox.setPromptText("No couriers available");
        }
    }

    private void showOrderDetailsDialog(Order order) {
        List<OrderDetail> details = orderDetailRepo.getOrderDetailsByOrderId(order.getId());

        Dialog<Void> dialog = new Dialog<>();
        dialog.setTitle("Order Details - Order #" + order.getId());
        dialog.setHeaderText("Order Details for Order #" + order.getId());

        // Create TableView for order details
        TableView<OrderDetail> detailsTable = new TableView<>();
        detailsTable.setPrefHeight(300);
        detailsTable.setPrefWidth(500);

        TableColumn<OrderDetail, String> productCol = new TableColumn<>("Product Name");
        productCol.setCellValueFactory(cellData -> {
            String name = cellData.getValue().getProductName();
            return new javafx.beans.property.SimpleStringProperty(name != null ? name : "Unknown");
        });
        productCol.setPrefWidth(200);

        TableColumn<OrderDetail, Integer> qtyCol = new TableColumn<>("Quantity");
        qtyCol.setCellValueFactory(new PropertyValueFactory<>("quantity"));
        qtyCol.setPrefWidth(100);

        TableColumn<OrderDetail, Double> subtotalCol = new TableColumn<>("Subtotal (Rp)");
        subtotalCol.setCellValueFactory(new PropertyValueFactory<>("subtotal"));
        subtotalCol.setPrefWidth(150);
        subtotalCol.setCellFactory(column -> new TableCell<OrderDetail, Double>() {
            @Override
            protected void updateItem(Double item, boolean empty) {
                super.updateItem(item, empty);
                if (empty || item == null) {
                    setText(null);
                } else {
                    setText(String.format("%,.0f", item));
                }
            }
        });

        detailsTable.getColumns().addAll(productCol, qtyCol, subtotalCol);
        detailsTable.setItems(FXCollections.observableArrayList(details));

        VBox content = new VBox(10);
        content.setPadding(new Insets(15));

        Label totalLabel = new Label("Total Order: Rp " + String.format("%,.0f", order.getTotal()));
        totalLabel.setStyle("-fx-font-size: 14px; -fx-font-weight: bold; -fx-text-fill: #065F46;");

        content.getChildren().addAll(detailsTable, totalLabel);

        dialog.getDialogPane().setContent(content);
        dialog.getDialogPane().getButtonTypes().add(ButtonType.CLOSE);
        dialog.showAndWait();
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
        loadOrders();

        scene = new Scene(borderPane, 1000, 700);
        stage.setScene(scene);
        stage.setTitle("JoyMarket - Assign Couriers");
        stage.show();
    }

    private String primaryButtonStyle() {
        return "-fx-background-color: " + ACCENT_COLOR + "; -fx-text-fill: white; -fx-font-size: 14px; -fx-font-weight: bold; -fx-background-radius: 10;";
    }

    private String ghostButtonStyle() {
        return "-fx-background-color: transparent; -fx-border-color: " + ACCENT_COLOR + "; -fx-text-fill: " + ACCENT_COLOR + "; -fx-font-size: 14px; -fx-font-weight: bold; -fx-border-radius: 10; -fx-background-radius: 10;";
    }
}
