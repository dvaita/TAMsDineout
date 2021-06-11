package com.example.tamsdineout;

public class itemMyMeal {
    private int type;
    private java.lang.String name;
    private java.lang.String quantity;
    private java.lang.String price;

    itemMyMeal(int type, java.lang.String name,  java.lang.String quantity,  java.lang.String price) {
        this.type = type;
        this.name = name;
        this.quantity = quantity;
        this.price = price;
    }

    int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    String getQuantity() {
        return quantity;
    }

    public void setQuantity(String quantity) {
        this.quantity = quantity;
    }

    String getPrice() {
        return price;
    }

    public void setPrice(String price) {
        this.price = price;
    }
}
