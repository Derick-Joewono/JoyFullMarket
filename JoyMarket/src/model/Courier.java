package model;

public class Courier {
    private int courierId;
    private String name;
    private String email;
    private String phone;
    private String password;
    private String status;
    private String vehicleType;
    private String vehiclePlate;

    public Courier(int courierId, String name, String email, String phone, String password, String status, String vehicleType, String vehiclePlate) {
        this.courierId = courierId;
        this.name = name;
        this.email = email;
        this.phone = phone;
        this.password = password;
        this.status = status;
        this.vehicleType = vehicleType;
        this.vehiclePlate = vehiclePlate;
    }

    public int getCourierId() {
        return courierId;
    }

    public void setCourierId(int courierId) {
        this.courierId = courierId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getVehicleType() {
        return vehicleType;
    }

    public void setVehicleType(String vehicleType) {
        this.vehicleType = vehicleType;
    }

    public String getVehiclePlate() {
        return vehiclePlate;
    }

    public void setVehiclePlate(String vehiclePlate) {
        this.vehiclePlate = vehiclePlate;
    }

    public boolean isActive() {
        return "ACTIVE".equalsIgnoreCase(this.status);
    }
}
