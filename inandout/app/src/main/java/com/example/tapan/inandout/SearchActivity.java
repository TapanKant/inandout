package com.example.tapan.inandout;

import android.app.ProgressDialog;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Paint;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
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

import com.example.tapan.inandout.com.example.tapan.app.MainApplication;
import com.example.tapan.inandout.com.example.tapan.app.Product;
import com.example.tapan.inandout.fragments.CategoryViewFragment;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.ksoap2.SoapEnvelope;
import org.ksoap2.SoapFault;
import org.ksoap2.serialization.SoapObject;
import org.ksoap2.serialization.SoapPrimitive;
import org.ksoap2.serialization.SoapSerializationEnvelope;
import org.ksoap2.transport.HttpResponseException;
import org.ksoap2.transport.HttpTransportSE;
import org.xmlpull.v1.XmlPullParserException;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

import static java.security.AccessController.getContext;

public class SearchActivity extends AppCompatActivity {

    private RecyclerView recyclerView;
    ArrayList<Product> products;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);

        SearchView searchView = (SearchView) findViewById(R.id.activity_search_search_view);
        recyclerView = (RecyclerView) findViewById(R.id.activity_search_recycler_view);
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String s) {
                //Toast.makeText(SearchActivity.this, "Search Query " + s, Toast.LENGTH_SHORT).show();
                FetchProductData fetchProductData = new FetchProductData();
                fetchProductData.execute(s);
                return false;
            }

            @Override
            public boolean onQueryTextChange(String s) {
                return false;
            }
        });
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
            final String prodDe = dataSet.get(arg1).getId();
            String rsPriceSymbol = context.getResources().getString(R.string.price);
            holder.price.setText( rsPriceSymbol + " " +String.valueOf(dataSet.get(arg1).getPrice()) );
            holder.price.setPaintFlags( holder.price.getPaintFlags() | Paint.STRIKE_THRU_TEXT_FLAG);
            String rsSPSymbol = context.getResources().getString(R.string.sp);
            holder.sp.setText(rsSPSymbol + " " +String.valueOf(dataSet.get(arg1).getSp()) );
            holder.productImage.setImageBitmap(dataSet.get(arg1).getImage());

            //holder.productImage.setOnClickListener(new ProductImageListener(prodDe));
            //Bitmap bmp = BitmapFactory.decodeResource(context.getResources(), R.drawable.ibpsfour);
            //holder.customImage.setImageBitmap(bmp);
            //holder.customImage.setOnClickListener(new View.OnClickListener() {

               /* @Override
                public void onClick(View v) {
                    // TODO Auto-generated method stub
                    Toast.makeText(context, "Book details to be opened in new intent...", Toast.LENGTH_SHORT).show();
                }
            });*/

            /*holder.addToCart.setOnClickListener(new View.OnClickListener() {

                @Override
                public void onClick(View v) {
                    // TODO Auto-generated method stub
                    MainApplication mainApplication = MainApplication.getInstance();
                    mainApplication.addToCart(new Product(prodDe));
                    if (getActivity() instanceof MainActivity){
                        ((MainActivity)getActivity()).updateCartCounter(1);
                    }
                    //Toast.makeText(context, "Total Number of Products in Cart is " + mainApplication.getCart().getProductCount(), Toast.LENGTH_SHORT).show();
                }
            });*/

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
                    String itemimage = jsonObject.getString("itemimage");
                   // if ( itemimage !=null){
                        String catImage = "http://rajdhaniinandout.com/"+jsonObject.getString("itemimage");
                        URL imageURL = new URL(catImage);
                        Bitmap bmp = bmp = BitmapFactory.decodeStream(imageURL.openConnection().getInputStream());
                    //}

                    //Bitmap bitmapImage = BitmapFactory.decodeStream()
                    //Bitmap image, String id, Category category, String name, String sp, String price
                    products.add(new Product(bmp, slno, strings[0], itemname, salePrice, mrp, ""));
                }

            } catch (JSONException e) {
                e.printStackTrace();
            } catch (MalformedURLException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }
            return result;
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            progressDialog = ProgressDialog.show(SearchActivity.this, "", "Please wait...");
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
            Log.d("CatView" , s);
            progressDialog.dismiss();

            if ( s == null || s.trim().equals("")){
                Toast.makeText(getApplicationContext(), "", Toast.LENGTH_SHORT).show();
            } else {
                GridLayoutManager gridLayoutManager = new GridLayoutManager(getApplicationContext(), 1);
                recyclerView.setLayoutManager(gridLayoutManager);
                CategoryBookRecyclerAdapter categoryBookRecyclerAdapter = new CategoryBookRecyclerAdapter(getApplicationContext(),products);
                recyclerView.setAdapter(categoryBookRecyclerAdapter);
            }
        }
    }

    @Override
    public void onBackPressed() {
        finish();
    }
}
