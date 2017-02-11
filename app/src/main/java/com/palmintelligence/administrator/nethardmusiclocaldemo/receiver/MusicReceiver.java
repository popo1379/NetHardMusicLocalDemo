package com.palmintelligence.administrator.nethardmusiclocaldemo.receiver;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

import com.palmintelligence.administrator.nethardmusiclocaldemo.MainActivity;

/**
 * Created by Administrator on 2016/10/14 0014.
 */
//测试广播功能用，无实际功能
   public class MusicReceiver extends BroadcastReceiver {
        private boolean isplay=true;


        @Override
public void onReceive(Context context, Intent intent) {
    if (isplay){
        Log.d("MusicReceiver","isplay_OK!");
    }else {
        Log.d("MusicReceiver"," isPause_OK!");
    }
}
}

