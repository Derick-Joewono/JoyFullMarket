package view;

import controller.AdminController;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.stage.Stage;
import model.Product;

import java.util.List;

public class AdminEditStockPage {

    private static final String ACCENT_COLOR = "#EF4444";

    private TableView<Product> table;
    private ObservableList<Product> data;

    private TextField nameField;
    private TextField priceField;
    private TextField stockField;
    private Button addProductBtn;

    private Button backBtn;
    private Button refreshBtn;

    private BorderPane borderPane;
    private VBox card;
    private Scene scene;

    private AdminController controller = new AdminController();

    private void initiate() {
        borderPane = new BorderPane();
        borderPane.setStyle("-fx-background-color: linear-gradient(to bottom right, #FEF2F2, #FEE2E2);");
        borderPane.setPadding(new Insets(24));

        card = new VBox(20);
        card.setPadding(new Insets(32));
        card.setAlignment(Pos.TOP_CENTER);
        card.setStyle("-fx-background-color: #FFFFFF; -fx-background-radius: 18; -fx-effect: dropshadow(gaussian, rgba(239,68,68,0.25), 24, 0.12, 0, 8);");

        table = new TableView<>();
        table.setStyle("-fx-background-radius: 10;");
        table.setPrefHeight(350);

        nameField = new TextField();
        nameField.setPromptText("Product Name");
        nameField.setStyle(inputStyle());

        priceField = new TextField();
        priceField.setPromptText("Price (Rp)");
        priceField.setStyle(inputStyle());

        stockField = new TextField();
        stockField.setPromptText("Stock");
        stockField.setStyle(inputStyle());

        addProductBtn = new Button("Add Product");
        addProductBtn.setStyle(primaryButtonStyle());

        refreshBtn = new Button("Refresh");
        refreshBtn.setStyle(ghostButtonStyle());

        backBtn = new Button("Back");
        backBtn.setStyle(ghostButtonStyle());

        data = FXCollections.observableArrayList();

        scene = new Scene(borderPane, 1000, 700);
    }

    private void setLayout() {
        Label heading = new Label("Manage Products");
        heading.setStyle("-fx-font-size: 24px; -fx-font-weight: bold; -fx-text-fill: #0F172A;");

        // Add Product Form
        Label addProductLabel = new Label("Add New Product");
        addProductLabel.setStyle("-fx-font-size: 18px; -fx-font-weight: bold; -fx-text-fill: #0F172A; -fx-padding: 0 0 10 0;");

        HBox addForm = new HBox(10);
        addForm.setAlignment(Pos.CENTER_LEFT);
        addForm.getChildren().addAll(
            nameField,
            priceField,
            stockField,
            addProductBtn
        );

        // Table Columns
        TableColumn<Product, Integer> cId = new TableColumn<>("ID");
        cId.setCellValueFactory(p ->
                new javafx.beans.property.SimpleIntegerProperty(p.getValue().getId()).asObject()
        );
        cId.setPrefWidth(60);

        TableColumn<Product, String> cName = new TableColumn<>("Product Name");
        cName.setCellValueFactory(p ->
                new javafx.beans.property.SimpleStringProperty(p.getValue().getName())
        );
        cName.setPrefWidth(250);

        TableColumn<Product, Integer> cStock = new TableColumn<>("Stock");
        cStock.setCellValueFactory(p ->
                new javafx.beans.property.SimpleIntegerProperty(p.getValue().getStock()).asObject()
        );
        cStock.setPrefWidth(100);

        TableColumn<Product, Double> cPrice = new TableColumn<>("Price (Rp)");
        cPrice.setCellValueFactory(p ->
                new javafx.beans.property.SimpleDoubleProperty(p.getValue().getPrice()).asObject()
        );
        cPrice.setPrefWidth(150);
        cPrice.setCellFactory(column -> new TableCell<Product, Double>() {
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

        // Action column for stock controls and price update
        TableColumn<Product, Void> actionCol = new TableColumn<>("Actions");
        actionCol.setPrefWidth(200);
        actionCol.setCellFactory(column -> new TableCell<Product, Void>() {
            private final Button incBtn = new Button("+");
            private final Button decBtn = new Button("-");
            private final Button priceBtn = new Button("Edit Price");
            private final HBox buttonsBox = new HBox(5);

            {
                incBtn.setStyle("-fx-background-color: #10B981; -fx-text-fill: white; -fx-font-weight: bold; -fx-min-width: 35; -fx-background-radius: 5;");
                decBtn.setStyle("-fx-background-color: #F59E0B; -fx-text-fill: white; -fx-font-weight: bold; -fx-min-width: 35; -fx-background-radius: 5;");
                priceBtn.setStyle("-fx-background-color: #3B82F6; -fx-text-fill: white; -fx-font-size: 11px; -fx-background-radius: 5;");
                buttonsBox.setAlignment(Pos.CENTER);
                buttonsBox.getChildren().addAll(incBtn, decBtn, priceBtn);

                incBtn.setOnAction(e -> {
                    Product product = getTableView().getItems().get(getIndex());
                    showStockAdjustDialog(product, true);
                });

                decBtn.setOnAction(e -> {
                    Product product = getTableView().getItems().get(getIndex());
                    showStockAdjustDialog(product, false);
                });

                priceBtn.setOnAction(e -> {
                    Product product = getTableView().getItems().get(getIndex());
                    showPriceDialog(product);
                });
            }

            @Override
            protected void updateItem(Void item, boolean empty) {
                super.updateItem(item, empty);
                if (empty) {
                    setGraphic(null);
                } else {
                    setGraphic(buttonsBox);
                }
            }

            private void showStockAdjustDialog(Product product, boolean isIncrease) {
                Dialog<String> dialog = new Dialog<>();
                dialog.setTitle(isIncrease ? "Increase Stock" : "Decrease Stock");
                dialog.setHeaderText("Product: " + product.getName() + "\nCurrent Stock: " + product.getStock());
                
                Label label = new Label(isIncrease ? "Enter amount to add:" : "Enter amount to subtract:");
                label.setStyle("-fx-text-fill: #475569; -fx-font-size: 12px;");
                
                TextField amountInput = new TextField();
                amountInput.setPromptText("Enter quantity");
                amountInput.setStyle(inputStyle());
                
                VBox content = new VBox(10);
                content.setPadding(new Insets(15));
                content.getChildren().addAll(label, amountInput);
                
                dialog.getDialogPane().setContent(content);
                dialog.getDialogPane().getButtonTypes().addAll(ButtonType.OK, ButtonType.CANCEL);
                
                dialog.setResultConverter(dialogButton -> {
                    if (dialogButton == ButtonType.OK) {
                        return amountInput.getText();
                    }
                    return null;
                });
                
                dialog.showAndWait().ifPresent(result -> {
                    try {
                        int amount = Integer.parseInt(result);
                        if (amount <= 0) {
                            showAlert(Alert.AlertType.WARNING, "Invalid Amount", "Amount must be greater than 0!");
                            return;
                        }
                        
                        if (!isIncrease) {
                            amount = -amount; // Make it negative for decrease
                        }
                        
                        if (controller.adjustStock(product.getId(), amount)) {
                            refreshTable();
                            String action = isIncrease ? "increased" : "decreased";
                            showAlert(Alert.AlertType.INFORMATION, "Success", 
                                "Stock " + action + " by " + Math.abs(amount) + " successfully!");
                        } else {
                            showAlert(Alert.AlertType.ERROR, "Failed", "Failed to adjust stock.");
                        }
                    } catch (NumberFormatException ex) {
                        showAlert(Alert.AlertType.ERROR, "Invalid Format", "Please enter a valid number!");
                    }
                });
            }

            private void showPriceDialog(Product product) {
                Dialog<String> dialog = new Dialog<>();
                dialog.setTitle("Update Price");
                dialog.setHeaderText("Product: " + product.getName());
                
                TextField priceInput = new TextField(String.format("%.0f", product.getPrice()));
                priceInput.setPromptText("Enter new price");
                priceInput.setStyle(inputStyle());
                
                dialog.getDialogPane().setContent(priceInput);
                dialog.getDialogPane().getButtonTypes().addAll(ButtonType.OK, ButtonType.CANCEL);
                
                dialog.setResultConverter(dialogButton -> {
                    if (dialogButton == ButtonType.OK) {
                        return priceInput.getText();
                    }
                    return null;
                });
                
                dialog.showAndWait().ifPresent(result -> {
                    try {
                        double newPrice = Double.parseDouble(result);
                        if (newPrice < 0) {
                            showAlert(Alert.AlertType.WARNING, "Invalid Price", "Price must be >= 0!");
                            return;
                        }
                        if (controller.updatePrice(product.getId(), newPrice)) {
                            refreshTable();
                            showAlert(Alert.AlertType.INFORMATION, "Success", "Price updated successfully!");
                        } else {
                            showAlert(Alert.AlertType.ERROR, "Failed", "Failed to update price.");
                        }
                    } catch (NumberFormatException ex) {
                        showAlert(Alert.AlertType.ERROR, "Invalid Format", "Please enter a valid number!");
                    }
                });
            }
        });

        table.getColumns().addAll(cId, cName, cStock, cPrice, actionCol);
        table.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);

        // Instructions
        Label instructionLabel = new Label("Click + to increase stock (enter amount), - to decrease stock (enter amount), or 'Edit Price' to update price");
        instructionLabel.setStyle("-fx-text-fill: #6B7280; -fx-font-size: 12px;");

        HBox actionButtons = new HBox(10);
        actionButtons.setAlignment(Pos.CENTER);
        actionButtons.getChildren().addAll(refreshBtn, backBtn);

        VBox addProductSection = new VBox(10);
        addProductSection.getChildren().addAll(addProductLabel, addForm);

        card.getChildren().addAll(heading, addProductSection, table, instructionLabel, actionButtons);
        borderPane.setCenter(card);
    }

    private void setEventHandler(Stage stage) {
        addProductBtn.setOnAction(e -> addProduct());

        refreshBtn.setOnAction(e -> refreshTable());

        backBtn.setOnAction(e -> {
            AdminDashboardPage dashboard = new AdminDashboardPage(controller.getCurrentAdmin());
            dashboard.show(stage);
        });
    }

    private void addProduct() {
        String name = nameField.getText().trim();
        String priceText = priceField.getText().trim();
        String stockText = stockField.getText().trim();

        if (name.isEmpty() || priceText.isEmpty() || stockText.isEmpty()) {
            showAlert(Alert.AlertType.WARNING, "Validation Error", "Please fill in all fields!");
            return;
        }

        try {
            double price = Double.parseDouble(priceText);
            int stock = Integer.parseInt(stockText);

            if (price < 0) {
                showAlert(Alert.AlertType.WARNING, "Invalid Price", "Price must be >= 0!");
                return;
            }

            if (stock < 0) {
                showAlert(Alert.AlertType.WARNING, "Invalid Stock", "Stock must be >= 0!");
                return;
            }

            boolean result = controller.addProduct(name, price, stock);
            if (result) {
                showAlert(Alert.AlertType.INFORMATION, "Success", "Product added successfully!");
                nameField.clear();
                priceField.clear();
                stockField.clear();
                refreshTable();
            } else {
                showAlert(Alert.AlertType.ERROR, "Failed", 
                    "Failed to add product. Please check:\n" +
                    "1. Database connection is active\n" +
                    "2. Products table exists\n" +
                    "3. Check console for error details");
            }

        } catch (NumberFormatException ex) {
            showAlert(Alert.AlertType.ERROR, "Invalid Format", "Please enter valid numbers for price and stock!");
        }
    }

    private void refreshTable() {
        List<Product> products = controller.listProducts();
        data.setAll(products);
        table.setItems(data);
        table.refresh();
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
        refreshTable();

        stage.setScene(scene);
        stage.setTitle("JoyMarket - Manage Products");
        stage.show();
    }

    private String primaryButtonStyle() {
        return "-fx-background-color: " + ACCENT_COLOR + "; -fx-text-fill: white; -fx-font-size: 14px; -fx-font-weight: bold; -fx-background-radius: 10;";
    }

    private String ghostButtonStyle() {
        return "-fx-background-color: transparent; -fx-border-color: " + ACCENT_COLOR + "; -fx-text-fill: " + ACCENT_COLOR + "; -fx-font-size: 14px; -fx-font-weight: bold; -fx-border-radius: 10; -fx-background-radius: 10;";
    }

    private String inputStyle() {
        return "-fx-background-radius: 10; -fx-border-radius: 10; -fx-border-color: #FCA5A5; -fx-padding: 8;";
    }
}
