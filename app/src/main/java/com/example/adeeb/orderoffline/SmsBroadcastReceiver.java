package com.example.adeeb.orderoffline;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

public class SmsBroadcastReceiver extends BroadcastReceiver {
    public SmsBroadcastReceiver() {
    }
Context context;

    @Override
    public void onReceive(Context context, Intent intent) {
        // TODO: This method is called when the BroadcastReceiver is receiving
        // an Intent broadcast.
      this.context=context;
        
    }
}
