package savvy.example.tapan.inandout.fragments;

import android.app.ProgressDialog;
import android.content.Context;
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
import android.widget.RadioButton;
import android.widget.TextView;
import android.widget.Toast;

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

import savvy.example.tapan.inandout.R;
import savvy.example.tapan.inandout.com.example.tapan.app.MainApplication;

/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link OnShippingFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link ShippingAddress#newInstance} factory method to
 * create an instance of this fragment.
 */
public class ShippingAddress extends Fragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";
    private static final String TAG = ShippingAddress.class.getSimpleName();

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    private RadioButton newAddress;
    private RadioButton existingAddress;
    private EditText userName;
    private EditText userPhone;
    private EditText userAddressLine1;
    private EditText userCity;
    private EditText userState;
    private EditText userPIN;
    private TextView payMode;
    private TextView payAmount;
    private Button confirmButton;

    private OnShippingFragmentInteractionListener mListener;

    public ShippingAddress() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment ShippingAddress.
     */
    // TODO: Rename and change types and number of parameters
    public static ShippingAddress newInstance(String param1, String param2) {
        ShippingAddress fragment = new ShippingAddress();
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
        View aView = inflater.inflate(R.layout.fragment_shipping_address, container, false);
        newAddress = (RadioButton) aView.findViewById(R.id.fragment_shipping_new_address);
        newAddress.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                clearAllFields();
            }
        });
        existingAddress = (RadioButton) aView.findViewById(R.id.fragment_shipping_existing_address);

        existingAddress.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                processExistingAddress();
            }
        });
        userName = (EditText) aView.findViewById(R.id.fragment_shipping_name);
        userPhone = (EditText) aView.findViewById(R.id.fragment_shipping_phone_number);
        userPhone.setText(MainApplication.getUser().getPhone());
        userAddressLine1 = (EditText) aView.findViewById(R.id.fragment_shipping_address_line_1);
        userCity = (EditText) aView.findViewById(R.id.fragment_shipping_city);
        userState = (EditText) aView.findViewById(R.id.fragment_shipping_state);
        userPIN = (EditText) aView.findViewById(R.id.fragment_shipping_pin_number);
        payMode = (TextView) aView.findViewById(R.id.fragment_shipping_payment_mode);
        payAmount = (TextView) aView.findViewById(R.id.fragment_shipping_payble_amount);
        confirmButton = (Button) aView.findViewById(R.id.fragment_shipping_confirm_button);

        payMode.setText(payMode.getText() + " " +mParam1);
        payAmount.setText(mParam2);
        confirmButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String isShipping = null;
                if ( newAddress.isChecked() ){
                    isShipping = "1";
                } if ( existingAddress.isChecked()){
                    isShipping = "0";
                }
                processConfirm(isShipping);
            }
        });
        return aView;
    }

    private void clearAllFields(){
        userName.setText("");
        userPhone.setText("");
        userAddressLine1.setText("");
        userPIN.setText("");
        userState.setText("");
        userCity.setText("");
    }

    private void processExistingAddress(){
        FetchShippingAddress fetchShippingAddress = new FetchShippingAddress();
        fetchShippingAddress.execute();
    }

    private void processConfirm(String isShipping){
        if (userName.getText().toString().trim().equals("")){
            userName.requestFocus();
            return;
        }

        if (userPhone.getText().toString().trim().equals("")){
            userPhone.requestFocus();
            return;
        }

        if (userAddressLine1.getText().toString().trim().equals("")){
            userAddressLine1.requestFocus();
            return;
        }

        if (userCity.getText().toString().trim().equals("")){
            userCity.requestFocus();
            return;
        }

        if (userState.getText().toString().trim().equals("")){
            userState.requestFocus();
            return;
        }

        if (userPIN.getText().toString().trim().equals("")){
            userPIN.requestFocus();
            return;
        }

        String uName = userName.getText().toString();
        String uPhone = userPhone.getText().toString();
        String address = userAddressLine1.getText().toString();
        String city = userCity.getText().toString();
        String state = userState.getText().toString();
        String pin = userPIN.getText().toString();
        String mode = mParam1;
        String amt = mParam2;

        CheckOutTask checkOutTask = new CheckOutTask();
        checkOutTask.execute(uName, uPhone, address, city, state, pin, mode, amt);
        //mListener.onShippingFragmentListener(uName, uPhone, address, city, state, pin, mode, isShipping);
    }

    // TODO: Rename method, update argument and hook method into UI event
    public void onButtonPressed(String uri) {
        if (mListener != null) {
            //mListener.onShippingFragmentListener(uri);
        }
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnShippingFragmentInteractionListener) {
            mListener = (OnShippingFragmentInteractionListener) context;
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
    public interface OnShippingFragmentInteractionListener {
        // TODO: Update argument type and name
        void onShippingFragmentListener();//String uName, String uPhone, String address, String city, String state, String pin, String mode, String isShipping);
    }

    private class FetchShippingAddress extends AsyncTask<String, String, String> {

        String result;
        ProgressDialog progressDialog;

        @Override
        protected String doInBackground(String... strings) {

            SoapObject request = new SoapObject("http://rajdhaniinandout.com/", "GetCustomer");
            if ( MainApplication.getUser() != null){
                request.addProperty("struserid",MainApplication.getUser().getPhone());
            } else{
                String unique_id = Settings.Secure.getString(getActivity().getApplicationContext().getContentResolver(), Settings.Secure.ANDROID_ID);
                String imie = unique_id;
                request.addProperty("struserid", imie);
            }

            SoapSerializationEnvelope envelope = new SoapSerializationEnvelope(SoapEnvelope.VER11);
            envelope.setOutputSoapObject(request);
            envelope.dotNet = true;
            HttpTransportSE ht = new HttpTransportSE("http://rajdhaniinandout.com/inoutapijson.asmx");
            try {
                ht.call("http://rajdhaniinandout.com/GetCustomer", envelope);
                final SoapPrimitive soapPrimitive = (SoapPrimitive) envelope.getResponse();
                result = soapPrimitive.toString();
                Log.d("shippingResult", result);
            } catch (IOException e) {
                e.printStackTrace();
            } catch (XmlPullParserException e) {
                e.printStackTrace();
            }
            return result;
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            progressDialog = ProgressDialog.show(getActivity(), "", "Loading...");
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
            progressDialog.dismiss();
            if ( s!= null){
                processResult(s);
            }

        }
    }

    private void processResult(String string){

        String customerid = null;
        String customername = null;
        String address1 = null;
        String city = null;
        String state = null;
        String pincode = null;
        String Contactno = null;
        try {
            JSONArray jsonArray = new JSONArray(string);
            for (int i = 0; i < jsonArray.length(); i++){
                JSONObject jsonObject = jsonArray.getJSONObject(i);
                customerid = jsonObject.getString("Customerid");
                customername = jsonObject.getString("customername");
                address1 = jsonObject.getString("address1");
                city = jsonObject.getString("city");
                state = jsonObject.getString("state");
                pincode = jsonObject.getString("Pincode");
                Contactno = jsonObject.getString("Contactno");
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }

        userName.setText(customername);
        userPhone.setText(Contactno);
        userAddressLine1.setText(address1);
        userCity.setText(city);
        userState.setText(state);
        userPIN.setText(pincode);
        /*private RadioButton newAddress;
        private RadioButton existingAddress;
        private EditText userName;
        private EditText userPhone;
        private EditText userAddressLine1;
        private EditText userCity;
        private EditText userState;
        private EditText userPIN;
        private TextView payMode;
        private TextView payAmount;
        private Button confirmButton;*/
    }

    private class CheckOutTask extends AsyncTask<String, String, String>{

        ProgressDialog progressDialog;
        String kartResult;

        @Override
        protected String doInBackground(String... strings) {
            SoapObject request = new SoapObject("http://rajdhaniinandout.com/", "SaveShippingaddresswithorder");
            // uName, uPhone, address, city, state, pin, mode, amt
            request.addProperty("strareapincode", strings[5]);
            request.addProperty("strcustomerid", MainApplication.getUser().getPhone());
            String unique_id = Settings.Secure.getString(getActivity().getApplicationContext().getContentResolver(), Settings.Secure.ANDROID_ID);
            request.addProperty("strcustuniqid", unique_id);
            request.addProperty("strshippingaddrs", strings[2]);
            request.addProperty("strcity", strings[3]);
            request.addProperty("strcontactno", MainApplication.getUser().getPhone());
            request.addProperty("strcustomername", strings[0]);
            request.addProperty("strstate", strings[4]);
            request.addProperty("isshipingaddres", strings[7]);
            request.addProperty("strpaymode", strings[6]);

            SoapSerializationEnvelope envelope = new SoapSerializationEnvelope(SoapEnvelope.VER11);
            envelope.setOutputSoapObject(request);
            envelope.dotNet = true;
            HttpTransportSE ht = new HttpTransportSE("http://rajdhaniinandout.com/inoutapijson.asmx");
            try {
                ht.call("http://rajdhaniinandout.com/SaveShippingaddresswithorder", envelope);
                final SoapPrimitive soapPrimitive = (SoapPrimitive) envelope.getResponse();
                kartResult = soapPrimitive.toString();
                Log.d("ShippingK", kartResult);
            } catch (IOException e) {
                e.printStackTrace();
            } catch (XmlPullParserException e) {
                e.printStackTrace();
            }
            return kartResult;
        }

        protected void onPreExecute() {
            super.onPreExecute();
            progressDialog = ProgressDialog.show(getActivity(), "", "Loading...");
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
            progressDialog.dismiss();
            showCheckoutMessage(s);
        }
    }

    private void showCheckoutMessage(String orderid){
        if (orderid.trim().contains("Pincode")){
            Toast.makeText(getActivity().getApplicationContext(), orderid, Toast.LENGTH_LONG).show();
            userPIN.requestFocus();
            return;
        }
        Toast.makeText(getActivity().getApplicationContext(), "Your order id is " + orderid, Toast.LENGTH_LONG).show();
        mListener.onShippingFragmentListener();//uName, uPhone, address, city, state, pin, mode, amt);
    }

    public static String getTAG(){
        return TAG;
    }
}
