package com.example.adeeb.orderoffline;

import android.content.Context;
import android.location.Address;
import android.location.Geocoder;

import java.io.IOException;
import java.util.List;
import java.util.Locale;

/**
 * Created by AdeeB on 11/15/2016.
 */
public class LocationCurrent {
    Context context;
    public LocationCurrent(Context context){
       this.context=context;
    }

    public String getMyLocationAddress(double lati,double longi ) {
        String AddressStr=null;
        Geocoder geocoder= new Geocoder(context, Locale.ENGLISH);

        try {

            //Place your latitude and longitude

            List<Address> addresses = geocoder.getFromLocation(lati,longi, 1);

            if(addresses != null) {

                Address fetchedAddress = addresses.get(0);
                String city,postal,province,particularPlace,district,sub1 ,s2,s3;
                String country="Country: "+addresses.get(0).getCountryName();
                String country1=addresses.get(0).getCountryName();
                province="Province: "+addresses.get(0).getAdminArea();
                city="City: "+addresses.get(0).getLocality();
               String city1=addresses.get(0).getLocality();;
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
