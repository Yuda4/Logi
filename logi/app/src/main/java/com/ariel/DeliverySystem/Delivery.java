package com.ariel.DeliverySystem;

import java.util.Date;

public class Delivery {
	
	private long DELIVERYID = 4652822;
	long deliveryId;
	private long courierId, customerId, productId;
	private Date date;
	private String status = "Order placed";
	
	Delivery(long courierId, long customerId, long productId, Date date, String status){
		this.deliveryId = this.DELIVERYID;
		this.DELIVERYID++;
		
	}
	
	public long getDeliveryId() {
		return 1;
	}
	
	public boolean setCourierId(long courierId){
		return true;
	}
	
	public boolean setProductId(long productId){
		return true;
	}
	
	public boolean setDate(Date date) {
		if(this.date.before(date)) {
			System.out.println("Invalid date");
			return false;
		}
		return true;
	}
	
	public boolean setStatus(long deliveryId) {
		return true;
	}
	
	public String getDetails() {
		return "s";
	}
	
	
	public static void main(String[] args) {
		Date today = new Date();
		System.out.println(today);
		
	}
	

}
