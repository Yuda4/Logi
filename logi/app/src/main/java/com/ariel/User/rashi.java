package com.ariel.User;

public class rashi {

	public static void main(String[] args) {
		// TODO Auto-generated method stub
		User logi = new User("email1", "name11", "0546002807");
		System.out.println(logi.toString() + " "+ logi.getUSERID());
		
		Customer yuda = new Customer("email :)", "name", "0546500907", "address", "deliveryInfo");
		Customer tammy = new Customer("Tammy", "tam@gmail.com", "0526752215", "TLV", "DINFO");
		System.out.println(yuda.toString()+ " " + yuda.getUSERID());
		System.out.println(tammy.toString() + " " + tammy.getUSERID());
	}

}
