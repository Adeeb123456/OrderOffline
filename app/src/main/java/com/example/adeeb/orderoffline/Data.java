package com.example.adeeb.orderoffline;

import android.content.Context;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

/**
 * Created by AdeeB on 1/31/2016.
 */
public class Data {

    Context context;
    public Data(Context context) {
        this.context=context;
    }
    String[] foodnames;
    double[] foodprices;
    String[] size;
    String[] restaorantname;
    String[] restroForDistances;


    public ArrayList<Information> getData(){

       // new ServerRequest(context).FetchFoodMenu("mrpizza");
        ArrayList<Information> data;
        int[] images={R.drawable.pizzass,

                R.drawable.burger2,

                R.drawable.shurmanews,

        };




        String[] catagories={"pizza","burger","shurma","drinkss","drinkss","drinkss","drinkss","drinkss","drinkss"
                ,"drinkss","drinkss","drinkss","drinkss","drinkss","drinkss","drinkss","drinkss","drinkss","drinkss"
        };




        String jsonfoodmenu=UserLocalStore.getJsonFoodMenuString(context);
        if(jsonfoodmenu!=null) {
            try {
                JSONObject foodmenuJobj = new JSONObject(jsonfoodmenu);
                int arraysize = foodmenuJobj.getInt("arraysize");
                JSONArray jsonArray = foodmenuJobj.getJSONArray("foodnames");
                JSONArray jsonPricesArray = foodmenuJobj.getJSONArray("foodprices");
                JSONArray jsonRestorantArray = foodmenuJobj.getJSONArray("restorantname");
                foodnames = new String[arraysize];
                foodprices = new double[arraysize];
                restaorantname = new String[arraysize];
                for (int i = 0; i < foodnames.length; i++) {

                    foodnames[i] = jsonArray.getString(i);
                    foodprices[i] = jsonPricesArray.getDouble(i);
                    restaorantname[i] = jsonRestorantArray.getString(i);
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }

            data = new ArrayList<>();
        if(foodnames!=null) {
            for (int i = 0; i < foodnames.length; i++) {
                Information information = new Information();


                if (foodnames[i].equals("pizza")) {
                    information.imageId = getPizzaImage();
                }
                if (foodnames[i].equals("drinks")) {
                    information.imageId = getDrinksImage();
                }
                if (foodnames[i] == "burger") {
                    information.imageId = getDrinksImage();
                }

                information.title = foodnames[i];
                information.btnid = getbtnImage();
                information.list_foodprise = foodprices[i];
                information.list_foodsize = "-";
                information.offerby = restaorantname[i];
                data.add(information);
            }
        }

            return data;
        }



    public static int getPizzaImage(){

        return R.drawable.piza3;
    }

    public static int getBurgerImage(){

        return R.drawable.burger2;
    }

    public static int getShurmaImage(){

        return R.drawable.shurmanews;
    }
    public static int getDrinksImage(){

        return R.drawable.drinkss;
    }

    public static int getbtnImage(){

      return 0;
    }





    public ArrayList<Information> getData2orderlist(){


        ArrayList<Information> data;






        String[] catagories={"pizza","burger","shurma","drinkss","drinkss","drinkss","drinkss","drinkss","drinkss"
                ,"drinkss","drinkss","drinkss","drinkss","drinkss","drinkss","drinkss","drinkss","drinkss","drinkss"
        };




        String time="00:00",name="swa",country="pk",city="abt",items="Pizza",restorant="mr.",size="xx";
        double totallprice;

        totallprice= Double.parseDouble(UserLocalStore.getTotalPriceStr(context));
        items=UserLocalStore.getItems(context);
        restorant=UserLocalStore.getRestarrant(context);
        size=UserLocalStore.getSize(context);
        city=UserLocalStore.getCity(context);
        country=UserLocalStore.getCountry(context);
        User user=UserLocalStore.myGetLogInUserLocalData(context);
        name=user.name;
        time=UserLocalStore.getTime(context);




        data = new ArrayList<>();

                Information information = new Information();





                information.name1 =name;
                information.time=time;
                information.totalprice =totallprice;
                information.list_foodsize =size;
                information.offerby = restorant;
        information.items=items;
        information.city=city;
        information.country=country;
                data.add(information);



        return data;
    }


}
