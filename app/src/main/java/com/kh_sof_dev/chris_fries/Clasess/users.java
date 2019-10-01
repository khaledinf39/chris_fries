package com.kh_sof_dev.chris_fries.Clasess;

public class users {
    private  String name,phone,id,address;
    private  String nb,token;
private int request_wail_nb;
    private Double wallet;

    public Double getWallet() {
        return wallet;
    }

    public void setWallet(Double wallet) {
        this.wallet = wallet;
    }
    public int getRequest_wail_nb() {
        return request_wail_nb;
    }

    public void setRequest_wail_nb(int request_wail_nb) {
        this.request_wail_nb = request_wail_nb;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

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
