package com.example.adeeb.orderoffline;

import android.app.ProgressDialog;
import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;
import android.widget.Toast;


import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;

/**
 * Created by AdeeB on 9/30/2015.
 */
public class ServerRequest {

    ProgressDialog progressDialog;
    ProgressDialog orderProgressDialog;
    ProgressDialog notificationDialog;
    public static final int CONNECTION_TIME_OUT = 1000 * 15;
    public static final String SERVER_ADDRESS = "http://pdwaiter.net16.net/";

    String homeaddress;
    User user;
    Context context;
    double lati;
    double longi;
    String time="",name="",country="",city="",items="",restorant="",size="",date,totalPriceStr;
    String orderResponse = "";
    String notifications="";
    String foodlistRetur="";
    String restrorantName="";
    JSONObject foodmenuJobj;
    public String Registrationresponse = null;
    public ServerRequest(Context contex) {
        progressDialog = new ProgressDialog(contex);
        progressDialog.setCancelable(false);
        progressDialog.setTitle("Sending Request to Server");
        progressDialog.setMessage("please Wait...");
        this.context = contex;
    }                                                 //callback is when user data finished storing we call back from backgrnd

    public void myStoreUserDataInBackground(User user, GetUserCallBack userCallBack) {

        //when the backgrnd process starts we want progress to show
        progressDialog.show();
        // when above method calls we want the asyntask class will starts
        new StoreUserDataAsynTask(user, userCallBack).execute();

    }

    //callback is when user data finished fetching we call back from backgrnd
    public void myFetchUserDataInBackground(User user, GetUserCallBack userCallBack) {
        progressDialog.show();
        new FetchUserDataAsynTask(user, userCallBack).execute();
    }// asyntsk takes no inputdata,donot want to receive progress,and no retuns


    public class StoreUserDataAsynTask extends AsyncTask<Void, Void, Void> {
        // when storing data in backgrnd we neeed the constructor to note user whose data is being store
        User user;

        //and also note user call back that should iform when it finishes
        GetUserCallBack userCallBack;

        //constructor
        public StoreUserDataAsynTask(User user, GetUserCallBack userCallBack) {
            this.user = user;
            this.userCallBack = userCallBack;

        }

        @Override
        protected Void doInBackground(Void... params) {
            // Create data variable for sent values to server



            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);
            progressDialog.dismiss();
           // User user = new User("", "");
          //  user.setRegistrationResponse(Registrationresponse);
          //  userCallBack.myDone(user);// tell the user background process is finish
        }
    }

    public class FetchUserDataAsynTask extends AsyncTask<Void, Void, User> {
        // when storing data in backgrnd we neeed the constructor to note user whose data is being store
        User user;

        //and also note user call back that should iform when it finishes
        GetUserCallBack userCallBack;

        //constructor
        public FetchUserDataAsynTask(User user, GetUserCallBack userCallBack) {
            this.user = user;
            this.userCallBack = userCallBack;

        }

        @Override
        protected User doInBackground(Void... params) {


            return  null;
        }

        @Override
        protected void onPostExecute(User returneduser) {
            super.onPostExecute(returneduser);
            progressDialog.dismiss();

            userCallBack.myDone(returneduser);// tell the user background process is finish
        }
    }

    GetUserCallBack orderCallback;

    public void mySendOrder(String homeaddress,GetUserCallBack serverResponse) {

        this.homeaddress = homeaddress;
        orderCallback=serverResponse;
time=UserLocalStore.getTime(context);


        user = UserLocalStore.myGetLogInUserLocalData(context);
        lati = UserLocalStore.getLatitudeSp(context);
        longi = UserLocalStore.getLontitudeSp(context);
        city=UserLocalStore.getCity(context);
        country=UserLocalStore.getCountry(context);
        items=UserLocalStore.getItems(context);
        totalPriceStr=UserLocalStore.getTotalPriceStr(context);
        date=UserLocalStore.getDate(context);

     restrorantName=UserLocalStore.getRestarrant(context);
        Toast.makeText(context, "" + user.cell, Toast.LENGTH_SHORT).show();
        orderProgressDialog=new ProgressDialog(context);
        orderProgressDialog.setTitle("Admin Response");
        orderProgressDialog.setMessage("please wait ...");
        orderProgressDialog.setCanceledOnTouchOutside(false);

       progressDialog.show();
        new SendOrderAsynTask().execute();
    }

    public class SendOrderAsynTask extends AsyncTask<Void, Void, Void> {
        @Override
        protected Void doInBackground(Void... params) {


            return null;
        }


        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);

            progressDialog.dismiss();

          //  User user=new User("","");
          //  user.setOrderResponse(orderResponse);
         // orderCallback.myDone(user);

        }
    }


    public void FetchNotications(){
        notificationDialog=new ProgressDialog(context);
        notificationDialog.setTitle("Fetching data");
        notificationDialog.setMessage("please wait....");
        notificationDialog.show();
        String s="welcome to Italian pizza restauren you will get 20 percent discount";

        UserLocalStore.storeNotificationsSp(s, context);
        new FetchNotifications().execute();

    }

    public class FetchNotifications extends AsyncTask<Void,Void,Void> {

        @Override
        protected Void doInBackground(Void... params) {



            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);
            UserLocalStore.storeNotificationsSp(notifications,context);
            notificationDialog.dismiss();
        }
    }

    public void FetchFoodList(String restorantName){
        notificationDialog=new ProgressDialog(context);
        notificationDialog.setTitle("Fetching data");
        notificationDialog.setMessage("please wait....");
        notificationDialog.show();
        this.restrorantName=restorantName;
        new GetServerFoodListAsyn().execute();

    }

    public class GetServerFoodListAsyn extends AsyncTask<Void,Void,Void> {
        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);
            UserLocalStore.storeFoodList(foodlistRetur,context);
            notificationDialog.dismiss();
        }

        @Override
        protected Void doInBackground(Void... params) {




            return null;
        }
    }

    HttpURLConnection httpURLConnection;
    URL url;
    String feedbk,restroname;
    Context contextfeedbk;

    public void sendFeedBack(String feedbk,Context context,String restroname){
        this.feedbk=feedbk;
        this.contextfeedbk=context;
        this.restroname=restroname;
        new FeedBack().execute();
    }

public class FeedBack extends AsyncTask<Void,Void,Void> {
    @Override
    protected Void doInBackground(Void... params) {
        String data="";
        JSONObject jsonObject=new JSONObject();
        try {
            JSONObject serverJsondata;
            //  url=new URL("http://pakdigitalwaiter.netne.net/restroLatiLongiDownload.php");

            url=new URL(" http://pdwaiter.net16.net/feedback.php");
            //     http://maps.googleapis.com/maps/api/distancematrix/json?origins=34.2007766,73.2466427&destinations=34.1887583,73.2312702&mode=driving&language=en-EN&sensor=false
            //   url=new URL("http://maps.googleapis.com/maps/api/distancematrix/json?origins=34.2007766,73.2466427&destinations=34.1887583,73.2312702&mode=driving&language=en-EN&sensor=false\n");
            httpURLConnection=(HttpURLConnection)url.openConnection();
            httpURLConnection.setChunkedStreamingMode(0);
            httpURLConnection.setDoInput(true);
            httpURLConnection.setDoOutput(true);

//httpURLConnection.setConnectTimeout(100);
            httpURLConnection.setReadTimeout(15000);
            httpURLConnection.setRequestMethod("POST");
            httpURLConnection.setRequestProperty("Content-Type",
                    "application/x-www-form-urlencoded");
            if(CheckInternetConnection.
                    myCheckInternet(contextfeedbk)) {
                httpURLConnection.connect();

                JSONObject data1 = new JSONObject();
                User user=UserLocalStore.myGetLogInUserLocalData(contextfeedbk);
                String cell=user.cell;


                data1.put("feedback", feedbk);
                data1.put("cell",cell);
                data1.put("restroname",restroname);


                DataOutputStream dataOutputStream = new DataOutputStream(httpURLConnection.getOutputStream());
                dataOutputStream.writeBytes(data1.toString());
                dataOutputStream.flush();
                dataOutputStream.close();

                int response = httpURLConnection.getResponseCode();
                String line = "";
                if (response == httpURLConnection.HTTP_OK) {


                    Log.i("TA", data);

                } else {
                    if (response == httpURLConnection.HTTP_CLIENT_TIMEOUT) {
                    } else {
                    }

                }
            }

        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (JSONException e) {
            e.printStackTrace();
        }

        return null;
    }

    @Override
    protected void onPostExecute(Void aVoid) {
        super.onPostExecute(aVoid);
        httpURLConnection.disconnect();
    }
}

}