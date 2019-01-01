package com.ariel.DeliverySystem;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class Delivery {

	private String delivery_id;
    private String product_id;
    private String courier_email;
    private String courier_phone;
    private String customer_email;
    private String customer_phone;
    private String address;
    private String storage_id;
	private String date;
	private String status;
    private String city;
    private String company_name;

    public Delivery() {
		// Default constructor required for calls to DataSnapshot.getValue(User.class)
	}

    public String getDelivery_id() {
        return delivery_id;
    }

    public void setDelivery_id(String delivery_id) {
        this.delivery_id = delivery_id;
    }

    public String getCourier_email() {
        return courier_email;
    }

    public void setCourier_email(String courier_email) {
        this.courier_email = courier_email;
    }

    public String getCourier_phone() {
        return courier_phone;
    }

    public void setCourier_phone(String courier_phone) {
        this.courier_phone = courier_phone;
    }

    public String getCustomer_email() {
        return customer_email;
    }

    public void setCustomer_email(String customer_email) {
        this.customer_email = customer_email;
    }

    public String getCustomer_phone() {
        return customer_phone;
    }

    public void setCustomer_phone(String customer_phone) {
        this.customer_phone = customer_phone;
    }

    public String getProduct_id() {
        return product_id;
    }

    public void setProduct_id(String product_id) {
        this.product_id = product_id;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getDate() {
        /*DateFormat dateFormat = new SimpleDateFormat("dd-MM-yy hh:mm");
            return dateFormat.format(this.date);*/
        return date;
    }

    public void setDate(String date) {
        /*DateFormat dateFormat = new SimpleDateFormat("dd-MM-yy hh:mm");
        try {
            this.date = dateFormat.parse(date);
        } catch (ParseException e) {
            e.printStackTrace();
        }*/
        this.date = date;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getStorage_id() {
        return storage_id;
    }

    public void setStorage_id(String storage_id) {
        this.storage_id = storage_id;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getCompany_name() {
        return company_name;
    }

    public void setCompany_name(String company_name) {
        this.company_name = company_name;
    }
}
