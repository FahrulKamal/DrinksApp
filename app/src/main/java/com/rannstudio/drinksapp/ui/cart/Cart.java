package com.rannstudio.drinksapp.ui.cart;

public class Cart {
    private String image;
    private String type;
    private String title;
    private String description;
    private String buyer_email;
    private String table_number;
    private int price;
    private int amount;
    private boolean already_paid;

    public Cart() {

    }

    public Cart(String image, String type, String title, String description, String buyer_email, String table_number, int price, int amount, boolean already_paid) {
        this.image = image;
        this.type = type;
        this.title = title;
        this.description = description;
        this.buyer_email = buyer_email;
        this.table_number = table_number;
        this.price = price;
        this.amount = amount;
        this.already_paid = already_paid;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getBuyer_email() {
        return buyer_email;
    }

    public void setBuyer_email(String buyer_email) {
        this.buyer_email = buyer_email;
    }

    public String getTable_number() {
        return table_number;
    }

    public void setTable_number(String table_number) {
        this.table_number = table_number;
    }

    public int getPrice() {
        return price;
    }

    public void setPrice(int price) {
        this.price = price;
    }

    public int getAmount() {
        return amount;
    }

    public void setAmount(int amount) {
        this.amount = amount;
    }

    public boolean isAlready_paid() {
        return already_paid;
    }

    public void setAlready_paid(boolean already_paid) {
        this.already_paid = already_paid;
    }
}
