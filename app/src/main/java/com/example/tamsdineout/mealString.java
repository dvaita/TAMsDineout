package com.example.tamsdineout;

public class mealString {
    String mealName;
    String  mealQty;

    public String getMealQty() {
        return mealQty;
    }

    public void setMealQty(String mealQty) {
        this.mealQty = mealQty;
    }

    public mealString() {
    }

    public mealString(String mealName, String mealQty) {
        this.mealName = mealName;
        this.mealQty = mealQty;
    }

    public String getMealList() {
        return mealName;
    }

    public void setMealList(String mealList) {
        this.mealName = mealList;
    }
}
