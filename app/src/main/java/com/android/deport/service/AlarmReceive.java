package com.android.deport.service;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

import com.android.deport.ui.MainActivity;

public class AlarmReceive extends BroadcastReceiver {
    @Override
    public void onReceive(Context context, Intent intent) {
        //循环启动Service
        if (MainActivity.isInternet){
            Intent i = new Intent(context, AlarmService.class);
            context.startService(i);
        }
    }
}
