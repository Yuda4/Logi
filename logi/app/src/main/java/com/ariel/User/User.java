package com.ariel.User;

import android.os.Parcel;
import android.os.Parcelable;

public class User implements Parcelable {

	//private static long USERID = 17942;
    protected String userId;
	protected String email;
	protected String name;
	protected String phone;

	User(String name, String email, String phone){
		//this.userId = this.USERID;
		//this.USERID++;
		this.email = email;
		this.name = name;
		this.phone = phone;
		
	}

	public User() {
		// Default constructor required for calls to DataSnapshot.getValue(User.class)
	}

	// getter
	public String getEmail() {
		return this.email;
	}

	public String getName() {
		return this.name;
	}

	public String getPhone() {
		return this.phone;
	}

    public String getUserId() {
        return this.userId;
    }

    //setter
    public void setEmail(String email) {
        this.email = email;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public void setName(String name) { this.name = name; }

    public void setPhone(String phone) { this.phone = phone; }

	public String toString() {
		return (this.name + ", " + this.email + ", " + this.phone );
	}

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeString(userId);
        parcel.writeString(email);
        parcel.writeString(name);
        parcel.writeString(phone);
    }

    protected User(Parcel in) {
        userId = in.readString();
        email = in.readString();
        name = in.readString();
        phone = in.readString();
    }

    public static final Creator<User> CREATOR = new Creator<User>() {
        @Override
        public User createFromParcel(Parcel in) {
            return new User(in);
        }

        @Override
        public User[] newArray(int size) {
            return new User[size];
        }
    };
}
