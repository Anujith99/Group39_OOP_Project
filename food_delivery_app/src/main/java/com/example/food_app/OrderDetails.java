package com.example.food_app;

public class OrderDetails {
    public String id,home,phone;

    public OrderDetails() {
    }

    public OrderDetails(String id, String home, String phone) {
        this.id = id;
        this.home = home;
        this.phone = phone;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getHome() {
        return home;
    }

    public void setHome(String home) {
        this.home = home;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }
}

