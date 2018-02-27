package com.example.tapan.inandout.fragments;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.graphics.Color;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.provider.Settings;
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

import com.example.tapan.inandout.MainActivity;
import com.example.tapan.inandout.R;
import com.example.tapan.inandout.com.example.tapan.app.BookKartBroadCastReceiver;
import com.example.tapan.inandout.com.example.tapan.app.MainApplication;
import com.example.tapan.inandout.com.example.tapan.app.StringConstants;
import com.example.tapan.inandout.com.example.tapan.app.User;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.ksoap2.SoapEnvelope;
import org.ksoap2.serialization.SoapObject;
import org.ksoap2.serialization.SoapPrimitive;
import org.ksoap2.serialization.SoapSerializationEnvelope;
import org.ksoap2.transport.HttpTransportSE;
import org.xmlpull.v1.XmlPullParserException;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;


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

                if (BookKartBroadCastReceiver.isConnected()){
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
                    + " must implement OnFragmentInteractionListener");
        }
    }

    public void newUserFragment(){
        mListener.newUserFragment();
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
        void onLiginFragmentListener();
        void continueYourShopping();
        void newUserFragment();
    }

    public static String getTAG(){
        return TAG;
    }

    private class LoginToDatabase extends AsyncTask<String, String, String>{

        //String result;
        ProgressDialog progressDialog;
        String user;

        @Override
        protected String doInBackground(String... strings) {
            //result = posData(strings[0], strings[1]);
            user = strings[0];
            SoapObject request = new SoapObject("http://rajdhaniinandout.com/", "login");
            request.addProperty("struserid", strings[0]);
            request.addProperty("strpassword", strings[1]);
            @SuppressLint("HardwareIds")
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

            return result;
        }

        @Override
        protected void onPostExecute(String s) {
            progressDialog.dismiss();
            result = s;
            //Log.d("onPostExecute"," Executed " + s);
            processResult(s);
            //uname.setText(s);
            //result = s;
            MainApplication.setUser(new User(user));

            if (mParam1.trim().equals("Main")){
                mListener.continueYourShopping();
            }
        }

        @Override
        protected void onPreExecute() {
            progressDialog = ProgressDialog.show(getActivity(), "","Please Wait...");
        }
    }

    private void processResult(String loginResult){

        if (loginResult!=null){
            if (loginResult.trim().equals("invalid User")){
                rmn.setTextColor(Color.RED);
                mobile.requestFocus();
                //mobile.requestFocus();
            } else if (loginResult.trim().equals("invalid Device")){
                rmn.setTextColor(Color.RED);
                mobile.requestFocus();
            } else if ( loginResult.trim().equals("No user found")){
                rmn.setTextColor(Color.RED);
                mobile.requestFocus();
            } else {
                rmn.setTextColor(Color.BLACK);
                MainActivity.setLogin("Logout");
                MainActivity.setUserDrawer(loginResult);
                Toast.makeText(getActivity().getApplicationContext(), "Login successfully...", Toast.LENGTH_SHORT).show();
            }
        }
    }

    private String posData(String loginId, String password){

        String result = new String();

        try{
            ArrayList<NameValuePair> array = new ArrayList<NameValuePair>();
            array.add(new BasicNameValuePair("LoginId", loginId));
            array.add(new BasicNameValuePair("LoginPswd", password));
            HttpClient httpClient = new DefaultHttpClient();
            HttpPost httpPost = new HttpPost(StringConstants.URL+"?ActType=1");

            httpPost.setEntity(new UrlEncodedFormEntity(array));

            HttpResponse httpResponse = httpClient.execute(httpPost);
            HttpEntity httpEntity = httpResponse.getEntity();
            InputStream inputStream = httpEntity.getContent();
            InputStreamReader inputStreamReader = new InputStreamReader(inputStream);
            BufferedReader bufferedReader = new BufferedReader(inputStreamReader);

            StringBuilder sb = new StringBuilder();
            String line = new String();
            while( (line = bufferedReader.readLine()) != null ){
                sb.append(line);
            }
            result = sb.toString();
            Log.d("Login1", result);

            bufferedReader.close();
            inputStreamReader.close();
            inputStream.close();
        } catch (UnsupportedEncodingException e) {
            Log.d("Unsupporte 1" , e.getMessage());
        } catch (ClientProtocolException e) {
            Log.d("Unsupporte 2" , e.getMessage());
            //Toast.makeText(getActivity().getApplicationContext(), e.getMessage(), Toast.LENGTH_SHORT).show();
        } catch (IOException e) {
            Log.d("Unsupporte 3" , e.getMessage());
            //Toast.makeText(getActivity().getApplicationContext(), e.getMessage(), Toast.LENGTH_SHORT).show();
        }
        //uname.setText(result);
        //Toast.makeText(getActivity().getApplicationContext(), result, Toast.LENGTH_SHORT).show();

        return result;
    }
}
