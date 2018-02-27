package savvy.example.tapan.inandout.fragments;

import android.app.ProgressDialog;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Paint;
import android.os.AsyncTask;
import android.os.Bundle;
import android.provider.Settings;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.SearchView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.toolbox.ImageLoader;
import com.android.volley.toolbox.NetworkImageView;

import savvy.example.tapan.inandout.MainActivity;
import savvy.example.tapan.inandout.R;
import savvy.example.tapan.inandout.com.example.tapan.app.KartBroadCastReceiver;
import savvy.example.tapan.inandout.com.example.tapan.app.MainApplication;
import savvy.example.tapan.inandout.com.example.tapan.app.Product;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.ksoap2.SoapEnvelope;
import org.ksoap2.serialization.SoapObject;
import org.ksoap2.serialization.SoapPrimitive;
import org.ksoap2.serialization.SoapSerializationEnvelope;
import org.ksoap2.transport.HttpTransportSE;
import org.xmlpull.v1.XmlPullParserException;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutionException;

/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link SearchFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link SearchFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class SearchFragment extends Fragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";
    private static final String TAG = SearchFragment.class.getSimpleName();

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    private ArrayList<Product> products;
    private RecyclerView recyclerView;

    private OnFragmentInteractionListener mListener;

    public SearchFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment SearchFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static SearchFragment newInstance(String param1, String param2) {
        SearchFragment fragment = new SearchFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View aView = inflater.inflate(R.layout.activity_search, container, false);
        recyclerView = (RecyclerView) aView.findViewById(R.id.activity_search_recycler_view);
        final SearchView searchView = (SearchView) aView.findViewById(R.id.activity_search_search_view);
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String s) {
                //Toast.makeText(SplashActivity.this, "Search Query " + s, Toast.LENGTH_SHORT).show();
                FetchProductData fetchProductData = new FetchProductData();
                fetchProductData.execute(s);
                searchView.clearFocus();
                return false;
            }

            @Override
            public boolean onQueryTextChange(String s) {
                return false;
            }
        });

        return aView;
    }

    // TODO: Rename method, update argument and hook method into UI event
    public void onButtonPressed(String string) {
        if (mListener != null) {
            mListener.onSearchFragmentListener(string);
        }
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnFragmentInteractionListener) {
            mListener = (OnFragmentInteractionListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnShippingFragmentInteractionListener");
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
        void onSearchFragmentListener(String string);
    }

    public static String getTAG(){
        return TAG;
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
        public void onBindViewHolder(CategoryBookRecyclerAdapter.ViewHolder holder, final int arg1) {
            // TODO Auto-generated method stub
            //holder.price.setText(dataSet.get(arg1));

            holder.productDetails.setText(dataSet.get(arg1).getName());
            final String productID = dataSet.get(arg1).getId();
            String rsPriceSymbol = context.getResources().getString(R.string.price);
            holder.price.setText( rsPriceSymbol + " " +String.valueOf(dataSet.get(arg1).getPrice()) );
            holder.price.setPaintFlags( holder.price.getPaintFlags() | Paint.STRIKE_THRU_TEXT_FLAG);
            String rsSPSymbol = context.getResources().getString(R.string.sp);
            holder.sp.setText(rsSPSymbol + " " +String.valueOf(dataSet.get(arg1).getSp()) );
            //holder.productImage.setImageBitmap(dataSet.get(arg1).getImage());
            MainApplication mainApplication = MainApplication.getInstance ();
            ImageLoader imageLoader = mainApplication.getmImageLoader ();
            imageLoader.get ( dataSet.get(arg1).getImageURL (), ImageLoader.getImageListener ( holder.productImage, R.id.category_item_layout_for_view_product_image, android.R.drawable.ic_dialog_alert  ) );
            holder.productImage.setImageUrl ( dataSet.get(arg1).getImageURL (), imageLoader );
            holder.addToCart.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {

                    if (!KartBroadCastReceiver.isConnected ()){
                        Snackbar.make ( view, "No internet connection", 300 ).show ();
                        return;
                    }

                    String cartCount = null;
                    AddToCartAsyncTask addToCartAsyncTask = new AddToCartAsyncTask();
                    try {
                        cartCount = addToCartAsyncTask.execute(productID).get();
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    } catch (ExecutionException e) {
                        e.printStackTrace();
                    }
                }
            });
        }

        @Override
        public CategoryBookRecyclerAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int arg1) {
            // TODO Auto-generated method stub
            //Log.i(ADAPTER_TAG, "itemTV---" + ++COUNT_CACHE_VIEW);
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.category_items_for_view, null);
            CategoryBookRecyclerAdapter.ViewHolder holder = new CategoryBookRecyclerAdapter.ViewHolder(view);

            return holder;
        }

        class ViewHolder extends RecyclerView.ViewHolder{

            public TextView price;
            public TextView productDetails;
            public NetworkImageView productImage;
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
                productImage = (NetworkImageView) itemView.findViewById(R.id.category_item_layout_for_view_product_image);
                addToCart = (TextView) itemView.findViewById(R.id.category_item_layout_for_view_product_addToCart);
            }
        }
    }

    private class FetchProductData extends AsyncTask<String, String, String> {
        ProgressDialog progressDialog = null;
        private String result;

        @Override
        protected String doInBackground(String... strings) {

            SoapObject request = new SoapObject("http://rajdhaniinandout.com/", "SearchProduct");
            request.addProperty("strkey", strings[0]);
            Log.d("paramT", strings[0]);
            SoapSerializationEnvelope envelope = new SoapSerializationEnvelope(SoapEnvelope.VER11);
            envelope.setOutputSoapObject(request);
            envelope.dotNet = true;
            HttpTransportSE ht = new HttpTransportSE("http://rajdhaniinandout.com/inoutapijson.asmx");
            try {
                ht.call("http://rajdhaniinandout.com/SearchProduct", envelope);
                final SoapPrimitive soapPrimitive = (SoapPrimitive) envelope.getResponse();
                Log.d("line1", "Test");
                result = soapPrimitive.toString();
                //Log.d("line2", "Test");
            } catch (IOException e) {
                e.printStackTrace();
            } catch (XmlPullParserException e) {
                e.printStackTrace();
            }

            products = new ArrayList<>();

            try {

                JSONArray jsonArray;
                jsonArray = new JSONArray(result);
                //Toast.makeText(getActivity().getApplicationContext(), "Result " + jsonArray, Toast.LENGTH_SHORT).show();
                for(int i = 0; i < jsonArray.length(); i++){
                    Log.d("Tapan", "");
                    final JSONObject jsonObject = jsonArray.getJSONObject(i);
                    String slno = jsonObject.getString("itemcode");
                    String itemname = jsonObject.getString("Itemname");
                    String mrp = jsonObject.getString("MRP");
                    String salePrice = jsonObject.getString("SalePrice");
                    Log.d("JST", slno + itemname + mrp);
                    //String itemimage = jsonObject.getString("itemimage");
                    // if ( itemimage !=null){
                    String catImage = "http://rajdhaniinandout.com/"+jsonObject.getString("itemimage");
                    //URL imageURL = new URL(catImage);
                    //Bitmap bmp = bmp = BitmapFactory.decodeStream(imageURL.openConnection().getInputStream());
                    //}

                    //Bitmap bitmapImage = BitmapFactory.decodeStream()
                    //Bitmap image, String id, Category category, String name, String sp, String price
                    Product product = new Product(null, slno, strings[0], itemname, salePrice, mrp, "");
                    product.setImageURL ( catImage );
                    products.add(product);
                }

            } catch (JSONException e) {
                e.printStackTrace();
            }
            return result;
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            progressDialog = ProgressDialog.show(getActivity(), "", "Please wait...");
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
            Log.d("CatView" , s);
            progressDialog.dismiss();

            if ( s == null || s.trim().equals("")){
                Toast.makeText(getActivity().getApplicationContext(), "", Toast.LENGTH_SHORT).show();
            } else {
                GridLayoutManager gridLayoutManager = new GridLayoutManager(getActivity().getApplicationContext(), 1);
                recyclerView.setLayoutManager(gridLayoutManager);
                CategoryBookRecyclerAdapter categoryBookRecyclerAdapter = new CategoryBookRecyclerAdapter(getActivity().getApplicationContext(),products);
                recyclerView.setAdapter(categoryBookRecyclerAdapter);
            }
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
            ((MainActivity) getActivity()).setBadgeTextView(s);
            //result = s;
        }
    }
}
