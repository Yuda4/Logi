package User;
import java.io.*;
import java.util.*;

import Storage.Product;

public class User {

	private static long USERID = 17942;
	long userId;
	protected String email;
	protected String name;
	protected String phone;

	User(String name, String email, String phone){
		this.userId = this.USERID;
		this.USERID++;
		this.email = email;
		if(setName(name))this.name = name;
		if(setPhone(phone)) this.phone = phone;
		
	}

	
	public long getUSERID() {
		return this.userId;
	}

	protected void setEmail(String email) {
		this.email = email;
	}

	protected boolean setName(String name) {
		if(name == null || name.isEmpty() || name.trim().isEmpty()) {
			System.out.println("You should enter a valid name");
			return false;
		}
		this.name = name;
		return true;
	}

	protected boolean setPhone(String phone) {
		if (!onlyContainsNumbers(phone) || !(phone.length() == 10)) {
			System.out.println("10 number please");
			return false;
		}
		this.phone = phone;
		return true;
	}

	private boolean onlyContainsNumbers(String phone) {
	    try {
	        Long.parseLong(phone);
	        return true;
	    } catch (NumberFormatException ex) {
	        return false;
	    }
	} 
	
	public String getEmail() {
		return email;
	}

	public String getName() {
		return this.name;
	}

	public String getPhone() {
		return phone;
	}

	protected boolean changePassword(String pass) {
		return true;
	}

	public String toString() {
		return (this.name + ", " + this.email + ", " + this.phone );
	}


	public static void main(String[] args) {
		User yuda = new User("Yehuda Neumann", "yuda@gmail.com", "0546500907");
		User A = new User ("a","aa","0546500908");
		System.out.println(A.getName() + "   " + A.getUSERID());
		yuda.setName(" ");
		System.out.println(yuda.getName() + " " + yuda.getUSERID());
		yuda.setPhone("0546055622");
		System.out.println(yuda.getPhone() +"  " + yuda.getUSERID());
	}

}
