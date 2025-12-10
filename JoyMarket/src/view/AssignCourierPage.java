package view;

import controller.AdminController;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.stage.Stage;
import model.Courier;
import model.Order;

import java.util.ArrayList;
import java.util.List;

public class AssignCourierPage {

    // =========================
    // 1. FIELD / PROPERTIES
    // =========================
    private AdminController controller = new AdminController();

    private VBox root;
    private List<Order> orders;
    private List<Courier> couriers;

    // Kita butuh simpan ComboBox & Label untuk tiap order
    private List<ComboBox<Courier>> courierDropdowns = new ArrayList<>();
    private List<Label> orderLabels = new ArrayList<>();
    private List<Button> assignButtons = new ArrayList<>();

    // =========================
    // 3. INITIATE
    // =========================
    private void initiate() {
        root = new VBox(8);
        root.setPadding(new Insets(10));

        // Load data
        orders = controller.listOpenOrders();
        couriers = controller.listCouriers();
    }


    // =========================
    // 4. SET LAYOUT
    // =========================
    private void setLayout() {
        root.getChildren().add(new Label("Assign Courier to Orders"));

        for (Order o : orders) {

            // Per Order
            HBox row = new HBox(8);

            Label lbl = new Label(
                    "Order #" + o.getId() +
                    " Customer: " + o.getCustomerId() +
                    " Status: " + o.getStatus()
            );

            ComboBox<Courier> cb = new ComboBox<>();
            cb.getItems().addAll(couriers);

            // Dropdown menampilkan nama user
            cb.setCellFactory(lv -> new ListCell<Courier>() {
                @Override
                public void updateItem(Courier item, boolean empty) {
                    super.updateItem(item, empty);
                    setText(empty || item == null ? null : item.getName());
                }
            });

            cb.setButtonCell(new ListCell<Courier>() {
                @Override
                public void updateItem(Courier item, boolean empty) {
                    super.updateItem(item, empty);
                    setText(empty || item == null ? null : item.getName());
                }
            });

            Button btnAssign = new Button("Assign");

            // Simpan komponen supaya bisa dipakai di setEventHandler()
            courierDropdowns.add(cb);
            orderLabels.add(lbl);
            assignButtons.add(btnAssign);

            row.getChildren().addAll(lbl, cb, btnAssign);
            root.getChildren().add(row);
        }
    }


    // =========================
    // 5. SET EVENT HANDLER
    // =========================
    private void setEventHandler() {

        for (int i = 0; i < orders.size(); i++) {

            Order order = orders.get(i);
            ComboBox<Courier> cb = courierDropdowns.get(i);
            Label lbl = orderLabels.get(i);
            Button btnAssign = assignButtons.get(i);

            btnAssign.setOnAction(e -> {
                Courier selected = cb.getValue();

                if (selected == null) {
                    new Alert(Alert.AlertType.INFORMATION, "Select courier").show();
                    return;
                }

                boolean ok = controller.assignCourier(order.getId(), selected.getCourierId());

                if (ok) {
                    lbl.setText(
                            "Order #" + order.getId() +
                            " assigned to " + selected.getName()
                    );

                    new Alert(Alert.AlertType.INFORMATION, "Courier Assigned Successfully").show();
                } else {
                    new Alert(Alert.AlertType.ERROR, "Assignment Failed").show();
                }
            });
        }
    }
    // =========================
    // 2. START METHOD
    // =========================
    public void start(Stage stage) {
        initiate();
        setLayout();
        setEventHandler();

        Scene sc = new Scene(root, 800, 600);
        stage.setScene(sc);
        stage.setTitle("Assign Couriers");
        stage.show();
    }
}
