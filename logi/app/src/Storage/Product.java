package Storage;

public class Product {
	static long PRODUCTID = 102172;
	long productId;
	String name, desc;
	int amount;
	
	Product(String name, String desc, int amount){
		this.productId = this.PRODUCTID;
		this.PRODUCTID++;
		if(setName(name)) this.name = name;
		if(setAmount(amount)) this.amount = amount;
		this.desc = desc;
	}
	
	Product (Product other){
		this.PRODUCTID = other.PRODUCTID;
		this.name = other.name;
		this.desc = other.desc;
		this.amount = other.amount;
	}
	
	public long getProductId() {
		return this.productId;
	}
	
	public String getName() {
		return this.name;
	}
	
	public String getDesc() {
		return this.desc;
	}
	
	public int getAmount() {
		return this.amount;
	}
	
	public boolean setName(String name) {
		if(name == null || name.isEmpty() || name.trim().isEmpty()) {
			System.out.println("You should enter a valid name");
			return false;
		}
		this.name = name;
		return true;
	}
	
	public boolean setDesc(String desc) {
		if(desc == null || desc.isEmpty() || desc.trim().isEmpty()) {
			System.out.println("You should enter a valid description");
			return false;
		}
		this.desc = desc;
		return true;
	}
	
	public boolean setAmount(int amount) {
		if(amount < 0) {
			return false;
		}
		this.amount = amount;
		return true;
	}
	
	public String getDetails() {
		return ("Product name: " + this.name + ", desc: "+ this.desc + ", amount: "+ this.amount);
	}
	
	public String toString() {
		return ("Product name: " + this.name + ", desc: "+ this.desc + ", amount: "+ this.amount);
	}
	
}
