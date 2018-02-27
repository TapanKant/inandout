package savvy.example.tapan.inandout.adapter;

import android.content.Context;
import android.graphics.Bitmap;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;

import com.android.volley.toolbox.ImageLoader;
import com.android.volley.toolbox.NetworkImageView;
import com.squareup.picasso.Picasso;

import savvy.example.tapan.inandout.R;
import savvy.example.tapan.inandout.com.example.tapan.app.MainApplication;
import savvy.example.tapan.inandout.utils.MyImages;

import java.util.ArrayList;

/**
 * Created by Tapan on 09-03-2017.
 */

public class CustomAdapterViewFlipper extends BaseAdapter {

    private Context context;
    private ArrayList<String> imageList;
    LayoutInflater layoutInflater;


    public CustomAdapterViewFlipper(Context context, ArrayList<String> imageList){
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
        NetworkImageView imageView = (NetworkImageView ) view.findViewById(R.id.flipper_layout_image_view);
        MainApplication mainApplication = MainApplication.getInstance ();
        ImageLoader imageLoader = mainApplication.getmImageLoader ();
        imageLoader.get ( imageList.get ( i ), ImageLoader.getImageListener ( imageView, R.id.cart_item_layout_image, android.R.drawable.ic_dialog_alert ) );
        imageView.setImageUrl ( imageList.get ( i ), imageLoader );
        //Picasso.with(context).load(imageList.get(i)).into(imageView);
        //imageView.setImageBitmap(imageList.get(i).getImage());
        return view;
    }
}
