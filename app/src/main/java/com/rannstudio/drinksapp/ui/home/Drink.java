package com.rannstudio.drinksapp.ui.home;

public class Drink {
    private String image;
    private String type;
    private String title;
    private String description;
    private int harga;

    public Drink() {

    }

    public Drink(String type, String image, String title, String description, int harga) {
        this.type = type;
        this.image = image;
        this.title = title;
        this.description = description;
        this.harga = harga;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
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

    public int getHarga() {
        return harga;
    }

    public void setHarga(int harga) {
        this.harga = harga;
    }
}
