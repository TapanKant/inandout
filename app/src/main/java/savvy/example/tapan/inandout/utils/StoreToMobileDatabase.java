package savvy.example.tapan.inandout.utils;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import savvy.example.tapan.inandout.com.example.tapan.app.StringConstants;

/**
 * Created by Tapan on 02-05-2017.
 */

public class StoreToMobileDatabase extends SQLiteOpenHelper {
    public StoreToMobileDatabase(Context context, String name, SQLiteDatabase.CursorFactory factory, int version) {
        super(context, name, factory, version);
    }

    @Override
    public void onOpen(SQLiteDatabase db) {
        super.onOpen ( db );
        if ( db.isReadOnly ()){

        }
    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        sqLiteDatabase.execSQL("create table bannerImages(imageName text)");
        sqLiteDatabase.execSQL("create table login(id text, name text, status text)");
        sqLiteDatabase.execSQL("create table categoryList(id text, name text)");
        sqLiteDatabase.execSQL("create table subcategoryList(catid text, subcatid text, subcatname text)");
        sqLiteDatabase.execSQL("create table hotProduct(id text, itemname text, image text, catname text, sp text, mrp text)");
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {

    }

    @Override
    public void onDowngrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        super.onDowngrade ( db, oldVersion, newVersion );
    }
}
