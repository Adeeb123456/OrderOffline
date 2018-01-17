package com.example.adeeb.orderoffline;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Intent;
import android.location.LocationManager;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

public class Start extends ActionBarActivity {
    AlertDialog.Builder builder;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_start);

        //when back button is press from RestaurantsDistances , this will completly app
        if (getIntent().getExtras() != null && getIntent().getExtras().getBoolean("EXIT", false)) {
            finish();
        }else {
            alertDialodIntroInfater();
            Thread thread = new Thread() {
                public void run() {
                    try {
                        sleep(2000);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    } finally {
                      startActivity(new Intent(getApplicationContext(), Restaurant.class));
                       // startActivity(new Intent(getApplicationContext(), BirthdayStart.class));

                    }
                }
            };
            thread.start();


        }


    }
    public boolean isMyGpsEnable(){
        boolean gpsBoolean=false;
        LocationManager locationManager=(LocationManager)getSystemService(LOCATION_SERVICE);
      if(locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER)){
            gpsBoolean=true;
        }

        if(locationManager.isProviderEnabled(LocationManager.NETWORK_PROVIDER)){
            gpsBoolean=true;
        }
       return gpsBoolean;
    }

    public void initializeGeoFence(){
        //GeofenceController.getInstance().init(this);
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



      //  GeofenceInfo geofence = new GeofenceInfo();
      //  geofence.name ="abbott";
      //  geofence.latitude =34.1963822;
      //  geofence.longitude =73.2450349;
      //  geofence.radius =100;

      //  GeofenceController.getInstance().addGeofence(geofence);

    }
public void alertDialodIntroInfater(){
     builder=new AlertDialog.Builder(Start.this);
    LayoutInflater layoutInflater= LayoutInflater.from(Start.this);
    View view=layoutInflater.inflate(R.layout.intro1, null);
    builder.setView(view);
    Dialog dialog=builder.create();
    dialog.show();


}
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_start, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}
