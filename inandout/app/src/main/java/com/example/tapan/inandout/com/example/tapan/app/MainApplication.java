/**
 * 
 */
package com.example.tapan.inandout.com.example.tapan.app;


import android.app.Application;
import android.util.Log;

import com.example.tapan.inandout.MainActivity;

/**
 * @author Tapan
 *
 */
public class MainApplication extends Application{
	
	public static final String TAG = MainApplication.class.getSimpleName();
	private static MainApplication mInstance;
	private static User user;
	private static Cart cart;

	public static User getUser() {
		return user;
	}

	public static void setUser(User user) {
		MainApplication.user = user;
	}

	@Override
	public void onCreate(){
		super.onCreate();
		mInstance = this;
		TypefaceUtils.overrideFont(getApplicationContext(), "SERIF", FontManager.FONTAWESOME);
		//setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
		Log.d(TAG, "Application started");
		cart = new Cart();
        user = null;
	}
	
	/**
	 * 
	 * @return
	 */
	public static synchronized MainApplication getInstance(){
		return mInstance;
	}
	
	/**
	 * 
	 * @param product
	 */
	public static synchronized void addToCart(Product product){

        boolean flag = false;
        for(Product p : cart.getProducts()){
            if ( p.getId().equals(product.getId())){
                flag = true;
                product = p;
                break;
            }
        }

        if (flag){
            product.setQty(product.getQty()+1);
            cart.updateQty(product, product.getQty());
        } else	cart.addProduct(product);
	}
	
	/**
	 * 
	 * @param product
	 */
	public static synchronized void removeFromCart(Product product){
		cart.removeProduct(product);
	}
	
	public static boolean hasProducts(){
		return cart.isEmpty();
	}
	
	/**
	 * 
	 * @return
	 */
	public static Cart getCart(){
		return cart;
	}

    public static void Login(String id){
        user = new User(id);
    }

    public static void Logout(){
        user = null;
        cart = new Cart();
    }

	public static void removeProduct(Product p){
		cart.removeProduct(p);
	}

    public static synchronized void emptyCart(){
        cart.empty();
    }

	public static synchronized void updateCartQuantity(Product product, int quantity){
        cart.updateQty(product, quantity);

	}

	public void setConnectivityListener(MainActivity listener){
        BookKartBroadCastReceiver.myBroadCastReceiverListener = listener;
	}

}
