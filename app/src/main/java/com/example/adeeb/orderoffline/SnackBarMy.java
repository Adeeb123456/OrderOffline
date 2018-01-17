package com.example.adeeb.orderoffline;

import android.content.Context;
import android.content.Intent;
import android.support.design.widget.Snackbar;
import android.view.View;

/**
 * Created by AdeeB on 11/15/2016.
 */
public class SnackBarMy {
    Context context;
    public SnackBarMy( Context context) {
        this.context=context;

    }

    public void showSnakBar( View coordinatorLayout,String text){
        final Snackbar snackbar =
                Snackbar.make(coordinatorLayout, text, Snackbar.LENGTH_INDEFINITE).setAction("Retry", new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        Intent intent=new Intent(context,Restaurant.class);
                        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                        context.getApplicationContext().startActivity(intent);


                    }
                });
        snackbar.show();
    }

    public void showSnakOffline( View coordinatorLayout,String text){
        final Snackbar snackbar =
                Snackbar.make(coordinatorLayout, text, Snackbar.LENGTH_INDEFINITE).setAction("Go Request", new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        Intent intent=new Intent(context,BirthdayStart.class);
                        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                        context.getApplicationContext().startActivity(intent);


                    }
                });
        snackbar.show();
    }
}
