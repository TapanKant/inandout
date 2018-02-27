package com.example.tapan.inandout.adapter;

import android.content.Context;
import android.graphics.Bitmap;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;

import com.example.tapan.inandout.R;

import java.util.ArrayList;

/**
 * Created by Tapan on 09-03-2017.
 */

public class CustomAdapterViewFlipper extends BaseAdapter {

    private Context context;
    private ArrayList<Bitmap> imageList;
    LayoutInflater layoutInflater;


    public CustomAdapterViewFlipper(Context context, ArrayList<Bitmap> imageList){
        this.context = context;
        this.imageList = imageList;
        layoutInflater = LayoutInflater.from(context);

    }
    @Override
    public int getCount() {
        return imageList.size();
    }

    @Override
    public Object getItem(int i) {
        return null;
    }

    @Override
    public long getItemId(int i) {
        return 0;
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
        view = layoutInflater.inflate(R.layout.flipper_layout, viewGroup, false);
        ImageView imageView = (ImageView) view.findViewById(R.id.flipper_layout_image_view);
        imageView.setImageBitmap(imageList.get(i));
        return view;
    }
}
