package savvy.example.tapan.inandout.fragments;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Color;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.provider.Settings;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import savvy.example.tapan.inandout.MainActivity;
import savvy.example.tapan.inandout.R;
import savvy.example.tapan.inandout.com.example.tapan.app.KartBroadCastReceiver;
import savvy.example.tapan.inandout.com.example.tapan.app.MainApplication;
import savvy.example.tapan.inandout.com.example.tapan.app.StringConstants;
import savvy.example.tapan.inandout.com.example.tapan.app.User;
import savvy.example.tapan.inandout.utils.StoreToMobileDatabase;

import org.ksoap2.SoapEnvelope;
import org.ksoap2.serialization.SoapObject;
import org.ksoap2.serialization.SoapPrimitive;
import org.ksoap2.serialization.SoapSerializationEnvelope;
import org.ksoap2.transport.HttpTransportSE;
import org.xmlpull.v1.XmlPullParserException;

import java.io.IOException;


/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link Login.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link Login#newInstance} factory method to
 * create an instance of this fragment.
 */
public class Login extends Fragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER

    private static final String TAG = Login.class.getSimpleName();

    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;
    private String strKartCount = "0";
    private EditText mobile;
    private EditText password;
    private TextView rmn;
    private TextView passwordTXT;
    private String result;


    private OnFragmentInteractionListener mListener;

    public Login() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment Login.
     */
    // TODO: Rename and change types and number of parameters
    public static Login newInstance(String param1, String param2) {
        Login fragment = new Login();
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
        View aView = inflater.inflate(R.layout.fragment_login, container, false);

        final Button loginButton = (Button) aView.findViewById(R.id.fragment_login_login_button);
        Button newUserButton = (Button) aView.findViewById(R.id.fragment_login_signup_button);
        mobile = (EditText) aView.findViewById(R.id.fragment_login_mobile);
        password = (EditText) aView.findViewById(R.id.fragment_login_password);
        rmn = (TextView) aView.findViewById(R.id.fragment_login_rmn);
        passwordTXT = (TextView) aView.findViewById(R.id.fragment_login_password_txt);

        loginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if (!KartBroadCastReceiver.isConnected ()){
                    Snackbar.make ( view, "No internet connection!!!", 300 ).show ();
                    return;
                }
                view.requestFocus();
                final String mobileSTR = mobile.getText().toString();
                final String passwordSTR = password.getText().toString().trim();
                LoginToDatabase loginToDatabase = new LoginToDatabase();
                InputMethodManager inputMethodManager = (InputMethodManager) getActivity().getSystemService(Activity.INPUT_METHOD_SERVICE);


                if ( (mobileSTR.equalsIgnoreCase(""))){
                    rmn.setTextColor(Color.RED);
                    mobile.requestFocus();
                    return;
                } else if ((passwordSTR.equals(""))){
                    rmn.setTextColor(Color.BLACK);
                    passwordTXT.setTextColor(Color.RED);
                    password.requestFocus();
                    return;
                } else {
                    rmn.setTextColor(Color.BLACK);
                    passwordTXT.setTextColor(Color.BLACK);
                    //Toast.makeText(getActivity().getApplicationContext(),"Login Successful", Toast.LENGTH_SHORT).show();
                }

                if (KartBroadCastReceiver.isConnected()){
                    loginToDatabase.execute(mobileSTR, passwordSTR);
                } else {
                    Toast.makeText(getActivity().getApplicationContext(), "Internet not connected", Toast.LENGTH_SHORT).show();
                }
                /*
                if ( !(mobileSTR.equalsIgnoreCase("8687526152"))){
                    rmn.setTextColor(Color.RED);
                    mobile.requestFocus();
                } else if (!(passwordSTR.equals("tapan"))){
                    rmn.setTextColor(Color.BLACK);
                    passwordTXT.setTextColor(Color.RED);
                    password.requestFocus();
                } else {
                    rmn.setTextColor(Color.BLACK);
                    passwordTXT.setTextColor(Color.BLACK);
                    Toast.makeText(getActivity().getApplicationContext(),"Login Successful", Toast.LENGTH_SHORT).show();
                }*/
                //inputMethodManager.hideSoftInputFromInputMethod(view.getWindowToken(), 0);
            }
        });

        newUserButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (!KartBroadCastReceiver.isConnected ()){
                    Snackbar.make ( view, "No internet connection!!!", 300 ).show ();
                    return;
                }
                newUserFragment();
            }
        });

        return aView;
    }

    // TODO: Rename method, update argument and hook method into UI event
    public void onButtonPressed(Uri uri) {
        if (mListener != null) {
            mListener.onLiginFragmentListener();
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

    public void newUserFragment(){
        mListener.newUserFragment(mParam1, mParam2);
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
    public interface OnFragmentInteractionListener extends TrackYourOrder.OnFragmentInteractionListener {
        // TODO: Update argument type and name
        void onLiginFragmentListener();
        void continueYourShopping();
        void checkout(String checkoutType, String payableAmount);
        void newUserFragment(String mParam1, String mParam2);
    }

    public static String getTAG(){
        return TAG;
    }

    private class LoginToDatabase extends AsyncTask<String, String, String>{

        //String result;
        ProgressDialog progressDialog;
        String user;

        @Override
        protected void onPreExecute() {
            progressDialog = ProgressDialog.show(getActivity(), "","Please Wait...");
        }

        @Override
        protected String doInBackground(String... strings) {
            //result = posData(strings[0], strings[1]);
            user = strings[0];
            SoapObject request = new SoapObject("http://rajdhaniinandout.com/", "login");
            request.addProperty("struserid", strings[0]);
            request.addProperty("strpassword", strings[1]);
            String unique_id = Settings.Secure.getString(getActivity().getApplicationContext().getContentResolver(), Settings.Secure.ANDROID_ID);
            request.addProperty("strtempuser", unique_id);
            SoapSerializationEnvelope envelope = new SoapSerializationEnvelope(SoapEnvelope.VER11);
            envelope.setOutputSoapObject(request);
            envelope.dotNet = true;
            HttpTransportSE ht = new HttpTransportSE("http://rajdhaniinandout.com/inoutapijson.asmx");

            try {
                ht.call("http://rajdhaniinandout.com/login", envelope);
                final SoapPrimitive soapPrimitive = (SoapPrimitive) envelope.getResponse();
                result = soapPrimitive.toString();
            } catch (IOException e) {
                e.printStackTrace();
            } catch (XmlPullParserException e) {
                e.printStackTrace();
            }
            Log.d("LoginK", result);

            SoapObject cartCountSoap = new SoapObject("http://rajdhaniinandout.com/", "Getcartcount");
            //cartCountSoap.addProperty("struserid", "");

            cartCountSoap.addProperty("struserid", strings[0]);

            SoapSerializationEnvelope cartCountSoapSerialEnv = new SoapSerializationEnvelope(SoapEnvelope.VER11);
            cartCountSoapSerialEnv.setOutputSoapObject(cartCountSoap);
            cartCountSoapSerialEnv.dotNet = true;
            HttpTransportSE cartCountResponse = new HttpTransportSE("http://rajdhaniinandout.com/inoutapijson.asmx");

            strKartCount = new String();
            try {
                //Log.d("TS1", strKartCount);
                cartCountResponse.call("http://rajdhaniinandout.com/Getcartcount", cartCountSoapSerialEnv);
                final SoapPrimitive soapPrimitive = (SoapPrimitive) cartCountSoapSerialEnv.getResponse();
                strKartCount = soapPrimitive.toString();
                //Log.d("TS2", strKartCount);
                String str = strKartCount.replace('"', ' ');
                strKartCount = str.trim();
                //Log.d("TS3", strKartCount);
            } catch (IOException e) {
                e.printStackTrace();
            } catch (XmlPullParserException e) {
                e.printStackTrace();
            }

            if ( strKartCount ==  null || strKartCount.equals("") || strKartCount.equals("[]") ){
                strKartCount = "0";
            }
            return result;
        }

        @Override
        protected void onPostExecute(String s) {
            progressDialog.dismiss();
            processResult(s, user);
        }
    }

    private void processResult(String loginResult, String user){

        if (loginResult!=null){
            if (loginResult.trim().equals("invalid User")){
                rmn.setTextColor(Color.RED);
                mobile.requestFocus();
                return;
                //mobile.requestFocus();
            } else if (loginResult.trim().equals("invalid Device")){
                rmn.setTextColor(Color.RED);
                mobile.requestFocus();
                return;
            } else if ( loginResult.trim().equals("No user found")){
                Toast.makeText(getActivity().getApplicationContext(), "Invalid mobile number or password", Toast.LENGTH_SHORT).show();
                rmn.setTextColor(Color.RED);
                mobile.requestFocus();

                return;
            } else {
                rmn.setTextColor(Color.BLACK);
                MainActivity.setLogin("Logout");
                MainActivity.setUserDrawer(loginResult);
                Toast.makeText(getActivity().getApplicationContext(), "Login successfully...", Toast.LENGTH_LONG).show();
                ((MainActivity)getActivity()).setBadgeTextView(strKartCount);
            }
        }

        try{

            StoreToMobileDatabase storeToMobileDatabase = new StoreToMobileDatabase(getActivity().getApplicationContext(), StringConstants.DATABASE_NAME, null, StringConstants.DATABASE_VERSION);
            SQLiteDatabase db = storeToMobileDatabase.getWritableDatabase();
            ContentValues contentValues = new ContentValues();
            contentValues.put("id", user);
            contentValues.put("name", loginResult);
            contentValues.put("status", "1");
            Cursor cursor = db.rawQuery("select * from login",null);
            if (cursor.getCount() == 1){
                db.update("login",contentValues,null,null);
            } else if ( cursor.getCount() == 0){
                db.insert("login",null, contentValues);
            }
            cursor.close();
            db.close();
            storeToMobileDatabase.close();

        } catch(Exception ex){
            Log.d("LoginData", ex.getMessage());
        }

        MainApplication.setUser(new User(user));
        if (mParam1.trim().equals(StringConstants.CHECKOUT_COD) ||  mParam1.equals(StringConstants.CHECKOUT_DR_CR)){
            mListener.checkout(mParam1, mParam2);
        } else if (mParam1.trim().equals("Main")){
            mListener.continueYourShopping();
        }

        if (mParam1.trim().equals("Track")){
            mListener.onTrackYourOrderListener("Login");
        }
    }
}
