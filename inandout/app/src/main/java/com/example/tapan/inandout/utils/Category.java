package com.example.tapan.inandout.utils;

import android.graphics.Bitmap;
import android.os.Parcel;
import android.os.Parcelable;

import java.io.ByteArrayOutputStream;

/**
 * Created by Tapan on 05-04-2017.
 */

public class Category implements Parcelable {

    private String categoryName;
    private String slno;
    private String categoryImage;
    private Bitmap categoryBitmapImage;

    public Category(String slno, String categoryName, Bitmap categoryBitmapImage){
        this.slno = slno;
        this.categoryName = categoryName;
        this.categoryBitmapImage = categoryBitmapImage;
    }

    public String getSlno(){
        return this.slno;
    }

    public Category(String slno, String categoryName){
        this.slno = slno;
        this.categoryName = categoryName;
        //this.categoryBitmapImage = categoryBitmapImage;
    }

    public Bitmap getCategoryBitmapImage() {
        return categoryBitmapImage;
    }

    public void setCategoryBitmapImage(Bitmap categoryBitmapImage) {
        this.categoryBitmapImage = categoryBitmapImage;
    }

    public String getCategoryImage() {
        return categoryImage;
    }

    public void setCategoryImage(String categoryImage) {
        this.categoryImage = categoryImage;
    }

    public String getCategoryName() {
        return categoryName;
    }

    public void setCategoryName(String categoryName) {
        this.categoryName = categoryName;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeString(categoryName);
        parcel.writeString(slno);
        //parcel.writeString(categoryImage);
        ByteArrayOutputStream stream = new ByteArrayOutputStream();
        if ( categoryBitmapImage != null){
            categoryBitmapImage.compress(Bitmap.CompressFormat.PNG, 100, stream);
            parcel.writeByteArray(stream.toByteArray());
        }
    }

    public Category(Parcel in){
        categoryName = in.readString();
        slno = in.readString();
        //categoryImage = in.readString();
        /*byte[] bytes = null;
        in.readByteArray(bytes);
        if (bytes != null){
            categoryBitmapImage = BitmapFactory.decodeByteArray(bytes, 0, bytes.length);
        }*/
    }

    public static final Parcelable.Creator<Category> CREATOR = new Creator<Category>() {
        @Override
        public Category createFromParcel(Parcel parcel) {
            return new Category(parcel);
        }

        @Override
        public Category[] newArray(int i) {
            return new Category[i];
        }
    };

    @Override
    public String toString() {
        return slno + " " +categoryName + " " + categoryImage;
    }
}
