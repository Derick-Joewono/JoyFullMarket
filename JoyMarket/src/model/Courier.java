package model;

public class Courier {
    private int courierId;
    private String name;
    private String email;

    public Courier(int courierId, String name, String email) {
        this.courierId = courierId;
        this.name = name;
        this.email = email;
    }

    public int getCourierId() {
        return courierId;
    }

    public String getName() {
        return name;
    }

    public String getEmail() {
        return email;
    }
}


