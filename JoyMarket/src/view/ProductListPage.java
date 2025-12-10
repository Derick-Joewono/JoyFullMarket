package view;

import controller.CartController;
import controller.ProductController;
import model.Product;
import helper.SessionManager;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.stage.Stage;

public class ProductListPage {

    private final ProductController productController = new ProductController();
    private final CartController cartController = new CartController();

    public void show(Stage stage) {
        stage.setTitle("Product List");

        VBox root = new VBox(10);
        root.setPadding(new Insets(10));

        Label title = new Label("Available Products");
        title.setStyle("-fx-font-size: 18px; -fx-font-weight: bold;");

        ListView<Product> productListView = new ListView<>();
        productListView.getItems().addAll(productController.getAllProducts());

        Button addToCartButton = new Button("Add to Cart");
        addToCartButton.setOnAction(e -> {
            Product selected = productListView.getSelectionModel().getSelectedItem();
            if (selected == null) {
                showAlert(Alert.AlertType.WARNING, "Please select a product!");
                return;
            }

            TextInputDialog dialog = new TextInputDialog("1");
            dialog.setHeaderText(null);
            dialog.setContentText("Enter Quantity:");
            dialog.setTitle("Add to Cart");
            dialog.showAndWait();

            try {
                int qty = Integer.parseInt(dialog.getEditor().getText());
                if (qty <= 0) throw new NumberFormatException();
                int userId = SessionManager.getInstance().getCurrentCustomer().getId();

                boolean success = cartController.addToCart(userId, selected.getId(), qty);
                if (success) showAlert(Alert.AlertType.INFORMATION, "Product added to cart!");
                else showAlert(Alert.AlertType.ERROR, "Failed to add item. Not enough stock!");
            } catch (Exception ex) {
                showAlert(Alert.AlertType.WARNING, "Invalid quantity input!");
            }
        });

        Button goToCart = new Button("Go To Cart");
        
        goToCart.setOnAction(e -> new CartPage().show(stage));

        
        Button backBtn = new Button("Back");
        
        backBtn.setOnAction(e -> new SessionPage().show(stage));
        
        HBox buttons = new HBox(10, addToCartButton, goToCart, backBtn);

        root.getChildren().addAll(title, productListView, buttons);

        stage.setScene(new Scene(root, 400, 500));
        stage.show();
    }

    private void showAlert(Alert.AlertType type, String msg) {
        Alert alert = new Alert(type);
        alert.setHeaderText(null);
        alert.setTitle("Notification");
        alert.setContentText(msg);
        alert.show();
    }
}
