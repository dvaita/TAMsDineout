package com.example.tamsdineout;

import java.util.Map;

public class placeMealOrder {

    private String price;
    private String status;
    private String date;
    private Map<String,mealString> meal;

    public String getPrice() {
        return price;
    }

    public void setPrice(String price) {
        this.price = price;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public Map<String, mealString> getMap() {
        return meal;
    }

    public void setMap(Map<String, mealString> meal) {
        this.meal = meal;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }
    public placeMealOrder(String price, String status, String date, Map<String,mealString> meal) {
        this.price = price;
        this.status = status;
        this.date=date;
        this.meal = meal;
    }

    public placeMealOrder() {
    }
}
