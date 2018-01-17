package com.example.adeeb.orderoffline;

/**
 * Created by AdeeB on 9/26/2015.
 */
public class User {
    String name,email,password,registrationResponse,orderResponse;
    String cell,birthday,birthmonth;
    String city;

public User(String name,String cell,String email,String password,String birthday,String birthmonth){
    this.name=name;
    this.email=email;
    this.password=password;
    this.cell=cell;
    this.birthday=birthday;
    this.birthmonth=birthmonth;
}

    public User(String name,String birthday,String city){
        this.name=name;
        this.birthday=birthday;
        this.city=city;

    }
public void setRegistrationResponse(String response){
    registrationResponse=response;
}

    public void setOrderResponse(String response){
        orderResponse=response;
    }
}
