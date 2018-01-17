package com.example.adeeb.orderoffline;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.squareup.picasso.Picasso;

import java.util.ArrayList;

/**
 * Created by AdeeB on 3/11/2016.
 */
public  class RestroAdapter extends RecyclerView.Adapter<RestroAdapter.MyViewHolder> {
    Context context;
  static ArrayList<InformationRestro> informationRestros ;
  static LayoutInflater inflater;
    int previousPosition=0;

   public RestroAdapter(Context context,ArrayList<InformationRestro> informationRestros){
       this.context=context;
       this.informationRestros=informationRestros;
       inflater= LayoutInflater.from(context);
   }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        View view=inflater.inflate(R.layout.restro_list_card_new,parent,false);
        MyViewHolder myViewHolder=new MyViewHolder(view);
        return myViewHolder;

    }

    @Override
    public void onBindViewHolder(final MyViewHolder holder, final int position) {
//        holder.pizaRestroButton.setImageResource(informationRestros.get(position).imageIdRestro);
       // Typeface custom_font = Typeface.createFromAsset(context.getAssets(), "fonts/exotica.ttf");
      //  Typeface custom_font1 = Typeface.createFromAsset(context.getAssets(), "fonts/jok.ttf");
       // holder.restroname.setTypeface(custom_font1);
      // holder.restroDistance0.setTypeface(custom_font1);
    //  holder.restroDistance.setTypeface(custom_font1);
    //    holder.byfoot0.setTypeface(custom_font1);
     ///  holder.byroad0.setTypeface(custom_font1);
     //  holder.byfoot.setTypeface(custom_font1);
      //  holder.byroad.setTypeface(custom_font1);
holder.restroname.setText(informationRestros.get(position).restroName);
       // holder.restroDistance.setText(informationRestros.get(position).distance+"");
       // holder.byfoot.setText(informationRestros.get(position).timeByfoot);

        String url="https://pakbit.000webhostapp.com/AndroidImges/";
reqPicassa(url+informationRestros.get(position).restaurant_number+".png",holder.restroLogo);


        holder.restroDistance.setText("Total distance: " + informationRestros.get(position).distance);
        holder.byroad.setText("Time by road: "+informationRestros.get(position).timeByRoad);
       if(position>previousPosition){
           AnimationUtils.Animate(holder,true);
           AnimationUtils.Animatebtn2(holder.itemView);
      //  AnimationUtils.AnimateCustomAdaptrObjs(holder);
        //   AnimationUtils.Animatebtn(holder.restroDistance);
        //   AnimationUtils.Animatebtn(holder.restroDistance0);
         //  AnimationUtils.Animatebtn(holder.restroname0);
         //  AnimationUtils.Animatebtn(holder.restroname);
         //  AnimationUtils.Animatebtn(holder.byfoot0);
          // AnimationUtils.Animatebtn(holder.byfoot);
         //  AnimationUtils.Animatebtn(holder.byroad0);
         //  AnimationUtils.Animatebtn(holder.byroad);
       }else
       {
          AnimationUtils.Animate(holder,false);
            AnimationUtils.Animatebtn(holder.itemView);
           // AnimationUtils.Animatebtn(holder.restroDistance0);
          // AnimationUtils.Animatebtn(holder.restroname0);
          // AnimationUtils.Animatebtn(holder.restroname);
          // AnimationUtils.Animatebtn(holder.byfoot0);
          // AnimationUtils.Animatebtn(holder.byfoot);
          // AnimationUtils.Animatebtn(holder.byroad0);
          // AnimationUtils.Animatebtn(holder.byroad);
       }
        previousPosition=position;
      //  AnimationUtils.Animatebtn(holder.itemView);
        holder.menuBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AnimationUtils.Animatebtn(v);
                if(checkDistanceRange(holder.restroDistance.getText().toString())) {
                    Intent menufoodIntent = new Intent(context.getApplicationContext(), FoodMenu.class);
                    menufoodIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    menufoodIntent.putExtra("restaurantname", holder.restroname.getText().toString());
                    menufoodIntent.putExtra("restaurant_number", informationRestros.get(position).restaurant_number);
                    //     UserLocalStore.setDistance(context, holder.restroDistance.getText().toString());
                    //  UserLocalStore.setTimeByRoad(context, holder.diliveryTime.getText().toString());
                    //   UserLocalStore.setREstaurant(holder.restroname.getText().toString(),context);
                    //  Toast.makeText(context, holder.restroname.getText().toString(),Toast.LENGTH_SHORT).show();
                    context.getApplicationContext().startActivity(menufoodIntent);
                }//end of if statement
                 else {
                    new SnackBarMy(context).showSnakBar(holder.snakbarLiner,":. Not Allow ! You are not with in 20km .:");
                }
            }
        });
    }

    public boolean checkDistanceRange(String distanceStr){
       String numStr=SeparateNumbers.stripNonDigits(distanceStr);
        float distance=0f;
try{
    distance=Float.parseFloat(numStr);
}catch (Exception e){
e.printStackTrace();
}

       // Toast.makeText(context,distance+" :"+numStr,Toast.LENGTH_LONG).show();
        if(distance<=20){ //2.1 is converted to 21 , we use 200
            return true;
        }else
            return false;
    }

    @Override
    public int getItemCount() {
        return informationRestros.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {
        TextView textView,restroname,restroDistance,byfoot0,diliveryTime,byfoot,byroad,restroname0,restroDistance0;
        ImageButton pizaRestroButton;
        ImageView restroLogo;
        Button menuBtn;
        LinearLayout snakbarLiner;
        public MyViewHolder(View itemView) {
            super(itemView);
              snakbarLiner=(LinearLayout)itemView.findViewById(R.id.snackbarliner);
            //textView=(TextView)itemView.findViewById(R.id.pizatxt);
         //   pizaRestroButton=(ImageButton)itemView.findViewById(R.id.pizza_image_id);
           // restroname0=(TextView)itemView.findViewById(R.id.distancettv0);
            restroname=(TextView)itemView.findViewById(R.id.restroname);
           // restroDistance0=(TextView)itemView.findViewById(R.id.distancettv0);
           restroDistance=(TextView)itemView.findViewById(R.id.retro_list_distance);
          //  byfoot=(TextView)itemView.findViewById(R.id.list_byfoot1);
          byroad=(TextView)itemView.findViewById(R.id.list_byroad1);
          //  byfoot0=(TextView)itemView.findViewById(R.id.list_byfoot);
          //  diliveryTime=(TextView)itemView.findViewById(R.id.dilivery_time);
            menuBtn= (Button)itemView.findViewById(R.id.mnu_btn);
            restroLogo= (ImageButton)itemView.findViewById(R.id.restro_logo);

            menuBtn.setOnTouchListener(new View.OnTouchListener() {
                @Override
                public boolean onTouch(View v, MotionEvent event) {
                    switch (event.getAction()) {
                        case MotionEvent.ACTION_DOWN:

                            break;
                        case MotionEvent.ACTION_UP:

                            break;

                    }


                    return false;
                }
            });




        }



    }
    public void  updateList(){


        notifyDataSetChanged();
    }
    public void reqPicassa(String imgUrl, ImageView imgref){

        Picasso.with(context).load(imgUrl).resize(100,100).into(imgref);
    }

}
