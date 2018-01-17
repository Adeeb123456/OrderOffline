package com.example.adeeb.orderoffline;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;
import android.widget.Toast;

import java.util.ArrayList;

/**
 * Created by AdeeB on 8/25/2016.
 */
public class RestaurantListDb extends SQLiteOpenHelper {
    public  static  final  int DATABASE_VERSION = 1;
    public  static  final String DTABASE_Name ="RESTAURANT_DB.db";
    public  static  final String TABLE_NAME = "RESTAURANT_TABLE";
    public  static  final String RESTAURANT_ID="RESTAURANT_id";

    public  static String RESTAURANT_NAME="restaurant_name";
    public  static String RESTAURANT_DISTANCE="restaurant_distance";
    public  static String RESTAURANT_TIME="restaurant_time1";
   // public  static  final String FOOD_PRICE = "food_price";
   public  static String RESTAURANT_NUMBER="restaurant_number";
    public  static String RESTAURANT_LATI="restroLati";
    public  static String RESTAURANT_LONGI="restroLongi";
    Context context;
    public RestaurantListDb(Context context, String name, SQLiteDatabase.CursorFactory factory, int version) {
        super(context, DTABASE_Name, null, DATABASE_VERSION);
        this.context=context;

     //   Toast.makeText(context, "table constructor", Toast.LENGTH_SHORT).show();


    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        String query="CREATE TABLE "+TABLE_NAME+"( "+RESTAURANT_ID+" INTEGER AUTO INCREMENT, "+RESTAURANT_NAME+" TEXT , "
                +RESTAURANT_NUMBER+" TEXT UNIQUE, "+RESTAURANT_LATI+" DOUBLE, "+RESTAURANT_LONGI+" DOUBLE, "
                +RESTAURANT_DISTANCE+" TEXT , "+RESTAURANT_TIME+" TEXT )";
        db.execSQL(query);
   //     Toast.makeText(context,"table created",Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
db.execSQL("DROP TABLE IF EXIST "+TABLE_NAME);
        onCreate(db);
    }

    public void addRestaurantsList(InformationRestro informationRestro){
        boolean exception=false;
if(informationRestro!=null) {
    SQLiteDatabase sqLiteDatabase = null;
    ContentValues contentValues = new ContentValues();
    contentValues.put(RESTAURANT_ID, informationRestro.id);
    contentValues.put(RESTAURANT_NAME, informationRestro.restroName);
    contentValues.put(RESTAURANT_DISTANCE, informationRestro.distance);
    contentValues.put(RESTAURANT_TIME, informationRestro.timeByRoad);
    contentValues.put(RESTAURANT_NUMBER, informationRestro.restaurant_number);
    try {
int i=1;
         sqLiteDatabase = getWritableDatabase();
        sqLiteDatabase.rawQuery("DELETE FROM " + TABLE_NAME, null);
        sqLiteDatabase.insert(TABLE_NAME, null, contentValues);
        // Toast.makeText(context,"data stored"+i++,Toast.LENGTH_SHORT).show();
    }catch (Exception e){
        e.printStackTrace();
        exception=true;
        Log.i("exception catch ",""+e);
    }finally {
        if(sqLiteDatabase!=null)
        sqLiteDatabase.close();
    }
if(exception){
    Log.i("exception out ","exception true");
}else {

}


}
    }


    public ArrayList<InformationRestro> getRestaurantList(){
        ArrayList<InformationRestro> restaurantTempArrayList=new ArrayList<>();
        InformationRestro informationRestro;
        SQLiteDatabase sqLiteDatabase=getReadableDatabase();

        Cursor c=sqLiteDatabase.rawQuery("SELECT * FROM "+TABLE_NAME,null);
        if(c!=null){
            if(c.moveToFirst()){
                do{
                    int Restaurant_id=c.getInt(c.getColumnIndex(RESTAURANT_ID));
                    String name=c.getString(c.getColumnIndex(RESTAURANT_NAME));
                    String time=c.getString(c.getColumnIndex(RESTAURANT_TIME));
                    String distance=c.getString(c.getColumnIndex(RESTAURANT_DISTANCE));
                    String number=c.getString(c.getColumnIndex(RESTAURANT_NUMBER));
                    informationRestro=new InformationRestro();
                    informationRestro.id=Restaurant_id;
                    informationRestro.restroName=name;
                    informationRestro.timeByRoad=time;
                    informationRestro.distance=distance;
                    informationRestro.restaurant_number=number;

                    restaurantTempArrayList.add(informationRestro);

                }while (c.moveToNext());
            }
          //  Toast.makeText(context,""+restaurantTempArrayList,Toast.LENGTH_SHORT).show();

        }
        return restaurantTempArrayList;
    }


    public InformationRestro getRestaurant(String restroNmbr){

        InformationRestro informationRestro = null;
        SQLiteDatabase sqLiteDatabase=getReadableDatabase();
        String[] nmbrStringArray = new String[] {restroNmbr};
        Cursor c=sqLiteDatabase.rawQuery("SELECT * FROM "+TABLE_NAME+" WHERE "+RESTAURANT_NUMBER+"=?",nmbrStringArray);
        if(c!=null){
            if(c.moveToFirst()){
                do{
                    int Restaurant_id=c.getInt(c.getColumnIndex(RESTAURANT_ID));
                    String name=c.getString(c.getColumnIndex(RESTAURANT_NAME));
                    String time=c.getString(c.getColumnIndex(RESTAURANT_TIME));
                    String distance=c.getString(c.getColumnIndex(RESTAURANT_DISTANCE));
                    String number=c.getString(c.getColumnIndex(RESTAURANT_NUMBER));
                    informationRestro=new InformationRestro();
                    informationRestro.id=Restaurant_id;
                    informationRestro.restroName=name;
                    informationRestro.timeByRoad=time;
                    informationRestro.distance=distance;
                    informationRestro.restaurant_number=number;



                }while (c.moveToNext());
            }
            //  Toast.makeText(context,""+restaurantTempArrayList,Toast.LENGTH_SHORT).show();

        }
        return informationRestro;
    }

}
