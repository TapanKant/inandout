package com.example.tapan.inandout.fragments;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.AsyncTask;
import android.os.Bundle;
import android.provider.Settings;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.tapan.inandout.MainActivity;
import com.example.tapan.inandout.R;
import com.example.tapan.inandout.com.example.tapan.app.Cart;
import com.example.tapan.inandout.com.example.tapan.app.MainApplication;
import com.example.tapan.inandout.com.example.tapan.app.Product;
import com.example.tapan.inandout.com.example.tapan.app.StringConstants;

import org.ksoap2.SoapEnvelope;
import org.ksoap2.serialization.SoapObject;
import org.ksoap2.serialization.SoapPrimitive;
import org.ksoap2.serialization.SoapSerializationEnvelope;
import org.ksoap2.transport.HttpTransportSE;
import org.xmlpull.v1.XmlPullParserException;

import java.io.IOException;
import java.util.ArrayList;

/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link CartViewFragment.OnCartViewFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link CartViewFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class CartViewFragment extends Fragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String TAG = CartViewFragment.class.getSimpleName();
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;
    private ArrayList<Product> products;

    TextView numberofproducts;
    private OnCartViewFragmentInteractionListener mListener;

    public CartViewFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment CartViewFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static CartViewFragment newInstance(String param1, String param2, ArrayList<Product> products) {
        CartViewFragment fragment = new CartViewFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        args.putParcelableArrayList("KartItems", products);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
            products = getArguments().getParcelableArrayList("KartItems");
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
            products = getArguments().getParcelableArrayList("KartItems");
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_cart_view, container, false);

        final Button checkout = (Button) view.findViewById(R.id.cart_item_layout_check_out);
        checkout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
                builder.setTitle("Checkout").setItems(R.array.checkout, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {

                        if (i == 0){
                            checkoutType(StringConstants.CHECKOUT_COD);
                        } else {
                            checkoutType(StringConstants.CHECKOUT_DR_CR);
                        }
                        //Toast.makeText(getActivity().getApplicationContext(), ""+i, Toast.LENGTH_SHORT).show();
                    }
                });/*.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        Toast.makeText(getActivity().getApplicationContext(), "OK"+i, Toast.LENGTH_SHORT).show();
                    }
                }).setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        Toast.makeText(getActivity().getApplicationContext(), "Cancel"+i, Toast.LENGTH_SHORT).show();
                    }
                });*/
                builder.create();
                builder.show();

            }
        });
        final Button continueShopping = (Button) view.findViewById(R.id.cart_item_layout_continue_shopping);
        continueShopping.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                continueYourShopping();
            }
        });
        final LinearLayout linearLayout = (LinearLayout) view.findViewById(R.id.fragment_cart_view_linear_layout);
        //numberofproducts = (TextView) view.findViewById(R.id.number_of_products);

        final Button emptyCart = (Button) view.findViewById(R.id.cart_item_layout_empty_cart);
        emptyCart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                linearLayout.removeAllViews();
                MainApplication.emptyCart();
                if (getActivity() instanceof MainActivity){
                    ((MainActivity)getActivity()).setCartCount("0");
                }
            }
        });

        //String productCount =  ""+cart.getProductCount();
        //Spinner qtySpinner = null;
        //numberofproducts.setText(productCount);
        //((MainActivity)getActivity()).setBadgeTextView(cartProducts.size());
        //Toast.makeText(getActivity().getApplicationContext(), "" + cart.getProductCount(), Toast.LENGTH_SHORT).show();
        for(final Product p : products){
            final String productId = p.getId();
            final String productQTY = Integer.toString(p.getQty());

            final View v = LayoutInflater.from(getActivity().getApplicationContext()).inflate(R.layout.cart_item_layout, null);

            final ImageView imageViewprodImage = (ImageView) v.findViewById(R.id.cart_item_layout_image);
            imageViewprodImage.setImageBitmap(p.getImage());
            final TextView textViewproductDetails = (TextView) v.findViewById(R.id.cart_item_layout_prod_details);
            textViewproductDetails.setText(p.getName());
            final TextView textViewmrp = (TextView) v.findViewById(R.id.cart_item_layout_prod_MRP);
            String tprice = getActivity().getApplicationContext().getResources().getString(R.string.price);

            textViewmrp.setText(tprice +" "+p.getPrice());
            final TextView textViewsp = (TextView) v.findViewById(R.id.cart_item_layout_prod_SP);
            String tsp = getActivity().getApplicationContext().getResources().getString(R.string.sp);
            textViewsp.setText(tsp+" "+p.getSp());
            //final EditText textQty = (EditText) v.findViewById(R.id.cart_item_layout_prod_qty);

            final TextView textQty = (TextView) v.findViewById(R.id.cart_item_layout_prod_qty);
            textQty.setText(""+p.getQty());

            ImageView remove = (ImageView) v.findViewById(R.id.cart_item_layout_prod_remove);
            remove.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    linearLayout.removeView(v);
                    if (getActivity() instanceof MainActivity){
                        RemoveFromCartAsyncTask removeFromCartAsyncTask = new RemoveFromCartAsyncTask();
                        removeFromCartAsyncTask.execute(p.getId());
                    }
                }
            });

            imageViewprodImage.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    showProductDetails(p.getId());
                }
            });
            linearLayout.addView(v);
        }
        return view;
    }

    // TODO: Rename method, update argument and hook method into UI event
    public void onButtonPressed(String uri) {
        if (mListener != null) {
            mListener.onCartViewFragmentInteractionListener(uri);
        }
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnCartViewFragmentInteractionListener) {
            mListener = (OnCartViewFragmentInteractionListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnFragmentInteractionListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    void continueYourShopping(){
        mListener.continueYourShopping();
    }

    public void checkoutType(String checkoutType){
        //Toast.makeText(getActivity().getApplicationContext(), checkoutType, Toast.LENGTH_SHORT).show();

        mListener.checkout(checkoutType);
    }



    void showProductDetails(String productId){
        mListener.showProductDetails(productId);
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
    public interface OnCartViewFragmentInteractionListener {
        // TODO: Update argument type and name
        void onCartViewFragmentInteractionListener(String uri);
        void continueYourShopping();
        void showProductDetails(String productId);
        void checkout(String checkoutType);
    }

    public static String getTAG(){
        return TAG;
    }

    class QuantityListener implements View.OnClickListener{

        public Product p;
        public int qty;
        public String tQty;

        public QuantityListener(Product p){
            this.p = p;
            this.qty = p.getQty();
        }

        @Override
        public void onClick(View view) {

            LayoutInflater inflater = LayoutInflater.from(getActivity().getApplicationContext());
            View promptView = inflater.inflate(R.layout.quantity_dialog_box_layout, null);
            final EditText txtQty = (EditText) promptView.findViewById(R.id.quantity_dialog_box_layout_qty);
            txtQty.setText(""+p.getQty());

            final AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
            builder.setView(promptView);
            builder.setTitle("Enter Quantity");
            builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialogInterface, int i) {
                    tQty = txtQty.getText().toString();
                    Toast.makeText(getActivity().getApplicationContext(), tQty, Toast.LENGTH_SHORT).show();
                    qty = Integer.parseInt(tQty);
                    //cart.updateQty(p, qty);
                }
            }).setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialogInterface, int i) {
                    dialogInterface.cancel();;
                }
            });
            builder.create();
            builder.show();
        }
    }

    private class RemoveFromCartAsyncTask extends AsyncTask<String, String, String> {
        private String result;
        ProgressDialog progressDialog;

        @Override
        protected String doInBackground(String... strings) {

            SoapObject request = new SoapObject("http://rajdhaniinandout.com/", "Deletefromcart");
            request.addProperty("itemcode", strings[0]);
            //request.addProperty("strquantity", "1");

            if ( MainApplication.getUser() != null){
                request.addProperty("struserid",MainApplication.getUser().getPhone());
            } else{
                String unique_id = Settings.Secure.getString(getActivity().getApplicationContext().getContentResolver(), Settings.Secure.ANDROID_ID);
                //objuniqid.setUniqueid(unique_id);
                String imie = unique_id;
                //String imie = telephonyManager.getDeviceId();
                //Log.d("IMIE3", imie);
                request.addProperty("struserid", imie);
            }

            SoapSerializationEnvelope envelope = new SoapSerializationEnvelope(SoapEnvelope.VER11);
            envelope.setOutputSoapObject(request);
            envelope.dotNet = true;
            HttpTransportSE ht = new HttpTransportSE("http://rajdhaniinandout.com/inoutapijson.asmx");
            try {
                ht.call("http://rajdhaniinandout.com/Deletefromcart", envelope);
                final SoapPrimitive soapPrimitive = (SoapPrimitive) envelope.getResponse();
                result = soapPrimitive.toString();
            } catch (IOException e) {
                Log.d("Remove 1", e.getMessage());
                e.printStackTrace();
            } catch (XmlPullParserException e) {
                Log.d("Remove 2", e.getMessage());
                e.printStackTrace();
            }
            Log.d("Remove", result);
            return result;
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            progressDialog = ProgressDialog.show(getActivity(), "", "Removing item...");
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
            //progressDialog.setMessage("Item has been added");
            progressDialog.dismiss();

            if (result!=null){
                onButtonPressed(s);
            }
            //result = s;
        }
    }
}
