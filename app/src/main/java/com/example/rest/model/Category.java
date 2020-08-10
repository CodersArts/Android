package com.example.rest.model;

public class Category {
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    int id;
    String category;

    public Category() {
        this.id = id;
        this.category = category;
    }


}
