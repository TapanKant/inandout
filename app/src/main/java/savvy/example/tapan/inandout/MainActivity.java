package savvy.example.tapan.inandout;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.ContentValues;
import android.content.DialogInterface;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.provider.Settings;
import android.support.design.widget.NavigationView;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.SearchView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ExpandableListView;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import savvy.example.tapan.inandout.adapter.CustomExpandableListAdapter;
import savvy.example.tapan.inandout.com.example.tapan.app.KartBroadCastReceiver;
import savvy.example.tapan.inandout.com.example.tapan.app.MainApplication;
import savvy.example.tapan.inandout.com.example.tapan.app.Product;
import savvy.example.tapan.inandout.com.example.tapan.app.StringConstants;
import savvy.example.tapan.inandout.com.example.tapan.app.User;
import savvy.example.tapan.inandout.fragments.CartViewFragment;
import savvy.example.tapan.inandout.fragments.CategoryViewFragment;
import savvy.example.tapan.inandout.fragments.ChangePassword;
import savvy.example.tapan.inandout.fragments.Login;
import savvy.example.tapan.inandout.fragments.MainFragment;
import savvy.example.tapan.inandout.fragments.NewUser;
import savvy.example.tapan.inandout.fragments.ProductDetails;
import savvy.example.tapan.inandout.fragments.SearchFragment;
import savvy.example.tapan.inandout.fragments.ShippingAddress;
import savvy.example.tapan.inandout.fragments.TrackYourOrder;
import savvy.example.tapan.inandout.utils.Category;
import savvy.example.tapan.inandout.utils.MyImages;
import savvy.example.tapan.inandout.utils.StoreToMobileDatabase;

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
        KartBroadCastReceiver.MyBroadCastReceiverListener,
        SearchFragment.OnFragmentInteractionListener,
        ShippingAddress.OnShippingFragmentInteractionListener,
        TrackYourOrder.OnFragmentInteractionListener
{
    private static MainFragment mainFragment;
    private static CategoryViewFragment cvFragment;
    private static CartViewFragment cartViewFragment;
    private static ProductDetails mProductDetails;
    private static int cartCount = 0;
    private static TextView badgeTextView;
    static TextView draweruser;

    private static ExpandableListView listView;
    private static ImageView home;
    private static Map<String, List<String>> mainCategoryList;
    private static Map<String, List<String>> itemCodes;
    private static List<String> mainCategoryListTitle;
    private static ExpandableListView account;
    private static TextView login;
    private static String strKartCount;
    private static ArrayList<Category> categories;
    private static ArrayList<Product> products;
    private static ArrayList<Product> hotProducts;
    private static ArrayList<String> bannerImages;
    DrawerLayout drawer;

    private static Toolbar toolbar;
    private static SearchView search;
    /**
     * ATTENTION: This was auto-generated to implement the App Indexing API.
     * See https://g.co/AppIndexing/AndroidStudio for more information.
     */
    private GoogleApiClient client;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        init();
        setContentView(R.layout.activity_main);
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        //search = (SearchView) findViewById(R.id.activity_main_search_view);
        //search.setIconifiedByDefault(false);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayShowTitleEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        final DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();

        final NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        listView = (ExpandableListView) navigationView.findViewById(R.id.activity_main_list_view);
        draweruser = (TextView) navigationView.findViewById(R.id.user_name);
        home = (ImageView) navigationView.findViewById(R.id.activity_main_home);
        account = (ExpandableListView) navigationView.findViewById(R.id.activity_main_account);
        login = (TextView) navigationView.findViewById(R.id.activity_main_login);
        login.setOnClickListener(new LoginViewListener());

        initCategoryList();

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
                if (!KartBroadCastReceiver.isConnected ()){

                    if (drawer.isDrawerOpen(GravityCompat.START)) {
                        drawer.closeDrawer ( GravityCompat.START );
                    }
                    account.collapseGroup(i);
                    Snackbar.make ( view, "No internet connection", 300 ).show ();
                    return false;
                }
                account.collapseGroup(i);
                fireExpandableListView(i, i1);


                //Log.d("Expandable List View", " Clicked");
                return true;
            }
        });

        home.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (!KartBroadCastReceiver.isConnected ()){
                    if (drawer.isDrawerOpen(GravityCompat.START)) {
                        drawer.closeDrawer ( GravityCompat.START );
                    }
                    Snackbar.make ( view, "No internet connection", 300 ).show ();
                    return;
                }
                replaceFragment(mainFragment, MainFragment.getTAG());
            }
        });

        navigationView.setNavigationItemSelectedListener(this);

        mainFragment = MainFragment.newInstance("", "", categories, hotProducts, bannerImages);
        replaceFragment(mainFragment, MainFragment.getTAG());
        try{
            StoreToMobileDatabase storeToMob = new StoreToMobileDatabase(getApplicationContext(), StringConstants.DATABASE_NAME,null, StringConstants.DATABASE_VERSION);
            SQLiteDatabase db = storeToMob.getReadableDatabase();
            Cursor cursor = db.rawQuery("select * from login", null);
            if ( cursor.getCount() > 0){
                cursor.moveToFirst();
                String status = cursor.getString(cursor.getColumnIndex("status"));
                if (status.trim().equals("1")){
                    String name = cursor.getString(cursor.getColumnIndex("name"));
                    String id = cursor.getString(cursor.getColumnIndex("id"));
                    MainApplication.setUser(new User(id));
                    setLogin("Logout");
                    setUserDrawer(name);
                }
            }
            cursor.close();
            db.close();
            storeToMob.close();
        } catch(Exception ex){ Log.d("MainActivityT", ex.getMessage()); }
        CartCount cartCount1 = new CartCount();
        cartCount1.execute();
    }

    private void collapseAllCategoryListView(){
        for(int i = 0; i < mainCategoryListTitle.size(); i++){
            listView.collapseGroup(i);
        }
    }

    private void retry(){
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setMessage("Something went wrong. Please check network connection").setTitle("Oops!!!").setPositiveButton("Retry", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                //code for retyr button
            }
        }).setNegativeButton("Ok", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                finish();
            }
        }).show();
    }
    private void initCategoryList(){


        if ( mainCategoryList == null){
            retry();
            return;
        }
        Set<String> keySet = mainCategoryList.keySet();
        Iterator<String> keyIterator = keySet.iterator();
        mainCategoryListTitle = MainApplication.getInstance().getMainCategoryListTitle();
        /*while(keyIterator.hasNext()){
            mainCategoryListTitle.add(keyIterator.next());
        }*/

        CustomExpandableListAdapter adapter = new CustomExpandableListAdapter(getApplicationContext(),mainCategoryListTitle, mainCategoryList);
        listView.setAdapter(adapter);
        listView.setOnChildClickListener(new ExpandableListView.OnChildClickListener() {
            @Override
            public boolean onChildClick(ExpandableListView expandableListView, View view, int i, int i1, long l) {
                if (!KartBroadCastReceiver.isConnected ()){
                    Snackbar.make ( view, "No internet connection", 300 ).show ();
                    listView.collapseGroup(i);
                    return false;
                }

                listView.collapseGroup(i);
                String title = mainCategoryListTitle.get(i);
                List<String> subtitle = mainCategoryList.get(title);
                String subItem = subtitle.get(i1);
                //Toast.makeText(MainActivity.this, title + " " + subItem, Toast.LENGTH_SHORT).show();
                List<String> subId = itemCodes.get(title);
                String subSubId = subId.get(i1);
                //Toast.makeText(MainActivity.this, ""+ subId.get(i1), Toast.LENGTH_SHORT).show();
                onSubCategoryClick(subSubId);
                //onCategoryClick(""+i, ""+i1);
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

    private void onSubCategoryClick(String subCatID){
        FetchProductDataSub fetchData = new FetchProductDataSub();
        fetchData.execute(subCatID);
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        collapseAllCategoryListView();

        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else if (getSupportFragmentManager().getBackStackEntryCount() > 1) {

            if ( getSupportFragmentManager().getBackStackEntryCount() == 1 ){
                Toast.makeText(this, "Press back again to exit", Toast.LENGTH_SHORT).show();
            }
            super.onBackPressed();
            //clearListViewBackground();
        }
        else {
            super.onBackPressed();
            super.onBackPressed();
        }
    }

    @Override
    public void onTrackYourOrderListener(String args1){

        if (MainApplication.getUser() == null){
            replaceFragment(Login.newInstance("Track",""), Login.getTAG());
        } else {
            TrackOrders trackOrders = new TrackOrders();
            trackOrders.execute();
        }

    }

    @Override
    protected void onRestart() {
        super.onRestart();
    }

    private void fireExpandableListView(int groupID, int childID){
        switch(childID){
            case 0:
                //Toast.makeText(getApplicationContext(), "Track your order clicked", Toast.LENGTH_SHORT).show();
                onTrackYourOrderListener("Main");
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
            updateCartCounter(strKartCount);

            cartImage.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if (!KartBroadCastReceiver.isConnected ()){
                        Snackbar.make ( view, "No internet connection", 300 ).show ();
                        return;
                    }
                    FetchKartItems fetchKartItems = new FetchKartItems();
                    fetchKartItems.execute();
                }
            });


            searchImage.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if (!KartBroadCastReceiver.isConnected ()){
                        Snackbar.make ( view, "No internet connection", 300 ).show ();
                        return;
                    }
                    replaceFragment(SearchFragment.newInstance("",""), SearchFragment.getTAG());
                    /*Intent intent = new Intent(MainActivity.this, SplashActivity.class);
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
    public void checkout(String checkoutType, String payableAmount) {

        if (MainApplication.getUser() == null) {
            replaceFragment(Login.newInstance(checkoutType, payableAmount), Login.getTAG());
        } else {
            replaceFragment(ShippingAddress.newInstance(checkoutType, payableAmount), ShippingAddress.getTAG());
        }
        /*if (MainApplication.getUser() != null){
            replaceFragment(ShippingAddress.newInstance("",""), ShippingAddress.getTAG());
            //ShippingAddress shippingAddress = ShippingAddress.newInstance();
            //Toast.makeText(this, "user login", Toast.LENGTH_SHORT).show();
            CheckOutTask checkOutTask = new CheckOutTask();
            checkOutTask.execute();
        } else{
            replaceFragment(Login.newInstance(checkoutType,""), Login.getTAG());
        }*/
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
//        if ( mainFragment != null )
//        replaceFragment(mainFragment, MainFragment.getTAG());
    }

    @Override
    protected void onPause() {
        super.onPause();
    }

    @Override
    public void onStart() {
        super.onStart();
        // ATTENTION: This was auto-generated to implement the App Indexing API.
        // See https://g.co/AppIndexing/AndroidStudio for more information.

        /*FetchData fetchData = new FetchData();
        fetchData.execute();
        */
        init();
        //AppIndex.AppIndexApi.start(client, getIndexApiAction());
    }

    private void init(){
        MainApplication application = MainApplication.getInstance();
        bannerImages = application.getBannerImages();
        mainCategoryListTitle = application.getMainCategoryListTitle();
        categories = application.getCategories();
        strKartCount = application.getStrKartCount();
        hotProducts = application.getHotProducts();
        mainCategoryList = application.getMainCategoryList();
        itemCodes = application.getItemCodes();
        //updateCartCounter(strKartCount);
        //initCategoryList();
    }


    @Override
    public void onStop() {
        super.onStop();
        //clearAllFragments();
        // ATTENTION: This was auto-generated to implement the App Indexing API.
        // See https://g.co/AppIndexing/AndroidStudio for more information.
        //AppIndex.AppIndexApi.end(client, getIndexApiAction());
        //client.disconnect();

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
    public void newUserFragment(String arg1, String arg2) {
        replaceFragment(NewUser.newInstance(arg1,arg2), NewUser.getTAG() );
    }

    @Override
    public void onNewUserFragmentInteraction(String arg1, String arg2) {
        if ( arg1.equals("Main") || arg1.equals("Track")){
            continueYourShopping();
        } else if (arg1.equals(StringConstants.CHECKOUT_COD) || arg1.equals(StringConstants.CHECKOUT_DR_CR)){
            checkout(arg1, arg2);
        }
        //replaceFragment(mainFragment, MainFragment.getTAG());
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
        StoreToMobileDatabase storeToMobileDatabase = new StoreToMobileDatabase(getApplicationContext(), StringConstants.DATABASE_NAME, null,StringConstants.DATABASE_VERSION);
        SQLiteDatabase db = storeToMobileDatabase.getWritableDatabase();
        Cursor cursor = db.rawQuery("select * from login", null);

        if ( cursor.getCount() > 0){
            ContentValues cv = new ContentValues();
            cv.put("status", "0");
            db.update("login",cv,null, null);
        }
        cursor.close();
        db.close();
        storeToMobileDatabase.close();

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

    @Override
    public void onShippingFragmentListener(){//String uName, String uPhone, String address, String city, String state, String pin, String mode, String amt) {
        /*CheckOutTask checkOutTask = new CheckOutTask();
        checkOutTask.execute(uName, uPhone, address, city, state, pin, mode, amt);*/
        updateCartCounter("0");
        replaceFragment(mainFragment, MainFragment.getTAG());
    }

    class LoginViewListener implements View.OnClickListener{

        @Override
        public void onClick(View view) {
            if (!KartBroadCastReceiver.isConnected ()){
                Snackbar.make ( view, "No internet connection", 300 ).show ();
                return;
            }
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
                    //URL imageURL = new URL(catImage);
                    //Bitmap bmp = bmp = BitmapFactory.decodeStream(imageURL.openConnection().getInputStream());
                    //Bitmap bitmapImage = BitmapFactory.decodeStream()
                    //Bitmap image, String id, Category category, String name, String sp, String price
                    Product product = new Product(null, slno, strings[1], itemname, salePrice, mrp, "");
                    product.setImageURL ( catImage );
                    products.add(product);
                }

            } catch (JSONException e) {
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
                    //String itemimage = jsonObject.getString("itemimage");
                    String catImage = "http://rajdhaniinandout.com/"+jsonObject.getString("itemimage");
                    //URL imageURL = new URL(catImage);
                    //Bitmap bmp = bmp = BitmapFactory.decodeStream(imageURL.openConnection().getInputStream());
                    //Bitmap bitmapImage = BitmapFactory.decodeStream()
                    //Bitmap image, String id, Category category, String name, String sp, String price
                    p = new Product(null, slno, strings[0], itemname, salePrice, mrp, description);
                    p.setImageURL ( catImage );

                }

            } catch (JSONException e) {
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

                        //un-comment both lines
                        //URL imageURL = new URL(catImage);
                        Bitmap bmp = null; //BitmapFactory.decodeStream(imageURL.openConnection().getInputStream());

                        //Bitmap bitmapImage = BitmapFactory.decodeStream()
                        //Bitmap image, String id, Category category, String name, String sp, String price

                        Product p = new Product(bmp, slno, "", itemname, salePrice, mrp, "");
                        p.setImageURL ( catImage );
                        Double iqty = Double.parseDouble(qty);
                        p.setQty(iqty.intValue());
                        kartItems.add(p);
                    }

                } catch (JSONException e) {
                    Log.d("xxx", e.getMessage());
                    e.printStackTrace();
                } catch (NumberFormatException ne){
                    Log.d("xxx", ne.getMessage());
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
                updateCartCounter(""+kartItems.size());
                cartViewFragment = CartViewFragment.newInstance("","", kartItems);
                replaceFragment(cartViewFragment, CartViewFragment.getTAG());
            } else {
                updateCartCounter("0");
            }
            //updateCartCounter(""+s);
        }
    }




    private class FetchProductDataSub extends AsyncTask<String, String, String> {
        ProgressDialog progressDialog = null;
        private String result;
        ArrayList<Product> subProduct;

        @Override
        protected String doInBackground(String... strings) {
            SoapObject request = new SoapObject("http://rajdhaniinandout.com/", "GetproductsbySubcat");
            request.addProperty("strSubcategory", strings[0]);
            Log.d("paramT", strings[0]);
            SoapSerializationEnvelope envelope = new SoapSerializationEnvelope(SoapEnvelope.VER11);
            envelope.setOutputSoapObject(request);
            envelope.dotNet = true;
            HttpTransportSE ht = new HttpTransportSE("http://rajdhaniinandout.com/inoutapijson.asmx");
            try {
                ht.call("http://rajdhaniinandout.com/GetproductsbySubcat", envelope);
                final SoapPrimitive soapPrimitive = (SoapPrimitive) envelope.getResponse();
                Log.d("line1", "Test");
                result = soapPrimitive.toString();
                Log.d("line2", result);
            } catch (IOException e) {
                e.printStackTrace();
            } catch (XmlPullParserException e) {
                e.printStackTrace();
            }

            subProduct = new ArrayList<>();
            try {

                JSONArray jsonArray;
                jsonArray = new JSONArray(result);
                for(int i = 0; i < jsonArray.length(); i++){
                    //Log.d("Tapan", "");
                    final JSONObject jsonObject = jsonArray.getJSONObject(i);
                    String slno = jsonObject.getString("itemcode");
                    String itemname = jsonObject.getString("Itemname");
                    String mrp = jsonObject.getString("MRP");
                    String salePrice = jsonObject.getString("SalePrice");
                    String itemimage = jsonObject.getString("itemimage");
                    String catImage = "http://rajdhaniinandout.com/"+itemimage;
                    String description = jsonObject.getString("Descriptions");
                    //URL imageURL = new URL(catImage);
                    //Bitmap bmp = null; //bmp = BitmapFactory.decodeStream(imageURL.openConnection().getInputStream());
                    //Bitmap bitmapImage = BitmapFactory.decodeStream()
                    //Bitmap image, String id, Category category, String name, String sp, String price
                    Product product = new Product(null, slno, strings[0], itemname, salePrice, mrp, description);
                    product.setImageURL ( catImage );
                    subProduct.add(product);
                }

            } catch (JSONException e) {
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
            if ( subProduct.size() > 0 ){
                cvFragment = CategoryViewFragment.newInstance("", "", subProduct);
                replaceFragment(cvFragment, CategoryViewFragment.getTAG());
            } else {
                Toast.makeText(MainActivity.this, "No Products...", Toast.LENGTH_LONG).show();
            }
        }
    }

    private class TrackOrders extends AsyncTask<String, String, String> {
        ProgressDialog progressDialog = null;
        private String result;
        ArrayList<Product> subProduct;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            progressDialog = ProgressDialog.show(MainActivity.this, "", "Please wait...");
        }

        @Override
        protected String doInBackground(String... strings) {
            SoapObject request = new SoapObject("http://rajdhaniinandout.com/", "GetOrders");
            request.addProperty("struserid", MainApplication.getUser().getPhone());
            SoapSerializationEnvelope envelope = new SoapSerializationEnvelope(SoapEnvelope.VER11);
            envelope.setOutputSoapObject(request);
            envelope.dotNet = true;
            HttpTransportSE ht = new HttpTransportSE("http://rajdhaniinandout.com/inoutapijson.asmx");
            try {
                ht.call("http://rajdhaniinandout.com/GetOrders", envelope);
                final SoapPrimitive soapPrimitive = (SoapPrimitive) envelope.getResponse();
                result = soapPrimitive.toString();
                Log.d("OrdersDe", result);
            } catch (IOException e) {
                e.printStackTrace();
            } catch (XmlPullParserException e) {
                e.printStackTrace();
            }
            return result;
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
            progressDialog.dismiss();
            if (s!=null){
                orderTracker(s);
            }
        }
    }

    private void orderTracker(String json){

        ArrayList<String> arrayList = new ArrayList<>();
        try {
            JSONArray jsonArray = new JSONArray(json);
            for(int i = 0; i < jsonArray.length(); i++){
                JSONObject jsonObject = jsonArray.getJSONObject(i);
                String orderid = jsonObject.getString("orderid");
                String expDate = jsonObject.getString("expdelivery");
                if (expDate != null)
                    expDate = expDate.substring(0, 10);

                String orderStatus = jsonObject.getString("orderstatus");
                arrayList.add(orderid+","+expDate+","+orderStatus);
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
        replaceFragment(TrackYourOrder.newInstance(arrayList,""), TrackYourOrder.getTAG());
    }

    private class CartCount extends AsyncTask<Void, Void, Void>{

        @Override
        protected Void doInBackground(Void... voids) {
            SoapObject cartCountSoap = new SoapObject("http://rajdhaniinandout.com/", "Getcartcount");
            if ( MainApplication.getUser() != null){
                cartCountSoap.addProperty("struserid",MainApplication.getUser().getPhone());
            } else{
                String unique_id = Settings.Secure.getString(getApplicationContext().getContentResolver(), Settings.Secure.ANDROID_ID);
                cartCountSoap.addProperty("struserid", unique_id);
            }

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

            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);
            updateCartCounter(strKartCount);
        }
    }
}
