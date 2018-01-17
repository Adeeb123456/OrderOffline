package com.example.adeeb.orderoffline;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;

/**
 * Created by AdeeB on 1/31/2016.
 */
public class MyCustomAdapter extends RecyclerView.Adapter<MyCustomAdapter.MyViewHolder>{

Context context;
    int previousPosition=0;
    ArrayList<Information> data;
    LayoutInflater inflater;

   private static double totalprice;
    private String singleItem="",ItemList="",restaurantName="",size="";
    public MyCustomAdapter(Context context, ArrayList<Information> data) {
        this.context=context;
        this.data=data;
        inflater= LayoutInflater.from(context);
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup viewGroup, int position) {

        View view=inflater.inflate(R.layout.food_menu_recycler,viewGroup,false);
        MyViewHolder myViewHolder=new MyViewHolder(view);
        return myViewHolder;
    }

    @Override
    public void onBindViewHolder(final MyViewHolder myViewHolder, final int position) {
int i=position;i++;
        myViewHolder.textView.setText(data.get(position).title);
      //  myViewHolder.pizaButton.setImageResource(data.get(position).imageId);
      //  myViewHolder.imageBtn.setImageResource(data.get(position).btnid);
        myViewHolder.listPricetv.setText(data.get(position).list_foodprise + "");
        myViewHolder.listPizaSize.setText(data.get(position).list_foodsize);
        myViewHolder.listPizaRestaurant.setText(data.get(position).offerby);
        //myViewHolder.itemnmbr.setText("Item # "+i);


       if(position>previousPosition){

         AnimationUtils.Animate(myViewHolder,true);
       }else
       {
           AnimationUtils.Animate(myViewHolder,false);
       }

       previousPosition=position;

AnimationUtils.Animatebtn(myViewHolder.itemView);
        myViewHolder.additem.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //    if(UserLocalStore.mygetUserLogInBoolean(context)){
             //   AnimationUtils.AnimateCustomAdaptrObjs(myViewHolder);
                totalprice += Double.parseDouble(myViewHolder.listPricetv.getText().toString());
                singleItem = myViewHolder.textView.getText().toString();
                ItemList += singleItem + ",";
                restaurantName = myViewHolder.listPizaRestaurant.getText().toString();
                size = myViewHolder.listPizaSize.getText().toString();
String restroCell=data.get(position).restaurant_number;
                  /// Toast.makeText(context, "" + singleItem, Toast.LENGTH_SHORT).show();
                FoodMenu.dispalyOrderList(context,restroCell, singleItem, ItemList, restaurantName, totalprice, size);
                //    }
                //    else{
                //       Toast.makeText(context,"You Must be Login To Place an Order",Toast.LENGTH_LONG).show();
                //       AnimationUtils.AnimateCustomAdaptrObjs(myViewHolder);
                //  }


                new AnimationXml(context).animateview(myViewHolder);
            }
        });
        myViewHolder.callbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(context, "Calling...", Toast.LENGTH_LONG).show();
            }
        });
    }
    public static void cleanTprice(){
        totalprice=0;
    }

    @Override
    public int getItemCount() {
        return data.size();
    }

    class MyViewHolder extends RecyclerView.ViewHolder{

        TextView textView,listPricetv,listPizaSize,listPizaRestaurant,itemnmbr;
        ImageButton pizaButton;
        ImageButton imageBtn;
        Button additem,callbtn,directOrder;
     //   LinearLayout linear_order_list_display,bottom_view_order_list;

        public MyViewHolder(View itemView) {
            super(itemView);
            textView=(TextView)itemView.findViewById(R.id.pizatxt);
           // pizaButton=(ImageButton)itemView.findViewById(R.id.pizza_image_id);
            additem=(Button)itemView.findViewById(R.id.additem);
           callbtn=(Button)itemView.findViewById(R.id.callbtn);
            listPricetv=(TextView)itemView.findViewById(R.id.list_foodprice);
            listPizaSize=(TextView)itemView.findViewById(R.id.list_piza_size2);
            listPizaRestaurant=(TextView)itemView.findViewById(R.id.ingredients);
           // itemnmbr=(TextView)itemView.findViewById(R.id.itemnmbr);
          //  directOrder=(Button)itemView.findViewById(R.id.direct_order);
           // inflateOrderBtn();


        }

public void inflateOrderBtn(){
    LayoutInflater layoutInflater= LayoutInflater.from(context);
   directOrder=(Button)layoutInflater.inflate(R.layout.btn_order_bg,null);

}
    }

    public void updateList(){

        data = new Data(context).getData2orderlist();

        notifyDataSetChanged();
    }

    public void updateFoodMenu(String restroNmbr){
        data=new Data(context).getData();
        notifyDataSetChanged();
    }
    public void updateFoodMenuLocalDB(String restroNmbr){
        FoodDb foodDb = new FoodDb(context,null,null,1);


        data =foodDb.getFoodMenuDb(restroNmbr);

        notifyDataSetChanged();
    }
}
