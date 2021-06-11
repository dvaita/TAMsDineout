package com.example.tamsdineout;

public class myOrderItems {
    private java.lang.String name;
    private java.lang.String quantity;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getQuantity() {
        return quantity;
    }

    public void setQuantity(String quantity) {
        this.quantity = quantity;
    }

    public myOrderItems(String name, String quantity) {
        this.name = name;
        this.quantity = quantity;
    }

    public myOrderItems() {
    }
}
