package com.example.adeeb.orderoffline;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import java.util.ArrayList;

/**
 * Created by AdeeB on 8/25/2016.
 */
public class FoodDb extends SQLiteOpenHelper {
    public  static  final  int DATABASE_VERSION = 1;
    public  static  final String DTABASE_Name ="FOOD.db";
    public  static  final String TABLE_NAME = "FOOD_TABLE";
    public  static  final String FOOD_ID="food_id";
    public  static  final String RESTAURANT_ID="restaurant_id";
    public  static  final String RESTAURANT_NAME="restaurant_name";
    public  static  final String RESTAURANT_NUMBER="restaurant_number";
    public  static String FOOD_CATAGORY="food_catagory";
    public  static String FOOD_NAME="food_name";
    public  static String FOOD_INGRE="food_ingredients";
    public  static String FOOD_SIZE="food_size";
    public  static  final String FOOD_PRICE = "food_price";

    SQLiteDatabase dbmy;
    SQLiteDatabase db;
    Context context;

    public FoodDb(Context context, String name, SQLiteDatabase.CursorFactory factory, int version) {
        super(context, DTABASE_Name, null, DATABASE_VERSION);

    this.context=context;
    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        String query="CREATE TABLE FOOD_TABLE (food_id INTEGER AUTO INCREMENT ,restaurant_id INTEGER ,food_catagory TEXT ,food_name TEXT,food_ingredients TEXT,food_price " +
                "INTEGER,food_size TEXT,restaurant_name TEXT,restaurant_number TEXT)";
        sqLiteDatabase.execSQL(query);
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {
sqLiteDatabase.execSQL("DROP TABLE IF EXIST "+TABLE_NAME);
        onCreate(sqLiteDatabase);
    }
    public void addFoodMenu(Information foofmenuinfo){
        SQLiteDatabase sqLiteDatabase=null;
        ContentValues contentValues;
        try {
           contentValues = new ContentValues();
            contentValues.put(FOOD_ID, foofmenuinfo.food_id);
            contentValues.put(RESTAURANT_ID, foofmenuinfo.restaurant_id);
            contentValues.put(RESTAURANT_NAME, foofmenuinfo.offerby);
            contentValues.put(FOOD_NAME, foofmenuinfo.title);
            contentValues.put(FOOD_INGRE, foofmenuinfo.ingredients);
            contentValues.put(FOOD_CATAGORY, foofmenuinfo.food_catagory);
            contentValues.put(FOOD_SIZE, foofmenuinfo.list_foodsize);
            contentValues.put(FOOD_PRICE, foofmenuinfo.list_foodprise);
            contentValues.put(RESTAURANT_NUMBER, foofmenuinfo.restaurant_number);
             sqLiteDatabase = getWritableDatabase();
            sqLiteDatabase.insert(TABLE_NAME, null, contentValues);
        }catch (Exception e){
Log.i("", e + "");
        }finally {
            if(sqLiteDatabase!=null)
            sqLiteDatabase.close();

        }
    }

    public ArrayList<Information> getFoodMenuDb(String number){
        ArrayList<Information> foodInfolist=new ArrayList<>();
        Information foodinfo;
SQLiteDatabase sqLiteDatabase2=getReadableDatabase();
        String fname=" ";
        int fprice=0;
        String[] nmberstrAry=new String[]{number};
        Cursor c=sqLiteDatabase2.rawQuery("SELECT * FROM "+TABLE_NAME+" WHERE "+RESTAURANT_NUMBER+"=?",nmberstrAry);

        if(c!=null){
            if ((c.moveToFirst())){
                do{
                    foodinfo=new Information();
                    fname=c.getString(c.getColumnIndex(FOOD_NAME));
                    fprice=c.getInt(c.getColumnIndex(FOOD_PRICE));
                    foodinfo.title=c.getString(c.getColumnIndex(FOOD_NAME));
                    foodinfo.food_id=c.getInt(c.getColumnIndex(FOOD_ID));
                    foodinfo.list_foodprise=c.getInt(c.getColumnIndex(FOOD_PRICE));
                    foodinfo.list_foodsize=c.getString(c.getColumnIndex(FOOD_SIZE));
                    foodinfo.ingredients=c.getString(c.getColumnIndex(FOOD_INGRE));
                    foodinfo.food_catagory=c.getString(c.getColumnIndex(FOOD_CATAGORY));
foodInfolist.add(foodinfo);
                }while (c.moveToNext());
            }
        }

//        Toast.makeText(context, fname + fprice, Toast.LENGTH_LONG).show();
        return foodInfolist;
    }

}
