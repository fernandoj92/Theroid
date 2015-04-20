package com.client.thera.theroid.presentation;

import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.AsyncTask;
import android.os.BatteryManager;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import com.client.thera.theroid.R;
import com.client.thera.theroid.data.MessageTable;
import com.client.thera.theroid.data.MessagesContentProvider;
import com.client.thera.theroid.domain.Message;
import com.client.thera.theroid.services.BatteryService;
import com.client.thera.theroid.services.PostMessageService;
import com.client.thera.theroid.services.RegisterMessageService;
import com.client.thera.theroid.services.RegisterMessageTask;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

/**
 * Created by Fer on 17/03/2015.
 */
public class MainActivity extends Activity {

    private TextView batteryInfoView;
    private Message batteryInfo;

    private BroadcastReceiver batteryInfoReceiver = new BroadcastReceiver() {

        @Override
        public void onReceive(Context context, Intent intent) {
            int  health= intent.getIntExtra(BatteryManager.EXTRA_HEALTH,0);
            int  icon_small= intent.getIntExtra(BatteryManager.EXTRA_ICON_SMALL,0);
            int  level= intent.getIntExtra(BatteryManager.EXTRA_LEVEL,0);
            int  plugged= intent.getIntExtra(BatteryManager.EXTRA_PLUGGED,0);
            boolean  present= intent.getExtras().getBoolean(BatteryManager.EXTRA_PRESENT);
            int  scale= intent.getIntExtra(BatteryManager.EXTRA_SCALE,0);
            int  status= intent.getIntExtra(BatteryManager.EXTRA_STATUS,0);
            String  technology= intent.getExtras().getString(BatteryManager.EXTRA_TECHNOLOGY);
            int  temperature= intent.getIntExtra(BatteryManager.EXTRA_TEMPERATURE,0);//Devuele 10x la temperatura, para obtener asi el decimal sin ser double
            int  voltage= intent.getIntExtra(BatteryManager.EXTRA_VOLTAGE,0);//Devuelve 10x el voltage


            batteryInfoView.setText(
                    "Health: " + health + "\n" +
                            "Icon Small:" + icon_small + "\n" +
                            "Level: " + level + "\n" +
                            "Plugged: " + plugged + "\n" +
                            "Present: " + present + "\n" +
                            "Scale: " + scale + "\n" +
                            "Status: " + status + "\n" +
                            "Technology: " + technology + "\n" +
                            "Temperature: " + temperature + "\n" +
                            "Voltage: " + voltage + "\n");

            batteryInfo = new Message(temperature,health,voltage);
        }
    };


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);
        batteryInfoView =(TextView)findViewById(R.id.textViewBatteryInfo);

        this.registerReceiver(this.batteryInfoReceiver,	new IntentFilter(Intent.ACTION_BATTERY_CHANGED));
    }
    @Override
    public void onDestroy(){
        super.onDestroy();
        unregisterReceiver(batteryInfoReceiver);
    }

    public void showMessageList(View view){
        Intent intent = new Intent(this, MessageListActivity.class);
        startActivity(intent);
    }

    public void sendBatteryInfo(View view){
        RegisterMessageTask registerTask = new RegisterMessageTask(getApplicationContext());
        registerTask.execute(batteryInfo);
    }

    public void startRegisterService(View view){
        Intent intent = new Intent(this, RegisterMessageService.class);
        intent.putExtra("message", UUID.randomUUID().toString());//Random message
        startService(intent);
    }

    public void startPostService(View view){
        Intent intent = new Intent(this, PostMessageService.class);
        intent.putExtra("batteryInfo",batteryInfo);
        startService(intent);
    }

    public void startBatteryService(){

    }

    public void stopBatteryService(){
        Intent intent = new Intent(this, BatteryService.class);
        stopService(intent);
    }

}

