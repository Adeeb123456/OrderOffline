package com.example.adeeb.orderoffline;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;

import org.json.JSONObject;

/**
 * Created by AdeeB on 9/26/2015.
 */
public class UserLocalStore {

    public static final String SP_name = "userDetails";// name of SharedPreferences
    SharedPreferences userLocalDB;//sp store data on our phone

    //constructor with Contex argument;
    // note only activity can institiate sp , not java class so we use  context argumnt to instiate
    // our sp. the activity that use UserLocalStore class , give us its context to institiate
    public UserLocalStore(Context context) {
        //  userLocalDB=context.getSharedPreferences(SP_name,0);// initialize the sp
        userLocalDB = PreferenceManager.getDefaultSharedPreferences(context);
    }

    public static void myStoreUserData(User user, Context context) {
        // get user instance (name email pass)
        SharedPreferences StoreUserSp = PreferenceManager.getDefaultSharedPreferences(context);
        SharedPreferences.Editor spEditor = StoreUserSp.edit();
    /*  Interface used for modifying values in a SharedPreferences object. All
     changes you make in an editor are batched, and not copied back to the original
      SharedPreferences until you call commit() or apply() **/


        spEditor.putString("name", user.name);
        spEditor.putString("birthday", user.email);
        spEditor.putString("city", user.password);

        spEditor.commit();


        //for tempory use Testing
     /*spEditor.putString("name","adee");
    spEditor.putString("email","adee");
    spEditor.putString("password","adee");
    spEditor.putString("cell","adee");
    spEditor.putString("birthday","adee");
    spEditor.putString("birthmonth","adee");
    spEditor.commit();
    **/


    }

    public static User myGetLogInUserLocalData(Context context) {
        SharedPreferences userDataSp = PreferenceManager.getDefaultSharedPreferences(context);
        String name, password, email;
        String cell;
        name = userDataSp.getString("name", "");//" " default value , if no name return " "
        password = userDataSp.getString("password", "");
        email = userDataSp.getString("email", "");
        cell = userDataSp.getString("cell", "");
        String birthday = userDataSp.getString("birthday", "");
        String birthmonth = userDataSp.getString("birthmonth", "");

        User user = new User(name, cell, email, password, birthday, birthmonth);

        return user;
    }

    public static void mySetUserLogInBoolean(Boolean loggedIn, Context context) {
        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(context);
        SharedPreferences.Editor spEditor = sharedPreferences.edit();
        spEditor.putBoolean("loggedIn", loggedIn);
        spEditor.commit();
    }

    public static boolean mygetUserLogInBoolean(Context context) {
        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(context);
        return sharedPreferences.getBoolean("loggedIn", false);
    }

    public static void myClearData(Context context) {
        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(context);
        SharedPreferences.Editor spEdotor = sharedPreferences.edit();
        spEdotor.clear();
        spEdotor.commit();

    }

    public Boolean myGetUserLoginBoolean() {
        if (userLocalDB.getBoolean("login", false) == true) {
            return true;
        } else
            return false;
    }


    public static double getLatitudeSp(Context context) {
        SharedPreferences SpLatLon = PreferenceManager.getDefaultSharedPreferences(context);


        double latitude = Double.longBitsToDouble(SpLatLon.getLong("latitute", Double.doubleToLongBits(0)));

        return latitude;
    }


    public static void storeLatLongSP(double lat, double lon, Context context) {
        SharedPreferences SpLatLon = PreferenceManager.getDefaultSharedPreferences(context);

        SharedPreferences.Editor edts = SpLatLon.edit();
        edts.putLong("latitute", Double.doubleToLongBits(lat));
        edts.putLong("longitute", Double.doubleToLongBits(lon));
        edts.commit();
    }


    public static double getLontitudeSp(Context context) {
        SharedPreferences SpLatLon = PreferenceManager.getDefaultSharedPreferences(context);


        double lontitude = Double.longBitsToDouble(SpLatLon.getLong("longitute", Double.doubleToLongBits(0)));
        return lontitude;
    }

    public static void setUserCurrentLocationSp(String streetAddress, Context context) {
        SharedPreferences SpLatLon = PreferenceManager.getDefaultSharedPreferences(context);

        SharedPreferences.Editor edts = SpLatLon.edit();
        edts.putString("address", streetAddress);
        edts.commit();
    }

    public static String getUserCurrentLocationSp(Context context) {
        String address;
        SharedPreferences SpLatLon = PreferenceManager.getDefaultSharedPreferences(context);

        address = SpLatLon.getString("address", "no address store");
        return address;

    }


    public static void storeNotificationsSp(String notifications, Context context) {
        SharedPreferences spNotification = PreferenceManager.getDefaultSharedPreferences(context);
        SharedPreferences.Editor editor = spNotification.edit();
        editor.putString("notification", notifications);
        editor.commit();


    }

    public static String getNotificationsSp(Context context) {
        String notification;
        SharedPreferences spNotification = PreferenceManager.getDefaultSharedPreferences(context);
        notification = spNotification.getString("notification", "no updates");
        return notification;
    }

    public static void storeFoodList(String foodlist, Context context) {
        SharedPreferences foodlistSp = PreferenceManager.getDefaultSharedPreferences(context);
        SharedPreferences.Editor spEditor = foodlistSp.edit();
        spEditor.putString("foodlist", foodlist);

    }

    public static String getFoodListSp(Context context) {
        SharedPreferences spfoolist = PreferenceManager.getDefaultSharedPreferences(context);
        String flist;
        flist = spfoolist.getString("foodlist", "no food list store");
        return flist;
    }

    public static void storeJsonFoodMenu(JSONObject jsonObject, Context context) {
        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(context);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString("jsonfoodmenu", jsonObject.toString());
        editor.commit();

    }

    public static String getJsonFoodMenuString(Context context) {
        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(context);
        return preferences.getString("jsonfoodmenu", "no jsonstr found");
    }

    public static void storeMenuItemOrderList(Context context,String restrocell, double
            totalPrice, String items, String offerby, String size,
                                              String name, String time, String date) {
        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(context);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString("totalprice", totalPrice + "");
        editor.putString("items", items);
        editor.putString("offerby", offerby);
        editor.putString("size", size);
        editor.putString("name", name);
        editor.putString("time", time);
        editor.putString("date", date);
        editor.putString("restrocell", restrocell);
        editor.commit();
    }


    public static void setCityCountryAddress(Context context, String city, String country, String address) {
        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(context);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString("city", city);
        editor.putString("country", country);
        editor.putString("address", address);
        editor.commit();
    }


    public static String getClientName(Context context) {
        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(context);
        return preferences.getString("name", "client");
    }
    public static String getRestroCell(Context context) {
        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(context);
        return preferences.getString("restrocell", "");
    }

    public static String getCity(Context context) {
        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(context);
        return preferences.getString("city", "City name");
    }

    public static String getCountry(Context context) {
        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(context);
        return preferences.getString("country", "Country name");
    }

    public static String getTotalPriceStr(Context context) {
        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(context);
        return preferences.getString("totalprice", "not found");
    }

    public static String getItems(Context context) {
        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(context);
        return preferences.getString("items", "not found");
    }

    public static String getRestarrant(Context context) {
        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(context);
        return preferences.getString("offerby", "-");
    }

    public static String getSize(Context context) {
        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(context);
        return preferences.getString("size", "-");
    }

    public static String getTime(Context context) {
        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(context);
        return preferences.getString("time", "-");
    }

    public static String getDate(Context context) {
        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(context);
        return preferences.getString("date", "");
    }

    public static void storeFreeOffersSp(String offers, Context context) {
        SharedPreferences foodlistSp = PreferenceManager.getDefaultSharedPreferences(context);
        SharedPreferences.Editor spEditor = foodlistSp.edit();
        spEditor.putString("freeoffers", offers);
        spEditor.commit();

    }

    public static String getFreeOffersSp(Context context) {
        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(context);
        return preferences.getString("freeoffers", "not offers");
    }


    public static void setfetchRestroLatiLongiNamesOnce(boolean offers, Context context) {
        SharedPreferences foodlistSp = PreferenceManager.getDefaultSharedPreferences(context);
        SharedPreferences.Editor spEditor = foodlistSp.edit();
        spEditor.putBoolean("fetchRestroLatiLongiNamesOnce", offers);
        spEditor.commit();

    }

    public static boolean getfetchRestroLatiLongiNamesOnce(Context context) {
        SharedPreferences getfetch = PreferenceManager.getDefaultSharedPreferences(context);
        return getfetch.getBoolean("fetchRestroLatiLongiNamesOnce", false);

    }

    public static void setDistance(Context context, String dist) {
        SharedPreferences timeDistSp = PreferenceManager.getDefaultSharedPreferences(context);
        SharedPreferences.Editor spEditor = timeDistSp.edit();
        spEditor.putString("distance", dist);

        spEditor.commit();
    }

    public static void setTimeByRoad(Context context, String time) {
        SharedPreferences timeDistSp = PreferenceManager.getDefaultSharedPreferences(context);
        SharedPreferences.Editor spEditor = timeDistSp.edit();

        spEditor.putString("timebyroad", time);
        spEditor.commit();
    }

    public static String getDistance(Context context) {
        SharedPreferences timeDistSp = PreferenceManager.getDefaultSharedPreferences(context);
        return timeDistSp.getString("distance", "");

    }

    public static String getTimeByRoad(Context context) {
        SharedPreferences timeDistSp = PreferenceManager.getDefaultSharedPreferences(context);
        return timeDistSp.getString("timebyroad", "");

    }
    public static void setREstaurant(String restro, Context context) {
        SharedPreferences foodlistSp = PreferenceManager.getDefaultSharedPreferences(context);
        SharedPreferences.Editor spEditor = foodlistSp.edit();
        spEditor.putString("offerby1", restro);

    }
    public static void storeCities(String restro, Context context) {
        SharedPreferences foodlistSp = PreferenceManager.getDefaultSharedPreferences(context);
        SharedPreferences.Editor spEditor = foodlistSp.edit();
        spEditor.putString("cities", restro);
spEditor.commit();
    }

    public static String getCities(Context context) {
        SharedPreferences timeDistSp = PreferenceManager.getDefaultSharedPreferences(context);
        return timeDistSp.getString("cities", "");

    }

    public static void storeOwnerNumber(String restro, Context context) {
        SharedPreferences foodlistSp = PreferenceManager.getDefaultSharedPreferences(context);
        SharedPreferences.Editor spEditor = foodlistSp.edit();
        spEditor.putString("ownerNumber", restro);
        spEditor.commit();
    }

    public static String getOwnerNumber(Context context) {
        SharedPreferences timeDistSp = PreferenceManager.getDefaultSharedPreferences(context);
        return timeDistSp.getString("ownerNumber", "");

    }
    public static void storeSubscribtion(String restro, Context context) {
        SharedPreferences foodlistSp = PreferenceManager.getDefaultSharedPreferences(context);
        SharedPreferences.Editor spEditor = foodlistSp.edit();
        spEditor.putString("userblocked", restro);
        spEditor.commit();
    }
    public static String getsub(Context context) {
        SharedPreferences timeDistSp = PreferenceManager.getDefaultSharedPreferences(context);
        return timeDistSp.getString("userblocked", "block");

    }
}

