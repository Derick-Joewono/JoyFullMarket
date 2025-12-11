package view;

import controller.CartController;
import controller.ProductController;
import model.Product;
import helper.SessionManager;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.stage.Stage;

public class ProductListPage {

    private static final String ACCENT_COLOR = "#64748B";

    private final ProductController productController = new ProductController();
    private final CartController cartController = new CartController();

    private BorderPane borderPane;
    private VBox card;
    private TableView<Product> productTable;
    private Button addToCartButton;
    private Button goToCartButton;
    private Button backBtn;

    public void show(Stage stage) {
        initiate();
        setLayout();
        setEventHandler(stage);
        loadProducts();

        stage.setScene(new Scene(borderPane, 1000, 700));
        stage.setTitle("JoyMarket - Shop Now");
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

        productTable = new TableView<>();
        productTable.setStyle("-fx-background-radius: 10;");
        productTable.setPrefHeight(400);

        addToCartButton = new Button("Add to Cart");
        addToCartButton.setStyle(primaryButtonStyle());
        addToCartButton.setPrefWidth(150);

        goToCartButton = new Button("View Cart");
        goToCartButton.setStyle(secondaryButtonStyle());
        goToCartButton.setPrefWidth(150);

        backBtn = new Button("Back");
        backBtn.setStyle(ghostButtonStyle());
        backBtn.setPrefWidth(100);
    }

    private void setLayout() {
        Label heading = new Label("Available Products");
        heading.setStyle("-fx-font-size: 24px; -fx-font-weight: bold; -fx-text-fill: #0F172A;");

        // Table Columns
        TableColumn<Product, Integer> idCol = new TableColumn<>("ID");
        idCol.setCellValueFactory(p -> new javafx.beans.property.SimpleIntegerProperty(p.getValue().getId()).asObject());
        idCol.setPrefWidth(60);

        TableColumn<Product, String> nameCol = new TableColumn<>("Product Name");
        nameCol.setCellValueFactory(p -> new javafx.beans.property.SimpleStringProperty(p.getValue().getName()));
        nameCol.setPrefWidth(250);

        TableColumn<Product, Double> priceCol = new TableColumn<>("Price (Rp)");
        priceCol.setCellValueFactory(p -> new javafx.beans.property.SimpleDoubleProperty(p.getValue().getPrice()).asObject());
        priceCol.setPrefWidth(150);
        priceCol.setCellFactory(column -> new TableCell<Product, Double>() {
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

        TableColumn<Product, Integer> stockCol = new TableColumn<>("Stock");
        stockCol.setCellValueFactory(p -> new javafx.beans.property.SimpleIntegerProperty(p.getValue().getStock()).asObject());
        stockCol.setPrefWidth(100);
        stockCol.setCellFactory(column -> new TableCell<Product, Integer>() {
            @Override
            protected void updateItem(Integer item, boolean empty) {
                super.updateItem(item, empty);
                if (empty || item == null) {
                    setText(null);
                    setStyle("");
                } else {
                    setText(item.toString());
                    if (item == 0) {
                        setStyle("-fx-text-fill: #EF4444; -fx-font-weight: bold;");
                    } else if (item < 10) {
                        setStyle("-fx-text-fill: #F59E0B; -fx-font-weight: bold;");
                    } else {
                        setStyle("-fx-text-fill: #10B981; -fx-font-weight: bold;");
                    }
                }
            }
        });

        productTable.getColumns().addAll(idCol, nameCol, priceCol, stockCol);
        productTable.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);

        Label instructionLabel = new Label("Select a product and click 'Add to Cart' to add items to your cart");
        instructionLabel.setStyle("-fx-text-fill: #6B7280; -fx-font-size: 12px;");

        HBox buttonBox = new HBox(10);
        buttonBox.setAlignment(Pos.CENTER);
        buttonBox.getChildren().addAll(addToCartButton, goToCartButton, backBtn);

        card.getChildren().addAll(heading, productTable, instructionLabel, buttonBox);
        borderPane.setCenter(card);
        BorderPane.setAlignment(card, Pos.CENTER);
    }

    private void setEventHandler(Stage stage) {
        addToCartButton.setOnAction(e -> {
            Product selected = productTable.getSelectionModel().getSelectedItem();
            if (selected == null) {
                showAlert(Alert.AlertType.WARNING, "No Selection", "Please select a product first!");
                return;
            }

            if (selected.getStock() == 0) {
                showAlert(Alert.AlertType.WARNING, "Out of Stock", "This product is currently out of stock!");
                return;
            }

            TextInputDialog dialog = new TextInputDialog("1");
            dialog.setHeaderText("Add to Cart");
            dialog.setTitle("Quantity");
            dialog.setContentText("Enter Quantity (Max: " + selected.getStock() + "):");

            dialog.showAndWait().ifPresent(quantityText -> {
                try {
                    int qty = Integer.parseInt(quantityText);
                    if (qty <= 0) {
                        showAlert(Alert.AlertType.WARNING, "Invalid Quantity", "Quantity must be greater than 0!");
                        return;
                    }
                    if (qty > selected.getStock()) {
                        showAlert(Alert.AlertType.WARNING, "Insufficient Stock", "Only " + selected.getStock() + " items available!");
                        return;
                    }

                    int userId = SessionManager.getInstance().getCurrentCustomer().getId();
                    boolean success = cartController.addToCart(userId, selected.getId(), qty);

                    if (success) {
                        showAlert(Alert.AlertType.INFORMATION, "Success", "Product added to cart successfully!");
                        loadProducts();
                    } else {
                        showAlert(Alert.AlertType.ERROR, "Failed", "Failed to add item. Not enough stock!");
                    }
                } catch (NumberFormatException ex) {
                    showAlert(Alert.AlertType.ERROR, "Invalid Input", "Please enter a valid number!");
                }
            });
        });

        goToCartButton.setOnAction(e -> {
            CartPage cartPage = new CartPage();
            cartPage.show(stage);
        });

        backBtn.setOnAction(e -> {
            SessionPage sessionPage = new SessionPage();
            sessionPage.show(stage);
        });
    }

    private void loadProducts() {
        productTable.getItems().setAll(productController.getAllProducts());
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

    private String secondaryButtonStyle() {
        return "-fx-background-color: #10B981; -fx-text-fill: white; -fx-font-size: 14px; -fx-font-weight: bold; -fx-background-radius: 10;";
    }

    private String ghostButtonStyle() {
        return "-fx-background-color: transparent; -fx-border-color: " + ACCENT_COLOR + "; -fx-text-fill: " + ACCENT_COLOR + "; -fx-font-size: 14px; -fx-font-weight: bold; -fx-border-radius: 10; -fx-background-radius: 10;";
    }
}
