package controller;

import java.util.List;

import model.Courier;
import repository.CourierRepository;

/**
 * Handler khusus untuk data courier (bukan user/customer).
 * Fokus: operasi yang hanya relevan untuk akun courier,
 * misalnya update informasi kendaraan.
 */
public class CourierHandler {

    private final CourierRepository courierRepo = new CourierRepository();

    public Courier getCourier(int idCourier) {
        return courierRepo.getCourierById(idCourier);
    }

    public List<Courier> getAllCouriers() {
        return courierRepo.getAllCouriers();
    }

    /**
     * Update informasi kendaraan untuk courier.
     * Validasi: courier harus ada (diasumsikan role courier karena ada di tabel couriers).
     */
    public boolean editCourier(int idCourier, String vehicleType, String vehiclePlate) {
        Courier courier = courierRepo.getCourierById(idCourier);
        if (courier == null) {
            return false; // tidak ditemukan / bukan courier
        }

        // Sanitasi input sederhana
        String type = vehicleType == null ? "" : vehicleType.trim();
        String plate = vehiclePlate == null ? "" : vehiclePlate.trim();
        if (type.isEmpty() || plate.isEmpty()) {
            return false;
        }

        return courierRepo.updateVehicleInfo(idCourier, type, plate);
    }
}

