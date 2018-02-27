package savvy.example.tapan.inandout;

import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.content.ContentValues;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.StrictMode;
import android.provider.Settings;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.ksoap2.SoapEnvelope;
import org.ksoap2.serialization.SoapObject;
import org.ksoap2.serialization.SoapPrimitive;
import org.ksoap2.serialization.SoapSerializationEnvelope;
import org.ksoap2.transport.HttpTransportSE;
import org.xmlpull.v1.XmlPullParserException;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import savvy.example.tapan.inandout.com.example.tapan.app.KartBroadCastReceiver;
import savvy.example.tapan.inandout.com.example.tapan.app.MainApplication;
import savvy.example.tapan.inandout.com.example.tapan.app.Product;
import savvy.example.tapan.inandout.com.example.tapan.app.StringConstants;
import savvy.example.tapan.inandout.utils.Category;
import savvy.example.tapan.inandout.utils.FetchFromLocalDatabase;
import savvy.example.tapan.inandout.utils.MyImages;
import savvy.example.tapan.inandout.utils.StoreToMobileDatabase;

public class SplashActivity extends AppCompatActivity {

    private ArrayList<Category> categories;
    private String strKartCount = "0";
    private ArrayList<Product> hotProducts;
    private Map<String, List<String>> mainCategoryList;
    private Map<String, List<String>> itemCodes;
    private ArrayList<String> bannerImages;
    private ArrayList<String> mainCategoryListTitle;
    private MainApplication application;
    ProgressDialog progressDialog;

    private boolean databaseCheck = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);
        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
        StrictMode.setThreadPolicy(policy);
        //progressDialog = ProgressDialog.show (getApplicationContext (),"", "Loading data...");
        application = MainApplication.getInstance();
        if ( !(bannerImages == null && mainCategoryListTitle == null && categories == null
                && hotProducts == null && mainCategoryList == null && itemCodes == null) && databaseCheck ){
            //setData();
            Intent mainActivity = new Intent(SplashActivity.this, MainActivity.class);
            startActivity ( mainActivity );
            finish();
        }
        FetchFromLocalDatabase1 localDatabase = new FetchFromLocalDatabase1 ( );
        localDatabase.execute();

        /*try{
            startMainActivity();
        }catch (Exception e){
            retry(e.getMessage());
        }*/
    }

    @Override
    protected void onStart() {
        super.onStart();
    }

    @Override
    protected void onRestart() {
        super.onRestart();
        //startMainActivity();
    }

    @Override
    protected void onResume() {
        super.onResume();
    }

    @Override
    protected void onPause() {
        super.onPause();
    }

    private class FetchData extends AsyncTask<String, String, String> {
        private String categoryList;
        String hotProductString;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            progressDialog = ProgressDialog.show(SplashActivity.this, "", "Loading data...");
            Log.d ("FetchData","Started");
            if (!KartBroadCastReceiver.isConnected()){
                progressDialog.dismiss();
                this.cancel(false);
                retry ();
                //startMainActivity();
            }
        }


        @Override
        protected String doInBackground(String... strings) {

            /*StoreToMobileDatabase mobData = new StoreToMobileDatabase(getApplicationContext(), StringConstants.DATABASE_NAME, null, StringConstants.DATABASE_VERSION);
            SQLiteDatabase dh = mobData.getReadableDatabase ();
            Cursor cursor = dh.rawQuery ( "select * from login", null );
            //SQLiteDatabase sqLiteDatabase = mobData.getWritableDatabase ();
            if ( cursor == null){

            }
*/


            SoapObject request = new SoapObject("http://rajdhaniinandout.com/", "Category");
            SoapSerializationEnvelope envelope = new SoapSerializationEnvelope(SoapEnvelope.VER11);
            envelope.setOutputSoapObject(request);
            envelope.dotNet = true;
            HttpTransportSE ht = new HttpTransportSE("http://rajdhaniinandout.com/inoutapijson.asmx");
            try {
                ht.call("http://rajdhaniinandout.com/Category", envelope);
                final SoapPrimitive soapPrimitive = (SoapPrimitive) envelope.getResponse();
                categoryList = soapPrimitive.toString();
                processJSONObject(categoryList);
            } catch (IOException | XmlPullParserException e) {
                e.printStackTrace ();
                this.cancel ( false );
                progressDialog.dismiss ();
                retry ( e.getMessage () );
            }
            SoapObject hpSoapObject = new SoapObject("http://rajdhaniinandout.com/", "HotProducts");
            SoapSerializationEnvelope hpSoapSerialEnv = new SoapSerializationEnvelope(SoapEnvelope.VER11);
            hpSoapSerialEnv.setOutputSoapObject(hpSoapObject);
            hpSoapSerialEnv.dotNet = true;
            HttpTransportSE hpResponse = new HttpTransportSE("http://rajdhaniinandout.com/inoutapijson.asmx");
            try {
                hpResponse.call("http://rajdhaniinandout.com/HotProducts", hpSoapSerialEnv);
                final SoapPrimitive hpSoapPrimitive = (SoapPrimitive) hpSoapSerialEnv.getResponse();
                hotProductString = hpSoapPrimitive.toString();
                processHotProduct(hotProductString);
            } catch (IOException | XmlPullParserException e) {
                e.printStackTrace ();
                this.cancel ( false );
                progressDialog.dismiss ();
                retry ( e.getMessage () );
            }
            /*
              getting banner images
             */

            try {
                SoapObject bannerImg = new SoapObject("http://rajdhaniinandout.com/", "Getbanner");
                SoapSerializationEnvelope bannerEnv = new SoapSerializationEnvelope(SoapEnvelope.VER11);
                bannerEnv.setOutputSoapObject(bannerImg);
                bannerEnv.dotNet = true;
                HttpTransportSE bannerRes = new HttpTransportSE("http://rajdhaniinandout.com/inoutapijson.asmx");
                bannerRes.call("http://rajdhaniinandout.com/Getbanner", bannerEnv);
                SoapPrimitive bannerSOAP = (SoapPrimitive) bannerEnv.getResponse();
                String bannerString = bannerSOAP.toString();
                bannerString = bannerString.replace('"', ' ');
                bannerString = bannerString.replace('[', ' ');
                bannerString = bannerString.replace(']', ' ');
                String[] strings1 = bannerString.split(",");
                bannerImages = new ArrayList<>();
                //ArrayList<String> stringBannerImages = new ArrayList<> ( );
                for(final String k : strings1){
                    //stringBannerImages.add(k);
                    //URL bannerImageUrl = new URL(k);
                    //HttpURLConnection httpURLConnection;
                    //httpURLConnection = ( HttpURLConnection ) bannerImageUrl.openConnection ( );
                    //Bitmap bmp = BitmapFactory.decodeStream ( httpURLConnection.getInputStream ( ) );
                    bannerImages.add(k);
                }
                storeToLocaleDatabase();
            } catch (IOException e) {
                e.printStackTrace ();
                this.cancel ( false );
                progressDialog.dismiss ();
                retry ( e.getMessage () );
            } catch (XmlPullParserException e) {
                e.printStackTrace ();
                this.cancel ( false );
                retry ( e.getMessage () );
            }


            /*
             * strKartCount
             */


            SoapObject cartCountSoap = new SoapObject("http://rajdhaniinandout.com/", "Getcartcount");
            if ( MainApplication.getUser() != null){
                cartCountSoap.addProperty("struserid",MainApplication.getUser().getPhone());
            } else{
                @SuppressLint("HardwareIds")
                String unique_id = Settings.Secure.getString(getApplicationContext().getContentResolver(), Settings.Secure.ANDROID_ID);
                cartCountSoap.addProperty("struserid", unique_id);
            }
            SoapSerializationEnvelope cartCountSoapSerialEnv = new SoapSerializationEnvelope(SoapEnvelope.VER11);
            cartCountSoapSerialEnv.setOutputSoapObject(cartCountSoap);
            cartCountSoapSerialEnv.dotNet = true;
            HttpTransportSE cartCountResponse = new HttpTransportSE("http://rajdhaniinandout.com/inoutapijson.asmx");

            strKartCount = "";
            try {
                //Log.d("TS1", strKartCount);
                cartCountResponse.call("http://rajdhaniinandout.com/Getcartcount", cartCountSoapSerialEnv);
                final SoapPrimitive soapPrimitive = (SoapPrimitive) cartCountSoapSerialEnv.getResponse();
                strKartCount = soapPrimitive.toString();
                //Log.d("TS2", strKartCount);
                String str = strKartCount.replace('"', ' ');
                strKartCount = str.trim();
                //Log.d("TS3", strKartCount);
            } catch (IOException | XmlPullParserException e) {
                e.printStackTrace ();
                this.cancel ( false );
                progressDialog.dismiss ();
                retry ( e.getMessage () );
            }

            if ( strKartCount ==  null || strKartCount.equals("") || strKartCount.equals("[]") ){
                strKartCount = "0";
            }
            return "";
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
            if (!KartBroadCastReceiver.isConnected ()){
                if (progressDialog.isShowing ())
                    progressDialog.dismiss();
                retry ();
                //Snackbar.make ( view, "No internet connction", 300 ).show ();
                return;
            }
            progressDialog.dismiss();
            Log.d("TapanResult" , s);
            startMainActivity();
        }
    }

    private void startMainActivity(){

        Intent mainActivity = new Intent(SplashActivity.this, MainActivity.class);

        if ( !(bannerImages == null && mainCategoryListTitle == null && categories == null
                && hotProducts == null && mainCategoryList == null && itemCodes == null) && databaseCheck ){
            setData();
            startActivity ( mainActivity );
            finish();
        }
        else if (!KartBroadCastReceiver.isConnected ()) {
            retry ( );
            //return;
        } else{
            FetchData fetchData = new FetchData();
            fetchData.execute();
            //return;
        }

    }

    private void setData(){
        application.setBannerImages(bannerImages);
        application.setMainCategoryListTitle(mainCategoryListTitle);
        application.setCategories(categories);
        application.setStrKartCount(strKartCount);
        application.setHotProducts(hotProducts);
        application.setMainCategoryList(mainCategoryList);
        application.setItemCodes(itemCodes);

    }

    private void storeToLocaleDatabase(){
        StoreToMobileDatabase database = new StoreToMobileDatabase(getApplicationContext(),StringConstants.DATABASE_NAME, null, 1);
        SQLiteDatabase db = null;
        try{
            db = database.getWritableDatabase();

            for(String images: bannerImages){
                ContentValues bannerData = new ContentValues();
                //byte [] imgByte;// = null;
                //ByteArrayOutputStream baos = new ByteArrayOutputStream();
                //Bitmap bmp = images.getImage();
                //bmp.compress(Bitmap.CompressFormat.PNG, 0, baos);
                //imgByte = baos.toByteArray();
                bannerData.put("imageName", images);
                db.insert("bannerImages", null, bannerData);
            }
        }catch (Exception ex ){
            ex.printStackTrace ();
            //retry(ex.getMessage());
        } finally {
            if (db != null) {
                db.close();
            }
            database.close();
        }
        //database.onUpgrade(db, db.getVersion(), 1);
    }

    private void retry(){
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setMessage("Something went wrong. Please check network connection").setTitle("Oops!!!").setPositiveButton("Retry", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                startMainActivity ();
            }
        }).setNegativeButton("Ok", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                finish();
            }
        }).show();
    }

    private void retry(String msg){
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setMessage("Something went wrong. " +msg).setTitle("Oops!!!").setPositiveButton("Retry", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                FetchData fetchData = new FetchData();
                fetchData.execute();
            }
        }).setNegativeButton("Ok", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                finish();
            }
        }).show();
    }

    private String processHotProduct(String json){

        if ( json == null) return null;

        String resp = "";
        hotProducts = new ArrayList<>();

        try {
            StoreToMobileDatabase mobData = new StoreToMobileDatabase(getApplicationContext(), StringConstants.DATABASE_NAME, null, StringConstants.DATABASE_VERSION);
            //SQLiteDatabase dh = mobData.getReadableDatabase ();
            SQLiteDatabase db = mobData.getWritableDatabase();
            JSONArray jsonArray = new JSONArray(json);
            for(int i = 0; i < jsonArray.length(); i++){
                final JSONObject jsonObject = jsonArray.getJSONObject(i);
                String slno = jsonObject.getString("itemcode");
                String itemname = jsonObject.getString("Itemname");
                String mrp = jsonObject.getString("MRP");
                String salePrice = jsonObject.getString("SalePrice");
                //String itemimage = jsonObject.getString("itemimage");
                String catImage = "http://rajdhaniinandout.com/"+jsonObject.getString("itemimage");

                //un-comment both lines
                //URL imageURL = new URL(catImage);
                Bitmap bmp  = null; //BitmapFactory.decodeStream(imageURL.openConnection().getInputStream());
                //Bitmap bitmapImage = BitmapFactory.decodeStream()
                //Bitmap image, String id, Category category, String name, String sp, String price
                Product product = new Product(bmp, slno, "Hot Products", itemname, salePrice, mrp, "");
                product.setImageURL ( catImage );
                hotProducts.add(product);

                //stores to mobile device
                ContentValues cv = new ContentValues();
                cv.put("id", slno);
                cv.put("itemname",itemname);
                cv.put("sp", salePrice);
                cv.put("mrp", mrp);
                cv.put("catname", "Hot Products");
                //ByteArrayOutputStream byteArr = new ByteArrayOutputStream();
                //bmp.compress(Bitmap.CompressFormat.PNG, 0, byteArr);
                //byte[] arr = byteArr.toByteArray();
                cv.put("image", catImage);
                db.insert("hotProduct", null, cv);
            }
            db.close();
            mobData.close();
        } catch (JSONException | SQLException e) {
            Log.d(MainActivity.class.getSimpleName(),  e.getMessage());
            //e.printStackTrace();
        }
        return resp;
    }


    private String processJSONObject(String json){
        String resp = "";
        if (json == null) return null;
        categories = new ArrayList<>();

        try {
            StoreToMobileDatabase mobData = new StoreToMobileDatabase(getApplicationContext(), StringConstants.DATABASE_NAME, null, StringConstants.DATABASE_VERSION);
            SQLiteDatabase db = mobData.getWritableDatabase();

            JSONArray jsonArray;
            jsonArray = new JSONArray(json);
            //Toast.makeText(getActivity().getApplicationContext(), "Result " + jsonArray, Toast.LENGTH_SHORT).show();
            for(int i = 0; i < jsonArray.length(); i++){
                //Log.d("Tapan", "");
                ContentValues contentValues = new ContentValues();
                final JSONObject jsonObject = jsonArray.getJSONObject(i);
                String slno = jsonObject.getString("Categoryid");
                String catName = jsonObject.getString("categoryname");
                categories.add(new Category(slno, catName));
                contentValues.put("id", slno);
                contentValues.put("name", catName);
                db.insert("categoryList", null, contentValues);
            }
            db.close();
            mobData.close();
        } catch (JSONException| SQLException e) {
            Log.d("SplashDatabase", e.getMessage());
            //e.printStackTrace();
        }

        prepareSubCategory();
        return resp;
    }

    private void prepareSubCategory(){

        String strSubCat = "";

        mainCategoryList = new LinkedHashMap<>();
        itemCodes = new LinkedHashMap<>();
        mainCategoryListTitle = new ArrayList<>();
        //mainCategoryList = new LinkedHashMap<>();
        StoreToMobileDatabase storeMob = new StoreToMobileDatabase(getApplicationContext(), StringConstants.DATABASE_NAME, null, StringConstants.DATABASE_VERSION);
        SQLiteDatabase db = storeMob.getWritableDatabase();
        for(Category category: categories){

            final String cat = category.getSlno();

            SoapObject request = new SoapObject("http://rajdhaniinandout.com/", "GetSubCategory");
            request.addProperty("strCategoryid", cat);
            SoapSerializationEnvelope envelope = new SoapSerializationEnvelope(SoapEnvelope.VER11);
            envelope.setOutputSoapObject(request);
            envelope.dotNet = true;
            HttpTransportSE ht = new HttpTransportSE("http://rajdhaniinandout.com/inoutapijson.asmx");
            try {
                ht.call("http://rajdhaniinandout.com/GetSubCategory", envelope);
                final SoapPrimitive soapPrimitive = (SoapPrimitive) envelope.getResponse();
                strSubCat = soapPrimitive.toString();
                Log.d("subcate", strSubCat);
            } catch (IOException | XmlPullParserException e) {
                e.printStackTrace();
            }
            List<String> list = new LinkedList<>();
            List<String> aList  = new LinkedList<>();
            try {
                JSONArray jsonArray = new JSONArray(strSubCat);
                for(int i = 0; i < jsonArray.length(); i++){
                    JSONObject jsonObject = jsonArray.getJSONObject(i);
                    String subId = jsonObject.get("subcatID").toString();
                    String subName = jsonObject.get("Subcategoryname").toString();
                    list.add(subName);
                    aList.add(subId);
                    ContentValues cv = new ContentValues();
                    cv.put("catid", cat);
                    cv.put("subcatid", subId);
                    cv.put("subcatname", subName);
                    db.insert("subcategoryList", null, cv);
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
            itemCodes.put(category.getCategoryName(), aList);
            mainCategoryList.put(category.getCategoryName(), list);
            mainCategoryListTitle.add (category.getCategoryName());
        }
    }

    private class FetchFromLocalDatabase1 extends AsyncTask<String, String, String>{

        private final String TAG = FetchFromLocalDatabase.class.getSimpleName();
        private ProgressDialog progressDialog;
        private StoreToMobileDatabase storeToMobileDatabase;
        Cursor cursorBannerImages;
        private SQLiteDatabase db;

        FetchFromLocalDatabase1(){
            try{
                storeToMobileDatabase = new StoreToMobileDatabase(getApplicationContext(), StringConstants.DATABASE_NAME, null, StringConstants.DATABASE_VERSION);
            } catch (SQLException e){
                Log.d(TAG, e.getMessage());
            }
        }


        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            progressDialog = ProgressDialog.show(SplashActivity.this, "", "Loading data...");
            if ( storeToMobileDatabase != null){
                try{
                    db = storeToMobileDatabase.getWritableDatabase();
                }catch (SQLException e){
                    Log.d(TAG, e.getMessage());
                }
            }
        }

        @Override
        protected String doInBackground(String... strings) {
            try{
                cursorBannerImages = db.rawQuery("select * from bannerImages", null);
                if ( cursorBannerImages.getCount() > 0){
                    bannerImages = new ArrayList<>();
                    while( cursorBannerImages.moveToNext()){
                        String imageNames = cursorBannerImages.getString (cursorBannerImages.getColumnIndex("imageName"));
                        Log.d("XXTX", "Hello");
                        //ByteArrayInputStream byteArrayInputStream = new ByteArrayInputStream(bytes);
                        //Bitmap bmp  = BitmapFactory.decodeStream(byteArrayInputStream);
                        bannerImages.add( imageNames);
                    }
                }

                cursorBannerImages.close();

                Cursor cursor = db.rawQuery("select * from categoryList",null);
                if (cursor.getCount() > 0 ){
                    categories = new ArrayList<>();
                    mainCategoryListTitle = new ArrayList<>();
                    while(cursor.moveToNext()){

                        String id = cursor.getString(cursor.getColumnIndex("id"));
                        String name = cursor.getString(cursor.getColumnIndex("name"));
                        Log.d("categoryListTK", id+name);
                        categories.add(new Category(id, name));
                       mainCategoryListTitle.add(name);
                    }
                }
                cursor.close();


                if (categories!= null){
                    mainCategoryList = new LinkedHashMap<>();
                    itemCodes = new LinkedHashMap<>();
                    for(Category c: categories){
                        String slno = c.getSlno();
                        List<String> list = new LinkedList<>();
                        List<String> aList  = new LinkedList<>();
                        String sql = "select * from subcategoryList where catid = ?";
                        Cursor subCat = db.rawQuery(sql, new String[]{slno});

                        while( subCat.moveToNext()){
                            String subName = subCat.getString(subCat.getColumnIndex("subcatname"));
                            String subID = subCat.getString(subCat.getColumnIndex("subcatid"));
                            list.add(subName);
                            aList.add(subID);
                            Log.d("XXTP", c.getSlno() + " " + subID + subName);
                        }
                        itemCodes.put(c.getCategoryName(), aList);
                        mainCategoryList.put(c.getCategoryName(), list);
                        subCat.close ();
                    }
                }


                Cursor cursor1 = db.rawQuery("select * from hotProduct", null);

                if (  cursor1.getCount() > 0){
                    hotProducts = new ArrayList<>();
                    while( cursor1.moveToNext()){
                        String id = cursor1.getString(cursor1.getColumnIndex("id"));
                        String itemname = cursor1.getString(cursor1.getColumnIndex("itemname"));
                        String catname = cursor1.getString(cursor1.getColumnIndex("catname"));
                        String sp = cursor1.getString(cursor1.getColumnIndex("sp"));
                        String mrp = cursor1.getString(cursor1.getColumnIndex("mrp"));
                        Log.d("hotP", id +" " +itemname);
                        String imageURL = cursor1.getString ( cursor1.getColumnIndex ( "image" ) );
                        //byte[] image = cursor1.getBlob(cursor1.getColumnIndex("image"));
                        //Bitmap bitmap = BitmapFactory.decodeByteArray(image, 0, image.length);
                        Product product = new Product(null, id, catname, itemname, sp, mrp, "");
                        product.setImageURL (  imageURL);
                        hotProducts.add(product);
                    }
                }
                cursor1.close();

            }catch (SQLException e){
                Log.d("XXXT", e.getMessage());
            }
            return "";
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
            progressDialog.dismiss();
            storeToMobileDatabase.close();
            db.close();
            databaseCheck = true;
            startMainActivity();
        }
    }
}
