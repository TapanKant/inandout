package savvy.example.tapan.inandout.fragments;

import android.content.Context;
import android.graphics.Bitmap;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.PopupWindow;
import android.widget.TextView;

import savvy.example.tapan.inandout.R;

/**
 * Created by Tapan on 08-03-2017.
 */

public class MyPopupWindowListener{

    private Context context;
    private Bitmap image;
    private String productDetails;
    private String mrp;
    private String sp;  //Selling price
    private boolean inStock;
    private View view;


    /**
     *
     * @param context
     * @param image
     * @param productDetails
     * @param mrp
     * @param sp
     * @param inStock
     */
    public MyPopupWindowListener(Context context, Bitmap image, String productDetails, String mrp, String sp, boolean inStock) {
        //super(context);
        this.context = context;
        this.image = image;
        this.productDetails = productDetails;
        this.mrp = mrp;
        this.sp = sp;
        this.inStock = inStock;
        //setContentView(getView());
    }
    public void onClick(View view) {
        new PopupWindow(getView(), ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT, true);

    }

    public View getView(){
        View tempView = null;
        LayoutInflater layoutInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        tempView = layoutInflater.inflate(R.layout.popup_window_layout, null);
        ImageView tImage = (ImageView) tempView.findViewById(R.id.popup_window_layout_product_image);
        TextView tProductDetails = (TextView) tempView.findViewById(R.id.popup_window_layout_product_prodDetails);
        TextView tmrp = (TextView) tempView.findViewById(R.id.popup_window_layout_product__mrp);
        TextView tsp = (TextView) tempView.findViewById(R.id.popup_window_layout_product_sp);
        TextView tInStock = (TextView) tempView.findViewById(R.id.popup_window_layout_product_in_stock);

        tImage.setImageBitmap(image);
        tProductDetails.setText(productDetails);
        tmrp.setText(mrp);
        tsp.setText(sp);
        if (!inStock){
            tInStock.setText("Not in Stock");
        }

        this.view = tempView;

        return view;
    }
}
