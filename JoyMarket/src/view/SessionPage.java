package view;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import controller.CartController;
import controller.CustomerBalanceController;
import controller.CustomerController;
import helper.SessionManager;
import model.Customer;
import model.Order;
import repository.OrderRepository;

import java.util.List;

public class SessionPage {

    Scene scene;
    BorderPane borderPane;
    VBox mainContent;

    Button logoutBtn;
    Button viewProfileBtn;
    Button shopBtn;
    Button addBalanceBtn;
    Label balanceLabel;

    CustomerController customerController;
    Customer currentCustomer;
    CustomerBalanceController balanceController;
    CartController cartController;
    OrderRepository orderRepo;
    double currentBalance;

    private static final String ACCENT_COLOR = "#64748B";

    private void initiate() {
        borderPane = new BorderPane();
        borderPane.setPadding(new Insets(24));
        borderPane.setStyle("-fx-background-color: linear-gradient(to bottom right, #EEF2F6, #F8FAFC);");

        customerController = new CustomerController();
        currentCustomer = SessionManager.getInstance().getCurrentCustomer();
        balanceController = new CustomerBalanceController();
        cartController = new CartController();
        orderRepo = new OrderRepository();
        currentBalance = balanceController.getBalance(currentCustomer.getId());

        scene = new Scene(borderPane, 1000, 700);
    }

    private void setLayout(Stage stage) {
        if (currentCustomer == null) {
            return;
        }

        // Top bar with balance, add balance button, profile and logout buttons
        HBox topBar = new HBox();
        topBar.setPadding(new Insets(0, 0, 20, 0));
        topBar.setAlignment(Pos.CENTER_RIGHT);
        topBar.setSpacing(12);

        // Balance section in top bar
        HBox balanceBox = new HBox(8);
        balanceBox.setAlignment(Pos.CENTER);
        balanceBox.setStyle("-fx-background-color: #F1F5F9; -fx-background-radius: 8; -fx-padding: 8 12;");
        
        balanceLabel = new Label("💰 Rp " + String.format("%,.0f", currentBalance));
        balanceLabel.setStyle("-fx-font-size: 16px; -fx-font-weight: bold; -fx-text-fill: #16A34A;");
        
        addBalanceBtn = new Button("+");
        addBalanceBtn.setStyle("-fx-background-color: #16A34A; -fx-text-fill: white; -fx-font-size: 18px; -fx-font-weight: bold; -fx-background-radius: 6; -fx-min-width: 30; -fx-min-height: 30; -fx-padding: 0;");
        addBalanceBtn.setOnMouseEntered(e -> addBalanceBtn.setStyle("-fx-background-color: #22C55E; -fx-text-fill: white; -fx-font-size: 18px; -fx-font-weight: bold; -fx-background-radius: 6; -fx-min-width: 30; -fx-min-height: 30; -fx-padding: 0;"));
        addBalanceBtn.setOnMouseExited(e -> addBalanceBtn.setStyle("-fx-background-color: #16A34A; -fx-text-fill: white; -fx-font-size: 18px; -fx-font-weight: bold; -fx-background-radius: 6; -fx-min-width: 30; -fx-min-height: 30; -fx-padding: 0;"));
        addBalanceBtn.setOnAction(e -> {
            TopUpPage topUpPage = new TopUpPage();
            topUpPage.show(stage);
            // Refresh balance after returning
            refreshBalance();
        });
        
        balanceBox.getChildren().addAll(balanceLabel, addBalanceBtn);

        viewProfileBtn = new Button("👤 Profile");
        viewProfileBtn.setStyle("-fx-background-color: #334155; -fx-text-fill: white; -fx-font-size: 13px; -fx-font-weight: bold; -fx-background-radius: 8; -fx-padding: 8 16;");
        viewProfileBtn.setOnMouseEntered(e -> viewProfileBtn.setStyle("-fx-background-color: #475569; -fx-text-fill: white; -fx-font-size: 13px; -fx-font-weight: bold; -fx-background-radius: 8; -fx-padding: 8 16;"));
        viewProfileBtn.setOnMouseExited(e -> viewProfileBtn.setStyle("-fx-background-color: #334155; -fx-text-fill: white; -fx-font-size: 13px; -fx-font-weight: bold; -fx-background-radius: 8; -fx-padding: 8 16;"));

        logoutBtn = new Button("🚪 Logout");
        logoutBtn.setStyle("-fx-background-color: #E11D48; -fx-text-fill: white; -fx-font-size: 13px; -fx-font-weight: bold; -fx-background-radius: 8; -fx-padding: 8 16;");
        logoutBtn.setOnMouseEntered(e -> logoutBtn.setStyle("-fx-background-color: #F43F5E; -fx-text-fill: white; -fx-font-size: 13px; -fx-font-weight: bold; -fx-background-radius: 8; -fx-padding: 8 16;"));
        logoutBtn.setOnMouseExited(e -> logoutBtn.setStyle("-fx-background-color: #E11D48; -fx-text-fill: white; -fx-font-size: 13px; -fx-font-weight: bold; -fx-background-radius: 8; -fx-padding: 8 16;"));

        topBar.getChildren().addAll(balanceBox, viewProfileBtn, logoutBtn);

        // Main content card
        VBox card = new VBox(25);
        card.setPadding(new Insets(30));
        card.setAlignment(Pos.TOP_CENTER);
        card.setStyle("-fx-background-color: #FFFFFF; -fx-background-radius: 18; -fx-effect: dropshadow(gaussian, rgba(100,116,139,0.25), 24, 0.12, 0, 8);");

        // Welcome section
        VBox welcomeSection = new VBox(10);
        welcomeSection.setAlignment(Pos.CENTER);
        
        Label welcomeLabel = new Label("Welcome to JoyMarket!");
        welcomeLabel.setStyle("-fx-font-size: 28px; -fx-font-weight: bold; -fx-text-fill: #0F172A;");
        
        Label nameLabel = new Label("Hello, " + currentCustomer.getFull_name() + " 👋");
        nameLabel.setStyle("-fx-font-size: 16px; -fx-text-fill: #475569;");
        
        welcomeSection.getChildren().addAll(welcomeLabel, nameLabel);

        // Shop Now button with cart indicator
        VBox shopSection = new VBox(15);
        shopSection.setAlignment(Pos.CENTER);
        
        int cartItemCount = cartController.getCartItems(currentCustomer.getId()).size();
        String cartBadge = cartItemCount > 0 ? " (" + cartItemCount + ")" : "";
        
        shopBtn = new Button("🛒 Shop Now" + cartBadge);
        shopBtn.setStyle("-fx-background-color: " + ACCENT_COLOR + "; -fx-text-fill: white; -fx-font-size: 18px; -fx-font-weight: bold; -fx-background-radius: 12; -fx-padding: 16 40; -fx-min-width: 250;");
        shopBtn.setOnMouseEntered(e -> shopBtn.setStyle("-fx-background-color: #475569; -fx-text-fill: white; -fx-font-size: 18px; -fx-font-weight: bold; -fx-background-radius: 12; -fx-padding: 16 40; -fx-min-width: 250;"));
        shopBtn.setOnMouseExited(e -> shopBtn.setStyle("-fx-background-color: " + ACCENT_COLOR + "; -fx-text-fill: white; -fx-font-size: 18px; -fx-font-weight: bold; -fx-background-radius: 12; -fx-padding: 16 40; -fx-min-width: 250;"));
        
        if (cartItemCount > 0) {
            Label cartInfo = new Label("You have " + cartItemCount + " item" + (cartItemCount > 1 ? "s" : "") + " in your cart");
            cartInfo.setStyle("-fx-font-size: 14px; -fx-text-fill: #64748B;");
            shopSection.getChildren().add(cartInfo);
        }
        
        shopSection.getChildren().add(shopBtn);

        // Cart preview section (if items exist)
        if (cartItemCount > 0) {
            Button viewCartBtn = new Button("View Cart");
            viewCartBtn.setStyle("-fx-background-color: transparent; -fx-border-color: #64748B; -fx-border-radius: 8; -fx-text-fill: #64748B; -fx-font-size: 13px; -fx-font-weight: bold; -fx-padding: 8 20;");
            viewCartBtn.setOnMouseEntered(e -> viewCartBtn.setStyle("-fx-background-color: #F1F5F9; -fx-border-color: #64748B; -fx-border-radius: 8; -fx-text-fill: #64748B; -fx-font-size: 13px; -fx-font-weight: bold; -fx-padding: 8 20;"));
            viewCartBtn.setOnMouseExited(e -> viewCartBtn.setStyle("-fx-background-color: transparent; -fx-border-color: #64748B; -fx-border-radius: 8; -fx-text-fill: #64748B; -fx-font-size: 13px; -fx-font-weight: bold; -fx-padding: 8 20;"));
            
            viewCartBtn.setOnAction(e -> {
                CartPage cartPage = new CartPage();
                cartPage.show(stage);
            });
            
            shopSection.getChildren().add(viewCartBtn);
        }

        // Delivery Status Section
        VBox deliverySection = new VBox(12);
        deliverySection.setAlignment(Pos.TOP_LEFT);
        deliverySection.setPrefWidth(900);
        
        Label deliveryTitle = new Label("📦 Delivery Status");
        deliveryTitle.setStyle("-fx-font-size: 20px; -fx-font-weight: bold; -fx-text-fill: #0F172A;");
        
        List<Order> orders = orderRepo.getOrdersByCustomer(currentCustomer.getId());
        
        VBox ordersList = new VBox(10);
        ordersList.setAlignment(Pos.TOP_LEFT);
        
        if (orders.isEmpty()) {
            Label noOrdersLabel = new Label("No orders yet. Start shopping!");
            noOrdersLabel.setStyle("-fx-font-size: 14px; -fx-text-fill: #94A3B8; -fx-font-style: italic;");
            ordersList.getChildren().add(noOrdersLabel);
        } else {
            for (Order order : orders) {
                HBox orderCard = createOrderCard(order);
                ordersList.getChildren().add(orderCard);
            }
        }
        
        ScrollPane scrollPane = new ScrollPane(ordersList);
        scrollPane.setFitToWidth(true);
        scrollPane.setPrefHeight(200);
        scrollPane.setStyle("-fx-background-color: transparent; -fx-border-color: #E2E8F0; -fx-border-radius: 8;");
        scrollPane.setVbarPolicy(ScrollPane.ScrollBarPolicy.AS_NEEDED);
        scrollPane.setHbarPolicy(ScrollPane.ScrollBarPolicy.NEVER);
        
        deliverySection.getChildren().addAll(deliveryTitle, scrollPane);

        card.getChildren().addAll(welcomeSection, shopSection, deliverySection);

        mainContent = new VBox(20);
        mainContent.getChildren().addAll(topBar, card);

        borderPane.setCenter(mainContent);
    }

    private HBox createOrderCard(Order order) {
        HBox card = new HBox(15);
        card.setAlignment(Pos.CENTER_LEFT);
        card.setStyle("-fx-background-color: #F8FAFC; -fx-background-radius: 10; -fx-padding: 15; -fx-border-color: #E2E8F0; -fx-border-radius: 10;");
        card.setPrefWidth(850);

        // Order ID
        Label orderIdLabel = new Label("#" + order.getId());
        orderIdLabel.setStyle("-fx-font-size: 16px; -fx-font-weight: bold; -fx-text-fill: #0F172A; -fx-min-width: 80;");

        // Total
        Label totalLabel = new Label("Rp " + String.format("%,.0f", order.getTotal()));
        totalLabel.setStyle("-fx-font-size: 14px; -fx-text-fill: #475569; -fx-min-width: 150;");

        // Delivery Status with color
        String status = order.getDeliveryStatus() != null ? order.getDeliveryStatus() : "PENDING";
        Label statusLabel = new Label(status);
        String statusColor = "#94A3B8";
        String statusBg = "#F1F5F9";
        switch (status) {
            case "PENDING":
                statusColor = "#F59E0B";
                statusBg = "#FEF3C7";
                break;
            case "IN_PROGRESS":
                statusColor = "#3B82F6";
                statusBg = "#DBEAFE";
                break;
            case "DELIVERED":
                statusColor = "#10B981";
                statusBg = "#D1FAE5";
                break;
        }
        statusLabel.setStyle("-fx-font-size: 14px; -fx-font-weight: bold; -fx-text-fill: " + statusColor + "; -fx-background-color: " + statusBg + "; -fx-background-radius: 6; -fx-padding: 6 12; -fx-min-width: 120;");
        statusLabel.setAlignment(Pos.CENTER);

        // Order Status
        Label orderStatusLabel = new Label("Order: " + order.getStatus());
        orderStatusLabel.setStyle("-fx-font-size: 12px; -fx-text-fill: #64748B; -fx-min-width: 100;");

        card.getChildren().addAll(orderIdLabel, totalLabel, statusLabel, orderStatusLabel);
        
        return card;
    }

    private void refreshBalance() {
        if (currentCustomer != null && balanceController != null) {
            currentBalance = balanceController.getBalance(currentCustomer.getId());
            if (balanceLabel != null) {
                balanceLabel.setText("💰 Rp " + String.format("%,.0f", currentBalance));
            }
        }
    }

    private void setEventHandler(Stage stage) {
        logoutBtn.setOnAction(e -> {
            customerController.logout();
            showAlert(Alert.AlertType.INFORMATION, "Logged Out", 
                "You have been logged out successfully!");
            
            LoginPage loginPage = new LoginPage();
            loginPage.show(stage);
        });

        viewProfileBtn.setOnAction(e -> {
            ProfilePage profilePage = new ProfilePage();
            profilePage.show(stage);
        });

        shopBtn.setOnAction(e -> {
            ProductListPage productListPage = new ProductListPage();
            productListPage.show(stage);
        });
    }

    private void showAlert(Alert.AlertType alertType, String title, String message) {
        Alert alert = new Alert(alertType);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }

    public void show(Stage stage) {
        try {
            if (!SessionManager.getInstance().isCustomerLoggedIn()) {
                LoginPage loginPage = new LoginPage();
                loginPage.show(stage);
                return;
            }

            initiate();
            // Refresh balance data before layout
            if (currentCustomer != null) {
                currentBalance = balanceController.getBalance(currentCustomer.getId());
            }
            setLayout(stage);
            setEventHandler(stage);

            stage.setScene(scene);
            stage.setTitle("JoyMarket - Dashboard");
            stage.show();
        } catch (Exception e) {
            e.printStackTrace();
            System.err.println("Error loading dashboard: " + e.getMessage());
            Alert errorAlert = new Alert(Alert.AlertType.ERROR);
            errorAlert.setTitle("Error");
            errorAlert.setHeaderText(null);
            errorAlert.setContentText("Failed to load dashboard: " + e.getMessage());
            errorAlert.showAndWait();
        }
    }
}
