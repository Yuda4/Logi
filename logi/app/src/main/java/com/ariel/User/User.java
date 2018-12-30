package com.ariel.User;

public class User {

	//private static long USERID = 17942;
    protected String userId;
	protected String email;
	protected String name;
	protected String phone;

    protected String country;
    protected String city;
    protected String address;
    protected Long zip_code;
    protected String type;
    protected String image_uri;

    public User() {
        // Default constructor required for calls to DataSnapshot.getValue(User.class)
    }

    public User(String name, String email, String phone) {
        this.name = name;
        this.email = email;
        this.phone = phone;
    }

    public String getImage_uri() {
        return image_uri;
    }

    public void setImage_uri(String image_uri) {
        this.image_uri = image_uri;

    }

    public Long getZip_code() {
        return zip_code;
    }

    public void setZip_code(Long zip_code) {
        this.zip_code = zip_code;
    }

    public String getCountry() {
        return country;
    }

    public void setCountry(String country) {
        this.country = country;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
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

}
