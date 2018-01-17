package com.example.adeeb.orderoffline;

import android.app.AlertDialog;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.location.Address;
import android.location.Geocoder;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import java.io.IOException;
import java.util.List;
import java.util.Locale;

public class OrderInfo extends ActionBarActivity {
    ProgressDialog progressDialog;
    private LinearLayout linear_order_list_display;
    TextView orderItemInfoDispaly;
    Button button,sendOrder;
    double latutute;
    double longitute;
    private Toolbar toolbar;

    AlertDialog.Builder builder;
    String streetAddress,country1,city1;
    String houseAddress;
    MyAdapter2 adapter;
    RecyclerView recyclerView;
    LayoutInflater layoutInflateraddress;
    AlertDialog.Builder builder1address;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_order_list);

      //  linear_order_list_display=(LinearLayout)findViewById(R.id.orderItemInfoDispalyLinearLayout2);

        // orderlist=getIntent().getStringExtra("orderlist");

       // latutute=getIntent().getDoubleExtra("latitute", 0);
       // longitute=getIntent().getDoubleExtra("logtitude", 0);
        toolbar=(Toolbar)findViewById(R.id.toolbarorderlist);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("Item List");
        getSupportActionBar().setSubtitle(".");
        getSupportActionBar().setDisplayShowHomeEnabled(false);
        getSupportActionBar().setDisplayHomeAsUpEnabled(false);
       // getSupportActionBar().setLogo(R.mipmap.ic_add_shopping_cart_black_24dp);
        //recycler view stuff
        recyclerView=(RecyclerView)findViewById(R.id.recyclervieworderlist);

        adapter=new MyAdapter2(this,new Data(this).getData2orderlist());
        recyclerView.setAdapter(adapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
adapter.updateList();
        latutute=UserLocalStore.getLatitudeSp(this);
        longitute=UserLocalStore.getLontitudeSp(this);


        button=(Button)findViewById(R.id.viewmapid);
        sendOrder=(Button)findViewById(R.id.sendorder);


        sendOrder.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
alertdialogAddress();

            }
        });
       reverseGeoCodding();

    }
public void alertdialogAddress(){
     builder1address=new AlertDialog.Builder(OrderInfo.this);
     layoutInflateraddress = LayoutInflater.from(OrderInfo.this);

//text_entry is an Layout XML file containing two text field to display in alert dialog
// view root Optional view to be the parent of the generated hierarchy.
    final View textEntryView = layoutInflateraddress.inflate(R.layout.alert_dialog_location_input, null);

   // final EditText house = (EditText) textEntryView.findViewById(R.id.house);
  //  final EditText room = (EditText) textEntryView.findViewById(R.id.room);
    final EditText cityad = (EditText) textEntryView.findViewById(R.id.cityad);

    cityad.setText(UserLocalStore.getCity(getApplicationContext()));

    if(cityad.getText().toString().matches("City name")){


    builder1address.setTitle("GPS is unable to get Your city please fill ");


    builder1address.setView(textEntryView);

    builder1address.setPositiveButton("Next", new DialogInterface.OnClickListener() {
        @Override
        public void onClick(DialogInterface dialog, int which) {
            if(cityad.getText().toString().matches("City name")
                    ){
                Toast.makeText(getApplicationContext(), "Sorry ! \n Some fields are not filled", Toast.LENGTH_SHORT).show();
            }else {
                UserLocalStore.setCityCountryAddress(getApplicationContext(),cityad.getText().toString()
                        ,"countryname","house"
                        +"room");
              //  houseAddress=house.getText().toString()+room.getText().toString();
                ServerRequest serverRequest = new ServerRequest(OrderInfo.this);
                serverRequest.mySendOrder("houseAddress", new GetUserCallBack() {
                    @Override
                    public void myDone(User returnedUser) {
                        orderAlertDialog(returnedUser.orderResponse);
                    }
                });
            }

        }
    });
    Dialog dialog=builder1address.create();
    dialog.show();}
    else {
        ServerRequest serverRequest = new ServerRequest(OrderInfo.this);
        serverRequest.mySendOrder("houseAddress", new GetUserCallBack() {
            @Override
            public void myDone(User returnedUser) {
                orderAlertDialog(returnedUser.orderResponse);
            }
        });
    }
}
public void orderAlertDialog(String response){
    if(response!=null){
        builder=new AlertDialog.Builder(OrderInfo.this);
        builder.setTitle("Message");
        builder.setMessage(":: order\n" + response);
        builder.setPositiveButton("OK", null);
        Dialog dialog=builder.create();
        dialog.show();

    }


}


    public void reverseGeoCodding(){
        Toast.makeText(getApplicationContext(), "n" + longitute + latutute, Toast.LENGTH_SHORT).show();
        new AddressAsyn().execute();
    }


public void showMapActivity(){
  //  startActivity(new Intent(this,MapsActivity.class));


}


    public class AddressAsyn extends AsyncTask<Void,Void,Void> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            progressDialog=new ProgressDialog(OrderInfo.this);
            progressDialog.setTitle("Getting Your current location");
            progressDialog.setMessage("please  wait...");
            progressDialog.setCanceledOnTouchOutside(false);
            progressDialog.show();
        }

        @Override
        protected Void doInBackground(Void... params) {

            String streetAddressbackground=getMyLocationAddress(latutute,longitute);


            streetAddress=streetAddressbackground;
            UserLocalStore.setUserCurrentLocationSp(streetAddress,getApplicationContext());


            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);
            progressDialog.dismiss();
UserLocalStore.setCityCountryAddress(getApplicationContext(),city1,country1,streetAddress);
            UserLocalStore userLocalStore=new UserLocalStore(getApplicationContext());
adapter.updateList();
          //  orderItemInfoDispaly.setText(orderlist+"\n\nStreet Address: "+streetAddress+" : ");

        }


        public String getMyLocationAddress(double lati,double longi) {
            String AddressStr=null;
            Geocoder geocoder= new Geocoder(getApplicationContext(), Locale.ENGLISH);

            try {

                //Place your latitude and longitude

                List<Address> addresses = geocoder.getFromLocation(lati,longi, 1);

                if(addresses != null) {

                    Address fetchedAddress = addresses.get(0);
                    String city,postal,province,particularPlace,district,sub1 ,s2,s3;
                    String country="Country: "+addresses.get(0).getCountryName();
                    country1=addresses.get(0).getCountryName();
                    province="Province: "+addresses.get(0).getAdminArea();
                    city="City: "+addresses.get(0).getLocality();
                    city1=addresses.get(0).getLocality();;
                    postal="Postal: "+addresses.get(0).getPostalCode();
                    particularPlace="Current Location: "+addresses.get(0).getFeatureName(); // eg mirpurabbott, labs road
                    district="District: "+addresses.get(0).getSubAdminArea();
                  //  district="District: "+addresses.get(0).getSubLocality();

                    StringBuilder strAddress = new StringBuilder();

                    for(int i=0; i<fetchedAddress.getMaxAddressLineIndex(); i++) {
                        strAddress.append(fetchedAddress.getAddressLine(i)).append("\n");
                    }

                    AddressStr=" " + strAddress.toString()+country+"\n"+province+"\n"+city+"\n"+particularPlace;


                }

                else {
                    AddressStr="No Address found ! \n Sorry for the inconvience\n " +
                            "Kindly update your google Play Services from Play Store \n" +
                            "or Restart Your Phone \n";
                }
            }
            catch (IOException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
                AddressStr="No Address found ! \n" +
                        " Sorry for the inconvience either your wifi is off or\n" +
                        "updates your google Play Services from Play Store \n" +
                        "\n or Restart Your Phone!";
            }

            return AddressStr;
        }
    }

}
