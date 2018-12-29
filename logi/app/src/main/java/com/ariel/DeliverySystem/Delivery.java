package com.ariel.DeliverySystem;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class Delivery {
	
	private String deliveryId;
    private String productId;
    private String courierEmail;
    private String courierPhone;
    private String customerEmail;
    private String customerPhone;
	private Date date;
	private String status;

    public Delivery() {
		// Default constructor required for calls to DataSnapshot.getValue(User.class)
	}

    public String getDeliveryId() {
        return deliveryId;
    }

    public void setDeliveryId(String deliveryId) {
        this.deliveryId = deliveryId;
    }

    public String getCourierEmail() {
        return courierEmail;
    }

    public void setCourierEmail(String courierEmail) {
        this.courierEmail = courierEmail;
    }

    public String getCourierPhone() {
        return courierPhone;
    }

    public void setCourierPhone(String courierPhone) {
        this.courierPhone = courierPhone;
    }

    public String getCustomerEmail() {
        return customerEmail;
    }

    public void setCustomerEmail(String customerEmail) {
        this.customerEmail = customerEmail;
    }

    public String getCustomerPhone() {
        return customerPhone;
    }

    public void setCustomerPhone(String customerPhone) {
        this.customerPhone = customerPhone;
    }

    public String getProductId() {
        return productId;
    }

    public void setProductId(String productId) {
        this.productId = productId;
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
