package com.ariel.User;

public class Courier extends User {
    private String storage_id;
    private String company;

    public Courier() { }

    public String getStorage_id() {
        return storage_id;
    }

    public void setStorage_id(String storage_id) {
        this.storage_id = storage_id;
    }

    public String getCompany() {
        return company;
    }

    public void setCompany(String company) {
        this.company = company;
    }

    public Courier(String storage_id, String company) {
        this.storage_id = storage_id;
        this.company = company;
    }

/*    public Courier(String name, String email, String phone, String storage_id, String company) {
        super(name, email, phone);
        this.storage_id = storage_id;
        this.company = company;
        this.setAddress("Please fill");
        this.setCity("Please fill");
        this.setZip_code(0L);
        this.setCountry("Please fill");
        this.setType("courier");
        this.setImage_uri("default");
    }*/
}
