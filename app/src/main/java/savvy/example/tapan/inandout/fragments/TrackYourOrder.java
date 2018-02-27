package savvy.example.tapan.inandout.fragments;

import android.app.ProgressDialog;
import android.content.Context;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.TextView;

import org.ksoap2.SoapEnvelope;
import org.ksoap2.serialization.SoapObject;
import org.ksoap2.serialization.SoapPrimitive;
import org.ksoap2.serialization.SoapSerializationEnvelope;
import org.ksoap2.transport.HttpTransportSE;
import org.w3c.dom.Text;
import org.xmlpull.v1.XmlPullParserException;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import savvy.example.tapan.inandout.MainActivity;
import savvy.example.tapan.inandout.R;
import savvy.example.tapan.inandout.com.example.tapan.app.MainApplication;
import savvy.example.tapan.inandout.com.example.tapan.app.Product;

/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link TrackYourOrder.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link TrackYourOrder#newInstance} factory method to
 * create an instance of this fragment.
 */
public class TrackYourOrder extends Fragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";
    private static final String TAG = TrackYourOrder.class.getSimpleName();

    // TODO: Rename and change types of parameters
    private ArrayList<String> mParam1;
    private String mParam2;
    private RecyclerView listView;
    private ArrayList<String> orderList;

    private OnFragmentInteractionListener mListener;

    public TrackYourOrder() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment TrackYourOrder.
     */
    // TODO: Rename and change types and number of parameters
    public static TrackYourOrder newInstance(ArrayList<String> param1, String param2) {
        TrackYourOrder fragment = new TrackYourOrder();
        Bundle args = new Bundle();
        args.putStringArrayList(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getStringArrayList(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
            orderList = new ArrayList<>();
            orderList = mParam1;
        }
    }

    @Override
    public void onStart() {
        super.onStart();
        if (getArguments() != null) {
            mParam1 = getArguments().getStringArrayList(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
            orderList = new ArrayList<>();
            orderList = mParam1;
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        if (getArguments() != null) {
            mParam1 = getArguments().getStringArrayList(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
            orderList = new ArrayList<>();
            orderList = mParam1;
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment

        View view = inflater.inflate(R.layout.fragment_track_your_order, container, false);
        listView = (RecyclerView) view.findViewById(R.id.track_your_order_listView);
        GridLayoutManager gridLayoutManager = new GridLayoutManager(getContext().getApplicationContext(), 1);
        listView.setLayoutManager(gridLayoutManager);
        OrderTrackerRecyclerAdapter categoryBookRecyclerAdapter = new OrderTrackerRecyclerAdapter(getActivity().getApplicationContext(), orderList);
        listView.setAdapter(categoryBookRecyclerAdapter);

        return view;
    }

    // TODO: Rename method, update argument and hook method into UI event
    public void onButtonPressed(String arg1) {
        if (mListener != null) {
            mListener.onTrackYourOrderListener(arg1);
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
        void onTrackYourOrderListener(String arg0);
    }

    class OrderTrackerRecyclerAdapter extends RecyclerView.Adapter<OrderTrackerRecyclerAdapter.ViewHolder> {

        //private static int COUNT_CACHE_VIEW = 0;
        //private static final String ADAPTER_TAG = CategoryBookRecyclerAdapter.class.getSimpleName();
        private List<String> dataSet;
        private Context context;
        //private int layoutResourceId;

        public OrderTrackerRecyclerAdapter(Context context, List<String> dataSet){
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
            String string = dataSet.get(arg1);
            Log.d("Tracker", string);
            String[] strings = string.split(",");
            if (strings.length >=3 ){
                holder.orderid.setText("Order ID " + strings[0]);
                holder.expDate.setText("Expected Arrival Date " + strings[1]);
                holder.status.setText("Status " + strings[2]);
            }

        }

        @Override
        public ViewHolder onCreateViewHolder(ViewGroup parent, int arg1) {
            // TODO Auto-generated method stub
            //Log.i(ADAPTER_TAG, "itemTV---" + ++COUNT_CACHE_VIEW);
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.track_your_order_details, null);
            ViewHolder holder = new ViewHolder(view);

            return holder;
        }

        class ViewHolder extends RecyclerView.ViewHolder{

            public TextView orderid;
            public TextView expDate;
            public TextView status;


            //bmp, slno, mParam2, itemname, salePrice, mrp)

            public ViewHolder(View itemView) {
                super(itemView);
                orderid = (TextView) itemView.findViewById(R.id.track_your_order_text1);
                expDate = (TextView) itemView.findViewById(R.id.track_your_order_text2);
                status = (TextView) itemView.findViewById(R.id.track_your_order_text3);
            }
        }

    }

    public static String getTAG(){
        return TAG;
    }
}
