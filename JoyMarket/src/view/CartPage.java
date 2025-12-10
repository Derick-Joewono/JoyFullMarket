package view;

import controller.CartController;
import model.CartItem;
import helper.SessionManager;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.stage.Stage;

public class CartPage {

    private final CartController cartController = new CartController();

    public void show(Stage stage) {
        stage.setTitle("Your Cart");

        VBox root = new VBox(10);
        root.setPadding(new Insets(10));

        Label title = new Label("Your Cart Items");
        title.setStyle("-fx-font-size: 18px; -fx-font-weight: bold;");

        ListView<CartItem> cartListView = new ListView<>();
        int userId = SessionManager.getInstance().getCurrentCustomer().getId();
        cartListView.getItems().addAll(cartController.getCartItems(userId));

        Button updateQtyBtn = new Button("Update Quantity");
        updateQtyBtn.setOnAction(e -> {
            CartItem selected = cartListView.getSelectionModel().getSelectedItem();
            if (selected == null) {
                showAlert(Alert.AlertType.WARNING, "Select an item to update!");
                return;
            }

            TextInputDialog input = new TextInputDialog(String.valueOf(selected.getQuantity()));
            input.setTitle("Update Quantity");
            input.setHeaderText(null);
            input.setContentText("Enter new quantity:");
            input.showAndWait();

            try {
                int newQty = Integer.parseInt(input.getEditor().getText());
                if (newQty < 0) throw new NumberFormatException();

                boolean updated = cartController.updateCartQuantity(selected.getCartId(), newQty);
                if (updated) {
                    reloadCart(cartListView, userId);
                    showAlert(Alert.AlertType.INFORMATION, "Quantity updated!");
                } else {
                    showAlert(Alert.AlertType.ERROR, "Failed! Not enough stock!");
                }
            } catch (Exception ex) {
                showAlert(Alert.AlertType.WARNING, "Invalid quantity!");
            }
        });

        Button removeBtn = new Button("Remove Item");
        removeBtn.setOnAction(e -> {
            CartItem selected = cartListView.getSelectionModel().getSelectedItem();
            if (selected == null) {
                showAlert(Alert.AlertType.WARNING, "Choose an item to remove!");
                return;
            }

            if (cartController.removeFromCart(selected.getCartId())) {
                reloadCart(cartListView, userId);
                showAlert(Alert.AlertType.INFORMATION, "Item removed!");
            } else {
                showAlert(Alert.AlertType.ERROR, "Failed to remove item!");
            }
        });

        Button checkoutBtn = new Button("Checkout");
        checkoutBtn.setOnAction(e -> {
            boolean success = cartController.checkout(userId);
            if (success) {
                reloadCart(cartListView, userId);
                showAlert(Alert.AlertType.INFORMATION, "Checkout successful!");
            } else {
                showAlert(Alert.AlertType.ERROR, "Checkout failed! Not enough balance or stock!");
            }
        });

        Button backBtn = new Button("Back to Products");
        backBtn.setOnAction(e -> new ProductListPage().show(stage));

        HBox buttons = new HBox(10, updateQtyBtn, removeBtn, checkoutBtn, backBtn);

        root.getChildren().addAll(title, cartListView, buttons);

        stage.setScene(new Scene(root, 450, 500));
        stage.show();
    }

    private void reloadCart(ListView<CartItem> list, int userId) {
        list.getItems().setAll(cartController.getCartItems(userId));
    }

    private void showAlert(Alert.AlertType type, String msg) {
        Alert alert = new Alert(type);
        alert.setHeaderText(null);
        alert.setTitle("Notification");
        alert.setContentText(msg);
        alert.show();
    }
}
