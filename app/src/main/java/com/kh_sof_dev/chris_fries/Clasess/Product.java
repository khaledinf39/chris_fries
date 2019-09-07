package com.kh_sof_dev.chris_fries.Clasess;

public class Product {
    private  String name,id;
    private Double weight,price;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public Product() {
    }

    public Product(String name, Double weight, Double price) {
        this.name = name;
        this.weight = weight;
        this.price = price;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Double getWeight() {
        return weight;
    }

    public void setWeight(Double weight) {
        this.weight = weight;
    }

    public Double getPrice() {
        return price;
    }

    public void setPrice(Double price) {
        this.price = price;
    }
}
