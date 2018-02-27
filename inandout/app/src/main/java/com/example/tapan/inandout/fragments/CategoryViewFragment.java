package com.example.tapan.inandout.fragments;

import android.app.ProgressDialog;
import android.content.Context;
import android.graphics.Paint;
import android.os.AsyncTask;
import android.os.Bundle;
import android.provider.Settings;
import android.support.v4.app.Fragment;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.tapan.inandout.MainActivity;
import com.example.tapan.inandout.R;
import com.example.tapan.inandout.com.example.tapan.app.MainApplication;
import com.example.tapan.inandout.com.example.tapan.app.Product;

import org.ksoap2.SoapEnvelope;
import org.ksoap2.serialization.SoapObject;
import org.ksoap2.serialization.SoapPrimitive;
import org.ksoap2.serialization.SoapSerializationEnvelope;
import org.ksoap2.transport.HttpTransportSE;
import org.xmlpull.v1.XmlPullParserException;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutionException;

/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link CategoryViewFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link CategoryViewFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class CategoryViewFragment extends Fragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    private static final String TAG;

    static {
        TAG = MainFragment.class.getSimpleName();
    }

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;
    private ArrayList<Product> products;
    RecyclerView recyclerView;

    private OnFragmentInteractionListener mListener;

    public CategoryViewFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment CategoryViewFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static CategoryViewFragment newInstance(String param1, String param2, ArrayList<Product> cat) {
        CategoryViewFragment fragment = new CategoryViewFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        args.putParcelableArrayList("Product",cat);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
            products = getArguments().getParcelableArrayList("Product");
        }
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
    }

    @Override
    public void onStart() {
        super.onStart();

        if (getArguments() != null){
            products = getArguments().getParcelableArrayList("Product");
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment

        /*String textCat = null;
        if (getArguments() != null){
            textCat = getArguments().getString("catName");

        }*/
        View view = inflater.inflate(R.layout.fragment_category_view, container, false);
        recyclerView = (RecyclerView) view.findViewById(R.id.fragment_category_view_recycler_view);

        TextView tv = (TextView) view.findViewById(R.id.fragment_category_text);
        //LinearLayout ll = (LinearLayout) view.findViewById(R.id.fragment_category_ll);
        tv.setText(mParam1);
        GridLayoutManager gridLayoutManager = new GridLayoutManager(getContext().getApplicationContext(), 1);
        recyclerView.setLayoutManager(gridLayoutManager);
        CategoryBookRecyclerAdapter categoryBookRecyclerAdapter = new CategoryBookRecyclerAdapter(getActivity().getApplicationContext(),products);
        recyclerView.setAdapter(categoryBookRecyclerAdapter);
        /*
        ArrayList<String> list = new ArrayList<String>();
        list.add("Product Details 1");
        list.add("Product Details 2");
        list.add("Product Details 3");

        GridLayoutManager gridLayoutManager = new GridLayoutManager(getContext().getApplicationContext(), 1);
        recyclerView.setLayoutManager(gridLayoutManager);
        CategoryBookRecyclerAdapter categoryBookRecyclerAdapter = new CategoryBookRecyclerAdapter(getActivity().getApplicationContext(),list);
        recyclerView.setAdapter(categoryBookRecyclerAdapter);
        //View view1 = LayoutInflater.from(getActivity().getApplication()).inflate(R.layout.category_items_for_view, null);

        //ll.addView(view1);*/

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

        if ( getArguments() != null ){
            products = getArguments().getParcelableArrayList("Product");
        }
    }

    @Override
    public void onResume() {
        super.onResume();

        if ( getArguments() != null ){
            products = getArguments().getParcelableArrayList("Product");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
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
    }

    public void setmParam1(String param1){
        this.mParam1 = param1;
    }

    public static String getTAG(){
        return TAG;
    }

    private void onImageClick(String productId){
        mListener.onProductImageClick(productId);

    }




    class CategoryBookRecyclerAdapter extends RecyclerView.Adapter<CategoryBookRecyclerAdapter.ViewHolder> {

        //private static int COUNT_CACHE_VIEW = 0;
        //private static final String ADAPTER_TAG = CategoryBookRecyclerAdapter.class.getSimpleName();
        private List<Product> dataSet;
        private Context context;
        //private int layoutResourceId;

        public CategoryBookRecyclerAdapter(Context context, List<Product> dataSet){
            this.context = context;
            this.dataSet = dataSet;
            //this.layoutResourceId = layoutResourceId;
        }


        @Override
        public int getItemCount() {
            // TODO Auto-generated method stub
            return dataSet.size();
        }

        @Override
        public long getItemId(int position) {
            // TODO Auto-generated method stub
            return position;
        }

        @Override
        public void onBindViewHolder(ViewHolder holder, final int arg1) {
            // TODO Auto-generated method stub
            //holder.price.setText(dataSet.get(arg1));

            holder.productDetails.setText(dataSet.get(arg1).getName());
            final String productID = dataSet.get(arg1).getId();
            String rsPriceSymbol = context.getResources().getString(R.string.price);
            holder.price.setText( rsPriceSymbol + " " +String.valueOf(dataSet.get(arg1).getPrice()) );
            holder.price.setPaintFlags( holder.price.getPaintFlags() | Paint.STRIKE_THRU_TEXT_FLAG);
            String rsSPSymbol = context.getResources().getString(R.string.sp);
            holder.sp.setText(rsSPSymbol + " " +String.valueOf(dataSet.get(arg1).getSp()) );
            holder.productImage.setImageBitmap(dataSet.get(arg1).getImage());

            holder.productImage.setOnClickListener(new ProductImageListener(productID));
            //Bitmap bmp = BitmapFactory.decodeResource(context.getResources(), R.drawable.ibpsfour);
            //holder.customImage.setImageBitmap(bmp);
            //holder.customImage.setOnClickListener(new View.OnClickListener() {

               /* @Override
                public void onClick(View v) {
                    // TODO Auto-generated method stub
                    Toast.makeText(context, "Book details to be opened in new intent...", Toast.LENGTH_SHORT).show();
                }
            });*/

            holder.addToCart.setOnClickListener(new View.OnClickListener() {

                @Override
                public void onClick(View v) {
                    AddToCartAsyncTask addToCartAsyncTask = new AddToCartAsyncTask();
                    try {
                        String result = addToCartAsyncTask.execute(productID).get();
                        ((MainActivity) getActivity()).setBadgeTextView(result);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    } catch (ExecutionException e) {
                        e.printStackTrace();
                    }

                }
            });

        }

        @Override
        public ViewHolder onCreateViewHolder(ViewGroup parent, int arg1) {
            // TODO Auto-generated method stub
            //Log.i(ADAPTER_TAG, "itemTV---" + ++COUNT_CACHE_VIEW);
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.category_items_for_view, null);
            ViewHolder holder = new ViewHolder(view);

            return holder;
        }

        class ViewHolder extends RecyclerView.ViewHolder{

            public TextView price;
            public TextView productDetails;
            public ImageView productImage;
            public TextView addToCart;
            //public Spinner qty;
            public TextView sp;

            //bmp, slno, mParam2, itemname, salePrice, mrp)

            public ViewHolder(View itemView) {
                super(itemView);
                productDetails = (TextView) itemView.findViewById(R.id.category_item_layout_for_view_product_product_details);
                price = (TextView) itemView.findViewById(R.id.category_item_layout_for_view_product_MRP);
                sp = (TextView) itemView.findViewById(R.id.category_item_layout_for_view_product_SP);
                //qty = (Spinner) itemView.findViewById(R.id.category_item_layout_for_view_product_qty);
                productImage = (ImageView) itemView.findViewById(R.id.category_item_layout_for_view_product_image);
                addToCart = (TextView) itemView.findViewById(R.id.category_item_layout_for_view_product_addToCart);
            }
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


    private class AddToCartAsyncTask extends AsyncTask<String, String, String> {
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
