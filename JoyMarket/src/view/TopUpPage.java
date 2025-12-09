package view;

import controller.CustomerBalanceController;
import helper.SessionManager;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import model.Customer;

public class TopUpPage {

    private CustomerBalanceController controller = new CustomerBalanceController();

    public void start(Stage stage) {
        Customer current = SessionManager.getInstance().getCurrentCustomer();

        Label title = new Label("Top Up Balance");
        Label currentBalanceLbl = new Label("Current Balance: Rp " + controller.getBalance(current.getId()));

        Label amountLbl = new Label("Enter Amount:");
        TextField amountField = new TextField();
        Button topUpBtn = new Button("TOP UP");
        Button backBtn = new Button("BACK");

        Label status = new Label("");

        topUpBtn.setOnAction(e -> {
            try {
                double amount = Double.parseDouble(amountField.getText());
                if (amount <= 0) {
                    status.setText("Amount must be positive!");
                } else {
                    boolean success = controller.topUp(current.getId(), amount);
                    if (success) {
                        status.setText("Top Up Success!");
                        currentBalanceLbl.setText("Current Balance: Rp " + controller.getBalance(current.getId()));
                        amountField.clear();
                    }
                }
            } catch (Exception ex) {
                status.setText("Invalid input!");
            }
        });

        VBox root = new VBox(10, title, currentBalanceLbl, amountLbl, amountField, topUpBtn, backBtn, status);
        root.setPadding(new Insets(20));
        root.setAlignment(Pos.CENTER);

        stage.setTitle("Top Up");
        stage.setScene(new Scene(root, 350, 300));
        stage.show();
    }

    public void show(Stage stage) {
        // Ambil customer login saat ini
        Customer current = SessionManager.getInstance().getCurrentCustomer();
        if (current == null) {
            Alert alert = new Alert(Alert.AlertType.ERROR, "Anda belum login!");
            alert.show();
            return;
        }

        // Controller
        CustomerBalanceController balanceController = new CustomerBalanceController();

        // Label untuk menampilkan balance saat ini
        Label balanceLabel = new Label("Current Balance: Rp " + balanceController.getBalance(current.getId()));

        // Input amount top-up
        TextField amountField = new TextField();
        amountField.setPromptText("Enter amount (Rp)");

        // Tombol Top Up
        Button btnTopUp = new Button("Top Up");
        
        Button btnBack = new Button("Back");
        
        btnTopUp.setOnAction(e -> {
            try {
                double amount = Double.parseDouble(amountField.getText());
                if (amount <= 0) {
                    Alert alert = new Alert(Alert.AlertType.WARNING, "Amount must be greater than 0!");
                    alert.show();
                    return;
                }

                boolean success = balanceController.topUp(current.getId(), amount);

                if (success) {
                    Alert successAlert = new Alert(Alert.AlertType.INFORMATION, "Top Up Successful!");
                    successAlert.show();
                    balanceLabel.setText("Current Balance: Rp " + balanceController.getBalance(current.getId()));
                    amountField.clear();
                } else {
                    Alert errorAlert = new Alert(Alert.AlertType.ERROR, "Top Up Failed!");
                    errorAlert.show();
                }
            } catch (NumberFormatException ex) {
                Alert alert = new Alert(Alert.AlertType.ERROR, "Invalid number format!");
                alert.show();
            }
            
        });
        
        btnBack.setOnAction(e -> {
            SessionPage sessionPage = new SessionPage();
            sessionPage.show(stage);
        });
       
        
        // Layout
        VBox layout = new VBox(10);
        layout.setAlignment(Pos.CENTER);
        layout.setPadding(new Insets(20));
        layout.getChildren().addAll(balanceLabel, amountField, btnTopUp, btnBack);

        // Scene
        Scene scene = new Scene(layout, 350, 200);
        stage.setTitle("Top Up Balance");
        stage.setScene(scene);
        stage.show();
    }

}
