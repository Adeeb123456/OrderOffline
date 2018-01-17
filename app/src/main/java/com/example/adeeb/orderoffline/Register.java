package com.example.adeeb.orderoffline;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

public class Register extends Activity implements View.OnClickListener {
   private EditText EtEmail,EtPassword,EtName,EtCell,EtdayBirth,EtdayMonth;
    private Button Registerbtn;
    private String email,password,name, birthday,birthmonth;
    String cell;
    AlertDialog.Builder builder;
    Dialog dialog;

    TextView TvSignup;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        myGetIds();


        EtCell.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                EtCell.setText("");
                EtCell.setTextColor(Color.BLACK);
            }
        });


        myAssignClickListner();
    }

    public static boolean isEmailValid(String email) {
        CharSequence inputStr = email;
        return android.util.Patterns.EMAIL_ADDRESS.matcher(inputStr).matches();
    }
    public static boolean isPhoneValid(String phone) {

        char[] characters=phone.toCharArray();
        if(characters[0]=='0'&&characters.length>8){
            return true;
        }else
        return false;
    }


    public void myGetIds(){

       // EtEmail=(EditText)findViewById(R.id.email);
        EtPassword=(EditText)findViewById(R.id.mypass);
        Registerbtn=(Button)findViewById(R.id.registerbtn);
        TvSignup=(TextView)findViewById(R.id.tvsingup);
        EtName=(EditText)findViewById(R.id.name);
        EtCell=(EditText)findViewById(R.id.mycell);
      //  EtdayBirth=(EditText)findViewById(R.id.etdaybirth);
      //  EtdayMonth=(EditText)findViewById(R.id.etbirthmonth);


    }
    public void myCopyData() {
     //   email=EtEmail.getText().toString();
        password=EtPassword.getText().toString();
        name=EtName.getText().toString();
        cell=EtCell.getText().toString();
       // birthday=EtdayBirth.getText().toString();
       // birthmonth=EtdayMonth.getText().toString();
    }

    public void myAssignClickListner(){
        Registerbtn.setOnClickListener(this);


    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.registerbtn://note "registerbtn" is the xml actual id not java-1
            //    String mail = EtEmail.getText().toString();
                String phone=EtCell.getText().toString();
//||EtdayBirth.getText().toString().matches("")
              //  ||EtdayMonth.getText().toString().matches("")
                if(EtName.getText().toString().matches("")||EtPassword.getText().toString().matches("")
                        ||
                        EtCell.getText().toString().matches("")){
                    Toast.makeText(getApplicationContext(), "Empty fields", Toast.LENGTH_SHORT).show();
                }else {

                    if (isPhoneValid(phone)) {

                        myCopyData(); // store data from form to string name, email..
                        if(checkInternetConnection()) {
                            User dataToRegister = new User(name, cell, email, password,birthday,birthmonth);
                            //  User dataToRegister=new User("ddd",222,"dddd","ddd");

                            myRegisterUser(dataToRegister);
                        }else {
                            showInternetErrorDialog();
                        }


                    } else {
                        if(!isPhoneValid(phone)){
                            EtCell.setText("! invalid number");
                            EtCell.setTextColor(Color.RED);
                        }
                    }




                }
                break;



        }
    }

    private void myRegisterUser(User dataToRegister) {
       // UserLocalStore userLocalStore=new UserLocalStore(this);
       // userLocalStore.myStoreUserData(dataToRegister,getApplicationContext());
        ServerRequest serverReq=new ServerRequest(this);
        serverReq.myStoreUserDataInBackground(dataToRegister, new GetUserCallBack() {
            @Override
            public void myDone(User returnedUser) {
                Toast.makeText(getApplicationContext(), ":: " + returnedUser.registrationResponse, Toast.LENGTH_LONG).show();
               AlertDialog(returnedUser.registrationResponse);
                 dialog=builder.create();
                dialog.show();

            }
        });
    }

    private void AlertDialog(String response){
         builder=new AlertDialog.Builder(Register.this);
        builder.setTitle("Server Response");
        if(response!=null){
           // if(response.equals("Registered Successfully")) {
                builder.setMessage(": " + response + " " + "\n Please");
                builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        startActivity(new Intent(Register.this, Login.class));
                    }
                });
           // }
    }
            else{
            builder.setMessage(response + "\n Network Problem , Try Again");
            builder.setPositiveButton("SignUp", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    startActivity(new Intent(getApplicationContext(), Register.class));
                }
            });


        }

    }
    private boolean checkInternetConnection(){
        ConnectivityManager connectivityManager=(ConnectivityManager)getSystemService(CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo=connectivityManager.getActiveNetworkInfo();
        if(networkInfo!=null&&networkInfo.isConnected()){
            return true;
        }else {
            return false;
        }
    }

    public void showInternetErrorDialog(){
        AlertDialog.Builder builder=new AlertDialog.Builder(Register.this);
        builder.setTitle("Network Connection Problem");
        builder.setMessage("Not Connected to Internet");
        builder.setPositiveButton("OK",null);
        Dialog dialog=builder.create();
        dialog.show();
    }

}
