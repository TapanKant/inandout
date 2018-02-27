package savvy.example.tapan.inandout.utils;

import android.app.Activity;
import android.app.ProgressDialog;
import android.os.AsyncTask;

import savvy.example.tapan.inandout.com.example.tapan.app.Product;

import java.util.ArrayList;

/**
 * Created by Tapan on 07-04-2017.
 */

public class AsyncTaskProduct extends AsyncTask<String, String, ArrayList<Product>> {

    public interface AsyncTaskProductResponse{
        public void response(ArrayList<Product> products);
    }

    public AsyncTaskProductResponse delegate = null;
    ArrayList<Product> products;
    private ProgressDialog progressDialog;
    private Activity activity;

    public AsyncTaskProduct(Activity activity, AsyncTaskProductResponse asyncTaskProductResponse){
        this.activity = activity;
        this.delegate = asyncTaskProductResponse;
    }

    public AsyncTaskProduct(Activity activity){
        this.activity = activity;
    }

    @Override
    protected ArrayList<Product> doInBackground(String... strings) {
        products = new ArrayList<>();
        try {
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        return products;
    }

    @Override
    protected void onPreExecute() {
        super.onPreExecute();
        progressDialog = ProgressDialog.show(activity, "", "Product Loading...");
    }

    @Override
    protected void onPostExecute(ArrayList<Product> s) {
        super.onPostExecute(s);
        progressDialog.dismiss();
        //delegate.response(products);
    }
}
