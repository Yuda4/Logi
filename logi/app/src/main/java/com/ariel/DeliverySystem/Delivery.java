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
	private Date date;
	private String status;

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
        DateFormat dateFormat = new SimpleDateFormat("dd-MM-yy hh:mm");
            return dateFormat.format(this.date);
    }

    public void setDate(String date) {
        DateFormat dateFormat = new SimpleDateFormat("dd-MM-yy hh:mm");
        try {
            this.date = dateFormat.parse(date);
        } catch (ParseException e) {
            e.printStackTrace();
        }
    }

}
