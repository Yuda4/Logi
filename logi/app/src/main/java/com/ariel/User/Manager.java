package com.ariel.User;

public class Manager extends User {

	public Manager() {
		// Default constructor required for calls to DataSnapshot.getValue(User.class)
	}


	Manager(String email, String name, String phone) {
		super(email, name, phone);
		
	}

}
