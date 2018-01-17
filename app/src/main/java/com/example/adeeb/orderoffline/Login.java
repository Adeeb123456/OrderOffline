package com.example.adeeb.orderoffline;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.location.LocationManager;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.provider.Settings;
import android.util.Patterns;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class Login extends Activity implements View.OnClickListener{

    EditText EtEmail=null,EtPassword=null;
    Button loginbtn;
    String email,password;;
    Button TvSignup;
    private UserLocalStore userLocalStore;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        myGetIds();
//alertdialogIntro();
        myAssignClickListner();
      //  userLocalStore=new UserLocalStore(this);
AnimationUtils.Animatebtn2(loginbtn);
      //  Typeface custom_font = Typeface.createFromAsset(getAssets(), "fonts/jok.ttf");
      //  EtEmail.setTypeface(custom_font);
      //  EtPassword.setTypeface(custom_font);
      //  TvSignup.setTypeface(custom_font);
      //  loginbtn.setTypeface(custom_font);
    }
public void alertdialogIntro(){
    AlertDialog.Builder builder=new AlertDialog.Builder(Login.this);
    LayoutInflater layoutInflater= LayoutInflater.from(Login.this);
   View view= layoutInflater.inflate(R.layout.intro1, null);
    builder.setView(view);
    Dialog dialog=builder.create();
    dialog.show();

}


    public void myCopyData() {
        email=EtEmail.getText().toString();
        password=EtPassword.getText().toString();
    }

    public void myGetIds(){

        EtEmail=(EditText)findViewById(R.id.email);
        EtPassword=(EditText)findViewById(R.id.password);
        loginbtn=(Button)findViewById(R.id.loginbtn);
        TvSignup=(Button)findViewById(R.id.tvsingup);
    }


    public void myAssignClickListner(){
    loginbtn.setOnClickListener(this);
        TvSignup.setOnClickListener(this);

                   }

    public boolean isEmailValid(String email){
        CharSequence charSequence=email;

        return Patterns.EMAIL_ADDRESS.matcher(charSequence).matches();


    }
    @Override
    public void onClick(View v) {
        switch (v.getId())
        {
            case R.id.loginbtn://note "Loginbtn" is the xml actual id not java-1
              //  startActivity(new Intent(this,RestaurantsDistances.class));
           // mySetLoginTrue();
AnimationUtils.Animatebtn(v);

              String mail=EtEmail.getText().toString();
                 if(EtEmail.getText().toString().matches("")||EtPassword.getText().toString().matches("")) {
                     Toast.makeText(getApplicationContext(), "Empty fields", Toast.LENGTH_SHORT).show();
                 }
                else {

                     if(isMyCellValid(mail)){
                         if(myCheckInternet()) {


                             myGetServerDataAndStoreInSp();
                         }else {
                             myShowInternetErrorMsg();
                         }
                     }else{
                         EtEmail.setText("! Invalid Email");
                     }


                 }
                 // temprrary do comment for sake of testing

          //     startActivity(new Intent(this, MenuFood.class));
                break;

            case R.id.tvsingup:   //tvsignup use in xml

                //here when (user click on signup) we start Register activity by making its intent and snding it to start activity
          //     Intent startActivityIntent=new Intent(this,Register.class); OR Directly below
                AnimationUtils.Animatebtn(v);
                startActivity(new Intent(this,Register.class));

              //  startActivity(new Intent(this, MenuFood.class));
                break;

        }

    }

    private void myGetServerDataAndStoreInSp() {
     //   myCopyData();// get data from textview  , email and ,password

     //   User user=new User(email,password);


//comment temporarly
         //   myAutheticate(user);

    }

    private void myAutheticate(User user) {

        ServerRequest serverReq=new ServerRequest(this);
        serverReq.myFetchUserDataInBackground(user, new GetUserCallBack() {
            @Override
            public void myDone(User returnedUser) {
                if (returnedUser == null) {
                    myShowErrorMsg();
                    EtEmail.setText("");
                    EtPassword.setText("");
                } else {
                    mySetLoginTrue();
                    myLogUserIn(returnedUser);


                }
            }


        });

    }
    public boolean isMyCellValid(String cell){
        char[] chararray=cell.toCharArray();
        if(chararray[0]=='0'&&chararray.length==11){
            return true;
        }
        else
        return false;
    }

    private Boolean myCheckInternet() {
        ConnectivityManager connectivityManager=(ConnectivityManager)getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo=connectivityManager.getActiveNetworkInfo();
        if(networkInfo!=null&&networkInfo.isConnected()){
            return true;
        }
        else
            return false;
    }

    private void myLogUserIn(User returnedUser) {
        UserLocalStore.myStoreUserData(returnedUser, getApplicationContext());
        if(isMyGpsEnable()) {
            initializeGeoFence();
        }else {
            enableGPs();

        }

      // startActivity(new Intent(this, MenuFood.class));

       startActivity(new Intent(this,FoodMenu.class));

    }
    public boolean isMyGpsEnable(){
        boolean gps;
        LocationManager locationManager=(LocationManager)getSystemService(LOCATION_SERVICE);
        gps=locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER);


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
            }
        });

        Dialog dialog=builder.create();
        dialog.show();


    }

public void initializeGeoFence(){
  //  GeofenceController.getInstance().init(this);
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
 //   geofence.name ="abbott";
 //   geofence.latitude =34.1963822;
  //  geofence.longitude =73.2450349;
  //  geofence.radius =100;

  //  geofence.name = "adeebglt";
  //  geofence.latitude = 35.9014173;
  //  geofence.longitude = 74.3512267;
  //  geofence.radius = 100;

  //  GeofenceController.getInstance().addGeofence(geofence);

}
    private void myShowErrorMsg() {
        AlertDialog.Builder AlrtDialgBuldr=new AlertDialog.Builder(Login.this);
        AlrtDialgBuldr.setMessage("Incorrect cell or password");
        AlrtDialgBuldr.setPositiveButton("OK", null);
        AlrtDialgBuldr.show();
     }

    private void myShowInternetErrorMsg() {
        AlertDialog.Builder AlrtDialgBuldr=new AlertDialog.Builder(Login.this);
        AlrtDialgBuldr.setMessage("Check your Internet Connection");
        AlrtDialgBuldr.setPositiveButton("OK", null);
        AlrtDialgBuldr.show();
    }




    private void mySetLoginTrue() {
UserLocalStore.mySetUserLogInBoolean(true,getApplicationContext());
    }

} //end of activity
