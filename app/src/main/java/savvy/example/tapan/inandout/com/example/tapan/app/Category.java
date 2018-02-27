/**
 * 
 */
package savvy.example.tapan.inandout.com.example.tapan.app;

import java.util.ArrayList;

/**
 * @author Tapan
 *
 */
public class Category {
	
	private static String TAG = Category.class.getSimpleName();
	private String id;
	private ArrayList<Product> products;
	private String name;
	
	/**
	 * 
	 * @param id
	 * @param product
	 * @param name
	 */
	public Category(String id, ArrayList<Product> product, String name){
		setId(id);
		setName(name);
		setProducts(product);
	}
	
	/**
	 * 
	 */
	public Category(){
		
	}
	
	/**
	 * 
	 * @param product
	 */
	public void removeProduct(Product product){
		//removew product by id
	}
	
	/**
	 * 
	 * @param product
	 */
	public void addProduct(Product product){
		products.add(product);
	}

	/**
	 * @return the tAG
	 */
	public static String getTAG() {
		return TAG;
	}

	/**
	 * @param tAG the tAG to set
	 */
	public static void setTAG(String tAG) {
		TAG = tAG;
	}

	/**
	 * @return the products
	 */
	public ArrayList<Product> getProducts() {
		return products;
	}

	/**
	 * @param products the products to set
	 */
	public void setProducts(ArrayList<Product> products) {
		this.products = products;
	}

	/**
	 * @return the id
	 */
	public String getId() {
		return id;
	}

	/**
	 * @param id the id to set
	 */
	public void setId(String id) {
		this.id = id;
	}

	/**
	 * @return the name
	 */
	public String getName() {
		return name;
	}

	/**
	 * @param name the name to set
	 */
	public void setName(String name) {
		this.name = name;
	}

}
