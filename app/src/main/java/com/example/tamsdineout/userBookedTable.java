package com.example.tamsdineout;

public class userBookedTable {
    String type;
    String date;
    String time;
    String number;
    String payment;


    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public String getNumber() {
        return number;
    }

    public void setNumber(String number) {
        this.number = number;
    }

    public userBookedTable() {
    }

    public String getPayment() {
        return payment;
    }

    public void setPayment(String payment) {
        this.payment = payment;
    }

    public userBookedTable(String type, String date, String time, String number, String payment) {
        this.type = type;
        this.date = date;
        this.time = time;
        this.number = number;
        this.payment=payment;
    }
}
