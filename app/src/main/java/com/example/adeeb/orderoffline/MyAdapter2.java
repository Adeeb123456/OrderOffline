package com.example.adeeb.orderoffline;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;

import java.util.ArrayList;

/**
 * Created by AdeeB on 3/2/2016.
 */
public class MyAdapter2 extends RecyclerView.Adapter<MyAdapter2.MyViewHolder> implements CallbakToAdaptr{

    Context context;
    int previousPosition=0;
    ArrayList<Information> data;
    LayoutInflater inflater;
    private double totalprice;
    private String singleItem,ItemList,restaurantName,size;
    MyViewHolder myViewHolder;

    public MyAdapter2(Context context, ArrayList<Information> data) {
        this.context=context;
        this.data=data;
        inflater= LayoutInflater.from(context);
    }


    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view=inflater.inflate(R.layout.ordereditems_list,parent,false);
        MyViewHolder myViewHolder=new MyViewHolder(view);
        return myViewHolder;
    }

    @Override
    public void onBindViewHolder(MyViewHolder myViewHolder, int position) {

        myViewHolder.time.setText(data.get(position).time);
        myViewHolder.name1.setText(data.get(position).name1);
        myViewHolder.city.setText(data.get(position).city);
        myViewHolder.country.setText(data.get(position).country);
        myViewHolder.totalprice.setText(data.get(position).totalprice + "");
        myViewHolder.items.setText(data.get(position).items);
     //   myViewHolder.listPizaSize.setText(data.get(position).list_foodsize);
        myViewHolder.listPizaRestaurant.setText(data.get(position).offerby);
this.myViewHolder=myViewHolder;

        if(position>previousPosition){

            AnimationUtils.Animate(myViewHolder,true);

        }else
        {
            AnimationUtils.Animate(myViewHolder,false);
        }
        previousPosition=position;

    }

    @Override
    public int getItemCount() {
        return data.size();
    }

    @Override
    public String getAddressfill() {
        if(myViewHolder.exactLocation!=null){
            String addr=myViewHolder.exactLocation.getText().toString();
            return addr;

        }
        return "";
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {
        TextView textView,listPricetv,listPizaSize,listPizaRestaurant,time,name1,totalprice,items,city,country;
        EditText exactLocation;
        ImageButton pizaButton;
        ImageButton imageBtn;
        public MyViewHolder(View itemView) {
            super(itemView);
time=(TextView)itemView.findViewById(R.id.list_piza_time2);
  name1=(TextView)itemView.findViewById(R.id.list_piza_username2);
            totalprice=(TextView)itemView.findViewById(R.id.listprice2);
            city=(TextView)itemView.findViewById(R.id.list_piza_city2);
            country=(TextView)itemView.findViewById(R.id.list_piza_country2);
            items=(TextView)itemView.findViewById(R.id.list_piza_items2);

            listPizaSize=(TextView)itemView.findViewById(R.id.list_piza_size2);
            listPizaRestaurant=(TextView)itemView.findViewById(R.id.list_piza_offerby2);



        }



    }

    public void updateList(){

        data = new Data(context).getData2orderlist();

        notifyDataSetChanged();
    }
}
