package com.kh_sof_dev.chris_fries.Clasess;

public class users {
    private  String name,phone,id,address;
    private  String nb;

    public String getNb() {
        return nb;
    }

    public void setNb(String nb) {
        this.nb = nb;
    }

    public users(String name, String phone, String nb) {
        this.name = name;
        this.phone = phone;
        this.nb = nb;
    }

    public users() {
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }
}
