/**
 * 
 */
package com.example.tapan.inandout.com.example.tapan.app;

import android.graphics.Bitmap;
import android.os.Parcel;
import android.os.Parcelable;

import java.io.ByteArrayOutputStream;

/**
 * @author Tapan
 *
 */
public class Product implements Parcelable{
	
	private static String TAG = Product.class.getSimpleName();
	
	private Bitmap image;
	private String id;
	private String category;
	private String name;
    private double sp;
	private double price;
    private String description;
	private int qty = 1;
	

	/**
     * @param image
     * @param id
     * @param category
     * @param name
     * @param sp
     * @param price
     */
	public Product(Bitmap image, String id, String category, String name,
                   String sp, String price, String description) {
		
		this.image = image;
		this.id = id;
		this.category = category;
		this.name = name;
        this.description = description;
        this.sp = Double.parseDouble(sp);
        this.price = Double.parseDouble(price);
	}


    protected Product(Parcel in) {
        image = in.readParcelable(Bitmap.class.getClassLoader());
        id = in.readString();
        category = in.readString();
        name = in.readString();
        sp = in.readDouble();
        price = in.readDouble();
        qty = in.readInt();
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
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeString(id);
        parcel.writeString(category);
        parcel.writeString(name);
        parcel.writeDouble(price);
        parcel.writeDouble(sp);
        parcel.writeInt(qty);
        parcel.writeString(description);
        ByteArrayOutputStream stream = new ByteArrayOutputStream();
        if ( image != null){
            image.compress(Bitmap.CompressFormat.PNG, 100, stream);
            parcel.writeByteArray(stream.toByteArray());
        }
    }

    /**
     *
     * @param id
     */
	public Product(String id) {
		// TODO Auto-generated constructor stub
		this.id = id;
		this.price = 10.00;
        this.price = 20.00;
        this.sp = 12.00;
	}

    public Product(String id, int qty){
        this.id = id;
        this.qty = qty;
        this.price = 20.00;
        this.sp = 12.00;
    }

	/**
	 * @return the image
	 */
	public Bitmap getImage() {
		return image;
	}
	/**
	 * @param image the image to set
	 */
	public void setImage(Bitmap image) {
		this.image = image;
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
	 * @return the category
	 */
	public String getCategory() {
		return category;
	}
	/**
	 * @param category the category to set
	 */
	public void setCategory(String category) {
		this.category = category;
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
	/**
	 * @return the price
	 */
	public double getPrice() {
		return price;
	}

    public double getSp(){
        return this.sp;
    }
	/**
	 * @param price the price to set
	 */
	public void setPrice(double price) {
		this.price = price;
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
	
	public boolean equals(Object p){

        if ( p instanceof Product){
            Product tmp = (Product)p;
            if ( getId().equals(tmp.getId())){
                return true;
            }

        }
        //boolean flag = false;

        return false;
	}

    public int getQty(){
        return this.qty;
    }

    public void setQty(int qty){
        this.qty = qty;
    }

	@Override
	public int describeContents() {
		return 0;
	}

	public String getDescription(){
        return this.description;
    }
}
