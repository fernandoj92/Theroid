package com.client.thera.theroid.services;

import android.app.Service;
import android.content.BroadcastReceiver;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.BatteryManager;
import android.os.IBinder;

import com.client.thera.theroid.data.MessageTable;
import com.client.thera.theroid.data.MessagesContentProvider;
import com.client.thera.theroid.domain.Message;

import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter;

/**
 * Created by Fer on 25/03/2015.
 */
public class BatteryService extends Service{

    private BroadcastReceiver batteryInfoReceiver = new BroadcastReceiver() {

        @Override
        public void onReceive(Context context, Intent intent) {
            //Get data from the broadcast
            final int health = intent.getIntExtra(BatteryManager.EXTRA_HEALTH, 0);
            int icon_small = intent.getIntExtra(BatteryManager.EXTRA_ICON_SMALL, 0);
            int level = intent.getIntExtra(BatteryManager.EXTRA_LEVEL, 0);
            int plugged = intent.getIntExtra(BatteryManager.EXTRA_PLUGGED, 0);
            boolean present = intent.getExtras().getBoolean(BatteryManager.EXTRA_PRESENT);
            int scale = intent.getIntExtra(BatteryManager.EXTRA_SCALE, 0);
            int status = intent.getIntExtra(BatteryManager.EXTRA_STATUS, 0);
            String technology = intent.getExtras().getString(BatteryManager.EXTRA_TECHNOLOGY);
            final int temperature = intent.getIntExtra(BatteryManager.EXTRA_TEMPERATURE, 0);//Devuele 10x la temperatura, para obtener asi el decimal sin ser double
            final int voltage = intent.getIntExtra(BatteryManager.EXTRA_VOLTAGE, 0);//Devuelve 10x el voltage

            //Do something with the data
            Thread thread = new Thread(){
                public void run(){
                    Message message = new Message(temperature,health,voltage);
                    //Insert data into the DB
                    insertIntoDB(message);
                    //Send data to WebService
                    postMessage(message);
                }
            };

            thread.start();
        }
    };

    private void insertIntoDB(Message message){
        //Creates a value to insert
        ContentValues values = new ContentValues();
        values.put(MessageTable.COLUMN_TEMPERATURE,message.getContent().getTemperature());
        values.put(MessageTable.COLUMN_HEALTH,message.getContent().getHealth());
        values.put(MessageTable.COLUMN_VOLTAGE,message.getContent().getVoltage());
        values.put(MessageTable.COLUMN_STATUS,message.getStatus());
        //SQLite DateTime Format needed
        DateTimeFormatter dtfOut = DateTimeFormat.forPattern("yyyy-MM-dd HH:mm:ss");
        values.put(MessageTable.COLUMN_EVENT_TIME,dtfOut.print(message.getEventTime()));
        //Calls the Content Resolver for the insertion in the MessagesContentProvider
        getContentResolver().insert(MessagesContentProvider.CONTENT_URI,values);
    }

    private void postMessage(Message message){
        //TODO implementation...
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        //Register the BroadcastReceiver
        this.registerReceiver(this.batteryInfoReceiver,	new IntentFilter(Intent.ACTION_BATTERY_CHANGED));
        return Service.START_STICKY;
    }
    //Not used, background service
    @Override
    public IBinder onBind(Intent intent) {
        //TODO for communication return IBinder implementation
        return null;
    }
    //Called on stopSelf() or stopService()
    @Override
    public void onDestroy() {
        this.unregisterReceiver(this.batteryInfoReceiver);
    }
}
