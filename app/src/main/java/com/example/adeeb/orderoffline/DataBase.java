package com.example.adeeb.orderoffline;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * Created by AdeeB on 8/25/2016.
 */
public class DataBase extends SQLiteOpenHelper {
    Context context;
public static final String dbName="foodDB.db";
    public  static  final  int DATABASE_VERSION = 1;
    public  static  final String TABLE_NAME = "STUDENT_TABLE";
    public  static  final String STD_ID="_ID";
    public  static String _Regno="Regno";
    public  static String _name="Name";
    public  static String _address="Address";

    public DataBase(Context context, String name, SQLiteDatabase.CursorFactory factory, int version) {
        super(context, name, factory, version);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {

    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }
}
