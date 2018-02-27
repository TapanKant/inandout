package savvy.example.tapan.inandout.utils;

import android.app.Activity;
import android.app.ProgressDialog;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.util.Log;

import java.io.ByteArrayInputStream;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import savvy.example.tapan.inandout.com.example.tapan.app.MainApplication;
import savvy.example.tapan.inandout.com.example.tapan.app.Product;
import savvy.example.tapan.inandout.com.example.tapan.app.StringConstants;

/**
 * Created by Tapan on 02-05-2017.
 */

public class FetchFromLocalDatabase extends AsyncTask<String, String, String>{

    private final static String TAG = FetchFromLocalDatabase.class.getSimpleName();
    private Activity activity;
    private ProgressDialog progressDialog;
    private StoreToMobileDatabase storeToMobileDatabase;
    Cursor cursorBannerImages;
    private SQLiteDatabase db;
    private ArrayList<Category> categories;
    private String strKartCount;
    private ArrayList<Product> hotProducts;
    private Map<String, List<String>> mainCategoryList;
    private Map<String, List<String>> itemCodes;
    private ArrayList<MyImages> bannerImages;
    private ArrayList<String> mainCategoryListTitle;
    private MainApplication mainApplication;

    public FetchFromLocalDatabase(){

        mainApplication = MainApplication.getInstance();
        try{
            storeToMobileDatabase = new StoreToMobileDatabase(mainApplication.getApplicationContext(), StringConstants.DATABASE_NAME, null, StringConstants.DATABASE_VERSION);
        } catch (SQLException e){
            Log.d(getTAG(), e.getMessage());
        }
    }
    public FetchFromLocalDatabase(Activity activity){
        this.activity = activity;
        try{
            storeToMobileDatabase = new StoreToMobileDatabase(mainApplication.getApplicationContext(), StringConstants.DATABASE_NAME, null, StringConstants.DATABASE_VERSION);
        } catch (SQLException e){
            Log.d(getTAG(), e.getMessage());
        }
    }

    public static String getTAG() {
        return TAG;
    }

    public ArrayList<Category> getCategories() {
        if (categories==null || categories.size() == 0) return null;
        return categories;
    }

    public String getStrKartCount() {
        if (strKartCount==null || strKartCount.trim().length() == 0) return null;
        return strKartCount;
    }

    public ArrayList<Product> getHotProducts() {
        if (hotProducts== null || hotProducts.size() == 0) return null;
        return hotProducts;
    }

    public Map<String, List<String>> getMainCategoryList() {
        if (mainCategoryList== null || mainCategoryList.size() == 0) return null;
        return mainCategoryList;
    }

    public Map<String, List<String>> getItemCodes() {
        if (itemCodes==null || itemCodes.size() == 0) return null;
        return itemCodes;
    }

    public ArrayList<MyImages> getBannerImages() {
        if (bannerImages==null || bannerImages.size() == 0) return null;
        return bannerImages;
    }

    public ArrayList<String> getMainCategoryListTitle() {
        if (mainCategoryListTitle==null || mainCategoryListTitle.size() == 0) return null;
        return mainCategoryListTitle;
    }


   @Override
    protected void onPreExecute() {
        super.onPreExecute();
        //progressDialog = ProgressDialog.show(activity, "", "Loading data from mobile...");
        if ( storeToMobileDatabase != null){
            try{
                db = storeToMobileDatabase.getWritableDatabase();
            }catch (SQLException e){
                Log.d(getTAG(), e.getMessage());
            }
        }
    }

    public void execute1(){
        onPreExecute();
        doInBackground();
        onPostExecute("");
    }
    @Override
    protected String doInBackground(String... strings) {
        try{
            cursorBannerImages = db.rawQuery("select * from bannerImages", null);
            if ( cursorBannerImages != null){
                bannerImages = new ArrayList<>();
                while( cursorBannerImages.moveToNext()){
                    byte bytes[] = cursorBannerImages.getBlob(cursorBannerImages.getColumnIndex("imageName"));

                    ByteArrayInputStream byteArrayInputStream = new ByteArrayInputStream(bytes);
                    Bitmap bmp  = BitmapFactory.decodeStream(byteArrayInputStream);
                    bannerImages.add( new MyImages(bmp));
                }
            }

            cursorBannerImages.close();

            Cursor cursor = db.rawQuery("select * from categoryList",null);
            if (cursor != null ){
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
                }
                itemCodes.put(c.getCategoryName(), aList);
                mainCategoryList.put(c.getCategoryName(), list);
            }


            Cursor cursor1 = db.rawQuery("select * from hotProduct", null);

            if ( cursor1 != null ){
                hotProducts = new ArrayList<>();
                while( cursor1.moveToNext()){
                    String id = cursor1.getString(cursor1.getColumnIndex("id"));
                    String itemname = cursor1.getString(cursor1.getColumnIndex("itemname"));
                    String catname = cursor1.getString(cursor1.getColumnIndex("catname"));
                    String sp = cursor1.getString(cursor1.getColumnIndex("sp"));
                    String mrp = cursor1.getString(cursor1.getColumnIndex("mrp"));

                    byte[] image = cursor1.getBlob(cursor1.getColumnIndex("image"));
                    Bitmap bitmap = BitmapFactory.decodeByteArray(image, 0, image.length);
                    hotProducts.add(new Product(bitmap, id, "Hot Products", itemname, sp, mrp, ""));
                }
            }
            cursor1.close();

        }catch (SQLException e){
            Log.d("XXXT", e.getMessage());
        }

        //bannerImages = new ArrayList<>();
        return "";
    }

    @Override
    protected void onPostExecute(String s) {
        super.onPostExecute(s);
        //progressDialog.dismiss();
        storeToMobileDatabase.close();
        db.close();
    }
}
