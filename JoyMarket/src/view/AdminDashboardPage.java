package view;

import controller.AdminController;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import model.Admin;

public class AdminDashboardPage {

    private Admin admin;
    private AdminController controller;
    // UI Components
    private VBox root;
    private Label titleLabel;
    private Button btnEditStock;
    private Button btnAssignCourier;
    private Button btnLogout;

    private Scene scene;

    public AdminDashboardPage(Admin admin) {
        this.admin = admin;
    }

    // ============================
    // 1. INITIATE
    // ============================
    private void initiate() {
        root = new VBox(15);
        root.setPadding(new Insets(20));
        root.setAlignment(Pos.CENTER);

        titleLabel = new Label("Admin: " + admin.getName());
        titleLabel.setStyle("-fx-font-size: 20px; -fx-font-weight: bold;");

        btnEditStock = new Button("Edit Product Stock & Price");
        btnEditStock.setPrefWidth(220);

        btnAssignCourier = new Button("Assign Courier to Orders");
        btnAssignCourier.setPrefWidth(220);

        btnLogout = new Button("Logout");
        btnLogout.setPrefWidth(220);

        scene = new Scene(root, 320, 240);
    }

    // ============================
    // 2. SET LAYOUT
    // ============================
    private void setLayout() {
        root.getChildren().addAll(
                titleLabel,
                btnEditStock,
                btnAssignCourier,
                btnLogout
        );
    }

    // ============================
    // 3. SET EVENT HANDLER
    // ============================
    private void setEventHandler(Stage stage) {

        btnEditStock.setOnAction(e -> {
            new AdminEditStockPage().start(new Stage());
        });

        btnAssignCourier.setOnAction(e -> {
            new AssignCourierPage().start(new Stage());
        });

        btnLogout.setOnAction(e -> {
            controller.logout();
            LoginPage loginPage = new LoginPage();
            loginPage.show(stage);
        });
    }
    // ============================
    // SHOW PAGE
    // ============================
    public void show(Stage stage) {
        initiate();
        setLayout();
        setEventHandler(stage);

        stage.setScene(scene);
        stage.setTitle("Admin Dashboard");
        stage.show();
    }
}
