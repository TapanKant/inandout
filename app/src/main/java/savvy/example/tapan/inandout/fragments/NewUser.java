package savvy.example.tapan.inandout.fragments;

import android.app.ProgressDialog;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
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
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import org.ksoap2.SoapEnvelope;
import org.ksoap2.serialization.SoapObject;
import org.ksoap2.serialization.SoapPrimitive;
import org.ksoap2.serialization.SoapSerializationEnvelope;
import org.ksoap2.transport.HttpTransportSE;
import org.xmlpull.v1.XmlPullParserException;

import java.io.IOException;

import savvy.example.tapan.inandout.MainActivity;
import savvy.example.tapan.inandout.R;
import savvy.example.tapan.inandout.com.example.tapan.app.KartBroadCastReceiver;
import savvy.example.tapan.inandout.com.example.tapan.app.MainApplication;
import savvy.example.tapan.inandout.com.example.tapan.app.StringConstants;
import savvy.example.tapan.inandout.com.example.tapan.app.User;
import savvy.example.tapan.inandout.utils.StoreToMobileDatabase;

/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link NewUser.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link NewUser#newInstance} factory method to
 * create an instance of this fragment.
 */
public class NewUser extends Fragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER

    private static final String TAG = NewUser.class.getSimpleName();
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    private EditText fname;
    private EditText lname;
    private EditText mobile;
    private EditText email;
    private EditText address;
    private Spinner country;
    private Spinner state;
    private Spinner district;
    private EditText pincode;
    private EditText password;
    private EditText confirmPassword;

    private OnFragmentInteractionListener mListener;

    public NewUser() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment NewUser.
     */
    // TODO: Rename and change types and number of parameters
    public static NewUser newInstance(String param1, String param2) {
        NewUser fragment = new NewUser();
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
        View view = null;
        view = inflater.inflate(R.layout.fragment_new_user, container, false);
        //return inflater.inflate(, container, false);
        fname = (EditText) view.findViewById(R.id.fragment_new_user_first_name);
        mobile = (EditText) view.findViewById(R.id.fragment_new_user_mobile);
        email = (EditText) view.findViewById(R.id.fragment_new_user_email);
        final Button createUser = (Button) view.findViewById(R.id.fragment_new_user_submit_button);
        createUser.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (!KartBroadCastReceiver.isConnected ()){
                    Snackbar.make ( view, "No internet connection", 300 ).show ();
                    return;
                }
                processData();
            }
        });
        return view;
    }

    private void processData(){

        String mob, nm, em;
        mob = nm = em = null;
        if (fname.getText().toString().trim().equals("")){
            fname.requestFocus();
            return;
        }

        if (mobile.getText().toString().trim().equals("")){
            mobile.requestFocus();
            return;
        }

        if (email.getText().toString().trim().equals("")){
            email.requestFocus();
            return;
        }

        mob = mobile.getText().toString();
        nm = fname.getText().toString();
        em = email.getText().toString();

        SignupTask signupTask = new SignupTask();
        signupTask.execute(mob, nm, em);
    }

    // TODO: Rename method, update argument and hook method into UI event
    public void onButtonPressed(String uri) {
        if (mListener != null) {
            mListener.onNewUserFragmentInteraction(mParam1, mParam2);
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
        void onNewUserFragmentInteraction(String arg1, String arg2);
    }

    private class SignupTask extends AsyncTask<String, String, String> {

        ProgressDialog progressDialog;
        String result;
        String mobno;
        String name;
        String stremail;

        @Override
        protected String doInBackground(String... strings) {
            SoapObject request = new SoapObject("http://rajdhaniinandout.com/", "Signup");
            // uName, uPhone, address, city, state, pin, mode, amt
            mobno = strings[0];
            name = strings[1];
            stremail = strings[2];
            request.addProperty("strmobileno", strings[0]);
            request.addProperty("strname", strings[1]);
            request.addProperty("stremail", strings[2]);


            SoapSerializationEnvelope envelope = new SoapSerializationEnvelope(SoapEnvelope.VER11);
            envelope.setOutputSoapObject(request);
            envelope.dotNet = true;
            HttpTransportSE ht = new HttpTransportSE("http://rajdhaniinandout.com/inoutapijson.asmx");
            try {
                ht.call("http://rajdhaniinandout.com/Signup", envelope);
                final SoapPrimitive soapPrimitive = (SoapPrimitive) envelope.getResponse();
                result = soapPrimitive.toString();
                Log.d("signup", result);
            } catch (IOException e) {
                e.printStackTrace();
            } catch (XmlPullParserException e) {
                e.printStackTrace();
            }
            return result;
        }

        protected void onPreExecute() {
            super.onPreExecute();
            progressDialog = ProgressDialog.show(getActivity(), "", "Loading...");
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
            progressDialog.dismiss();
            Log.d("signup", s);
            processResult(result, mobno, name);
            //
            //
            //showCheckoutMessage(s);
        }
    }

    private void processResult(String result, String mobileNo, String name){
         if ( result.contains("customer")){
             Toast.makeText(getActivity(), result, Toast.LENGTH_LONG).show();
             MainActivity.setLogin("Logout");
             MainActivity.setUserDrawer(name);
             MainApplication.setUser(new User(mobileNo));
             onButtonPressed("");

             try{

                 StoreToMobileDatabase storeToMobileDatabase = new StoreToMobileDatabase(getActivity().getApplicationContext(), StringConstants.DATABASE_NAME, null, StringConstants.DATABASE_VERSION);
                 SQLiteDatabase db = storeToMobileDatabase.getWritableDatabase();
                 ContentValues contentValues = new ContentValues();
                 contentValues.put("id", mobileNo);
                 contentValues.put("name", name);
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
         } else {
             Toast.makeText(getActivity().getApplicationContext(), result, Toast.LENGTH_SHORT).show();
         }
    }

    public static String getTAG(){
        return TAG;
    }
}
