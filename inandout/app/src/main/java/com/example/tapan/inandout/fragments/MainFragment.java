package com.example.tapan.inandout.fragments;

import android.app.ProgressDialog;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Paint;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.provider.Settings;
import android.support.v4.app.Fragment;
import android.telephony.CellInfo;
import android.telephony.TelephonyManager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterViewFlipper;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.tapan.inandout.MainActivity;
import com.example.tapan.inandout.R;
import com.example.tapan.inandout.adapter.CustomAdapterViewFlipper;
import com.example.tapan.inandout.com.example.tapan.app.MainApplication;
import com.example.tapan.inandout.com.example.tapan.app.Product;
import com.example.tapan.inandout.utils.Category;
import com.example.tapan.inandout.utils.MyImages;

import org.ksoap2.SoapEnvelope;
import org.ksoap2.serialization.SoapObject;
import org.ksoap2.serialization.SoapPrimitive;
import org.ksoap2.serialization.SoapSerializationEnvelope;
import org.ksoap2.transport.HttpTransportSE;
import org.xmlpull.v1.XmlPullParserException;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.StringTokenizer;
import java.util.concurrent.ExecutionException;

/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link MainFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link MainFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class MainFragment extends Fragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String TAG;

    static {
        TAG = MainFragment.class.getSimpleName();
    }

    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    private OnFragmentInteractionListener mListener;

    ArrayList<Bitmap> imageList;// = new ArrayList<>();
    private ArrayList<Category> categories;
    private ArrayList<Product> hotProducts;
    private ArrayList<MyImages> bannerImages;

    public MainFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @param cat
     * @return A new instance of fragment MainFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static MainFragment newInstance(String param1, String param2, ArrayList<Category> cat,
                                           ArrayList<Product> hotProducts, ArrayList<MyImages> bannerImages) {
        MainFragment fragment = new MainFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        args.putParcelableArrayList("Category", cat);
        args.putParcelableArrayList("HotProducts", hotProducts);
        args.putParcelableArrayList("Banner", bannerImages);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onStart() {
        super.onStart();
        //Bundle bundle = getArguments();
        /*categories = bundle.getParcelableArrayList("Category");
        if (categories == null){
            Toast.makeText(getActivity().getApplicationContext(), "category is null", Toast.LENGTH_SHORT).show();
        }*/
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //ArrayList<Category> categoryParcel = null;
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
            //categories = new ArrayList<>();
            categories = getArguments().getParcelableArrayList("Category");
            hotProducts = getArguments().getParcelableArrayList("HotProducts");
            bannerImages = getArguments().getParcelableArrayList("Banner");
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        if (getArguments() != null ){
            categories = getArguments().getParcelableArrayList("Category");
            hotProducts = getArguments().getParcelableArrayList("HotProducts");
            bannerImages = getArguments().getParcelableArrayList("Banner");
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.main_layout, container, false);
        view.setClickable(true);
        LinearLayout hll = (LinearLayout) view.findViewById(R.id.main_layout_category_scrollview_LL);
        //categories = new ArrayList<>();

        for(Category category: categories){

            View v = LayoutInflater.from(getActivity()).inflate(R.layout.category_layout, null);
            final TextView t = (TextView) v.findViewById(R.id.category_text);
            //final ImageView iv = (ImageView) v.findViewById(R.id.category_image);
            t.setText(category.getCategoryName());
            CategoryViewListener categoryViewListener = new CategoryViewListener(t.getText().toString(), category.getSlno());
            t.setOnClickListener(categoryViewListener);
            hll.addView(v);
        }


        AdapterViewFlipper banner = (AdapterViewFlipper) view.findViewById(R.id.main_page_banner);
        imageList = new ArrayList<>();
        for(MyImages images : bannerImages){
            imageList.add(images.getImage());
        }
        CustomAdapterViewFlipper customAdapterViewFlipper = new CustomAdapterViewFlipper(getActivity().getApplicationContext(), imageList);
        banner.setAdapter(customAdapterViewFlipper);
        banner.startFlipping();

        TextView txtviewAll11 = (TextView) view.findViewById(R.id.main_layout_strip_one_category);
        txtviewAll11.setText("Hot Products");

        TextView proDetails;
        ImageView prodImage;
        TextView mrp;
        TextView sp;
        TextView addToCart;


        LinearLayout cat1 = (LinearLayout) view.findViewById(R.id.category_view_one);
        for(Product product: hotProducts){
            final View v = LayoutInflater.from(getActivity().getApplicationContext()).inflate(R.layout.category_item_layout, null);

            proDetails = (TextView) v.findViewById(R.id.category_item_layout_product_prodDetails);
            proDetails.setText(product.getName());
            mrp = (TextView) v.findViewById(R.id.category_item_layout_product_mrp);
            mrp.setPaintFlags( mrp.getPaintFlags() | Paint.STRIKE_THRU_TEXT_FLAG);
            String rsPriceSymbol = getActivity().getApplicationContext().getResources().getString(R.string.price);
            mrp.setText( rsPriceSymbol + " " +String.valueOf( product.getPrice() ));
            sp = (TextView) v.findViewById(R.id.category_item_layout_product_sp);
            String rsSPSymbol = getActivity().getApplicationContext().getResources().getString(R.string.sp);
            sp.setText( rsSPSymbol + " " +String.valueOf(product.getSp()) );

            final String productId = product.getId();

            prodImage = (ImageView) v.findViewById(R.id.category_item_layout_product_image);
            prodImage.setImageBitmap(product.getImage());

            prodImage.setOnClickListener(new ProductImageListener(productId));
            addToCart = (TextView) v.findViewById(R.id.category_item_layout_product_addToCart);
            addToCart.setOnClickListener(new AddToCartListener(productId) );
            cat1.addView(v);

        }

        return view;
    }

    // TODO: Rename method, update argument and hook method into UI event
    public void onButtonPressed(String uri) {
        if (mListener != null) {
            mListener.onFragmentInteraction(uri);
        }
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnFragmentInteractionListener) {
            mListener = (OnFragmentInteractionListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnFragmentInteractionListener");
        }
        if (getArguments() != null){
            categories = getArguments().getParcelableArrayList("Category");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    private void onImageClick(String productId){
        mListener.onProductImageClick(productId);
    }

    public void onCategoryClick(String categoryName, String slno){
        mListener.onCategoryClick(categoryName, slno);
    }

    public ArrayList<Category> getCategories() {
        return categories;
    }

    public void setCategories(ArrayList<Category> categories) {
        this.categories = categories;
    }

    /**
     * This interface must be implemented by activities that contain this
     * fragment to allow an interaction in this fragment to be communicated
     * to the activity and potentially other fragments contained in that
     * activity.
     * <p>
     * See the Android Training lesson <a href=
     * "http://developer.android.com/training/basics/fragments/communicating.html"
     * >Communicating with Other Fragments</a> for more information.
     */
    public interface OnFragmentInteractionListener {
        // TODO: Update argument type and name
        void onFragmentInteraction(String uri);
        void onProductImageClick(String productId);
        void onCategoryClick(String categoryName, String slno);
    }

    public static String getTAG(){
        return TAG;
    }

    class AddToCartListener implements View.OnClickListener{

        private String productID;
        private MainActivity mainActivity;

        public AddToCartListener(String productID){
            this.productID = productID;

            if (getActivity() instanceof MainActivity){
                this.mainActivity = (MainActivity) getActivity();
            }
        }
        @Override
        public void onClick(View view) {

            String cartCount = null;
            AddToCartAsyncTask addToCartAsyncTask = new AddToCartAsyncTask();
            try {
                cartCount = addToCartAsyncTask.execute(productID).get();
            } catch (InterruptedException e) {
                e.printStackTrace();
            } catch (ExecutionException e) {
                e.printStackTrace();
            }
            if ( mainActivity!=null ){
                mainActivity.updateCartCounter(cartCount);
            }
            //Toast.makeText(getActivity().getApplicationContext(), "Total Number of Products in Cart is " + mainApplication.getCart().getProductCount(), Toast.LENGTH_SHORT).show();
        }
    }

    class ProductImageListener implements  View.OnClickListener{
        private String productId;
        public ProductImageListener(String productId){
            this.productId = productId;
        }
        @Override
        public void onClick(View view) {
            onImageClick(productId);
        }
    }

    class CategoryViewListener implements View.OnClickListener{
        private String categoryName;
        private String slno;

        public CategoryViewListener(String categoryName, String slno){
            this.categoryName = categoryName;
            this.slno = slno;
        }

        @Override
        public void onClick(View view) {
            onCategoryClick(categoryName, slno);
        }
    }

    private class AddToCartAsyncTask extends AsyncTask<String, String, String>{
        private String result;
        ProgressDialog progressDialog;

        @Override
        protected String doInBackground(String... strings) {

            SoapObject request = new SoapObject("http://rajdhaniinandout.com/", "AddTocart");
            request.addProperty("stritemcode", strings[0]);
            request.addProperty("strquantity", "1");

            if ( MainApplication.getUser() != null){
                request.addProperty("struniqid",MainApplication.getUser().getPhone());
            } else{
                String unique_id = Settings.Secure.getString(getActivity().getApplicationContext().getContentResolver(), Settings.Secure.ANDROID_ID);
                //objuniqid.setUniqueid(unique_id);
                String imie = unique_id;
                //String imie = telephonyManager.getDeviceId();
                //Log.d("IMIE3", imie);
                request.addProperty("struniqid", imie);
            }

            SoapSerializationEnvelope envelope = new SoapSerializationEnvelope(SoapEnvelope.VER11);
            envelope.setOutputSoapObject(request);
            envelope.dotNet = true;
            HttpTransportSE ht = new HttpTransportSE("http://rajdhaniinandout.com/inoutapijson.asmx");
            try {
                ht.call("http://rajdhaniinandout.com/AddTocart", envelope);
                final SoapPrimitive soapPrimitive = (SoapPrimitive) envelope.getResponse();
                result = soapPrimitive.toString();
            } catch (IOException e) {
                e.printStackTrace();
            } catch (XmlPullParserException e) {
                e.printStackTrace();
            }
            Log.d("AddToCartTapan", result);

            String str = result.replace('"', ' ');
            result = str.trim();
            return result;
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            progressDialog = ProgressDialog.show(getActivity(), "", "Item is being added to cart...");
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
            //progressDialog.setMessage("Item has been added");
            progressDialog.dismiss();

            //result = s;
        }
    }
}
