package com.example.tapan.inandout.utils;

import android.graphics.Bitmap;
import android.os.Parcel;
import android.os.Parcelable;

import java.io.ByteArrayOutputStream;

/**
 * Created by Tapan on 08-04-2017.
 */

public class MyImages implements Parcelable {

    private Bitmap image;

    public MyImages(Bitmap bitmap){
        this.image = bitmap;
    }

    protected MyImages(Parcel in) {
        image = in.readParcelable(Bitmap.class.getClassLoader());
    }

    public static final Creator<MyImages> CREATOR = new Creator<MyImages>() {
        @Override
        public MyImages createFromParcel(Parcel in) {
            return new MyImages(in);
        }

        @Override
        public MyImages[] newArray(int size) {
            return new MyImages[size];
        }
    };

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        ByteArrayOutputStream stream = new ByteArrayOutputStream();
        if ( image != null){
            image.compress(Bitmap.CompressFormat.PNG, 100, stream);
            parcel.writeByteArray(stream.toByteArray());
        }
    }

    public Bitmap getImage() {
        return image;
    }

    public void setImage(Bitmap image) {
        this.image = image;
    }
}
