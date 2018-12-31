package com.ariel.User;

import android.os.Parcel;
import android.os.Parcelable;

public class Courier extends User implements Parcelable {
    private String storage_id;
    private String company;

    public Courier() { }

    protected Courier(Parcel in) {
        storage_id = in.readString();
        company = in.readString();
    }

    public static final Creator<Courier> CREATOR = new Creator<Courier>() {
        @Override
        public Courier createFromParcel(Parcel in) {
            return new Courier(in);
        }

        @Override
        public Courier[] newArray(int size) {
            return new Courier[size];
        }
    };

    public String getStorage_id() {
        return storage_id;
    }

    public void setStorage_id(String storage_id) {
        this.storage_id = storage_id;
    }

    public String getCompany() {
        return company;
    }

    public void setCompany(String company) {
        this.company = company;
    }

    public String describeName(){
        return this.name;
    }

    public Courier(String storage_id, String company) {
        this.storage_id = storage_id;
        this.company = company;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeString(name);
        parcel.writeString(storage_id);
        parcel.writeString(company);
    }

    public Courier(String name, String email, String phone, String storage_id, String company) {
        super(name, email, phone);
        this.storage_id = storage_id;
        this.company = company;

    }
}
