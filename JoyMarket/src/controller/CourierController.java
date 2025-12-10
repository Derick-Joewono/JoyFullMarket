package controller;

import helper.SessionManager;
import model.Courier;
import model.Order;
import repository.CourierRepository;
import repository.OrderRepository;

import java.util.List;

public class CourierController {

    private CourierRepository courierRepo = new CourierRepository();
    private OrderRepository orderRepo = new OrderRepository();

    public boolean login(String email, String password) {
        Courier courier = courierRepo.getCourierByEmail(email);

        if (courier == null) {
            return false;
        }

        // Check if courier is active
        if (!courier.isActive()) {
            return false;
        }

        // Verify password (plain text comparison for simplicity)
        if (password.equals(courier.getPassword())) {
            SessionManager.getInstance().setCurrentCourier(courier);
            return true;
        }

        return false;
    }

    public void logout() {
        SessionManager.getInstance().logout();
    }

    public List<Order> getAssignedOrders() {
        Courier current = SessionManager.getInstance().getCurrentCourier();
        if (current == null) {
            return List.of();
        }
        return orderRepo.getOrdersByCourier(current.getCourierId());
    }

    public boolean updateDeliveryStatus(int orderId, String newStatus) {
        Courier current = SessionManager.getInstance().getCurrentCourier();
        if (current == null) {
            return false;
        }

        // Validate status
        if (!isValidDeliveryStatus(newStatus)) {
            return false;
        }

        // Verify the order belongs to this courier
        Order order = orderRepo.getOrderById(orderId);
        if (order == null || order.getCourierId() == null || order.getCourierId() != current.getCourierId()) {
            return false;
        }

        return orderRepo.updateDeliveryStatus(orderId, newStatus);
    }

    /**
     * Update info kendaraan courier yang sedang login.
     */
    public boolean updateVehicleInfo(String vehicleType, String vehiclePlate) {
        Courier current = SessionManager.getInstance().getCurrentCourier();
        if (current == null) {
            return false;
        }

        if (vehicleType == null || vehicleType.trim().isEmpty()
                || vehiclePlate == null || vehiclePlate.trim().isEmpty()) {
            return false;
        }

        boolean success = courierRepo.updateVehicleInfo(
                current.getCourierId(),
                vehicleType.trim(),
                vehiclePlate.trim());

        if (success) {
            // refresh session courier with updated data
            Courier refreshed = courierRepo.getCourierById(current.getCourierId());
            if (refreshed != null) {
                SessionManager.getInstance().setCurrentCourier(refreshed);
            }
        }
        return success;
    }

    private boolean isValidDeliveryStatus(String status) {
        return status != null && 
               (status.equals("PENDING") || 
                status.equals("IN_PROGRESS") || 
                status.equals("DELIVERED"));
    }

    public Courier getCurrentCourier() {
        return SessionManager.getInstance().getCurrentCourier();
    }
}

