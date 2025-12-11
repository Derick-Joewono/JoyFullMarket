package view;

import controller.CartController;
import model.CartItem;
import helper.SessionManager;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.stage.Stage;

public class CartPage {

    private static final String ACCENT_COLOR = "#64748B";

    private final CartController cartController = new CartController();

    private BorderPane borderPane;
    private VBox card;
    private TableView<CartItem> cartTable;
    private Label totalLabel;
    private Button updateQtyBtn;
    private Button removeBtn;
    private Button checkoutBtn;
    private Button backBtn;

    public void show(Stage stage) {
        int userId = SessionManager.getInstance().getCurrentCustomer().getId();
        
        initiate();
        setLayout();
        setEventHandler(stage, userId);
        loadCart(userId);

        stage.setScene(new Scene(borderPane, 1000, 700));
        stage.setTitle("JoyMarket - Your Cart");
        stage.show();
    }

    private void initiate() {
        borderPane = new BorderPane();
        borderPane.setStyle("-fx-background-color: linear-gradient(to bottom right, #EEF2F6, #F8FAFC);");
        borderPane.setPadding(new Insets(24));

        card = new VBox(20);
        card.setPadding(new Insets(32));
        card.setAlignment(Pos.TOP_CENTER);
        card.setStyle("-fx-background-color: #FFFFFF; -fx-background-radius: 18; -fx-effect: dropshadow(gaussian, rgba(100,116,139,0.25), 24, 0.12, 0, 8);");

        cartTable = new TableView<>();
        cartTable.setStyle("-fx-background-radius: 10;");
        cartTable.setPrefHeight(350);

        totalLabel = new Label("Total: Rp 0");
        totalLabel.setStyle("-fx-font-size: 18px; -fx-font-weight: bold; -fx-text-fill: #0F172A;");

        updateQtyBtn = new Button("Update Quantity");
        updateQtyBtn.setStyle(primaryButtonStyle());
        updateQtyBtn.setPrefWidth(150);

        removeBtn = new Button("Remove Item");
        removeBtn.setStyle("-fx-background-color: #EF4444; -fx-text-fill: white; -fx-font-size: 14px; -fx-font-weight: bold; -fx-background-radius: 10;");
        removeBtn.setPrefWidth(130);

        checkoutBtn = new Button("Checkout");
        checkoutBtn.setStyle("-fx-background-color: #10B981; -fx-text-fill: white; -fx-font-size: 14px; -fx-font-weight: bold; -fx-background-radius: 10;");
        checkoutBtn.setPrefWidth(120);

        backBtn = new Button("Back");
        backBtn.setStyle(ghostButtonStyle());
        backBtn.setPrefWidth(100);
    }

    private void setLayout() {
        Label heading = new Label("Your Shopping Cart");
        heading.setStyle("-fx-font-size: 24px; -fx-font-weight: bold; -fx-text-fill: #0F172A;");

        // Table Columns
        TableColumn<CartItem, Integer> idCol = new TableColumn<>("ID");
        idCol.setCellValueFactory(p -> new javafx.beans.property.SimpleIntegerProperty(p.getValue().getCartId()).asObject());
        idCol.setPrefWidth(60);

        TableColumn<CartItem, String> nameCol = new TableColumn<>("Product Name");
        nameCol.setCellValueFactory(p -> new javafx.beans.property.SimpleStringProperty(p.getValue().getProductName()));
        nameCol.setPrefWidth(300);

        TableColumn<CartItem, Double> priceCol = new TableColumn<>("Price (Rp)");
        priceCol.setCellValueFactory(p -> new javafx.beans.property.SimpleDoubleProperty(p.getValue().getPrice()).asObject());
        priceCol.setPrefWidth(150);
        priceCol.setCellFactory(column -> new TableCell<CartItem, Double>() {
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

        TableColumn<CartItem, Integer> qtyCol = new TableColumn<>("Quantity");
        qtyCol.setCellValueFactory(p -> new javafx.beans.property.SimpleIntegerProperty(p.getValue().getQuantity()).asObject());
        qtyCol.setPrefWidth(100);

        TableColumn<CartItem, Double> subtotalCol = new TableColumn<>("Subtotal (Rp)");
        subtotalCol.setCellValueFactory(p -> 
            new javafx.beans.property.SimpleDoubleProperty(p.getValue().getPrice() * p.getValue().getQuantity()).asObject());
        subtotalCol.setPrefWidth(150);
        subtotalCol.setCellFactory(column -> new TableCell<CartItem, Double>() {
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

        cartTable.getColumns().addAll(idCol, nameCol, priceCol, qtyCol, subtotalCol);
        cartTable.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);

        Label instructionLabel = new Label("Select an item to update quantity or remove from cart");
        instructionLabel.setStyle("-fx-text-fill: #6B7280; -fx-font-size: 12px;");

        HBox totalBox = new HBox();
        totalBox.setAlignment(Pos.CENTER_RIGHT);
        totalBox.setPadding(new Insets(10, 0, 0, 0));
        totalBox.getChildren().add(totalLabel);

        HBox buttonBox = new HBox(10);
        buttonBox.setAlignment(Pos.CENTER);
        buttonBox.getChildren().addAll(updateQtyBtn, removeBtn, checkoutBtn, backBtn);

        card.getChildren().addAll(heading, cartTable, totalBox, instructionLabel, buttonBox);
        borderPane.setCenter(card);
        BorderPane.setAlignment(card, Pos.CENTER);
    }

    private void setEventHandler(Stage stage, int userId) {
        updateQtyBtn.setOnAction(e -> {
            CartItem selected = cartTable.getSelectionModel().getSelectedItem();
            if (selected == null) {
                showAlert(Alert.AlertType.WARNING, "No Selection", "Please select an item to update!");
                return;
            }

            TextInputDialog input = new TextInputDialog(String.valueOf(selected.getQuantity()));
            input.setTitle("Update Quantity");
            input.setHeaderText("Product: " + selected.getProductName());
            input.setContentText("Enter new quantity:");

            input.showAndWait().ifPresent(quantityText -> {
                try {
                    int newQty = Integer.parseInt(quantityText);
                    if (newQty < 0) {
                        showAlert(Alert.AlertType.WARNING, "Invalid Quantity", "Quantity must be >= 0!");
                        return;
                    }

                    boolean updated = cartController.updateCartQuantity(selected.getCartId(), newQty);
                    if (updated) {
                        showAlert(Alert.AlertType.INFORMATION, "Success", "Quantity updated successfully!");
                        loadCart(userId);
                    } else {
                        showAlert(Alert.AlertType.ERROR, "Failed", "Failed to update! Not enough stock!");
                    }
                } catch (NumberFormatException ex) {
                    showAlert(Alert.AlertType.ERROR, "Invalid Input", "Please enter a valid number!");
                }
            });
        });

        removeBtn.setOnAction(e -> {
            CartItem selected = cartTable.getSelectionModel().getSelectedItem();
            if (selected == null) {
                showAlert(Alert.AlertType.WARNING, "No Selection", "Please select an item to remove!");
                return;
            }

            Alert confirmAlert = new Alert(Alert.AlertType.CONFIRMATION);
            confirmAlert.setTitle("Confirm Removal");
            confirmAlert.setHeaderText("Remove Item");
            confirmAlert.setContentText("Are you sure you want to remove " + selected.getProductName() + " from cart?");

            confirmAlert.showAndWait().ifPresent(buttonType -> {
                if (buttonType == ButtonType.OK) {
                    if (cartController.removeFromCart(selected.getCartId())) {
                        showAlert(Alert.AlertType.INFORMATION, "Success", "Item removed from cart!");
                        loadCart(userId);
                    } else {
                        showAlert(Alert.AlertType.ERROR, "Failed", "Failed to remove item!");
                    }
                }
            });
        });

        checkoutBtn.setOnAction(e -> {
            if (cartTable.getItems().isEmpty()) {
                showAlert(Alert.AlertType.WARNING, "Empty Cart", "Your cart is empty!");
                return;
            }

            Alert confirmAlert = new Alert(Alert.AlertType.CONFIRMATION);
            confirmAlert.setTitle("Confirm Checkout");
            confirmAlert.setHeaderText("Proceed to Checkout");
            confirmAlert.setContentText("Are you sure you want to checkout? This will deduct your balance.");

            confirmAlert.showAndWait().ifPresent(buttonType -> {
                if (buttonType == ButtonType.OK) {
                    boolean success = cartController.checkout(userId);
                    if (success) {
                        showAlert(Alert.AlertType.INFORMATION, "Success", "Checkout successful! Your order has been placed.");
                        loadCart(userId);
                    } else {
                        showAlert(Alert.AlertType.ERROR, "Failed", "Checkout failed! Not enough balance or stock!");
                    }
                }
            });
        });

        backBtn.setOnAction(e -> {
            ProductListPage productListPage = new ProductListPage();
            productListPage.show(stage);
        });
    }

    private void loadCart(int userId) {
        cartTable.getItems().setAll(cartController.getCartItems(userId));
        calculateTotal();
    }

    private void calculateTotal() {
        double total = 0;
        for (CartItem item : cartTable.getItems()) {
            total += item.getPrice() * item.getQuantity();
        }
        totalLabel.setText("Total: Rp " + String.format("%,.0f", total));
    }

    private void showAlert(Alert.AlertType type, String title, String msg) {
        Alert alert = new Alert(type);
        alert.setHeaderText(null);
        alert.setTitle(title);
        alert.setContentText(msg);
        alert.showAndWait();
    }

    private String primaryButtonStyle() {
        return "-fx-background-color: " + ACCENT_COLOR + "; -fx-text-fill: white; -fx-font-size: 14px; -fx-font-weight: bold; -fx-background-radius: 10;";
    }

    private String ghostButtonStyle() {
        return "-fx-background-color: transparent; -fx-border-color: " + ACCENT_COLOR + "; -fx-text-fill: " + ACCENT_COLOR + "; -fx-font-size: 14px; -fx-font-weight: bold; -fx-border-radius: 10; -fx-background-radius: 10;";
    }
}
