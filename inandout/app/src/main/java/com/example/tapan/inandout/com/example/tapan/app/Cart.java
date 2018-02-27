/**
 * 
 */
package com.example.tapan.inandout.com.example.tapan.app;

import java.util.ArrayList;

/**
 * @author Tapan
 *
 */
public class Cart {
	
	private static final String TAG = Cart.class.getSimpleName();
	private ArrayList<Product> products;
	
	
	public int getProductCount(){
			return products.size();
	}

    public void empty(){
        products = new ArrayList<>();
    }
	
	public boolean isEmpty(){
		return products.isEmpty();
	}
	/**
	 * 
	 * @param product
	 */
	public void addProduct(Product product){

        products.add(product);
	}

	/**
	 * 
	 * @param product Product
	 */
	public void removeProduct(Product product){
		String pid = product.getId();
		
		if ( products.isEmpty() )
			return;
        ArrayList<Product> tmp = new ArrayList<Product>();

		for(Product p : products){

            if (! p.equals(product)){
                tmp.add(p);
            }
        }
        setProducts(tmp);
			
		/*
		for(int i = 0; i < products.size(); i++){
			Product p = products.get(i);
			if (pid.trim().equalsIgnoreCase(p.getId().trim())){
				products.remove(i);
			
			}
		}*/
	}

	/**
     */
	public Cart() {
		products = new ArrayList<Product>();
	}

	/**
	 * @return the tag
	 */
	public static String getTag() {
		return TAG;
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
     * updates product qty
     * @param product
     * @param qty
     */
	public void updateQty(Product product, int qty){

        String pid = product.getId();

        if ( products.isEmpty() )
            throw new RuntimeException("Cart does not contains product id ");

        ArrayList<Product> tmp = new ArrayList<Product>();

        for(Product p : products){

            if (p.equals(product)){
                tmp.add(new Product(pid, qty));
            } else {
                tmp.add(new Product(p.getId(), p.getQty()));
            }
        }
        setProducts(tmp);
    }

}
