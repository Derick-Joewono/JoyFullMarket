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
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import model.Customer;

public class TopUpPage {

    private static final String ACCENT_COLOR = "#64748B";

    Scene scene;
    BorderPane borderPane;
    VBox card;
    GridPane formPane;

    Label balanceLabel;
    TextField amountField;

    Button topUpBtn;
    Button backBtn;

    CustomerBalanceController balanceController;
    Customer currentCustomer;

    public void show(Stage stage) {
        currentCustomer = SessionManager.getInstance().getCurrentCustomer();
        if (currentCustomer == null) {
            Alert alert = new Alert(Alert.AlertType.ERROR, "Anda belum login!");
            alert.show();
            return;
        }

        initiate();
        setLayout();
        setEventHandler(stage);

        stage.setScene(scene);
        stage.setTitle("JoyMarket - Top Up Balance");
        stage.show();
    }

    private void initiate() {
        borderPane = new BorderPane();
        borderPane.setStyle("-fx-background-color: linear-gradient(to bottom right, #EEF2F6, #F8FAFC);");
        borderPane.setPadding(new Insets(24));

        card = new VBox(18);
        card.setPadding(new Insets(32));
        card.setAlignment(Pos.CENTER);
        card.setMaxWidth(480);
        card.setStyle("-fx-background-color: #FFFFFF; -fx-background-radius: 18; -fx-effect: dropshadow(gaussian, rgba(100,116,139,0.25), 24, 0.12, 0, 8);");

        formPane = new GridPane();
        formPane.setVgap(12);
        formPane.setHgap(12);
        formPane.setAlignment(Pos.CENTER);

        balanceController = new CustomerBalanceController();

        balanceLabel = new Label("Current Balance: Rp " + String.format("%,.0f", balanceController.getBalance(currentCustomer.getId())));
        balanceLabel.setStyle("-fx-font-size: 18px; -fx-font-weight: bold; -fx-text-fill: #0F172A; -fx-padding: 10 20; -fx-background-color: #F1F5F9; -fx-background-radius: 10;");

        amountField = new TextField();
        amountField.setPromptText("Enter amount (Rp)");
        amountField.setStyle(inputStyle());
        amountField.setPrefWidth(200);

        topUpBtn = new Button("Top Up");
        topUpBtn.setPrefWidth(140);
        topUpBtn.setStyle(primaryButtonStyle());

        backBtn = new Button("Back");
        backBtn.setPrefWidth(100);
        backBtn.setStyle(ghostButtonStyle());

        scene = new Scene(borderPane, 680, 420);
    }

    private void setLayout() {
        Label heading = new Label("Top Up Balance");
        heading.setStyle("-fx-font-size: 24px; -fx-font-weight: bold; -fx-text-fill: #0F172A;");

        Label amountLabel = new Label("Amount");
        amountLabel.setStyle(labelStyle());

        formPane.add(amountLabel, 0, 0);
        formPane.add(amountField, 1, 0);

        HBox actions = new HBox(10);
        actions.setAlignment(Pos.CENTER);
        actions.getChildren().addAll(backBtn, topUpBtn);

        card.getChildren().setAll(heading, balanceLabel, formPane, actions);
        borderPane.setCenter(card);
        BorderPane.setAlignment(card, Pos.CENTER);
    }

    private void setEventHandler(Stage stage) {
        topUpBtn.setOnAction(e -> {
            try {
                String amountText = amountField.getText().trim();
                if (amountText.isEmpty()) {
                    showAlert(Alert.AlertType.ERROR, "Validation Error", "Please enter an amount!");
                    return;
                }

                double amount = Double.parseDouble(amountText);
                if (amount <= 0) {
                    showAlert(Alert.AlertType.WARNING, "Invalid Amount", "Amount must be greater than 0!");
                    return;
                }

                boolean success = balanceController.topUp(currentCustomer.getId(), amount);

                if (success) {
                    showAlert(Alert.AlertType.INFORMATION, "Success", "Top Up Successful!");
                    balanceLabel.setText("Current Balance: Rp " + String.format("%,.0f", balanceController.getBalance(currentCustomer.getId())));
                    amountField.clear();
                } else {
                    showAlert(Alert.AlertType.ERROR, "Failed", "Top Up Failed!");
                }
            } catch (NumberFormatException ex) {
                showAlert(Alert.AlertType.ERROR, "Invalid Input", "Please enter a valid number!");
            }
        });

        backBtn.setOnAction(e -> {
            SessionPage sessionPage = new SessionPage();
            sessionPage.show(stage);
        });
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

    private String inputStyle() {
        return "-fx-background-radius: 10; -fx-border-radius: 10; -fx-border-color: #CBD5E1; -fx-padding: 10;";
    }

    private String labelStyle() {
        return "-fx-text-fill: #475569;";
    }

    // Keep the old start method for backward compatibility but redirect to show
    public void start(Stage stage) {
        show(stage);
    }
}
