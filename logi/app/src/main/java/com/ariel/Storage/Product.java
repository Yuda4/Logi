package com.ariel.Storage;

import android.os.Parcel;
import android.os.Parcelable;

public class Product implements Parcelable {
	private String product_id;
	private String name, description;

	public String getProduct_id() {
		return product_id;
	}

	public void setProduct_id(String product_id) {
		this.product_id = product_id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public Product() {
		// Default constructor required for calls to DataSnapshot.getValue(User.class)
	}

	public Product(String product_id, String name, String description) {
		this.product_id = product_id;
		this.name = name;
		this.description = description;
	}

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeString(product_id);
        parcel.writeString(name);
        parcel.writeString(description);
    }

	protected Product(Parcel in) {
		product_id = in.readString();
		name = in.readString();
		description = in.readString();
	}

	public static final Creator<Product> CREATOR = new Creator<Product>() {
		@Override
		public Product createFromParcel(Parcel in) {
			return new Product(in);
		}

		@Override
		public Product[] newArray(int size) {
			return new Product[size];
		}
	};


	@Override
	public int describeContents() {
		return 0;
	}


}
