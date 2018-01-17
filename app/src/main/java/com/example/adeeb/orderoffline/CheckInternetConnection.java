package com.example.adeeb.orderoffline;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;

/**
 * Created by AdeeB on 4/23/2016.
 */
public class CheckInternetConnection {
    Context context;

    public static Boolean myCheckInternet(Context context) {
        ConnectivityManager connectivityManager=(ConnectivityManager)context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo=connectivityManager.getActiveNetworkInfo();
        if(networkInfo!=null&&networkInfo.isConnected()&&networkInfo.isAvailable()){
            return true;
        }
        else
            return false;

    }
}
