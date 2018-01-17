package com.example.adeeb.orderoffline;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v4.app.NotificationCompat;
import android.telephony.SmsMessage;
import android.util.Base64;
import android.util.Log;
import android.widget.Toast;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class SmsOnCustomPortReceiver extends BroadcastReceiver {
    public SmsOnCustomPortReceiver() {
    }
    Bundle bundle;
    SmsMessage[] msgs=null;
    String str="",sender="";
    String strr;
    boolean end=false;
    int count=0;
    Context context;
    @Override
    public void onReceive(Context context, Intent intent) {
        // TODO: This method is called when the BroadcastReceiver is receiving
        // an Intent broadcast.
       // Toast.makeText(context, "on receive1", Toast.LENGTH_SHORT).show();
        this.context=context;
        bundle=intent.getExtras();
        if(bundle!=null){
           // Toast.makeText(context, bundle + "on receive offline", Toast.LENGTH_SHORT).show();
            Object[] pdus=(Object[])bundle.get("pdus");
            msgs=new SmsMessage[pdus.length];
            strr=getRestroTemp(context);
            for(int i=0;i<msgs.length;i++) {
                byte[] databyte=null;
                msgs[i]= SmsMessage.createFromPdu((byte[]) pdus[i]);
                sender +=  msgs[i].getOriginatingAddress();

                databyte=msgs[i].getUserData();



                for(int index=0;index<databyte.length;index++){
                    str+= Character.toString((char) databyte[index]);
                    char c=(char) databyte[index];
                    if (c =='}') {
//Toast.makeText(context,c,Toast.LENGTH_SHORT).show();
                        end=true;
                        count++;
                        if(count==2){

                        }
                    }
                }


                Log.d("receSMSofflinewaiter", getRestroTemp(context));


            }

            strr+=str;
            setAddRestroTemp(strr,context);
            if(end) {
                Log.d("end ture offline waiter", getRestroTemp(context));
                try {
                    testingdataStore(getRestroTemp(context));

                } catch (JSONException e) {
                    e.printStackTrace();
             //       Toast.makeText(context, e + "exception sorry offline order", Toast.LENGTH_LONG).show();
                }
             //   Toast.makeText(context, "offline order: "+getRestroTemp(context), Toast.LENGTH_LONG).show();

                setAddRestroTemp("",context);
            }
        }
    }



    public  void setAddRestroTemp(String restro, Context context) {
        SharedPreferences RestroTemp = PreferenceManager.getDefaultSharedPreferences(context);
        SharedPreferences.Editor spEditor = RestroTemp.edit();
        spEditor.putString("RestroTemp", restro);
        spEditor.commit();

    }
    public String getRestroTemp(Context context) {
        SharedPreferences RestroTemp = PreferenceManager.getDefaultSharedPreferences(context);
        return RestroTemp.getString("RestroTemp", "");

    }
    public static Bitmap decodeBase64(String input)
    {
        byte[] decodedBytes = Base64.decode(input, 0);
        return null;
    }
    public void testingdataStore( String restroDataStr) throws JSONException {

        JSONObject jsonObject;
        jsonObject=new JSONObject(restroDataStr);

        if(jsonObject.isNull("code"))
        {
          //  Toast.makeText(context, "nulll rr", Toast.LENGTH_LONG).show();
          //  Log.i("null true", jsonObject.toString());
        }
        else
        {
            if((jsonObject.getString("code")).equals("addRestro")){
                try {
                    addRestaurant(jsonObject);

                } catch (Exception e) {
                    e.printStackTrace();
                }

                Log.i("==addrestro1", jsonObject.toString());
            }
            if((jsonObject.getString("code")).equals("addMenu")){
                Log.i("jsontestindata", jsonObject.toString());
                addFoodMenu(jsonObject);

            }
            if((jsonObject.getString("code")).equals("storeOwnerNmbr")){
                Log.i("jsontestindata", jsonObject.toString());
                UserLocalStore.storeOwnerNumber(jsonObject.getString("storeOwnerNmbr"), context);
Toast.makeText(context,"owner number change: "+jsonObject.getString("storeOwnerNmbr"),Toast.LENGTH_LONG).show();
            }


          //  Log.i("jsontestindata",jsonObject.toString());
        }
    }

    public void addRestaurant(JSONObject jsonObject) throws Exception {
        Log.i("addRestrodata2", jsonObject.toString());
        RestaurantListDb restaurantListDb=new RestaurantListDb(context,null,null,0);
        InformationRestro informationRestro=new InformationRestro();

            informationRestro.id = 2;
            informationRestro.restroName = jsonObject.getString("restroName");
            informationRestro.distance = jsonObject.getString("restroDistance");
            informationRestro.timeByRoad = jsonObject.getString("restroTime");
            informationRestro.restaurant_number = jsonObject.getString("restroNumber");
            informationRestro.restroLati = jsonObject.getDouble("restroLati");
            informationRestro.restroLongi = jsonObject.getDouble("restroLongi");
            restaurantListDb.addRestaurantsList(informationRestro);
        NotifyUserAny.notifyUser(context, "A new Restaurant added", "you have a new restaurant [( " + informationRestro.restroName + " )]");


    }

    public void addFoodMenu(JSONObject jsonObject) throws JSONException {
        FoodDb foodDb;
        foodDb=new FoodDb(context,null,null,0);

        Information informationtemp=new Information();

        informationtemp.food_id=4;
        informationtemp.restaurant_id=1;
        informationtemp.title=jsonObject.getString("fdNm");
        informationtemp.food_catagory=jsonObject.getString("fdCtgry");
        informationtemp.list_foodprise=jsonObject.getDouble("fdRs");
        informationtemp.list_foodsize=jsonObject.getString("fdSiz");
        informationtemp.ingredients=jsonObject.getString("fdIngre");;
        informationtemp.offerby="-";
        informationtemp.restaurant_number=jsonObject.getString("rstroNm");
        foodDb.addFoodMenu(informationtemp);
        RestaurantListDb restaurantListDb;
        restaurantListDb=new RestaurantListDb(context,null,null,0);
        InformationRestro restaurant = null;
        try{
            restaurant= restaurantListDb.getRestaurant(informationtemp.restaurant_number);
            NotifyUserAny.notifyUser(context, "New food sended", "you have a new food sended from."+restaurant.restroName);

        }catch (Exception e){
            e.printStackTrace();
        }

Log.i("restrodb", restaurant + "new food add");
    }

}
