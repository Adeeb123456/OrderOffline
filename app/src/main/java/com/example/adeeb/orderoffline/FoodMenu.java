package com.example.adeeb.orderoffline;

import android.Manifest;
import android.annotation.TargetApi;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.PendingIntent;
import android.app.ProgressDialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.location.Location;
import android.location.LocationManager;
import android.os.AsyncTask;
import android.os.Bundle;
import android.provider.Settings;
import android.support.design.*;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.telephony.SmsManager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GooglePlayServicesUtil;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationListener;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;


import org.apache.http.params.HttpConnectionParams;
import org.apache.http.params.HttpParams;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.security.Key;
import java.util.ArrayList;
import java.util.Date;

import javax.crypto.Cipher;
import javax.crypto.spec.SecretKeySpec;


public class FoodMenu extends AppCompatActivity implements View.OnClickListener,
        GoogleApiClient.ConnectionCallbacks, GoogleApiClient.OnConnectionFailedListener
        , LocationListener {


    ProgressDialog notificationDialog;
    public static final int CONNECTION_TIME_OUT = 12000;
    JSONObject foodmenuJobj;
    String restrorantName = "", restrorantNumber = "";
    ImageButton imhbtn;

    static boolean isUserPlacOrder = false;
    TextView freeOffersTv = null;
    TextView plswaitTv = null;
    MyCustomAdapter adapter;
    RecyclerView recyclerView;
    Toolbar toolbar;
    LocationRequest locationRequest;
    GoogleApiClient googleApiClient;  //http://developer.android.com/training/location/retrieve-current.html
    static String time, date;
    private double latitude;
    private double lontitude;
    String streetAddress;

    private static LinearLayout linear_order_list_display;
    private static LinearLayout bottom_view_order_list;


    int generateOnce = 0;
    String orderList = "\n";


    Button sendOnline, home, orderhistory, btntab2, sendSms;


    FoodDb foodDb;

    //new collapsing xml stuff
    ImageView imageView;
    Animation animationzoom;
    FloatingActionButton fab1, fab2, fab3;
    static LinearLayout linearLayoutMenuBotm;
    static CoordinatorLayout coordinatorLayout;

    JSONObject orderStrG;


    static TextView totalprice, hide;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_menu);
        Button button = new Button(this);
        button.setText("klk");
        notificationDialog = new ProgressDialog(this);

        notificationDialog.setTitle("Fetching data");
        notificationDialog.setMessage("please wait....");
        notificationDialog.setCancelable(false);
        notificationDialog.setView(button);


        toolbar = (Toolbar) findViewById(R.id.toolmenu);
        //  btntab2=(Button)findViewById(R.id.buttontab2);
        setSupportActionBar(toolbar);


        //   getSupportActionBar().setTitle("Menu");
        // getSupportActionBar().setSubtitle("....");
        // getSupportActionBar().setDisplayShowHomeEnabled(true);
        // getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        //   getSupportActionBar().setLogo(R.drawable.restrobnew2icon);


        //recycler view stuff
        recyclerView = (RecyclerView) findViewById(R.id.rc);

        adapter = new MyCustomAdapter(this, new Data(this).getData());
        recyclerView.setAdapter(adapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        //   bottom_view_order_list=(LinearLayout)findViewById(R.id.bottom_view_order_list);
        linear_order_list_display = (LinearLayout) findViewById(R.id.linear_order_list_display);
        sendOnline = (Button) findViewById(R.id.sendonline);
        sendSms = (Button) findViewById(R.id.sendsms);

        //  home=(Button)findViewById(R.id.buttontab2);
        //   orderhistory=(Button)findViewById(R.id.buttontab3);
        fab1 = (FloatingActionButton) findViewById(R.id.fabm1);
        fab2 = (FloatingActionButton) findViewById(R.id.fabm2);
        fab3 = (FloatingActionButton) findViewById(R.id.fabm3);
        linearLayoutMenuBotm = (LinearLayout) findViewById(R.id.menubottom);
        imageView = (ImageView) findViewById(R.id.img_collapse);


        totalprice = (TextView) findViewById(R.id.tprice);
        hide = (TextView) findViewById(R.id.hide);

        coordinatorLayout=(CoordinatorLayout)findViewById(R.id.coordi_food);

        animationStuff();
        sendSms.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
new AnimationXml(getApplicationContext()).animateview2(linearLayoutMenuBotm);
sendOrderSms();
            }
        });

        sendOnline.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AnimationUtils.Animatebtn(v);

                if (isUserPlacOrder) {
                    if (UserLocalStore.mygetUserLogInBoolean(getApplicationContext())) {
                        startOrderListActivity();
                    } else {
                        //  Toast.makeText(getApplicationContext(),"You Must be Login To Place an Order",Toast.LENGTH_LONG).show();
                        AlertDialog.Builder alertDialog = new AlertDialog.Builder(FoodMenu.this);
                        alertDialog.setTitle("Please Login to place order");
                        alertDialog.setMessage("to place an order You have to Login");
                        alertDialog.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                startActivity(new Intent(getApplicationContext(), Login.class));
                            }
                        });

                        Dialog dialog = alertDialog.create();
                        dialog.show();
                    }

                } else {
                    Toast.makeText(getApplicationContext(), "no item is added to list", Toast.LENGTH_SHORT).show();
                }
            }
        });


        /**    home.setOnClickListener(new View.OnClickListener() {
        @Override public void onClick(View v) {
        AnimationUtils.Animatebtn(v);
        startActivity(new Intent(getApplicationContext(),Restaurant.class));
        }
        });*/


        /**
         orderhistory.setOnClickListener(new View.OnClickListener() {

        @Override public void onClick(View v) {
        AnimationUtils.Animatebtn(v);
        AlertDialog.Builder builder=new AlertDialog.Builder(FoodMenu.this);
        //   builder.setTitle("Free Offers");
        LayoutInflater layoutInflater=LayoutInflater.from(FoodMenu.this);
        final View view=  layoutInflater.inflate(R.layout.free_offers,null);
        freeOffersTv=(TextView)view.findViewById(R.id.freeoffers_tv);
        freeOffersTv.setOnClickListener(new View.OnClickListener() {
        @Override public void onClick(View v) {


        Intent i=new Intent(getApplication(),FoodMenu.class);
        startActivity(i);

        }
        });


        plswaitTv=(TextView)view.findViewById(R.id.pleasewait);
        freeOffersTv.setText(UserLocalStore.getFreeOffersSp(getApplicationContext()));
        builder.setView(view);
        Dialog dialog=builder.create();
        dialog.show();
        new FetchFreeOffersAsyn().execute();



        }
        });
         */


        if (isPlayServicesAvailable()) {
            createLocationRequest();
            buildGoogleClientApi();
        }
        time = java.text.DateFormat.getTimeInstance().format(new Date());
        date = java.text.DateFormat.getDateInstance().format(new Date());

        if (getIntent().getExtras() != null && getIntent().getExtras().getString("restaurantname") != null) {


            restrorantName = getIntent().getExtras().getString("restaurantname");
            restrorantNumber = getIntent().getExtras().getString("restaurant_number");

            //initializationg foodDB
            foodDb = new FoodDb(this, null, null, 1);

            // here we chk if net available we bring data from web if not bring data from local db

            FetchFoodMenu(restrorantName);


        }
        /**  if( !UserLocalStore.getRestarrant(getApplicationContext()).equals("not found")){

         restrorantName = UserLocalStore.getRestarrant(getApplicationContext());
         Toast.makeText(this, "" + restrorantName, Toast.LENGTH_SHORT).show();
         FetchFoodMenu(restrorantName);
         }else {
         Toast.makeText(this, "Sorry Restart Your Application", Toast.LENGTH_SHORT).show();
         } **/

    }

    public void sendOrderSms(){
        JSONObject orderListjson = getSmsDataToOrder();
        orderStrG = orderListjson;
       // new SmsAsyn().execute();

        Snackbar snackbar;
        snackbar=Snackbar.make(coordinatorLayout,"please wait ...",Snackbar.LENGTH_INDEFINITE);
        snackbar.show();
        try {
            snackbar.setText("sending order to [" + restrorantName + "]");
          //  Toast.makeText(this, ""+orderStrG, Toast.LENGTH_SHORT).show();
            sendDataSmsForOrder(restrorantNumber,orderStrG.toString());
        } catch (Exception e) {
            e.printStackTrace();
          //  Toast.makeText(this, "7"+e, Toast.LENGTH_SHORT).show();
        }
    }


    public ArrayList<Information> fetchFoodMenuDataFromDB() {
        // Information informationtemp=new Information();
        ArrayList<Information> foodInfoList;
        /**  informationtemp.food_id=4;
         informationtemp.restaurant_id=1;
         informationtemp.title="kabab crust Pizza nm";
         informationtemp.food_catagory="pizza";
         informationtemp.list_foodprise=444;
         informationtemp.list_foodsize="small";
         informationtemp.ingredients="a b c vd";
         informationtemp.offerby="Italian Piza";
         informationtemp.restaurant_number="03101883575";

         foodDb.addFoodMenu(informationtemp);
         */

        foodInfoList = foodDb.getFoodMenuDb(restrorantNumber);
        return foodInfoList;
    }


    @Override
    protected void onStart() {
        super.onStart();
        if (googleApiClient != null) {
            googleApiClient.connect();
        }
    }

    private void buildGoogleClientApi() {
        googleApiClient = new GoogleApiClient.Builder(this).
                addApi(LocationServices.API).
                addConnectionCallbacks
                        (this).addOnConnectionFailedListener(this).build();

    }

    private void createLocationRequest() {
        locationRequest = new LocationRequest();
        locationRequest.setInterval(10000);
        locationRequest.setSmallestDisplacement(10);
        locationRequest.setFastestInterval(5000);
        locationRequest.setPriority(LocationRequest.PRIORITY_BALANCED_POWER_ACCURACY);
        // locationRequest.setPriority(LocationRequest.PRIORITY_NO_POWER);
    }


    @Override
    public void onConnected(Bundle bundle) {
        if (!isMyGpsEnable()) {
            enableGPs();


            getLatitudeAndLongitude();
        } else {
            getLatitudeAndLongitude();
        }
    }

    public boolean isMyGpsEnable() {
        boolean gps;
        LocationManager locationManager = (LocationManager) getSystemService(LOCATION_SERVICE);
        gps = locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER);


        return gps;
    }

    public void enableGPs() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("GPS IS OFF");
        builder.setMessage("Turn On GPS to use Our service");
        builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                Intent intent = new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS);
                startActivity(intent);
            }
        });

        Dialog dialog = builder.create();
        dialog.show();


    }


    @Override
    public void onConnectionSuspended(int i) {

    }


    @Override
    public void onConnectionFailed(ConnectionResult connectionResult) {

    }

    public Boolean isPlayServicesAvailable() {
        int resultCode = GooglePlayServicesUtil.isGooglePlayServicesAvailable(this);
        if (resultCode == ConnectionResult.SUCCESS) {
            return true;
        } else
            return false;
    }


    public void getLatitudeAndLongitude() {

        //mLastLocation= LocationServices.FusedLocationApi.getLastLocation(googleApiClient);

        checkPermission();
        LocationServices.FusedLocationApi.requestLocationUpdates(googleApiClient, locationRequest, this);
//storeLatitudeAndLongitudeAndCallGeoCodder(mLastLocation);

    }
    @TargetApi(23)
    public boolean checkPermission(){

            return  true;
    }

    public void storeLatitudeAndLongitudeInLocalStore(Location location){
        latitude=location.getLatitude();
        lontitude=location.getLongitude();

      UserLocalStore.storeLatLongSP(latitude, lontitude, getApplicationContext());





    }


    @Override
    public void onLocationChanged(Location location) {
        storeLatitudeAndLongitudeInLocalStore(location);
    }



    private void initializeFoodMenu() {



    }

    private void mysetListeners() {

    }

    public void myfindViews(){


    }


    @Override
    public boolean onCreateOptionsMenu(android.view.Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.


        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        switch (id){
            case R.id.action_settings:
                Toast.makeText(this, "", Toast.LENGTH_SHORT).show();
                break;
            case R.id.aboutus:
                AlertDialog.Builder builder=new AlertDialog.Builder(FoodMenu.this);
                builder.setIcon(R.drawable.pizaper2).setTitle("About Us").
                setMessage("Digital Waiter will get Your current location and display List of all restaurants that offers pizza " +
                        "and at what distance they are , how much time it will take to reach a particular restaurant ");
                builder.setPositiveButton("OK", null);

                Dialog dialog=builder.create();
                dialog.show();

                break;
            case R.id.help:
                AlertDialog.Builder builder1=new AlertDialog.Builder(FoodMenu.this);
                builder1.setIcon(R.drawable.pizaper2).setTitle("About Us").
                        setMessage("Digital Waiter will get Your current location and display List of all restaurants that offers pizza " +
                                "and at what distance they are , how much time it will take to reach a particular restaurant ");
                builder1.setPositiveButton("OK", null);

                Dialog dialog1=builder1.create();
                dialog1.show();
                break;
            case R.id.logout:
                UserLocalStore.myClearData(getApplicationContext());
                finish();

                startActivity(new Intent(getApplicationContext(), Login.class));

                Toast.makeText(this, "Logout Successfully ! thanks for using DW", Toast.LENGTH_SHORT).show();

                break;
            case R.id.refresh:
                FetchFoodMenu("mrpizza");
                Toast.makeText(this, "Updating Menu items Please Wait", Toast.LENGTH_SHORT).show();
                break;





            case R.id.userprofile:
                if(UserLocalStore.mygetUserLogInBoolean(getApplicationContext())){
                    LayoutInflater layoutInflater= LayoutInflater.from(FoodMenu.this);
                    View view=layoutInflater.inflate(R.layout.user_details, null);
                    TextView textView=(TextView)view.findViewById(R.id.name_ud);
                    TextView textView1=(TextView)view.findViewById(R.id.email_ud);
                    TextView textView2=(TextView)view.findViewById(R.id.cell_ud);
                    textView.setText("Name: " + UserLocalStore.myGetLogInUserLocalData(getApplicationContext()).name);
                    textView1.setText("Email: "+UserLocalStore.myGetLogInUserLocalData(getApplicationContext()).email);
                    textView2.setText("Cell#: "+UserLocalStore.myGetLogInUserLocalData(getApplicationContext()).cell);
                    builder=new AlertDialog.Builder(FoodMenu.this);
                    builder.setIcon(R.drawable.pizaper2);
                    builder.setView(view);

                    dialog=builder.create();
                    dialog.show();
                }else {
                    Toast.makeText(this, "Please Login..", Toast.LENGTH_SHORT).show();
                    startActivity(new Intent(getApplicationContext(), Login.class));
                }


                break;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){


        }
    }






public  void startOrderListActivity(){

    // start orderList activity:
    Intent orderListIntent=new Intent(this,OrderInfo.class);
    orderListIntent.putExtra("itemlist", OrderUi.items);
    orderListIntent.putExtra("latitute", OrderUi.lati);
    orderListIntent.putExtra("logtitude", OrderUi.longi);
    startActivity(orderListIntent);

}
   // unuse bcoz it will only use when we dislay orderlist with in this interface



public static void callfromadap(Context context){


   // dispalyOrderList(context);
}

public static void animateMenuBotm(Context context){
    Animation animation= android.view.animation.AnimationUtils.loadAnimation(context,R.anim.slide_down);
    linearLayoutMenuBotm.startAnimation(animation);
}

    public static void animateIconBtn(Context context,ImageButton imageButton){
        Animation animation= android.view.animation.AnimationUtils.loadAnimation(context,R.anim.move);
        imageButton.startAnimation(animation);
    }

static int animationCount=0;
    public static void dispalyOrderList(Context context,String restroCell,
                                        String singleItem,String itemList,String restaurantNam,
                                        double ttalPrice,String size) {

        isUserPlacOrder=true;
linearLayoutMenuBotm.setVisibility(View.VISIBLE);
        if(animationCount==0)
        {
            animateMenuBotm(context);
            animationCount=1;
        }
        ImageButton dynambtn=new ImageButton(context);
        LinearLayout.LayoutParams dynparams=new
                LinearLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        dynparams.setMargins(0, 0, 0, 0);
        dynambtn.setBackgroundResource(R.drawable.piza3small);
        dynambtn.setLayoutParams(new ViewGroup.LayoutParams(30, 30));
        dynambtn.setLayoutParams(dynparams);

        TextView textView=new TextView(context);
        textView.setText(singleItem);
        textView.setTextSize(View.resolveSize(10, 0));
       // textView.setLayoutParams(new ViewGroup.LayoutParams(50,25));
User user=UserLocalStore.myGetLogInUserLocalData(context);
        String name=user.name;
        UserLocalStore.storeMenuItemOrderList(context, restroCell, ttalPrice, itemList, restaurantNam, size, name, time, date);
        Toast.makeText(context, "Total price: " + UserLocalStore.getTotalPriceStr(context)
                , Toast.LENGTH_LONG).show();
        totalprice.setText("Total price: " + ttalPrice);
        linear_order_list_display.addView(dynambtn);
        linear_order_list_display.addView(textView);
AnimationUtils.Animatebtn(dynambtn);
       AnimationUtils.Animatebtn2(linear_order_list_display);
//animateIconBtn(context,dynambtn);
    }





    public void FetchFoodMenu(String restorantName){

//  notificationDialog.show();
        this.restrorantName=restorantName;
        new GetServerFoodMenuAsyn().execute();

    }



    public class GetServerFoodMenuAsyn extends AsyncTask<Void,Void,JSONObject> {
        HttpURLConnection httpURLConnection;
           URL url = null;

        @Override
        protected JSONObject doInBackground(Void... params) {
            String data="";
            JSONObject jsonObject=new JSONObject();
            try {
                JSONObject serverJsondata;
                //  url=new URL("http://pakdigitalwaiter.netne.net/restroLatiLongiDownload.php");

                url=new URL("http://pdwaiter.net16.net/foodmenu.php");
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
                        myCheckInternet(getApplicationContext())) {

                    httpURLConnection.connect();

                    JSONObject data1 = new JSONObject();
                    data1.put("restaurantNumber", restrorantNumber);


                    DataOutputStream dataOutputStream = new DataOutputStream(httpURLConnection.getOutputStream());
                    dataOutputStream.writeBytes(data1.toString());
                    dataOutputStream.flush();
                    dataOutputStream.close();

                    int response = httpURLConnection.getResponseCode();
                    String line = "";
                    if (response == httpURLConnection.HTTP_OK) {

                        BufferedReader bufferedReader = new BufferedReader(new
                                InputStreamReader(httpURLConnection.getInputStream()));
                        while ((line = bufferedReader.readLine()) != null) {
                            data += line;

                        }
                        Log.i("TA", data);
                        serverJsondata = new JSONObject(data);
                        jsonObject = serverJsondata;
                    } else {
                        if (response == httpURLConnection.HTTP_CLIENT_TIMEOUT) {

                        } else {
                        }

                    }
                }

            } catch (MalformedURLException e) {
                e.printStackTrace();
                httpURLConnection.disconnect();
            } catch (IOException e) {
                httpURLConnection.disconnect();
                e.printStackTrace();
            } catch (JSONException e) {
                e.printStackTrace();
                httpURLConnection.disconnect();
            }


            return jsonObject;




        }

        @Override
        protected void onPostExecute(JSONObject jsonObject) {
            super.onPostExecute(jsonObject);
            Toast.makeText(getApplicationContext(), "post execute", Toast.LENGTH_SHORT).show();
            if(jsonObject!=null||!(jsonObject.isNull("arraySiz"))){


//               UserLocalStore.storeJsonFoodMenu(foodmenuJobj, getApplicationContext());
          //  adapter.updateFoodMenu(restrorantNumber );
          notificationDialog.dismiss();
                fetchFoodMenuDataFromDB();
                adapter.updateFoodMenuLocalDB(restrorantNumber);
            }
            else {
                fetchFoodMenuDataFromDB();
                adapter.updateFoodMenuLocalDB(restrorantNumber);
            }
        }

    }

    URL urlfreeOffers;
    HttpURLConnection httpURLConnectionfreeOffers;
    String freeofferStr="",offersserver="";
    JSONObject freeOfferJson;
    public class FetchFreeOffersAsyn extends AsyncTask<Void,Void,Void> {
        @Override
        protected Void doInBackground(Void... params) {
            try {
               // urlfreeOffers=new URL("http://pakdigitalwaiter.netne.net/freeoffers.php");

                urlfreeOffers=new URL("http://pdwaiter.net16.net/freeoffers.php");
                httpURLConnectionfreeOffers=(HttpURLConnection)urlfreeOffers.openConnection();
                httpURLConnectionfreeOffers.setChunkedStreamingMode(0);
                httpURLConnectionfreeOffers.setRequestMethod("GET");
                httpURLConnectionfreeOffers.setDoInput(true);
                httpURLConnectionfreeOffers.setDoInput(true);
                httpURLConnectionfreeOffers.connect();

                int response=httpURLConnectionfreeOffers.getResponseCode();
                String line="";
                if(response==httpURLConnectionfreeOffers.HTTP_OK){
BufferedReader bufferedReader=new BufferedReader(new InputStreamReader(httpURLConnectionfreeOffers.getInputStream()));

if((line=bufferedReader.readLine())!=null){
offersserver+=line;
}
                }


                JSONObject jsonObject=new JSONObject(offersserver);
                if(jsonObject!=null) {
                    freeofferStr=jsonObject.getString("freeoffers");


                  //  freeOfferJson = jsonObject;
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

plswaitTv.setText("...");
            //String offers=UserLocalStore.getFreeOffersSp(getApplicationContext());
            //String offersSp;

            UserLocalStore.storeFreeOffersSp(freeofferStr, getApplicationContext());
            freeOffersTv.setText(UserLocalStore.getFreeOffersSp(getApplicationContext()));
          //  freeOffersTv.setText(freeofferStr);
        }
    }

    @Override
    public void finish() {
        super.finish();
        Toast.makeText(getApplicationContext(), "finish", Toast.LENGTH_SHORT).show();
    }


    public void animationStuff(){
        animateCollapsingImg();
        animateFab();
    }

    public void animateFab(){
        Animation animationRotate;
        animationRotate= android.view.animation.AnimationUtils.loadAnimation(getApplicationContext(), R.anim.rotate);
        animationRotate.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {

            }

            @Override
            public void onAnimationEnd(Animation animation) {

            }

            @Override
            public void onAnimationRepeat(Animation animation) {

            }
        });

        fab1.startAnimation(animationRotate);
        fab2.startAnimation(animationRotate);
        fab3.startAnimation(animationRotate);
    }
    public void animateCollapsingImg(){
        animationzoom= android.view.animation.AnimationUtils.loadAnimation(getApplicationContext(), R.anim.zoom_out);
        animationzoom.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {
                fab2.setBackgroundColor(Color.TRANSPARENT);
                fab2.setBackgroundColor(Color.TRANSPARENT);
            }

            @Override
            public void onAnimationEnd(Animation animation) {

            }

            @Override
            public void onAnimationRepeat(Animation animation) {

            }
        });

        imageView.startAnimation(animationzoom);
    }

    public JSONObject getSmsDataToOrder(){

        double lati;
        double longi;
        double  total_price;
        String time="",name="",country="",city="",items="",restrocell="",size="",date,totalPriceStr;
        JSONObject jsonObjectsms;
        String orderStr="";
    // because json create problem in encryption;

        lati = UserLocalStore.getLatitudeSp(getApplicationContext());
        longi = UserLocalStore.getLontitudeSp(getApplicationContext());
        city=UserLocalStore.getCity(getApplicationContext());
        country=UserLocalStore.getCountry(getApplicationContext());
        items=UserLocalStore.getItems(getApplicationContext());
        total_price= Double.parseDouble(UserLocalStore.getTotalPriceStr(getApplicationContext()));
        date=UserLocalStore.getDate(getApplicationContext());
        size=UserLocalStore.getSize(getApplicationContext());
        name=UserLocalStore.getClientName(getApplicationContext());


        jsonObjectsms=new JSONObject();
        try {
            jsonObjectsms.put("code","#2019co");// 95,9-5=4,9*5=45
            jsonObjectsms.put("tp",total_price);
            jsonObjectsms.put("size",size);
            jsonObjectsms.put("items",items);
            jsonObjectsms.put("date",date);
            jsonObjectsms.put("lati",lati);
            jsonObjectsms.put("longi",longi);
            jsonObjectsms.put("cn",name);

         //   orderStr="keyWord:#ad2019-tprice:"+totalPriceStr+"-size:"+size+"-items:"+items+"-date:"+date+"-lati:"+lati
            //        +"-longi:"+longi;

        } catch (JSONException e) {
            e.printStackTrace();
        }
        try {









        } catch (Exception e) {
            e.printStackTrace();
        }
return jsonObjectsms;
    }

    String  smsSendResult="";
    public void sendDataSmsForOrder(String ownerNmbr,String smsBody){


        PendingIntent pendingIntent=PendingIntent.getBroadcast(getApplicationContext(),0,new Intent("SMS_SENT"),0);
        registerReceiver(new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {
                switch (getResultCode()) {
                    case Activity.RESULT_OK:
                        // Toast.makeText(context, "successfully registered", Toast.LENGTH_SHORT).show();
                        smsSendResult = "success";
                        break;
                    case SmsManager.RESULT_ERROR_GENERIC_FAILURE:
                        smsSendResult = "Sorry ! fail to submit, Generic failure";

                        break;
                    case SmsManager.RESULT_ERROR_NO_SERVICE:
                        smsSendResult = "Sorry ! fail to submit, No Mobile service";

                        break;
                    case SmsManager.RESULT_ERROR_NULL_PDU:
                        smsSendResult = "Sorry ! fail to submit ,Null PDU";

                        break;
                    case SmsManager.RESULT_ERROR_RADIO_OFF:
                        smsSendResult = "Sorry ! fail to submit order,Radio off";

                        break;
                }
                // Toast.makeText(context, smsSendResult, Toast.LENGTH_SHORT).show();
                if (smsSendResult == "success") {
                    cleanDataAfterOrder();
                    Snackbar snackbar = Snackbar.make(coordinatorLayout, "Order submitted. you will soon receive a confirmation text", Snackbar.LENGTH_INDEFINITE)
                            .setAction("OK", new View.OnClickListener() {
                                @Override
                                public void onClick(View view) {

                                }
                            });
                    snackbar.show();
                } else {
                    final Snackbar snackbar =
                            Snackbar.make(coordinatorLayout, smsSendResult, Snackbar.LENGTH_INDEFINITE).setAction("Retry", new View.OnClickListener() {
                                @Override
                                public void onClick(View view) {
                                    Snackbar snackbar1 = Snackbar.make(coordinatorLayout, "please wait", Snackbar.LENGTH_LONG);
                                    snackbar1.show();
                                    try {
                                      //  storeAndSendUserData(getFieldData());
                                    } catch (Exception e) {
                                        e.printStackTrace();
                                    }

                                }
                            });
                    snackbar.show();
                }
            }
        },new IntentFilter("SMS_SENT"));





        String smsbdy="";
        smsbdy=smsBody;
        short port=6734;
        SmsManager smsManager=SmsManager.getDefault();
        byte[] smsBodyBytes=smsbdy.getBytes();
        String prtsData="";
        String remainingPrt="";
        char[] charArray=smsbdy.toCharArray();

        int c=0,remaing=charArray.length;

        for (int i = 0; i < charArray.length; i++) {
            if (remaing > 133) {
                if (c < 133) {
                    prtsData += charArray[i];
                    c++;

                }

                if (c == 133) {

                    smsManager.sendDataMessage(ownerNmbr, null, port, prtsData.getBytes(), pendingIntent, null);
                    Toast.makeText(getApplicationContext(),ownerNmbr,Toast.LENGTH_LONG).show();
                    prtsData="";
                    Log.i("part 1", ownerNmbr);
                    remaing = remaing - c;
                    c = 0;
                }
            } else {

                remainingPrt += charArray[i];

            }
        }

        smsManager.sendDataMessage(ownerNmbr, null, port, remainingPrt.getBytes(), pendingIntent, null);
        Log.i("part 2", ownerNmbr + remainingPrt);
        remainingPrt="";
        remaing=charArray.length;
        //Toast.makeText(getApplicationContext(),"part 2 sened"+ownerNmbr,Toast.LENGTH_LONG).show();

//        smsManager.sendDataMessage(phn, null, port, smsBodyBytes, null, null);


        //   smsManager.sendDataMessage(phn, null, port, prtsData.getBytes(), null, null);

    }



public void cleanDataAfterOrder(){
    UserLocalStore.storeMenuItemOrderList(getApplicationContext(), "", 0, "", "", "", "", "", "");
   // Toast.makeText(getApplicationContext(),"clean",Toast.LENGTH_SHORT).show();
    MyCustomAdapter.cleanTprice();
linearLayoutMenuBotm.setVisibility(View.INVISIBLE);
}




}



