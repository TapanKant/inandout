package com.example.tapan.inandout;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.provider.Settings;
import android.support.design.widget.NavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.SearchView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ExpandableListView;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.tapan.inandout.adapter.CustomExpandableListAdapter;
import com.example.tapan.inandout.com.example.tapan.app.BookKartBroadCastReceiver;
import com.example.tapan.inandout.com.example.tapan.app.MainApplication;
import com.example.tapan.inandout.com.example.tapan.app.Product;
import com.example.tapan.inandout.com.example.tapan.app.StringConstants;
import com.example.tapan.inandout.fragments.CartViewFragment;
import com.example.tapan.inandout.fragments.CategoryViewFragment;
import com.example.tapan.inandout.fragments.ChangePassword;
import com.example.tapan.inandout.fragments.Login;
import com.example.tapan.inandout.fragments.MainFragment;
import com.example.tapan.inandout.fragments.NewUser;
import com.example.tapan.inandout.fragments.ProductDetails;
import com.example.tapan.inandout.fragments.SearchFragment;
import com.example.tapan.inandout.utils.Category;
import com.example.tapan.inandout.utils.MyImages;
import com.google.android.gms.appindexing.Action;
import com.google.android.gms.appindexing.AppIndex;
import com.google.android.gms.appindexing.Thing;
import com.google.android.gms.common.api.GoogleApiClient;

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
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Set;

public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener,
        MainFragment.OnFragmentInteractionListener,
        CategoryViewFragment.OnFragmentInteractionListener,
        CartViewFragment.OnCartViewFragmentInteractionListener,
        ProductDetails.OnFragmentInteractionListener,
        Login.OnFragmentInteractionListener,
        NewUser.OnFragmentInteractionListener,
        ChangePassword.OnFragmentInteractionListener,
        BookKartBroadCastReceiver.MyBroadCastReceiverListener,
        SearchFragment.OnFragmentInteractionListener
{
    MainFragment mainFragment;
    CategoryViewFragment cvFragment;
    CartViewFragment cartViewFragment;
    ProductDetails mProductDetails;
    int cartCount = 0;
    TextView badgeTextView;
    static TextView draweruser;

    ExpandableListView listView;
    ImageView home;
    Map<String, List<String>> mainCategoryList;
    List<String> mainCategoryListTitle;
    ExpandableListView account;
    static TextView login;
    ArrayList<Category> categories;
    ArrayList<Product> products;
    ArrayList<Product> hotProducts;
    ArrayList<MyImages> bannerImages;

    Toolbar toolbar;
    SearchView search;
    /**
     * ATTENTION: This was auto-generated to implement the App Indexing API.
     * See https://g.co/AppIndexing/AndroidStudio for more information.
     */
    private GoogleApiClient client;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        setContentView(R.layout.activity_main);
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        //search = (SearchView) findViewById(R.id.activity_main_search_view);
        //search.setIconifiedByDefault(false);
        toolbar.setTitle("MyBookCart24x7");
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayShowTitleEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        listView = (ExpandableListView) navigationView.findViewById(R.id.activity_main_list_view);
        draweruser = (TextView) navigationView.findViewById(R.id.user_name);
        home = (ImageView) navigationView.findViewById(R.id.activity_main_home);
        account = (ExpandableListView) navigationView.findViewById(R.id.activity_main_account);
        login = (TextView) navigationView.findViewById(R.id.activity_main_login);
        login.setOnClickListener(new LoginViewListener());

        //initCategoryList();

        Map<String, List<String>> titleData = new LinkedHashMap<>();
        List<String> subItemData = new ArrayList<>();
        subItemData.add("Track Order");
        subItemData.add("Change Password");
        titleData.put("Account",subItemData);
        subItemData = new ArrayList<>();
        subItemData.add("Account");

        CustomExpandableListAdapter cadapter = new CustomExpandableListAdapter(getApplicationContext(),subItemData, titleData);
        account.setAdapter(cadapter);
        //account.setOnItemClickListener(new ExpandableListView.);

        account.setOnGroupCollapseListener(new ExpandableListView.OnGroupCollapseListener() {
            @Override
            public void onGroupCollapse(int i) {

            }
        });
        
        account.setOnGroupExpandListener(new ExpandableListView.OnGroupExpandListener() {
            @Override
            public void onGroupExpand(int i) {

            }
        });
        account.setOnChildClickListener(new ExpandableListView.OnChildClickListener() {
            @Override
            public boolean onChildClick(ExpandableListView expandableListView, View view, int i, int i1, long l) {
                /*String selectedItem = ((List) (mExpandableListData.get(mExpandableListTitle.get(groupPosition))))
                        .get(childPosition).toString();*/
                //String item = ((TextView)view).getText().toString();
                account.collapseGroup(i);
                fireExpandableListView(i, i1);


                //Log.d("Expandable List View", " Clicked");
                return true;
            }
        });

        home.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                replaceFragment(mainFragment, MainFragment.getTAG());
            }
        });



        /*ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, categoryList);
        listView.setAdapter(adapter);
        listView.setOnItemClickListener(new ListViewListener());
        */
        navigationView.setNavigationItemSelectedListener(this);
        //initFragments();

        //replaceFragment(mainFragment, MainFragment.getTAG());
        //FragmentManager manager = getSupportFragmentManager();
        //getSupportFragmentManager().p
        //manager.beginTransaction().replace(R.id.fragment_container, MainFragment.newInstance("", "", categories)).commit();
        // ATTENTION: This was auto-generated to implement the App Indexing API.
        // See https://g.co/AppIndexing/AndroidStudio for more information.
        client = new GoogleApiClient.Builder(this).addApi(AppIndex.API).build();
    }

    private void collapseAllCategoryListView(){
        for(int i = 0; i < mainCategoryListTitle.size(); i++){
            listView.collapseGroup(i);
        }
    }

    private void initCategoryList(){
        mainCategoryList = new LinkedHashMap<>();
        mainCategoryListTitle = new LinkedList<>();
        for(Category category : categories){
            String titleStr = category.getCategoryName();
            List<String> subStrList = new LinkedList<>();
            for(int j = 1; j <=10; j++){
                subStrList.add("Item " + j);
            }
            mainCategoryList.put(titleStr, subStrList);
        }

        Set<String> keySet = mainCategoryList.keySet();
        Iterator<String> keyIterator = keySet.iterator();
        while(keyIterator.hasNext()){
            mainCategoryListTitle.add(keyIterator.next());
        }

        CustomExpandableListAdapter adapter = new CustomExpandableListAdapter(getApplicationContext(),mainCategoryListTitle, mainCategoryList);
        listView.setAdapter(adapter);
        listView.setOnChildClickListener(new ExpandableListView.OnChildClickListener() {
            @Override
            public boolean onChildClick(ExpandableListView expandableListView, View view, int i, int i1, long l) {

                listView.collapseGroup(i);
                String title = mainCategoryListTitle.get(i);
                List<String> subtitle = mainCategoryList.get(title);
                String subItem = subtitle.get(i1);
                //Toast.makeText(MainActivity.this, title + " " + subItem, Toast.LENGTH_SHORT).show();
                onCategoryClick(title, subItem);
                return true;

            }
        });

        listView.setOnGroupCollapseListener(new ExpandableListView.OnGroupCollapseListener() {
            @Override
            public void onGroupCollapse(int i) {

            }
        });

        listView.setOnGroupExpandListener(new ExpandableListView.OnGroupExpandListener() {
            @Override
            public void onGroupExpand(int i) {

            }
        });

    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        collapseAllCategoryListView();
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else if (getSupportFragmentManager().getBackStackEntryCount() > 1) {
            //Toast.makeText(getApplicationContext(), "Back Pressed", Toast.LENGTH_LONG).show();
            //Log.d("Main Activity","Back Pressed");
            //getSupportFragmentManager().popBackStack();
            super.onBackPressed();
            //clearListViewBackground();
        }

        /*else {
            super.onBackPressed();
        }*/
    }

    private void fireExpandableListView(int groupID, int childID){
        switch(childID){
            case 0:
                Toast.makeText(getApplicationContext(), "Track your order clicked", Toast.LENGTH_SHORT).show();
                break;
            case 1:
                //Toast.makeText(getApplicationContext(), "Change your password clicked", Toast.LENGTH_SHORT).show();
                replaceFragment(ChangePassword.newInstance("",""), ChangePassword.getTAG());
                break;
        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        account.collapseGroup(groupID);
    }


    private void clearListViewBackground(){
        for(int k = 0; k < listView.getChildCount(); k++){
            listView.getChildAt(k).setBackgroundColor(Color.TRANSPARENT);
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        MenuItem cartItem = null;

        getMenuInflater().inflate(R.menu.main, menu);
        cartItem = menu.findItem(R.id.action_cart);
        cartItem.setActionView(R.layout.badge_layout);

        if (cartItem.getActionView().findViewById(R.id.badge_layout_cart_image) != null){
            ImageView cartImage = ((ImageView) cartItem.getActionView().findViewById(R.id.badge_layout_cart_image));
            ImageView searchImage = ((ImageView) cartItem.getActionView().findViewById(R.id.badge_layout_search_image));
            badgeTextView = ((TextView) cartItem.getActionView().findViewById(R.id.badge_layout_text_view));
            //updateCartCounter(strKartCount);

            cartImage.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    FetchKartItems fetchKartItems = new FetchKartItems();
                    fetchKartItems.execute();
                }
            });


            searchImage.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    replaceFragment(SearchFragment.newInstance("",""), SearchFragment.getTAG());
                    /*Intent intent = new Intent(MainActivity.this, SearchActivity.class);
                    startActivity(intent);*/
                    //Toast.makeText(getApplicationContext(), "Search View Clicked", Toast.LENGTH_SHORT).show();
                }
            });


            getSupportActionBar().setDisplayShowTitleEnabled(true);
            getSupportActionBar().setDisplayShowCustomEnabled(true);
            //getSupportActionBar().setWindowTitle("MyBookCart24x7");

        }

        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();
        //Toast.makeText(getApplicationContext(), "" +id, Toast.LENGTH_SHORT).show();
        //noinspection SimplifiableIfStatement
        if (id == R.id.action_cart) {
            replaceFragment(cartViewFragment, CartViewFragment.getTAG());
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }


    @Override
    public void onFragmentInteraction(String uri) {
        showProductDetails(uri);

    }

    public void setCartCount(String value){
        //this.strKartCount = value;
        updateCartCounter(value);
    }

    @Override
    public void onProductImageClick(String productId) {
        //showProductDetails(productId);
        FetchProductDescription fetchProductDescription = new FetchProductDescription();
        fetchProductDescription.execute(productId);
    }

    @Override
    public void onCategoryClick(String categoryName, String slno) {

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        FetchProductData fetchProductData = new FetchProductData();
        fetchProductData.execute(slno, categoryName);
    }

    @Override
    public void onCartViewFragmentInteractionListener(String uri) {
        updateCartCounter(uri);
    }

    public void showProductDetails(String productId){
        FetchProductDescription fetchProductDescription = new FetchProductDescription();
        fetchProductDescription.execute(productId);
        //replaceFragment(ProductDetails.newInstance(productId,""), ProductDetails.getTAG());
    }

    @Override
    public void checkout(String checkoutType) {
        if (MainApplication.getUser() != null){
            //Toast.makeText(this, "user login", Toast.LENGTH_SHORT).show();
            CheckOutTask checkOutTask = new CheckOutTask();
            checkOutTask.execute();
        } else{
            replaceFragment(Login.newInstance("Main",""), Login.getTAG());
        }
        //Toast.makeText(getApplicationContext(), "Main Activity"+checkoutType, Toast.LENGTH_SHORT).show();
    }

    @Override
    public void continueYourShopping() {
        //mainFragment = MainFragment.newInstance("","");
        replaceFragment(mainFragment, MainFragment.getTAG());
    }


    /**
     * Method creates fragment transaction and replace current fragment with new one.
     *
     * @param newFragment    new fragment used for replacement.
     * @param transactionTag text identifying fragment transaction.
     */
    private void replaceFragment(Fragment newFragment, String transactionTag) {

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        if (newFragment != null) {
            FragmentManager frgManager = getSupportFragmentManager();
            FragmentTransaction fragmentTransaction = frgManager.beginTransaction();
            fragmentTransaction.addToBackStack(transactionTag);
            fragmentTransaction.replace(R.id.fragment_container, newFragment).commit();
            //fragmentTransaction.commitAllowingStateLoss();
        } else {
            Log.d("" + (new RuntimeException()).getClass(), "Replace fragments with null newFragment parameter.");
        }
    }

    /**
     * ATTENTION: This was auto-generated to implement the App Indexing API.
     * See https://g.co/AppIndexing/AndroidStudio for more information.
     */
    public Action getIndexApiAction() {
        Thing object = new Thing.Builder()
                .setName("Main Page") // TODO: Define a title for the content shown.
                // TODO: Make sure this auto-generated URL is correct.
                .setUrl(Uri.parse("http://[ENTER-YOUR-URL-HERE]"))
                .build();
        return new Action.Builder(Action.TYPE_VIEW)
                .setObject(object)
                .setActionStatus(Action.STATUS_TYPE_COMPLETED)
                .build();
    }

    @Override
    protected void onResume() {
        super.onResume();
        MainApplication.getInstance().setConnectivityListener(this);
    }

    @Override
    public void onStart() {
        super.onStart();

        // ATTENTION: This was auto-generated to implement the App Indexing API.
        // See https://g.co/AppIndexing/AndroidStudio for more information.

        FetchData fetchData = new FetchData();
        fetchData.execute();



        AppIndex.AppIndexApi.start(client, getIndexApiAction());
    }


    @Override
    public void onStop() {
        super.onStop();
        //clearAllFragments();
        // ATTENTION: This was auto-generated to implement the App Indexing API.
        // See https://g.co/AppIndexing/AndroidStudio for more information.
        AppIndex.AppIndexApi.end(client, getIndexApiAction());
        client.disconnect();

    }

    private void clearAllFragments(){
        while(getSupportFragmentManager().getBackStackEntryCount()> 1){
            getSupportFragmentManager().popBackStack();
        }
    }

    @Override
    public void onProductDetailsListener(String uri) {
        showProductDetails(uri);
    }


    @Override
    public void onLiginFragmentListener() {

    }

    @Override
    public void newUserFragment() {
        replaceFragment(NewUser.newInstance("",""), NewUser.getTAG() );
    }

    @Override
    public void onNewUserFragmentInteraction() {

    }

    @Override
    public void onChangePasswordFragmentInteraction(String uri) {

    }


    public void setBadgeTextView(String c){
        //strKartCount = MainApplication.getCart().getProductCount();
        badgeTextView.setText(c);

    }

    public void updateCartCounter(String c){
        if (badgeTextView !=null){
            setBadgeTextView(c);
        }
    }

    public Activity getInstance(){
        return MainActivity.this;
    }

    private void logout(){
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        MainApplication.Logout();
        draweruser.setText("");
        Toast.makeText(MainActivity.this, "Logout Successfully", Toast.LENGTH_SHORT).show();

    }

    public static void setUserDrawer(String uname){
        draweruser.setText(uname);
    }

    public static void setLogin(String text){
        login.setText(text);
    }

    @Override
    public void onNetworkConnectionChanged(boolean isConnected) {
        Toast.makeText(this, "Internet connection changed", Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onSearchFragmentListener(String string) {

    }

    class ListViewListener implements AdapterView.OnItemClickListener{

        @Override
        public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
            TextView t1 = (TextView) view;
            for(int j = 0; j < adapterView.getChildCount(); j++){
                adapterView.getChildAt(j).setBackgroundColor(Color.TRANSPARENT);

            }
            view.setBackgroundColor(Color.LTGRAY);
            //onCategoryClick(((TextView)view).getText().toString());
            //Toast.makeText(getApplicationContext(),t1.getText(),Toast.LENGTH_SHORT).show();
        }
    }

    class LoginViewListener implements View.OnClickListener{

        @Override
        public void onClick(View view) {
            TextView textView = (TextView) view;
            String txt = textView.getText().toString();
            if ( txt.trim().equals("Login")){
                replaceFragment(Login.newInstance("Main",""), Login.getTAG());
            } else if ( txt.trim().equals("Logout")){
                //Toast.makeText(MainActivity.this, txt, Toast.LENGTH_SHORT).show();
                textView.setText("Login");
                logout();
            }
        }
    }

    private class FetchData extends AsyncTask<String, String, String> {
        ProgressDialog progressDialog = null;
        private String result;
        String hotProductString;
        String strKartCount;
        @Override
        protected String doInBackground(String... strings) {

            SoapObject request = new SoapObject("http://rajdhaniinandout.com/", "Category");
            SoapSerializationEnvelope envelope = new SoapSerializationEnvelope(SoapEnvelope.VER11);
            envelope.setOutputSoapObject(request);
            envelope.dotNet = true;
            HttpTransportSE ht = new HttpTransportSE("http://rajdhaniinandout.com/inoutapijson.asmx");
            try {
                ht.call("http://rajdhaniinandout.com/Category", envelope);
                final SoapPrimitive soapPrimitive = (SoapPrimitive) envelope.getResponse();
                result = soapPrimitive.toString();
            } catch (IOException e) {
                e.printStackTrace();
            } catch (XmlPullParserException e) {
                e.printStackTrace();
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
            } catch (IOException | XmlPullParserException e) {
                e.printStackTrace();
            }

            hotProducts = new ArrayList<>();

            try {
                JSONArray jsonArray = new JSONArray(hotProductString);
                for(int i = 0; i < jsonArray.length(); i++){
                    final JSONObject jsonObject = jsonArray.getJSONObject(i);
                    String slno = jsonObject.getString("itemcode");
                    String itemname = jsonObject.getString("Itemname");
                    String mrp = jsonObject.getString("MRP");
                    String salePrice = jsonObject.getString("SalePrice");
                    String itemimage = jsonObject.getString("itemimage");
                    String catImage = "http://rajdhaniinandout.com/"+jsonObject.getString("itemimage");
                    URL imageURL = new URL(catImage);
                    Bitmap bmp = bmp = BitmapFactory.decodeStream(imageURL.openConnection().getInputStream());
                    //Bitmap bitmapImage = BitmapFactory.decodeStream()
                    //Bitmap image, String id, Category category, String name, String sp, String price
                    hotProducts.add(new Product(bmp, slno, "Hot Products", itemname, salePrice, mrp, ""));
                }
            } catch (JSONException e) {
                e.printStackTrace();
            } catch (MalformedURLException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }
            /**
             * getting banner images
             */


            try {
                bannerImages = new ArrayList<>();
                URL b1 = new URL("http://rajdhaniinandout.com/Image/banner1.jpg");
                Bitmap bmp = BitmapFactory.decodeStream(b1.openConnection().getInputStream());
                bannerImages.add( new MyImages(bmp) );
                URL b2 = new URL("http://rajdhaniinandout.com/Image/banner2.jpg");
                bmp = BitmapFactory.decodeStream(b2.openConnection().getInputStream());
                bannerImages.add(new MyImages(bmp));
                URL b3 = new URL("http://rajdhaniinandout.com/Image/banner3.jpg");
                bmp = BitmapFactory.decodeStream(b3.openConnection().getInputStream());
                bannerImages.add(new MyImages(bmp));
            } catch (MalformedURLException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }

            /**
             * strKartCount
             */
            /*SoapObject cartCountSoap = new SoapObject("http://rajdhaniinandout.com/", "Getcartcount");
            //cartCountSoap.addProperty("struserid", "");

            if ( MainApplication.getUser() != null){
                cartCountSoap.addProperty("struserid",MainApplication.getUser().getPhone());
            } else{
                String unique_id = Settings.Secure.getString(getApplicationContext().getContentResolver(), Settings.Secure.ANDROID_ID);
                String imie = unique_id;
                cartCountSoap.addProperty("struserid", imie);
            }
            SoapSerializationEnvelope cartCountSoapSerialEnv = new SoapSerializationEnvelope(SoapEnvelope.VER11);
            cartCountSoapSerialEnv.setOutputSoapObject(cartCountSoap);
            hpSoapSerialEnv.dotNet = true;
            HttpTransportSE cartCountResponse = new HttpTransportSE("http://rajdhaniinandout.com/inoutapijson.asmx");

            try {
                Log.d("TS1", strKartCount);
                cartCountResponse.call("http://rajdhaniinandout.com/Getcartcount", envelope);
                final SoapPrimitive soapPrimitive = (SoapPrimitive) envelope.getResponse();
                strKartCount = soapPrimitive.toString();
                Log.d("TS2", strKartCount);
                String str = strKartCount.replace('"', ' ');
                strKartCount = str.trim();
                Log.d("TS3", strKartCount);
            } catch (IOException e) {
                e.printStackTrace();
            } catch (XmlPullParserException e) {
                e.printStackTrace();
            }*/


            processJSONObject(result);

            return result;
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            //progressDialog = ProgressDialog.show(MainActivity.this, "", "Please wait...");
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
            //progressDialog.dismiss();
            Log.d("TapanResult" , s);
            //Toast.makeText(MainActivity.this, "Result " + s, Toast.LENGTH_SHORT).show();
            //processJSONObject(s);
            setBadgeTextView(strKartCount);
            initCategoryList();
            mainFragment = MainFragment.newInstance("", "", categories, hotProducts, bannerImages);
            replaceFragment(mainFragment, MainFragment.getTAG());
        }
    }


    private String processHotProduct(String json){
        String resp = null;
        hotProducts = new ArrayList<>();

        try {
            JSONArray jsonArray = new JSONArray(json);
            for(int i = 0; i < jsonArray.length(); i++){
                final JSONObject jsonObject = jsonArray.getJSONObject(i);
                String slno = jsonObject.getString("itemcode");
                String itemname = jsonObject.getString("Itemname");
                String mrp = jsonObject.getString("MRP");
                String salePrice = jsonObject.getString("SalePrice");
                String itemimage = jsonObject.getString("itemimage");
                String catImage = "http://rajdhaniinandout.com/"+jsonObject.getString("itemimage");
                URL imageURL = new URL(catImage);
                Bitmap bmp = bmp = BitmapFactory.decodeStream(imageURL.openConnection().getInputStream());
                //Bitmap bitmapImage = BitmapFactory.decodeStream()
                //Bitmap image, String id, Category category, String name, String sp, String price
                products.add(new Product(bmp, slno, "Hot Products", itemname, salePrice, mrp, ""));
            }
        } catch (JSONException e) {
            e.printStackTrace();
        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return resp;
    }
    private String processJSONObject(String json){
        String resp = null;
        categories = new ArrayList<>();

        try {

            JSONArray jsonArray;
            jsonArray = new JSONArray(json);
            //Toast.makeText(getActivity().getApplicationContext(), "Result " + jsonArray, Toast.LENGTH_SHORT).show();
            for(int i = 0; i < jsonArray.length(); i++){
                //Log.d("Tapan", "");
                final JSONObject jsonObject = jsonArray.getJSONObject(i);
                String slno = jsonObject.getString("Categoryid");
                String catName = jsonObject.getString("categoryname");
                categories.add(new Category(slno, catName));
            }

        } catch (JSONException e) {
            e.printStackTrace();
        }
        return resp;
    }

    private class FetchProductData extends AsyncTask<String, String, String> {
        ProgressDialog progressDialog = null;
        private String result;

        @Override
        protected String doInBackground(String... strings) {

            SoapObject request = new SoapObject("http://rajdhaniinandout.com/", "Getproducts");
            request.addProperty("strcategory", strings[0]);
            Log.d("paramT", strings[0]);
            SoapSerializationEnvelope envelope = new SoapSerializationEnvelope(SoapEnvelope.VER11);
            envelope.setOutputSoapObject(request);
            envelope.dotNet = true;
            HttpTransportSE ht = new HttpTransportSE("http://rajdhaniinandout.com/inoutapijson.asmx");
            try {
                ht.call("http://rajdhaniinandout.com/Getproducts", envelope);
                final SoapPrimitive soapPrimitive = (SoapPrimitive) envelope.getResponse();
                Log.d("line1", "Test");
                result = soapPrimitive.toString();
                Log.d("line2", "Test");
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
                    //Log.d("Tapan", "");
                    final JSONObject jsonObject = jsonArray.getJSONObject(i);
                    String slno = jsonObject.getString("itemcode");
                    String itemname = jsonObject.getString("Itemname");
                    String mrp = jsonObject.getString("MRP");
                    String salePrice = jsonObject.getString("SalePrice");
                    String itemimage = jsonObject.getString("itemimage");
                    String catImage = "http://rajdhaniinandout.com/"+jsonObject.getString("itemimage");
                    URL imageURL = new URL(catImage);
                    Bitmap bmp = bmp = BitmapFactory.decodeStream(imageURL.openConnection().getInputStream());
                    //Bitmap bitmapImage = BitmapFactory.decodeStream()
                    //Bitmap image, String id, Category category, String name, String sp, String price
                    products.add(new Product(bmp, slno, strings[1], itemname, salePrice, mrp, ""));
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
            progressDialog = ProgressDialog.show(MainActivity.this, "", "Please wait...");
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
            Log.d("CatView" , s);
            progressDialog.dismiss();
            cvFragment = CategoryViewFragment.newInstance("", "", products);
            replaceFragment(cvFragment, CategoryViewFragment.getTAG());
        }
    }

    private String processJSONProductObjects(String json, String mCategoryName){
        String resp = null;
        products = new ArrayList<>();

        try {

            JSONArray jsonArray;
            jsonArray = new JSONArray(json);
            //Toast.makeText(getActivity().getApplicationContext(), "Result " + jsonArray, Toast.LENGTH_SHORT).show();
            for(int i = 0; i < jsonArray.length(); i++){
                //Log.d("Tapan", "");
                final JSONObject jsonObject = jsonArray.getJSONObject(i);
                String slno = jsonObject.getString("itemcode");
                String itemname = jsonObject.getString("Itemname");
                String mrp = jsonObject.getString("MRP");
                String salePrice = jsonObject.getString("SalePrice");
                String itemimage = jsonObject.getString("itemimage");
                String catImage = "http://rajdhaniinandout.com/"+jsonObject.getString("ProductCatImg");
                URL imageURL = new URL(catImage);
                Bitmap bmp = bmp = BitmapFactory.decodeStream(imageURL.openConnection().getInputStream());
                //Bitmap bitmapImage = BitmapFactory.decodeStream()
                //Bitmap image, String id, Category category, String name, String sp, String price

                products.add(new Product(bmp, slno, mCategoryName, itemname, salePrice, mrp, ""));
            }

        } catch (JSONException e) {
            e.printStackTrace();
        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return resp;
    }


    private class FetchProductDescription extends AsyncTask<String, String, Product> {
        ProgressDialog progressDialog = null;
        private String result;
        Product product;

        @Override
        protected Product doInBackground(String... strings) {

            SoapObject request = new SoapObject("http://rajdhaniinandout.com/", "GetProductDetails");
            request.addProperty("stritemcode", strings[0]);
            Log.d("paramT", strings[0]);
            SoapSerializationEnvelope envelope = new SoapSerializationEnvelope(SoapEnvelope.VER11);
            envelope.setOutputSoapObject(request);
            envelope.dotNet = true;
            HttpTransportSE ht = new HttpTransportSE("http://rajdhaniinandout.com/inoutapijson.asmx");
            try {
                ht.call("http://rajdhaniinandout.com/GetProductDetails", envelope);
                final SoapPrimitive soapPrimitive = (SoapPrimitive) envelope.getResponse();
                Log.d("line1", "Test");
                result = soapPrimitive.toString();
                Log.d("line2", "Test");
            } catch (IOException e) {
                e.printStackTrace();
            } catch (XmlPullParserException e) {
                e.printStackTrace();
            }
            Log.d("productDescriptionT", result);
            Product p = null;
            try {

                JSONArray jsonArray;
                jsonArray = new JSONArray(result);
                //Toast.makeText(getActivity().getApplicationContext(), "Result " + jsonArray, Toast.LENGTH_SHORT).show();
                for(int i = 0; i < jsonArray.length(); i++){
                    //Log.d("Tapan", "");
                    final JSONObject jsonObject = jsonArray.getJSONObject(i);
                    String slno = jsonObject.getString("itemcode");
                    String itemname = jsonObject.getString("Itemname");
                    String mrp = jsonObject.getString("MRP");
                    String salePrice = jsonObject.getString("SalePrice");
                    String description = jsonObject.getString("Descriptions");
                    String itemimage = jsonObject.getString("itemimage");
                    String catImage = "http://rajdhaniinandout.com/"+jsonObject.getString("itemimage");
                    URL imageURL = new URL(catImage);
                    Bitmap bmp = bmp = BitmapFactory.decodeStream(imageURL.openConnection().getInputStream());
                    //Bitmap bitmapImage = BitmapFactory.decodeStream()
                    //Bitmap image, String id, Category category, String name, String sp, String price
                    p = new Product(bmp, slno, strings[0], itemname, salePrice, mrp, description);

                }

            } catch (JSONException e) {
                e.printStackTrace();
            } catch (MalformedURLException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }
            return p;
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            progressDialog = ProgressDialog.show(MainActivity.this, "", "Please wait...");
        }

        @Override
        protected void onPostExecute(Product s) {
            super.onPostExecute(s);
            //Log.d("CatView" , s);
            progressDialog.dismiss();
            mProductDetails = ProductDetails.newInstance("", "", s);
            replaceFragment(mProductDetails, ProductDetails.getTAG());
        }
    }

    private class FetchKartItems extends AsyncTask<String, String, String>{

        String kartResult;
        ProgressDialog progressDialog;
        ArrayList<Product> kartItems;

        @Override
        protected String doInBackground(String... strings) {

            SoapObject request = new SoapObject("http://rajdhaniinandout.com/", "ListCartitems");
            if ( MainApplication.getUser() != null){
                request.addProperty("struserid",MainApplication.getUser().getPhone());
            } else{
                String unique_id = Settings.Secure.getString(getApplicationContext().getContentResolver(), Settings.Secure.ANDROID_ID);
                String imie = unique_id;
                request.addProperty("struserid", imie);
            }

            SoapSerializationEnvelope envelope = new SoapSerializationEnvelope(SoapEnvelope.VER11);
            envelope.setOutputSoapObject(request);
            envelope.dotNet = true;
            HttpTransportSE ht = new HttpTransportSE("http://rajdhaniinandout.com/inoutapijson.asmx");
            try {
                ht.call("http://rajdhaniinandout.com/ListCartitems", envelope);
                final SoapPrimitive soapPrimitive = (SoapPrimitive) envelope.getResponse();
                kartResult = soapPrimitive.toString();
                Log.d("kartResult", kartResult);
            } catch (IOException e) {
                e.printStackTrace();
            } catch (XmlPullParserException e) {
                e.printStackTrace();
            }

            if ( kartResult != null){
                kartItems = new ArrayList<>();
                try {
                    JSONArray jsonArray = new JSONArray(kartResult);
                    for(int i = 0; i < jsonArray.length(); i++){
                        //Log.d("Tapan", "");
                        final JSONObject jsonObject = jsonArray.getJSONObject(i);
                        String slno = jsonObject.getString("itemcode");
                        String itemname = jsonObject.getString("itemname");
                        String mrp = jsonObject.getString("MRP");
                        String salePrice = jsonObject.getString("Price");
                        //String description = jsonObject.getString("Descriptions");
                        String itemimage = jsonObject.getString("Picture");
                        String qty = jsonObject.getString("quantity");
                        String catImage = "http://rajdhaniinandout.com/"+ itemimage;
                        URL imageURL = new URL(catImage);
                        Bitmap bmp = bmp = BitmapFactory.decodeStream(imageURL.openConnection().getInputStream());
                        //Bitmap bitmapImage = BitmapFactory.decodeStream()
                        //Bitmap image, String id, Category category, String name, String sp, String price
                        Product p = new Product(bmp, slno, "", itemname, salePrice, mrp, "");
                        Double iqty = Double.parseDouble(qty);
                        p.setQty(iqty.intValue());
                        kartItems.add(p);
                    }

                } catch (JSONException e) {
                    e.printStackTrace();
                } catch (MalformedURLException e) {
                    e.printStackTrace();
                } catch (IOException e) {
                    e.printStackTrace();
                }
                catch (NumberFormatException ne){
                    ne.printStackTrace();
                }
            }
            return kartResult;
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            progressDialog = ProgressDialog.show(MainActivity.this, "", "Loading...");
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
            progressDialog.dismiss();
            if ( kartItems!=null ){
                cartViewFragment = CartViewFragment.newInstance("","", kartItems);
                replaceFragment(cartViewFragment, CartViewFragment.getTAG());
            }
        }
    }

    private class CheckOutTask extends AsyncTask<String, String, String>{

        ProgressDialog progressDialog;
        String kartResult;

        @Override
        protected String doInBackground(String... strings) {
            SoapObject request = new SoapObject("http://rajdhaniinandout.com/", "SaveShippingaddresswithorder");

            request.addProperty("strareapincode", "800014");
            request.addProperty("strcustomerid", MainApplication.getUser().getPhone());
            String unique_id = Settings.Secure.getString(getApplicationContext().getContentResolver(), Settings.Secure.ANDROID_ID);
            request.addProperty("strcustuniqid", unique_id);
            request.addProperty("strshippingaddrs", "abc");
            request.addProperty("strcity", "Patna");
            request.addProperty("strcontactno", MainApplication.getUser().getPhone());
            request.addProperty("strcustomername", "abc");
            request.addProperty("strstate", "Bihar");
            request.addProperty("isshipingaddres", "0");
            request.addProperty("strpaymode", "COD");

            SoapSerializationEnvelope envelope = new SoapSerializationEnvelope(SoapEnvelope.VER11);
            envelope.setOutputSoapObject(request);
            envelope.dotNet = true;
            HttpTransportSE ht = new HttpTransportSE("http://rajdhaniinandout.com/inoutapijson.asmx");
            try {
                ht.call("http://rajdhaniinandout.com/SaveShippingaddresswithorder", envelope);
                final SoapPrimitive soapPrimitive = (SoapPrimitive) envelope.getResponse();
                kartResult = soapPrimitive.toString();
                Log.d("orderid", kartResult);
            } catch (IOException e) {
                e.printStackTrace();
            } catch (XmlPullParserException e) {
                e.printStackTrace();
            }
            return kartResult;
        }

        protected void onPreExecute() {
            super.onPreExecute();
            progressDialog = ProgressDialog.show(MainActivity.this, "", "Loading...");
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
            progressDialog.dismiss();
            showCheckoutMessage(s);
        }
    }

    private void showCheckoutMessage(String orderid){
        Toast.makeText(this, "Your order id is " + orderid, Toast.LENGTH_SHORT).show();
        /*AlertDialog.Builder builder = new AlertDialog.Builder(getApplicationContext());
        builder.setTitle("Order Completed").setMessage(orderid).setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        Toast.makeText(getApplicationContext(), "OK"+i, Toast.LENGTH_SHORT).show();
                    }
                }).setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        Toast.makeText(getApplicationContext().getApplicationContext(), "Cancel"+i, Toast.LENGTH_SHORT).show();
                    }
                });
        builder.create();
        builder.show();*/

    }
}
