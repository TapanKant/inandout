package savvy.example.tapan.inandout.fragments;

import android.content.Context;
import android.graphics.Paint;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.android.volley.toolbox.ImageLoader;
import com.android.volley.toolbox.NetworkImageView;

import savvy.example.tapan.inandout.R;
import savvy.example.tapan.inandout.com.example.tapan.app.MainApplication;
import savvy.example.tapan.inandout.com.example.tapan.app.Product;

/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link ProductDetails.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link ProductDetails#newInstance} factory method to
 * create an instance of this fragment.
 */
public class ProductDetails extends Fragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";
    private static final String TAG = ProductDetails.class.getSimpleName();

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;
    private Product product;

    private OnFragmentInteractionListener mListener;

    public ProductDetails() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment ProductDetails.
     */
    // TODO: Rename and change types and number of parameters
    public static ProductDetails newInstance(String param1, String param2, Product product) {
        ProductDetails fragment = new ProductDetails();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        args.putParcelable("Product", product);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
            product = getArguments().getParcelable("Product");
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View aView = inflater.inflate(R.layout.fragment_product_details, container, false);

        NetworkImageView productImage = (NetworkImageView ) aView.findViewById(R.id.fragment_product_details_product_image);
        MainApplication mainApplication = MainApplication.getInstance ();
        ImageLoader imageLoader = mainApplication.getmImageLoader ();
        imageLoader.get ( product.getImageURL (), ImageLoader.getImageListener ( productImage, R.id.fragment_product_details_product_image, android.R.drawable.ic_dialog_alert ) );
        productImage.setImageUrl ( product.getImageURL (), imageLoader );
        //productImage.setImageBitmap(product.getImage());

        TextView prodDetails = (TextView) aView.findViewById(R.id.fragment_product_details_product_details);
        prodDetails.setText(product.getName());



        String rsPriceSymbol = getActivity().getApplicationContext().getResources().getString(R.string.price);
        String rsSPSymbol = getActivity().getApplicationContext().getResources().getString(R.string.sp);

        TextView mrp = (TextView) aView.findViewById(R.id.fragment_product_details__product_MRP);
        mrp.setPaintFlags( mrp.getPaintFlags() | Paint.STRIKE_THRU_TEXT_FLAG);
        mrp.setText(rsPriceSymbol + " " + product.getPrice());

        TextView sp = (TextView) aView.findViewById(R.id.fragment_product_details_product_SP);
        sp.setText(rsSPSymbol + " " + product.getSp());

        TextView description = (TextView) aView.findViewById(R.id.fragment_product_details_description);
        description.setText(product.getDescription());

        return aView;
    }

    // TODO: Rename method, update argument and hook method into UI event
    public void onButtonPressed(String uri) {
        if (mListener != null) {
            mListener.onProductDetailsListener(uri);
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

        if ( getArguments() != null ){
            product = getArguments().getParcelable("Product");
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
        void onProductDetailsListener(String uri);
    }

    public static String getTAG(){
        return TAG;
    }
}
