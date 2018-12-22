package Storage;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;
import java.util.stream.Stream;

import Storage.Product;

public class Storage {
	private static long STORAGEID = 1022;
	
	private Map<Long, Product> products = new HashMap<Long, Product>();
	
	public long getStogrageId() {
		return this.STORAGEID;
	}
	
	public String getProductDetails(Product prt) {
		return prt.getDetails();
	}
	
	public boolean addToStorage(Product prt) {
		if(products.containsKey(prt.getProductId())) return false;
		products.put(prt.getProductId(), prt);
		return true;
	}
	
	public boolean delProduct(Product prt) {
		if(!(products.containsKey(prt.getProductId()))) {
			return false;
		}
		products.remove(prt.name);
		return true;
	}
	
	public Map<Long, Product> showAll() {
		return products;
	}
	
	public boolean updateProduct(Product product, Map<String, String> newProduct) {
		
//		{
//			KEY: "name", VALUE: "pName",
//			"desc": "pDescreption",
//			"amount": "pAmount"
//		}
		
		if(newProduct.containsKey("name")) {
			product.setName(newProduct.get("name"));
		}
		if(newProduct.containsKey("desc")) {
			product.setDesc(newProduct.get("desc"));
		}
		if(newProduct.containsKey("amount")) {
			String amountString = newProduct.get("amount");
			int amountInt = Integer.parseInt(amountString);
			product.setAmount(amountInt);
		}
		return true;
	}
	
	
	
	
/*	String getProductDetails(String productName) {
		this.products.entrySet()  
	      //Returns a sequential Stream with this collection as its source  
	      .stream()  
	      //Sorted according to the provided Comparator    
	      //Performs an action for each element of this stream  
	      .forEach(System.out::println);
		
		products.keySet().stream()
		.forEach(System.out::println);
		
		return " ";
	}*/
	
	
	
	
	
public static void main(String[] args) {
	Storage logi = new Storage();
	Product book = new Product("Yehuda in wonder", "1542545", 10);
	Product watch = new Product("casio", "123", 2);
	System.out.println(book.getProductId() + "  " + watch.getProductId());
	logi.products.put(book.getProductId(), book);
	logi.products.put(watch.getProductId(), watch);
	
	System.out.println(logi.showAll());
	Map<String, String> update = new HashMap<String, String>();
	update.put("desc", "    ");
	logi.updateProduct(book,update);
	
	//System.out.println(book.getDetails());
	//System.out.println(logi.products.toString());
	System.out.println(logi.showAll());
	
}

}

