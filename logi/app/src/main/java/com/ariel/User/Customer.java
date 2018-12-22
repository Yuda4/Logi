package com.ariel.User;

public class Customer extends User { 
	
	protected String address;
	protected String deliveryInfo;

	public Customer() {
		// Default constructor required for calls to DataSnapshot.getValue(User.class)
	}

	Customer(String name, String email, String phone, String address, String deliveryInfo) {
		super(name, email, phone);
		this.address = address;
		this.deliveryInfo = deliveryInfo;
	}
	
	public String getAddress() {
		return address;
	}

	protected boolean setAddress(String address) {
		if(address.equals("") || address.equals(" ")) {
			System.out.println("You should enter a valid address");
			return false;
		}
		this.address = address;
		return true;
	}

	public String toString() {
		return super.toString() + ", "+ this.address + ", " + this.deliveryInfo;
	}

}
