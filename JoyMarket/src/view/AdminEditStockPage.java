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

    // UI Components
    private TableView<Product> table;
    private ObservableList<Product> data;

    private TextField adjustField;
    private TextField priceField;

    private Button incBtn;
    private Button decBtn;
    private Button updatePriceBtn;

    private VBox root;
    private HBox controls;
    private Scene scene;

    private AdminController controller = new AdminController();

    // =====================================================
    // 1. INITIATE()
    // =====================================================
    private void initiate() {
        table = new TableView<>();
        data = FXCollections.observableArrayList();

        adjustField = new TextField();
        adjustField.setPromptText("Adjust amount (int)");

        priceField = new TextField();
        priceField.setPromptText("New price (double)");

        incBtn = new Button("Increase");
        decBtn = new Button("Decrease");
        updatePriceBtn = new Button("Update Price");

        controls = new HBox(8);
        controls.setPadding(new Insets(6));

        root = new VBox(10);
        root.setPadding(new Insets(10));

        scene = new Scene(root, 700, 450);
    }

    // =====================================================
    // 2. SET LAYOUT()
    // =====================================================
    private void setLayout() {
        // Table Columns
        TableColumn<Product, Integer> cId = new TableColumn<>("ID");
        cId.setCellValueFactory(p ->
                new javafx.beans.property.SimpleIntegerProperty(p.getValue().getId()).asObject()
        );

        TableColumn<Product, String> cName = new TableColumn<>("Name");
        cName.setCellValueFactory(p ->
                new javafx.beans.property.SimpleStringProperty(p.getValue().getName())
        );

        TableColumn<Product, Integer> cStock = new TableColumn<>("Stock");
        cStock.setCellValueFactory(p ->
                new javafx.beans.property.SimpleIntegerProperty(p.getValue().getStock()).asObject()
        );

        TableColumn<Product, Double> cPrice = new TableColumn<>("Price");
        cPrice.setCellValueFactory(p ->
                new javafx.beans.property.SimpleDoubleProperty(p.getValue().getPrice()).asObject()
        );

        table.getColumns().addAll(cId, cName, cStock, cPrice);
        table.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);

        // Controls (bottom section)
        controls.getChildren().setAll(adjustField, incBtn, decBtn, priceField, updatePriceBtn);
        controls.setAlignment(Pos.CENTER_LEFT);

        // Root Layout
        root.getChildren().setAll(
                new Label("Products"),
                table,
                controls
        );
    }

    // =====================================================
    // 3. SET EVENT HANDLER()
    // =====================================================
    private void setEventHandler() {
        incBtn.setOnAction(e -> adjustStock(+1));
        decBtn.setOnAction(e -> adjustStock(-1));
        updatePriceBtn.setOnAction(e -> updatePrice());
    }

    // =====================================================
    // START
    // =====================================================
    public void start(Stage stage) {
        initiate();
        setLayout();
        setEventHandler();

        refreshTable();

        stage.setScene(scene);
        stage.setTitle("Edit Stock & Price");
        stage.show();
    }

    // =====================================================
    // LOGIC FUNCTIONS
    // =====================================================
    private void refreshTable() {
        List<Product> products = controller.listProducts();
        data.setAll(products);
        table.setItems(data);
        table.refresh();
    }

    private void adjustStock(int dir) {
        Product sel = table.getSelectionModel().getSelectedItem();
        if (sel == null) {
            new Alert(Alert.AlertType.INFORMATION, "Select a product first").show();
            return;
        }

        try {
            int amt = Integer.parseInt(adjustField.getText());
            if (amt <= 0) {
                new Alert(Alert.AlertType.INFORMATION, "Please enter a positive number").show();
                return;
            }

            int newStock = sel.getStock() + dir * amt;
            if (newStock < 0) newStock = 0;

            sel.setStock(newStock);

            if (controller.updateProduct(sel)) {
                refreshTable();
                new Alert(Alert.AlertType.INFORMATION, "Stock updated").show();
            } else {
                new Alert(Alert.AlertType.ERROR, "Update failed").show();
            }

        } catch (Exception ex) {
            new Alert(Alert.AlertType.INFORMATION, "Invalid number format").show();
        }
    }

    private void updatePrice() {
        Product sel = table.getSelectionModel().getSelectedItem();
        if (sel == null) {
            new Alert(Alert.AlertType.INFORMATION, "Select a product first").show();
            return;
        }

        try {
            double pr = Double.parseDouble(priceField.getText());
            if (pr < 0) {
                new Alert(Alert.AlertType.INFORMATION, "Price must be >= 0").show();
                return;
            }

            sel.setPrice(pr);

            if (controller.updateProduct(sel)) {
                refreshTable();
                new Alert(Alert.AlertType.INFORMATION, "Price updated").show();
            } else {
                new Alert(Alert.AlertType.ERROR, "Update failed").show();
            }

        } catch (Exception ex) {
            new Alert(Alert.AlertType.INFORMATION, "Invalid price format").show();
        }
    }
}
