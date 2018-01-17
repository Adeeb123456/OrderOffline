 package com.example.adeeb.orderoffline;

import android.Manifest;
import android.annotation.TargetApi;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentSender;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationManager;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.provider.Settings;
import android.support.design.widget.AppBarLayout;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.telephony.SmsManager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
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

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;

public class Restaurant extends ActionBarActivity implements GoogleApiClient.ConnectionCallbacks,GoogleApiClient.OnConnectionFailedListener,LocationListener {
String TAG="restrolati";
    ProgressDialog progressDialog;
    LocationRequest locationRequest;
    GoogleApiClient googleApiClient;
    // Request code to use when launching the resolution activity
    private static final int REQUEST_RESOLVE_ERROR = 1001;
    // Unique tag for the error dialog fragment
    private static final String DIALOG_ERROR = "dialog_error";
    // Bool to track whether the app is already resolving an error
    private boolean mResolvingError = false;

    private   double latitudeCurrent=0;
    private double lontitudeCurrent=0;
    HttpURLConnection httpURLConnection;
     static    URL url = null;

    Location userLocation;
    Location restroLocations[]=null;
    Location Restrolocation;
  public static String restroNames[]={"A","B","C"};
   public static double restroLati[]={0.0,0.0,0.0};
   public static double restroLongi[];
    public static String restroNumbers[];
    int indrexrestro=-1;
    double restrolati[],restrolongi[];
    int GpsCount=0; // if gps detect lati loni than only once the server req should be call

  public static ArrayList<InformationRestro> data;
  public static String timeByfoot[]={"-"};
    String timeByRoad[]={"-"};
    String distanceFromGoogleMtrx[]={"-"};
    int arraySiz=3;
   // int arraySizDivider;

    double roundOffDistances[];
    double distances[];
    double distancesInKmforTimeCal[];
    String distancesInKmStr[];
     // distance from googlematrix

    String distancesStr="";
    double tempDouble;
    String tempStr;
//TextView textView;
    //ui stuff
    Toolbar toolbarRestroDistances;
    DrawerLayout drawerLayoutRestroDistances;
    LinearLayout linearLayoutRestroDistances;
    ActionBarDrawerToggle actionBarDrawerToggleRestroDistances,actionBarDrawerToggleRestroAppbar;
    LinearLayout LLayout_restro_latilongiDrawer;
    LinearLayout LLayout_restro_latilongiDrawerEnd;
    LinearLayout LLayout_restro_latilongiFindByCity;
    LinearLayout LLayout_restro_latilongikey;
    Button login_or_signup_navigationalBtn;
    Boolean highAccuracyFail=false;




RecyclerView rvRestroDistances;
    RestroAdapter restroAdapter;

    LinearLayout restro_map_liner;
   // LinearLayout my_location_liner;

    RestaurantListDb restaurantListDb;
    AppBarLayout appBarLayout;
    NavigationView navigationView;
    CoordinatorLayout coordinatorLayout;
    ImageView imageViewbg;
    String urlImgbg="https://pakbit.000webhostapp.com/AndroidImges/mainbg2.png";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_restaurants_distances);
        toolbarRestroDistances=(Toolbar)findViewById(R.id.toolbar_restro_latilongi);
        drawerLayoutRestroDistances=(DrawerLayout)findViewById(R.id.drawerlout_restro_latilongi);
        navigationView=(NavigationView)findViewById(R.id.navigation_view);
        coordinatorLayout=(CoordinatorLayout)findViewById(R.id.cordinatlayout_restro);
        imageViewbg=(ImageView)findViewById(R.id.bgimage_restrolist);

        new PicassaImgDownload(getApplicationContext()).reqPicassa(urlImgbg,imageViewbg);

        new AnimationXml(getApplicationContext()).animateCollapsingview(imageViewbg);
      //t  restro_map_liner=(LinearLayout)findViewById(R.id.restro_map_liner);
       //  my_location_liner=(LinearLayout)findViewById(R.id.my_location_liner);
       //t LLayout_restro_latilongiDrawer=(LinearLayout)findViewById(R.id.linear_restro_latilongi);
       //t LLayout_restro_latilongiDrawerEnd=(LinearLayout)findViewById(R.id.linear_restro_latilongi_end);
       //t LLayout_restro_latilongiFindByCity=(LinearLayout)findViewById(R.id.my_location_liner_find_by_city);
      //t  LLayout_restro_latilongikey=(LinearLayout)findViewById(R.id.my_location_liner_key);

      //t  appBarLayout=(AppBarLayout)findViewById(R.id.apprestro);
        setListnersOnMapViews();

        setSupportActionBar(toolbarRestroDistances);
        getSupportActionBar().setTitle("home");
        getSupportActionBar().setSubtitle("...");
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        //getSupportActionBar().setHomeButtonEnabled(true);

        actionBarDrawerToggleRestroDistances=new ActionBarDrawerToggle(this,drawerLayoutRestroDistances,toolbarRestroDistances,
                R.string.open,R.string.close);
        actionBarDrawerToggleRestroDistances.syncState(); // it will display hand barger manu , without wich it was not displaye
        // only back arrow will display;
drawerLayoutRestroDistances.setDrawerListener(actionBarDrawerToggleRestroDistances);


        //recycler view part

        rvRestroDistances=(RecyclerView)findViewById(R.id.recycler_restro_distances);
      //t  login_or_signup_navigationalBtn=(Button)findViewById(R.id.login_or_signup_navigational);
//tt        login_or_signup_navigationalBtn.setOnClickListener(new View.OnClickListener() {
    //        @Override
     //       public void onClick(View v) {
            //    startActivity(new Intent(getApplicationContext(),Login.class));
     //       }
     //   });

      //  LLayout_restro_latilongiFindByCity.setOnClickListener(new View.OnClickListener() {
        //    @Override
         //   public void onClick(View v) {
            //    if(drawerLayoutRestroDistances.isDrawerOpen(LLayout_restro_latilongiDrawerEnd)){
             //       drawerLayoutRestroDistances.closeDrawer(LLayout_restro_latilongiDrawerEnd);

              //  }else {
                //    drawerLayoutRestroDistances.openDrawer(LLayout_restro_latilongiDrawerEnd);
              //  }
         //   }
     //   });

    //    LLayout_restro_latilongikey.setOnClickListener(new View.OnClickListener() {
      //      @Override
        //    public void onClick(View v) {
         //       startActivity(new Intent(getApplicationContext(), Login.class));
         //   }
      //  });
rvRestroDistances.addOnScrollListener(new RecyclerView.OnScrollListener() {
    @Override
    public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
        super.onScrollStateChanged(recyclerView, newState);
     //   AnimationUtils.Animatescroll(recyclerView);
    }
});

        navigationView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(MenuItem item) {
                item.setChecked(true);
                drawerLayoutRestroDistances.closeDrawers();
                Toast.makeText(getApplicationContext(),item.getTitle(),Toast.LENGTH_SHORT).show();
switch (item.getItemId()){
    case R.id.regbirthitem:
        startActivity(new Intent(getApplicationContext(),BirthdayStart.class));
        break;
    case R.id.regrestroitem:
        break;
    case R.id.nav3:

        break;
    case R.id.nav2:
        break;
}
                return true;
            }
        });

//textView=(TextView)findViewById(R.id.restro_distances);

        progressDialog=new ProgressDialog(this);
        progressDialog.setTitle("Searcing Restaurants");
        progressDialog.setMessage("Restaurants in Your Area");
        progressDialog.setCancelable(false);
        progressDialog.setIcon(R.drawable.searchicon);


        if(isPlayServicesAvailable()){
            createLocationRequest();
            buildGoogleClientApi();
            if(googleApiClient!=null){
                googleApiClient.connect();

            }
            else
            {
               // Toast.makeText(getApplicationContext(), "issue with Gps, Please restart the app or Turn on Gps...", Toast.LENGTH_LONG).show();
            }
        }
       // getLatitudeAndLongitude();

        setMyUserProfileDetailsDrawer();


   //callRecyclerAdapterTempBeforeFetchingServerData();

        //temporary use for testing
      //  UserLocalStore.myStoreUserData(new User("hjh","jkk"),getApplicationContext());
        restaurantListDb=new RestaurantListDb(this,null,null,1);
     //  restroAdapter = new RestroAdapter(getApplicationContext(),getRestroListDatafromLocalDB());
       // storeDataToLocalDB();// we use this in server req part ,latter

        initializeGeoFence();
    }

public void feedBack(){
    AlertDialog.Builder builder=new AlertDialog.Builder(getApplicationContext());
    builder.setTitle("Your Feedback");
    final EditText editText=new EditText(getApplicationContext());
    editText.setLayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT));
    editText.setHint("Kindly Give Us Your Feedbback here..");
    editText.setHeight(200);
    builder.setView(editText);
    builder.setPositiveButton("send", new DialogInterface.OnClickListener() {
        @Override
        public void onClick(DialogInterface dialog, int which) {
            String feedback = editText.getText().toString();
           // String restro=myViewHolder.listPizaRestaurant.getText().toString();
            new ServerRequest(getApplicationContext()).sendFeedBack(feedback, getApplicationContext(),"restro");
        }
    });
    Dialog dialog=builder.create();
    dialog.show();
}



    public void initializeGeoFence() {
  //      GeofenceController.getInstance().init(this);
        // the above line of code will make instance of GeofenceController using its
        //static method and call its method init(" giving current activity context as parameter")
        // init will initialize variables define in GeofenceController class
        // it will do five things:
        // 1.assign context : ( this.context = context.getApplicationContext();)
        // 2. make instance of gson :(  gson = new Gson();)
        // 3. make arraylist of class namedGeofences : ( namedGeofences = new ArrayList<>(); )
        // 4. initialize the Sharedprefrences :
        // prefs = this.context.getSharedPreferences(Constants.SharedPrefs.Geofences, Context.MODE_PRIVATE);
        // 5. load previously store geofences data {load stored keys from Sharedprefrences and classes from gson  }
        //    (loadGeofence)


//        GeofenceInfo geofence = new GeofenceInfo();
      //  geofence.name = "abbott";
     //   geofence.latitude = 34.1963822;
      //  geofence.longitude = 73.2450349;
       // geofence.radius = 100;


        //geofence.name = "adeebglt";
        //geofence.latitude = 35.9014173;
       // geofence.longitude = 74.3512267;
        //geofence.radius = 100;

        //GeofenceController.getInstance().addGeofence(geofence);

    }


    private void setListnersOnMapViews() {


/**tt        restro_map_liner.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AnimationUtils.Animatebtn3(v);
                //    Intent mapIntent=new Intent(getApplicationContext(),MapsActivity.class);
                //     mapIntent.putExtra("choice",0);
                //   startActivity(mapIntent);
                myCatagoricalMapRestroSearch(latitudeCurrent, lontitudeCurrent);

            }
        });   */

    }
    public void myCatagoricalMapRestroSearch(double lati,double longi){
        Intent mapintent= new Intent(Intent.ACTION_VIEW, Uri.parse("geo:" + lati + "," + longi +
                "?q=restaurants"));
        mapintent.setPackage("com.google.android.apps.maps");
        startActivity(mapintent);
    }


    public void  setMyUserProfileDetailsDrawer(){
        TextView textView=(TextView)findViewById(R.id.name_ud);
        TextView textView1=(TextView)findViewById(R.id.email_ud);
        TextView textView2=(TextView)findViewById(R.id.cell_ud);
//        textView. setText("Name:    " + UserLocalStore.myGetLogInUserLocalData(getApplicationContext()).name);
     //   textView1.setText("Email:   " + UserLocalStore.myGetLogInUserLocalData(getApplicationContext()).email);
     //   textView2.setText("Cell:    " + UserLocalStore.myGetLogInUserLocalData(getApplicationContext()).cell);
    }

    public void callRecyclerAdapterTempBeforeFetchingServerData(){

        restroAdapter = new RestroAdapter(getApplicationContext(), getRestroListDataTemporaryValues());
        rvRestroDistances.setLayoutManager(new LinearLayoutManager(getApplicationContext()));
        rvRestroDistances.setLayoutManager(new GridLayoutManager(getApplicationContext(), 2));
        rvRestroDistances.setAdapter(restroAdapter);
        restroAdapter.updateList();


    }


    public ArrayList<InformationRestro> getRestroListDataTemporaryValues(){

// data it is arrart list information objs for restro recyler view
      ArrayList<InformationRestro> data=new ArrayList<>();

        for(int i=0;i<8;i++){
            InformationRestro informationRestrotemp=new InformationRestro();
            informationRestrotemp.restroName="-";
            informationRestrotemp.distance="-";
            informationRestrotemp.timeByfoot="-";
            informationRestrotemp.timeByRoad="-";
            data.add(informationRestrotemp);
        }
        return data;

    }


    public void calculatDistance(){

        Location userLocation;
        Location restroLocations[]=null;
        userLocation=new Location("user Location");
        userLocation.setLatitude(latitudeCurrent);
        userLocation.setLongitude(lontitudeCurrent);





        for(int i=0;i<arraySiz/3;i++){

            restroLocations[i]=new Location(restroNames[i]);
            restroLocations[i].setLatitude(restroLati[i]);
            restroLocations[i].setLongitude(restroLongi[i]);
        }

        for(int i=0;i<arraySiz/3;i++){
            distances=new double[arraySiz/3];
            distances[i]=userLocation.distanceTo(restroLocations[i]);


        }

    }


    public void calculatDistance2(){
// here we use arraySize/3 as diviser because we hav ,each restro has 3 values (restro name, lati longi)
        // so if we 3 restroants our total record will be 3*3=9 if 4 restro than 3*4=12 recoeds



       // restroLati[0]=34.1978017;
       // restroLongi[0]=73.2464633;
       // restroLati[1]=34.1887583;
       // restroLongi[1]=73.2312702;
       // restroLati[2]=34.1978017;
       // restroLongi[2]=73.2464633;
        // Location userLocation;
        userLocation=new Location("user Location");
        userLocation.setLatitude(34.2007766);
        userLocation.setLongitude(73.2466427);

        userLocation.setLatitude(latitudeCurrent);
        userLocation.setLongitude(lontitudeCurrent);

        restroLocations=new Location[arraySiz];
        for(int i=0;i<arraySiz;i++){

            restroLocations[i]=new Location(restroNames[i]);
            restroLocations[i].setLatitude(restroLati[i]);
            restroLocations[i].setLongitude(restroLongi[i]);
        }


        distances=new double[arraySiz];
        roundOffDistances= new double[arraySiz];
        for(int i=0;i<arraySiz;i++){
            distances[i] = userLocation.distanceTo(restroLocations[i]);
            roundOffDistances[i]= Math.round(distances[i]);

//distancesStr+=" "+roundOffDistances[i]+" ";
        }

        //arrange distances in accending order
        //and arrange restro names a/c to distances




        for(int j=0;j<arraySiz;j++){
            for(int i=0;i<(arraySiz)-1;i++){
                if(roundOffDistances[i]>roundOffDistances[i+1]){
                    tempDouble=roundOffDistances[i];

                    roundOffDistances[i]=roundOffDistances[i+1];

                    roundOffDistances[i+1]=tempDouble;

//assign restroName a/c to assinding order of distances
                    tempStr=restroNames[i];
                    restroNames[i]=restroNames[i+1];
                    restroNames[i+1]=tempStr;
                }

            }}

        //here if distace is greater we convert it to Km
        distancesInKmStr=new String[arraySiz];
        for (int i=0;i<arraySiz;i++){
            if(roundOffDistances[i]>=1000){
                double distanceInKm= Math.round(roundOffDistances[i] / 1000);
               distancesInKmStr[i]=""+distanceInKm+" "+"Km";
            }

        }

        // for testing purpose:
        for(int i=0;i<arraySiz;i++) {
            distancesStr +=restroNames[i]+ ":" + distancesInKmStr[i] + "\n";
        }


     //   Toast.makeText(getApplicationContext(), distancesStr, Toast.LENGTH_LONG).show();




    }

    private void buildGoogleClientApi() {
      //  googleApiClient=null;
        googleApiClient=new  GoogleApiClient.Builder(this).addApi(LocationServices.API).addConnectionCallbacks
              (this).addOnConnectionFailedListener(this).build();
     //   googleApiClient=new GoogleApiClient.Builder(this).
      //          enableAutoManage(this/*this activity callback, faillistner **/,this).addApi(LocationServices.API).
      //          addOnConnectionFailedListener(this).build();
    }
    private void createLocationRequest(){
       // locationRequest=null;
        locationRequest=new LocationRequest();
        locationRequest.setInterval(10000);
        locationRequest.setSmallestDisplacement(100);
        locationRequest.setFastestInterval(5000);
       // if(highAccuracyFail){
          //  locationRequest.setPriority(LocationRequest.PRIORITY_NO_POWER);

     //   }else {
            locationRequest.setPriority(LocationRequest.PRIORITY_NO_POWER);
       // }
    }


     public Boolean isPlayServicesAvailable(){
        int resultCode= GooglePlayServicesUtil.isGooglePlayServicesAvailable(this);
        if(resultCode== ConnectionResult.SUCCESS){
            return true;
        }
        else {

            Toast.makeText(this, "Sorry for the inconvience ! \nPlease Update your Play Services", Toast.LENGTH_SHORT).show();
errorDialogAndUpdateMyPlayServices();
            return true;
        }
    }
    public void errorDialogAndUpdateMyPlayServices(){
        AlertDialog.Builder builder=new AlertDialog.Builder(Restaurant.this);
        builder.setTitle("Play-Services are not upDated ");
        builder.setMessage("please update Play-Services click update");
        builder.setPositiveButton("Update", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                Intent intent = new Intent(Intent.ACTION_VIEW,
                        Uri.parse("https://play.google.com/store/apps/details?id=com.google.android.gms&hl=en"));
                startActivity(intent);
            }
        });
        Dialog dialog=builder.create();
        dialog.show();
    }

    @Override
    protected void onStart() {
        super.onStart();

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_restaurants_distances, menu);
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
            case android.R.id.home:
                drawerLayoutRestroDistances.openDrawer(GravityCompat.START);
                return true;
            case R.id.action_settings:
                Toast.makeText(this, "settings", Toast.LENGTH_SHORT).show();
                break;
            case R.id.help:
            case R.id.aboutus:
                AlertDialog.Builder builder=new AlertDialog.Builder(Restaurant.this);
                builder.setIcon(R.drawable.piza3).setTitle("About Us").
                        setMessage("\"PakDigital Waiter will  display List of all restaurants around you that offers pizza " +
                                "and at what distance they are , how much time it will take to reach a particular restaurant. " +
                                "the Best part of the app is GeoFence whenever You go near to any Restaurant You Will automaticall get notify " +
                                "from the Restaurant about  latest Food trends and their Free & Discounts offers."+
                                "Not only this you can also order a delicious food on a Single Click, the app will automatically help the Pizza man to find your Location. " +
                                "   So don't forget to SignUp \n            Thank You");
                builder.setPositiveButton("OK", null);

                Dialog dialog=builder.create();
                dialog.show();

                break;

            case R.id.logout:
                UserLocalStore.myClearData(getApplicationContext());
                finish();

             startActivity(new Intent(getApplicationContext(), Login.class));

                Toast.makeText(this, "Logout Successfully ! thanks for using DW", Toast.LENGTH_SHORT).show();
                break;
            case R.id.refresh:
             //  getLatitudeAndLongitude();
              //  Toast.makeText(this,"Updating Menu items Please Wait",Toast.LENGTH_SHORT).show();
                break;


            case R.id.userprofile:
                if(drawerLayoutRestroDistances.isDrawerOpen(LLayout_restro_latilongiDrawer)){
                    drawerLayoutRestroDistances.closeDrawer(LLayout_restro_latilongiDrawer);

                }else {
                    drawerLayoutRestroDistances.openDrawer(LLayout_restro_latilongiDrawer);
                }

/**
                LayoutInflater layoutInflater=LayoutInflater.from(RestaurantsDistances.this);
                View view=layoutInflater.inflate(R.layout.user_details, null);
                TextView textView=(TextView)view.findViewById(R.id.name_ud);
                TextView textView1=(TextView)view.findViewById(R.id.email_ud);
                TextView textView2=(TextView)view.findViewById(R.id.cell_ud);
                textView.setText("Name: " + UserLocalStore.myGetLogInUserLocalData(getApplicationContext()).name);
                textView1.setText("Email: "+UserLocalStore.myGetLogInUserLocalData(getApplicationContext()).email);
                textView2.setText("Cell#: "+UserLocalStore.myGetLogInUserLocalData(getApplicationContext()).cell);
                builder=new AlertDialog.Builder(RestaurantsDistances.this);
                builder.setIcon(R.drawable.pizaper2);
                builder.setView(view);

                dialog=builder.create();
                dialog.show();
                Toast.makeText(this,UserLocalStore.myGetLogInUserLocalData(getApplicationContext()).name,Toast.LENGTH_SHORT).show();

**/



                break;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public void finish() {
        super.finish();
       // progressDialog.dismiss();
      //  finishActivity(1);
        Intent intent = new Intent(getApplicationContext(), Start.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        intent.putExtra("EXIT", true);
        startActivity(intent);
    }

    @Override
    public void onConnected(Bundle bundle) {
        if(!isMyGpsEnable()) {
            enableGPs();


           // getLatitudeAndLongitude();
          //  if(CheckInternetConnection.myCheckInternet(getApplicationContext())){
              //  getLatitudeAndLongitudeAccurate();
            getLatitudeAndLongitude();

          //  }else {
           //     Toast.makeText(getApplicationContext(),"Check your Internet Connection (!)",Toast.LENGTH_LONG).show();
           // }
        }
        else
        {

           // getLatitudeAndLongitude();
          //  if(CheckInternetConnection.myCheckInternet(getApplicationContext())){
               // getLatitudeAndLongitudeAccurate();
            getLatitudeAndLongitude();

           // }else {
            //    Toast.makeText(getApplicationContext(),"Check your Internet Connection (!)",Toast.LENGTH_LONG).show();

           // }
        }


    }


    public void getLatitudeAndLongitude(){
        Location mLastLocation = null;
        //   code changed! i add "LocationServices.FusedLocationApi.
        // requestLocationUpdates(googleApiClient, locationRequest, this);
        //because it will get lati and logi withoutthe presence of
        // wifi, i had a issue , gps was not getting latlongi when using requestLocationUpdates
        // because it depends on wifi  for precise points.(note: for request location updates, its not mandatory that
        // wifi is working properly or data is tranferring condition is that wifi must be On or nearly located)

          // we are not gett laties using requestLocationUpdates so commented
            //  LocationServices.FusedLocationApi.requestLocationUpdates(googleApiClient, locationRequest, this);

            int count=0;
        checkPermission();
            mLastLocation = LocationServices.FusedLocationApi.getLastLocation(googleApiClient);
            if (mLastLocation == null) {
                count++;
               // Toast.makeText(getApplicationContext(),
                    //    count + "get last known location issue with Gps, click on retry icon from app bar ...",
                     //   Toast.LENGTH_LONG).show();
//getLatitudeAndLongitudeAccurate();
               gpsFailSoGetCity();
            } else {
              //  Toast.makeText(getApplicationContext(), count + " success", Toast.LENGTH_LONG).show();
                storeLatitudeAndLongitudeInLocalStore(mLastLocation);
                // here we first fetch data and than go for matrix api
                 fetchRestroLatiLongiNamesAsyn();
            }



    }

    public class LocationCurrentAsyn extends AsyncTask<Void,Void,Void>{
        @Override
        protected Void doInBackground(Void... voids) {
            return null;
        }
    }

public void gpsFailSoGetCity(){
    fetchRestroLatiLongiNamesAsyn();
}



    public void getLatitudeAndLongitudeAccurate(){
        Location mLastLocation = null;
        //   code changed! i add "LocationServices.FusedLocationApi.
        // requestLocationUpdates(googleApiClient, locationRequest, this);
        //because it will get lati and logi withoutthe presence of
        // wifi, i had a issue , gps was not getting latlongi when using requestLocationUpdates
        // because it depends on wifi  for precise points.(note: for request location updates, its not mandatory that
        // wifi is working properly or data is tranferring condition is that wifi must be On or nearly locate@TargetApi(23)
checkPermission();
            // we are not gett laties using requestLocationUpdates so commented
             LocationServices.FusedLocationApi.requestLocationUpdates(googleApiClient, locationRequest, this);



    }
    @TargetApi(23)
    public boolean checkPermission(){


      return true;
    }












    public boolean isMyGpsEnable(){
        boolean gps=false;
        LocationManager locationManager=(LocationManager)getSystemService(LOCATION_SERVICE);

        if(locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER)){
            gps=true;

        }
if(locationManager.isProviderEnabled(LocationManager.NETWORK_PROVIDER)){
    gps=true;

}

        return gps;
    }




    public void enableGPs(){
        AlertDialog.Builder builder=new AlertDialog.Builder(this);
        builder.setTitle("GPS IS OFF");
        builder.setMessage("Turn On GPS to use Our service");
        builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                Intent intent = new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS);
                startActivity(intent);
                Toast.makeText(getApplicationContext(), ":.Gps.:", Toast.LENGTH_SHORT).show();
                new SnackBarMy(getApplicationContext()).showSnakBar(coordinatorLayout,":..please try again..:");
//getLatitudeAndLongitude();
            }
        });

        Dialog dialog=builder.create();
        dialog.show();


    }


    @Override
    public void onConnectionSuspended(int i) {
      //  Toast.makeText(getApplicationContext(), "issue with Gps...", Toast.LENGTH_LONG).show();
    }
 // Note: currently not in use because we are not calling requestLocationUpdates()
    @Override
    public void onLocationChanged(Location location) {
        Toast.makeText(getApplicationContext(), "location change", Toast.LENGTH_LONG).show();

       // storeLatitudeAndLongitudeInLocalStore(location);
        GpsCount++; // counter means we are using onlocation change function of gps , so whenever location is changed
        //fetchedRestroLatilongi will be call , so to avoid this and to call only once to server data we use counter
        if (location == null) {
            GpsCount=0;
         //   Toast.makeText(getApplicationContext(), "issue 8 with Gps, Re-connecting onloction changred...", Toast.LENGTH_LONG).show();
getLatitudeAndLongitude();
        } else {
            storeLatitudeAndLongitudeInLocalStore(location);

           // counter means we are using onlocation change function of gps , so whenever location is changed
            //fetchedRestroLatilongi will be call , so to avoid this and to call only once to server data we use counter
            if (GpsCount == 1) {
              //  Toast.makeText(getApplicationContext()," (!)Please Check your Internet Connection ",Toast.LENGTH_LONG).show();
                // if (CheckInternetConnection.myCheckInternet(getApplicationContext())) {
                fetchRestroLatiLongiNamesAsyn(); // here we first fetch data and than go for matrix api
                //  }else{
                //   Toast.makeText(getApplicationContext()," (!)Please Check your Internet Connection ",Toast.LENGTH_LONG).show();
                //  }

            }
        }
    }


    public void storeLatitudeAndLongitudeInLocalStore(Location location){
        latitudeCurrent=location.getLatitude();
        lontitudeCurrent=location.getLongitude();
Toast.makeText(getApplicationContext(), "Latitute: " + latitudeCurrent + "\nLogitude:" + lontitudeCurrent + " ", Toast.LENGTH_SHORT).show();
        UserLocalStore.storeLatLongSP(latitudeCurrent, lontitudeCurrent, getApplicationContext());

    }






    @Override
    public void onConnectionFailed(ConnectionResult connectionResult) {

        if (mResolvingError) {
            // Already attempting to resolve an error.
            return;
        } else if (connectionResult.hasResolution()) {
            try {
                mResolvingError = true;
               connectionResult.startResolutionForResult(this,REQUEST_RESOLVE_ERROR); // define above global 1001
            } catch (IntentSender.SendIntentException e) {
                // There was an error with the resolution intent. Try again.
               googleApiClient.connect();
            }
        } else {
            // Show dialog using GoogleApiAvailability.getErrorDialog()
            showErrorDialog(connectionResult.getErrorCode());
            mResolvingError = true;
        }

    }

    private void showErrorDialog(int errorCode) {
        Toast.makeText(getApplicationContext(), " " + errorCode, Toast.LENGTH_LONG).show();

    }


    public void fetchRestroLatiLongiNamesAsyn() {



           progressDialog.show();
            new ServerReqAsyn().execute();

    }


    public class ServerReqAsyn extends AsyncTask<Void,Void,JSONObject> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();

        }

        @Override
        protected JSONObject doInBackground(Void... params) {
            String data="";
            JSONObject jsonObject=new JSONObject();
            try {
                JSONObject serverJsondata;
              //  url=new URL("http://pakdigitalwaiter.netne.net/restroLatiLongiDownload.php");

                url=new URL("http://pdwaiter.net16.net/arestroLatiLongiDownloadnew.php");
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
                    data1.put("lati", latitudeCurrent);
                    data1.put("longi", lontitudeCurrent);

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
            //textView.setText(jsonObject.toString());
            httpURLConnection.disconnect();
if(jsonObject!=null){
    JSONArray jsonArrayRestro=new JSONArray();
    JSONArray jsonArrayLati=new JSONArray();
    JSONArray jsonArrayLongi=new JSONArray();
    JSONArray jsonArrayNum=new JSONArray();

    try {  //if json array size is not==null than go
       if(!(jsonObject.isNull("arraySize"))) {
           arraySiz = jsonObject.getInt("arraySize");

           jsonArrayRestro = jsonObject.getJSONArray("restroNames");
           jsonArrayLati = jsonObject.getJSONArray("restroLati");
           jsonArrayLongi = jsonObject.getJSONArray("restroLongi");
           jsonArrayNum=jsonObject.getJSONArray("restroNmbr");
           restroNames = new String[arraySiz];

           restroLati = new double[arraySiz];
           restroLongi = new double[arraySiz];
           restroNumbers=new String[arraySiz];


           for (int i = 0; i < arraySiz; i++) {
               restroNames[i] = jsonArrayRestro.getString(i);
               restroLati[i] = Double.parseDouble(jsonArrayLati.getString(i));
               restroLongi[i] = Double.parseDouble(jsonArrayLongi.getString(i));
               restroNumbers[i]=jsonArrayNum.getString(i);
           }
       }else {
          // Toast.makeText(getApplicationContext()," (!) Problem in  your Internet Connection ",Toast.LENGTH_LONG).show();
          showAlertDialogInternetError();
//           showAlertDialogAboutUsInflater();
       }

    } catch (JSONException e) {
        e.printStackTrace();
    }



}else {
   // Toast.makeText(getApplicationContext()," (!) Problem in  your Internet Connection ",Toast.LENGTH_LONG).show();
    showAlertDialogInternetError();
 //   showAlertDialogAboutUse();

}
         //   progressDialog.dismiss();


          //  Toast.makeText(getApplicationContext(),restroLati[0]+":"+restroLongi[0]+":"+restroNames[0],Toast.LENGTH_LONG).show();
     //textView.setText(restroLati[0]+":"+restroLongi[0]+":"+restroNames[0]);
           // calculatDistance2();
if(lontitudeCurrent!=0||latitudeCurrent!=0) {
if(!jsonObject.isNull("arraySize")) {


    timeByRoad = new String[arraySiz];
    timeByfoot = new String[arraySiz];
    distanceFromGoogleMtrx = new String[arraySiz];
    for (int i = 0; i < arraySiz; i++) {
        timeByfoot[i] = "-";
        timeByRoad[i] = "-";
        distanceFromGoogleMtrx[i] = "-";
    }

    // Toast.makeText(getApplicationContext()," Calculating distance and time Please wait ...",Toast.LENGTH_LONG).show();
    progressDialog.setTitle("Time & Distance");
    progressDialog.setMessage(" Calculating please wait ...");

    new GoogleApiReqForDriveTimeAsyn().
            execute();
}else {
  //  Toast.makeText(getApplicationContext(), "issue with Internet Connection, please Re-try...", Toast.LENGTH_LONG).show();
    progressDialog.cancel();
}

} else{
    Toast.makeText(getApplicationContext(), ":.Gps fail, please Re-try.:", Toast.LENGTH_LONG).show();
    progressDialog.cancel();
}
        }




    }

    public void makeFoodMenuTemp(){
        FoodMenuTemp foodMenuTemp=new FoodMenuTemp();
        foodMenuTemp.foodID=1;
        foodMenuTemp.foodCatagory="pizza";
        foodMenuTemp.foodName="italian Pizza";
        foodMenuTemp.foodPrice=120;

    }

public void temporaryJson(FoodMenuTemp foodMenuTemp) throws JSONException {

    JSONObject jsonObject=new JSONObject();
    jsonObject.put("foodCatagory", foodMenuTemp.foodCatagory);
    jsonObject.put("foodName", foodMenuTemp.foodName);
    jsonObject.put("foodIngredients", foodMenuTemp.foodIngredients);
    jsonObject.put("foodPrice", foodMenuTemp.foodPrice);
    jsonObject.put("foodSize", foodMenuTemp.foodSize);
}

    public class GoogleApiReqForDriveTimeAsyn extends AsyncTask<Void,Void,JSONObject> {
        URL urlForGoogle;
        HttpURLConnection httpURLConnection;
        String dataStr="";
        JSONObject jsonObjectDrivingDstnce;


        @Override
        protected JSONObject doInBackground(Void... params) {

                indrexrestro++;
                try {

                    if(restroLati[0]!=0.0){ // use this because app was crashed because 00web server didnt return data ,
                                            // error  was "attepting to read from null array"
                    if(indrexrestro<arraySiz) {
                        urlForGoogle = new URL("http://maps.googleapis.com/maps/api/distancematrix/json?origins=" + latitudeCurrent + "," +
                                lontitudeCurrent + "&destinations=" + restroLati[indrexrestro] + "," + restroLongi[indrexrestro] + "&mode=driving&language=en-EN&sensor=false");
                        httpURLConnection = (HttpURLConnection) urlForGoogle.openConnection();
                        httpURLConnection.setDoInput(true);
                        httpURLConnection.setChunkedStreamingMode(0);
                        httpURLConnection.setDoOutput(true);
                        httpURLConnection.setRequestMethod("POST");
                        httpURLConnection.setConnectTimeout(15000);
                        httpURLConnection.setReadTimeout(15000);
                        if(CheckInternetConnection.myCheckInternet(getApplicationContext())){


                        httpURLConnection.connect();

                        JSONObject jsonObject = new JSONObject();
                        jsonObject.put("data", "mydata");
                        DataOutputStream dataOutputStream = new DataOutputStream(httpURLConnection.getOutputStream());
                        dataOutputStream.writeBytes(jsonObject.toString());
                        dataOutputStream.flush();
                        dataOutputStream.close();
                        int response = httpURLConnection.getResponseCode();
                        String line = "";
                        if (response == httpURLConnection.HTTP_OK) {
                            BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(httpURLConnection.getInputStream()));
                            while ((line = bufferedReader.readLine()) != null) {
                                dataStr += line;

                            }
                            jsonObjectDrivingDstnce = new JSONObject(dataStr);

                        }


                    }} }

                } catch (MalformedURLException e) {
                    e.printStackTrace();
                } catch (IOException e) {
                    e.printStackTrace();
                } catch (JSONException e) {
                    e.printStackTrace();
                }

            return jsonObjectDrivingDstnce;        }

        @Override
        protected void onPostExecute(JSONObject jsonObject) {
            super.onPostExecute(jsonObject);
JSONArray jsonArray_destination_addresses;
            JSONArray jsonArray_rows;
            JSONArray jsonArray_origin_address;
            JSONObject jsonObject_elements;
            JSONArray jsonArray_elements;
            JSONObject jsonObject_distance;
            JSONObject jsonObject_duration;

            if(jsonObject!=null){

                jsonArray_destination_addresses=new JSONArray();
                jsonArray_rows=new JSONArray();
                jsonArray_origin_address=new JSONArray();
                jsonObject_elements=new JSONObject();
                jsonArray_elements=new JSONArray();
                jsonObject_distance=new JSONObject();
                jsonObject_duration=new JSONObject();
                String distanceStr;
                String durationStr;


                try {
                    jsonArray_destination_addresses=jsonObject.getJSONArray("destination_addresses");
                    jsonArray_rows=jsonObject.getJSONArray("rows");
                    jsonObject_elements=jsonArray_rows.getJSONObject(0);
                    jsonArray_elements=jsonObject_elements.getJSONArray("elements");
                    jsonObject_distance=jsonArray_elements.getJSONObject(0);
                    distanceFromGoogleMtrx[indrexrestro]=jsonObject_distance.getJSONObject("distance").getString("text");
                    timeByRoad[indrexrestro]=jsonArray_elements.getJSONObject(0).getJSONObject("duration").getString("text");
                 //   textView.setText(distanceStr+durationStr);
                   // Toast.makeText(getApplicationContext(),"please wait.. calculating "+restroNames[indrexrestro]+" Restaurant  distacne and time-",Toast.LENGTH_SHORT).show();

                    if(indrexrestro<arraySiz) {

                        new GoogleApiReqForDriveTimeAsyn().execute();
                        progressDialog.dismiss();
                        callRecyclerAdapter();
                    }
progressDialog.dismiss();
            /* if(indrexrestro==arraySiz-1){ // means 3==3 first send three request to googleMatrixApi and
                    // collect data and than call recycler adapter. issue if more than 100 restrorants than UI
                    // will not remain responsive untill 100th req completes . so we try another logic
                        callRecyclerAdapter();
                    }  **/

                } catch (JSONException e) {
                    e.printStackTrace();
                   // Toast.makeText(getApplicationContext(), "Error In Connection, Restart the app please", Toast.LENGTH_LONG);


                }


            } else {
               // Toast.makeText(getApplicationContext(), "connection error", Toast.LENGTH_LONG);

            }

        }



    }






public void showAlertDialogAboutUse(){
    AlertDialog.Builder builder=new AlertDialog.Builder(Restaurant.this);

    builder.setIcon(R.drawable.piza3);
    builder.setTitle("Important to Know");
    builder.setMessage("PakDigital Waiter will  display List of all restaurants around you that offers pizza " +
            "and at what distance they are , how much time it will take to reach a particular restaurant. " +
                    "the Best part of the app is GeoFence whenever You go near to any Restaurant You Will automaticall get notify " +
                    "from the Restaurant about  latest Food trends and their Free & Discounts offers."+
            "Not only this you can also order a delicious food on a Single Click, the app will automatically help the Pizza man to find your Location. " +
            "   So don't forget to SignUp \n            Thank You");
    builder.setPositiveButton("OK", null);

    Dialog dialog=builder.create();
    dialog.show();

}
   public void showAlertDialogAboutUsInflater(){
       AlertDialog.Builder builder=new AlertDialog.Builder(getApplicationContext());
       LayoutInflater layoutInflater= LayoutInflater.from(Restaurant.this);
       View view=layoutInflater.inflate(R.layout.intro1,null);
       builder.setView(view);
       Dialog dialog=builder.create();
       dialog.show();
   }


    public void showAlertDialogInternetError(){
      // showAlertDialogInternetErrorTwo();
     //   Toast.makeText(Restaurant.this, ":. System will load previously stored data .:", Toast.LENGTH_SHORT).show();
        loadOldDataFromDb();

    }




    public void showAlertDialogInternetErrorTwo(){
        AlertDialog.Builder  builder=new AlertDialog.Builder(Restaurant.this);
        builder.setTitle("Internet Problem").setIcon(R.drawable.restrobnew2icon);
        builder.setMessage("System will load previously stored data  \n Thank You");
        builder.setPositiveButton("ok", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                loadOldDataFromDb();
            }
        });
        Dialog dialog=builder.create();
        dialog.show();


    }
public void loadOldDataFromDb(){
  //  storeDataToLocalDB();
    callRecyclerAdapterLocalDB();
}


    int i=0;
    public void callRecyclerAdapter(){
        if(i==0) { i++;
            restroAdapter = new RestroAdapter(getApplicationContext(), getRestroListData());
            rvRestroDistances.setLayoutManager(new LinearLayoutManager(getApplicationContext()));
            // rvRestroDistances.setLayoutManager(new GridLayoutManager(getApplicationContext(),2));
            rvRestroDistances.setAdapter(restroAdapter);
            restroAdapter.updateList();
            if (indrexrestro == arraySiz - 1) {
                //  showAlertDialogAboutUsInflater();
            }

        }
        else {
            RestroAdapter.informationRestros =getRestroListData();
            restroAdapter.updateList();
            new StoreDataAsyn().execute();
        }
    }

    // here our method  return list of InformatioRestro class objects.
    InformationRestro informationRestro;
    public ArrayList<InformationRestro> getRestroListData(){

// data it is arrart list information objs for restro recyler view
        data=new ArrayList<>();

        for(int i=0;i<arraySiz;i++){
             informationRestro=new InformationRestro();
            informationRestro.restroName=restroNames[i];
            informationRestro.distance=distanceFromGoogleMtrx[i];
            informationRestro.timeByfoot=timeByfoot[i];
            informationRestro.timeByRoad=timeByRoad[i];
            int id=i; id++;
            informationRestro.id=id;
            informationRestro.restaurant_number=restroNumbers[i];
            data.add(informationRestro);
        }
        return data;

    }

    public class StoreDataAsyn extends AsyncTask<Void,Void,Void>{
        @Override
        protected Void doInBackground(Void... voids) {
            storeDataToLocalDB(getRestroListData());
            return null;
        }
    }
    public void storeDataToLocalDB(ArrayList<InformationRestro> informationRestrolist){

        //InformationRestro informationRestro=new InformationRestro();
        // informationRestro.id=1;
        // informationRestro.restroName="Italian Piza";
        // informationRestro.distance="1 km";
        // informationRestro.timeByRoad="10 min";
        // restaurantListDb=new RestaurantListDb(getApplicationContext(),null,null,1);

        for(int k=0;k<arraySiz;k++){
            restaurantListDb.addRestaurantsList(informationRestrolist.get(k));
        }


    }


    // local DB stuff






    public void callRecyclerAdapterLocalDB(){
        ArrayList<InformationRestro> informationRestrosArayLstDb=getRestroListDatafromLocalDB();
       // Toast.makeText(getApplicationContext(),""+informationRestrosArayLstDb,Toast.LENGTH_LONG).show();

        if(informationRestrosArayLstDb.size()==0){
           // Toast.makeText(getApplicationContext(),":. Check your internet connection .:",Toast.LENGTH_LONG).show();
       new SnackBarMy(getApplicationContext()).showSnakOffline(coordinatorLayout,"Connection Problem, please Request Offline data .:.");

        }else {
            restroAdapter = new RestroAdapter(getApplicationContext(), informationRestrosArayLstDb);


            rvRestroDistances.setLayoutManager(new LinearLayoutManager(getApplicationContext()));
            // rvRestroDistances.setLayoutManager(new GridLayoutManager(getApplicationContext(),2));
            rvRestroDistances.setAdapter(restroAdapter);
            restroAdapter.updateList();
        }


    }






    public ArrayList<InformationRestro> getRestroListDatafromLocalDB(){
//testingdataStore();
        ArrayList<InformationRestro> informationRestrosArayLstDb=new ArrayList<>();
        informationRestrosArayLstDb=restaurantListDb.getRestaurantList();
        Log.i("Local database size:",informationRestrosArayLstDb.size()+"");
     //   Toast.makeText(getApplicationContext(),"db size"+informationRestrosArayLstDb.size(),Toast.LENGTH_LONG).show();
        return informationRestrosArayLstDb;
    }

    public void testingdataStore(){
        InformationRestro informationRestro=new InformationRestro();
        informationRestro.id=1;
        informationRestro.restroName="Addeb pizza Restaurant";
        informationRestro.distance="5 km";
        informationRestro.timeByRoad="1 hr";
        informationRestro.restaurant_number="03101883575";
        restaurantListDb.addRestaurantsList(informationRestro);
    }



}
