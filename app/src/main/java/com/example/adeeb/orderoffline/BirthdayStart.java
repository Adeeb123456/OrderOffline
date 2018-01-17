package com.example.adeeb.orderoffline;

import android.annotation.TargetApi;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;

import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.telephony.SmsManager;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListAdapter;

import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

public class BirthdayStart extends AppCompatActivity implements View.OnClickListener,AdapterView.OnItemSelectedListener{
 static  EditText eTUtnmae,birthday,birthmonth,birthyear,city;
    Button birthButton;
    boolean emptyFileds=true;
    DatePickerDialog datePickerDialog;
   static CoordinatorLayout coordinatorLayout;
    LinearLayout linearLayout;
    Spinner spinner;
    List<String> listCites;
    ImageView imgCollapse;
    TextView skip;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.birthdayreg);

        eTUtnmae=(EditText)findViewById(R.id.username);
        birthday=(EditText)findViewById(R.id.birthday);
        city=(EditText)findViewById(R.id.city1);
        imgCollapse=(ImageView)findViewById(R.id.img_collapse);
        new AnimationXml(getApplicationContext()).animateCollapsingview(imgCollapse);

        coordinatorLayout=(CoordinatorLayout)findViewById(R.id.coordinatebieth);
       // spinner=(Spinner)findViewById(R.id.spinnercity);
//spinner.setAdapter(getAdapter2());
linearLayout=(LinearLayout)findViewById(R.id.birdaylo);

birthButton=(Button)findViewById(R.id.birthreg);
        skip = (TextView) findViewById(R.id.skip);

        skip.setOnClickListener(this);



//birthday.setOnClickListener(this);
      //  city.setOnClickListener(this);
        birthButton.setOnClickListener(this);


//birthday.setShowSoftInputOnFocus(false);

        birthday.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {
                switch (motionEvent.getAction()){
                    case MotionEvent.ACTION_DOWN:
                        getDate();
                        break;
                    case MotionEvent.ACTION_UP:
                }

                return false;
            }
        });
    }

public ArrayAdapter getAdapter2(){
    ArrayAdapter<String> adapter;
    UserLocalStore.storeCities("-- City --,abbottabad,gilgit,islamabad,lahore,karachi", getApplicationContext());

    String cities=UserLocalStore.getCities(getApplicationContext());
    String[] s=cities.split(",");
    Toast.makeText(getApplicationContext(),s[0]+"---"+cities,Toast.LENGTH_LONG).show();
    listCites=new ArrayList<>();
    for(int i=0;i<s.length;i++){

        listCites.add(s[i]);
    }
    adapter=new ArrayAdapter<String>(getApplicationContext(),android.R.layout.simple_spinner_item,listCites);
    adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
    //   spinner.setAdapter(adapter);
    return adapter;
}

    public class MyDialogueSpinner extends Spinner{

        public MyDialogueSpinner(Context context) {
            super(context);
        }

        @Override
        public boolean performClick() {
            new AlertDialog.Builder(getContext()).setAdapter((ListAdapter) getAdapter2(), new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialogInterface, int i) {
                    setSelection(i);
                    dialogInterface.dismiss();
                }
            }).setTitle("city").create().show();

            return true;
        }
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.birthreg:


                if(checkEmptyFields()){
                   // Toast.makeText(getApplicationContext(),"ghghg",Toast.LENGTH_SHORT).show();
                }else {


                    try {
                        storeAndSendUserData(getFieldData());
                        new AnimationXml(getApplicationContext()).animateview3(linearLayout);
                    } catch (JSONException e) {
                        e.printStackTrace();
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
                break;
            case R.id.birthday:
              //  Snackbar snackbar=Snackbar.make(coordinatorLayout,"User name must not be empty",Snackbar.LENGTH_SHORT)
                  //      .setAction("ok",null);
               // Toast.makeText(getApplicationContext(),"ghghg",Toast.LENGTH_SHORT).show();

                break;
            case R.id.skip:
                startRestaurntActvty();
                break;


        }
    }


public void startRestaurntActvty(){
    Intent intent=new Intent(getApplicationContext(),Restaurant.class);
    startActivity(intent);
}

    public void getDate(){

        DialogFragment newFragment = new DatePickerDialog2();

        newFragment.show(getFragmentManager(), "datePicker");

    }
    public User getFieldData()throws Exception{
        User user;
        String name=eTUtnmae.getText().toString();
        String bir=birthday.getText().toString();
        String citystr=city.getText().toString();
        user=new User(name,bir,citystr);
        return  user;

    }

    public void storeAndSendUserData(User user) throws JSONException {
        UserLocalStore.myStoreUserData(user, getApplicationContext());
        JSONObject jsonObject=new JSONObject();
        jsonObject.put("code","userRegReq");
        jsonObject.put("nm",user.name);
        jsonObject.put("bd",user.birthday);
        jsonObject.put("cty", user.city);
       UserLocalStore.storeOwnerNumber("03101883575",getApplicationContext());
String ownerNmbr=UserLocalStore.getOwnerNumber(getApplicationContext());
        Log.i("userdta", ownerNmbr+" "+jsonObject);
sendDataSmsToRegister(ownerNmbr,jsonObject.toString());


    }
    String  smsSendResult="";
    public void sendDataSmsToRegister(String ownerNmbr,String smsBody){

   //     Toast.makeText(getApplicationContext(), smsBody, Toast.LENGTH_LONG).show();

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
                        smsSendResult = "Sorry ! fail to Register, Generic failure";

                        break;
                    case SmsManager.RESULT_ERROR_NO_SERVICE:
                        smsSendResult = "Sorry ! fail to Register, No Mobile service";

                        break;
                    case SmsManager.RESULT_ERROR_NULL_PDU:
                        smsSendResult = "Sorry ! fail to Register ,Null PDU";

                        break;
                    case SmsManager.RESULT_ERROR_RADIO_OFF:
                        smsSendResult = "Sorry ! fail to Register,Radio off";

                        break;
                }
                Toast.makeText(context, smsSendResult, Toast.LENGTH_SHORT).show();
                if (smsSendResult == "success") {
                    Snackbar snackbar = Snackbar.make(coordinatorLayout, "You will soon receive restaurants details", Snackbar.LENGTH_LONG);
                    snackbar.show();
                    eTUtnmae.setText("");
                    city.setText("");
                    birthday.setText("");

                } else {
                    Snackbar snackbar =
                            Snackbar.make(coordinatorLayout, smsSendResult, Snackbar.LENGTH_INDEFINITE).setAction("Retry", new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        Snackbar snackbar1 = Snackbar.make(coordinatorLayout, "please wait", Snackbar.LENGTH_LONG);
                        snackbar1.show();
                        try {
                            storeAndSendUserData(getFieldData());
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
                  //      Toast.makeText(getApplicationContext(),ownerNmbr,Toast.LENGTH_LONG).show();
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
        //    Toast.makeText(getApplicationContext(),"part 2 sened"+ownerNmbr,Toast.LENGTH_LONG).show();

//        smsManager.sendDataMessage(phn, null, port, smsBodyBytes, null, null);


        //   smsManager.sendDataMessage(phn, null, port, prtsData.getBytes(), null, null);

    }



    @Override
    public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
        String city=adapterView.getItemAtPosition(i).toString();
        Snackbar snackbar=Snackbar.make(coordinatorLayout,city,Snackbar.LENGTH_LONG);
        snackbar.show();
    //    Toast.makeText(getApplicationContext()," "+city,Toast.LENGTH_LONG).show();
    }

    @Override
    public void onNothingSelected(AdapterView<?> adapterView) {

    }

    public static  class DatePickerDialog2 extends DialogFragment implements android.app.DatePickerDialog.OnDateSetListener {

        public DatePickerDialog2() {
            super();
        }


        @Override
        public Dialog onCreateDialog(Bundle savedInstanceState) {
            final Calendar calendar=Calendar.getInstance();
            int year=calendar.get(Calendar.YEAR);
            int month=calendar.get(Calendar.MONTH);
            int day=calendar.get(Calendar.DAY_OF_MONTH);
            year=1992;
            month=4;
            day=13;

          //  return new android.app.DatePickerDialog(getActivity(),this,year,month,day);
            return new DatePickerDialog(getActivity(),R.style.MyDialogTheme,this,year,month,day);

        }

        @Override
        public void onDateSet(DatePicker datePicker, int i, int i1, int i2) {
           // Toast.makeText(getActivity(),i+":"+i1+":"+i2,Toast.LENGTH_LONG).show();
            Snackbar snackbar=Snackbar.make(coordinatorLayout,"We will wish you on your birthday Thank you",Snackbar.LENGTH_LONG)
                    .setAction("ok", null);

            snackbar.show();
           birthday.setText(i2+"-"+i1+"-"+i);
        }
    }



    public boolean checkEmptyFields(){
        boolean emtyFields=true;
        if(eTUtnmae.getText().toString().matches(""))
        {
            Snackbar snackbar=Snackbar.make(coordinatorLayout,"User name must not be empty",Snackbar.LENGTH_LONG)
                    .setAction("ok",null);
snackbar.show();
        }else
        if(birthday.getText().toString().matches(""))
        {
            Snackbar snackbar=Snackbar.make(coordinatorLayout,"birth day name must not be empty",Snackbar.LENGTH_LONG)
                    .setAction("ok",null);
            snackbar.show();
        }else
        if(city.getText().toString().matches(""))
        {
            Snackbar snackbar=Snackbar.make(coordinatorLayout,"City must not be empty",Snackbar.LENGTH_LONG)
                    .setAction("ok",null);
            snackbar.show();
        }else{
            emtyFields=false;
        }
return emtyFields;
    }

   }

