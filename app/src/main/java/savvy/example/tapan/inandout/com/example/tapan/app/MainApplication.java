/**
 * 
 */
package savvy.example.tapan.inandout.com.example.tapan.app;


import android.app.Activity;
import android.app.Application;
import android.database.SQLException;
import android.os.AsyncTask;
import android.util.Log;

import com.android.volley.RequestQueue;
import com.android.volley.toolbox.ImageLoader;
import com.android.volley.toolbox.Volley;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import savvy.example.tapan.inandout.MainActivity;
import savvy.example.tapan.inandout.SplashActivity;
import savvy.example.tapan.inandout.utils.*;

/**
 * @author Tapan
 *
 */
public class MainApplication extends Application{
	
	public static final String TAG = MainApplication.class.getSimpleName();
	private static MainApplication mInstance;
	private static User user;
	private static Cart cart;
	private List<String> mainCategoryListTitle;
    private ArrayList<savvy.example.tapan.inandout.utils.Category> categories;
    private String strKartCount;
    private ArrayList<Product> hotProducts;
    private Map<String, List<String>> mainCategoryList;
    private Map<String, List<String>> itemCodes;
    private ArrayList<String> bannerImages;
    private RequestQueue mRequestQueue;
    private ImageLoader mImageLoader;

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
        //checkLocalDatabase();
    }

    public void checkLocalDatabase(){
        /*try{
            bannerImages = localDatabase.getBannerImages();
            Log.d("TapanKS", "" + (bannerImages == null));
            mainCategoryListTitle = localDatabase.getMainCategoryListTitle();
            categories = localDatabase.getCategories();
            strKartCount = localDatabase.getStrKartCount();
            hotProducts = localDatabase.getHotProducts();
            mainCategoryList = localDatabase.getMainCategoryList();
            itemCodes = localDatabase.getItemCodes();
        } catch(SQLException e){
            Log.d("MainAppT", e.getMessage());
        }*/
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
        KartBroadCastReceiver.myBroadCastReceiverListener = listener;
	}

    public List<String> getMainCategoryListTitle() {
        return mainCategoryListTitle;
    }

    public void setMainCategoryListTitle(List<String> mainCategoryListTitle) {
        this.mainCategoryListTitle = mainCategoryListTitle;
    }

    public ArrayList<savvy.example.tapan.inandout.utils.Category> getCategories() {
        return categories;
    }

    public void setCategories(ArrayList<savvy.example.tapan.inandout.utils.Category> categories) {
        this.categories = categories;
    }

    public String getStrKartCount() {
        return strKartCount;
    }

    public void setStrKartCount(String strKartCount) {
        this.strKartCount = strKartCount;
    }

    public ArrayList<Product> getHotProducts() {
        return hotProducts;
    }

    public void setHotProducts(ArrayList<Product> hotProducts) {
        this.hotProducts = hotProducts;
    }

    public Map<String, List<String>> getMainCategoryList() {
        return mainCategoryList;
    }

    public void setMainCategoryList(Map<String, List<String>> mainCategoryList) {
        this.mainCategoryList = mainCategoryList;
    }

    public Map<String, List<String>> getItemCodes() {
        return itemCodes;
    }

    public void setItemCodes(Map<String, List<String>> itemCodes) {
        this.itemCodes = itemCodes;
    }

    public ArrayList<String> getBannerImages() {
        return bannerImages;
    }

    public void setBannerImages(ArrayList<String> bannerImages) {
        this.bannerImages = bannerImages;
    }

    public RequestQueue getmRequestQueue() {
        if ( mRequestQueue == null){
            mRequestQueue = Volley.newRequestQueue(getApplicationContext());
        }
        return this.mRequestQueue;
    }

    public void setmRequestQueue(RequestQueue mRequestQueue) {
        this.mRequestQueue = mRequestQueue;
    }

    public ImageLoader getmImageLoader() {
        getmRequestQueue();
        if (mImageLoader == null){
            mImageLoader = new ImageLoader(this.mRequestQueue, new LruBitmapCache());
        }
        return this.mImageLoader;
    }

    public void setmImageLoader(ImageLoader mImageLoader) {
        this.mImageLoader = mImageLoader;
    }
}
