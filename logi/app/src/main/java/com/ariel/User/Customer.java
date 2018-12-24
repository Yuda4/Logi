package com.ariel.User;

import android.os.Parcel;
import android.os.Parcelable;

public class Customer extends User implements Parcelable {
	
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

//	protected boolean setAddress(String address) {
//		if(address.equals("") || address.equals(" ")) {
//			System.out.println("You should enter a valid address");
//			return false;
//		}
//		this.address = address;
//		return true;
//	}

	public String toString() {
		return super.toString() + ", "+ this.address + ", " + this.deliveryInfo;
	}

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
       super.writeToParcel(parcel, i);
       parcel.writeString(address);
       parcel.writeString(deliveryInfo);
    }

    protected Customer(Parcel in) {
        super(in);
        address = in.readString();
        deliveryInfo = in.readString();
    }

    public static final Creator<Customer> CREATOR = new Creator<Customer>() {
        @Override
        public Customer createFromParcel(Parcel in) {
            return new Customer(in);
        }

        @Override
        public Customer[] newArray(int size) {
            return new Customer[size];
        }
    };

}
