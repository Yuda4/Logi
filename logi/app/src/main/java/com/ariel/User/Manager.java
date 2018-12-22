package com.ariel.User;

import android.os.Parcel;
import android.os.Parcelable;

public class Manager extends User implements Parcelable {

	public Manager() {
		// Default constructor required for calls to DataSnapshot.getValue(User.class)
	}


	Manager(String email, String name, String phone) {
		super(email, name, phone);
		
	}

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        super.writeToParcel(parcel, i);
    }

    protected Manager(Parcel in) {
        super(in);
    }

    public static final Creator<Manager> CREATOR = new Creator<Manager>() {
        @Override
        public Manager createFromParcel(Parcel in) {
            return new Manager(in);
        }

        @Override
        public Manager[] newArray(int size) {
            return new Manager[size];
        }
    };

}
