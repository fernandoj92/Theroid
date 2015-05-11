package com.client.thera.theroid.presentation;

import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.ContentUris;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.database.Cursor;
import android.net.Uri;
import android.os.BatteryManager;
import android.os.Bundle;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.client.thera.theroid.R;
import com.client.thera.theroid.data.MessageTable;
import com.client.thera.theroid.data.MessagesContentProvider;
import com.client.thera.theroid.domain.Message;
import com.client.thera.theroid.services.BatteryService;
import com.client.thera.theroid.services.PostMessageService;
import com.client.thera.theroid.services.RegisterMessageService;
import com.client.thera.theroid.services.RegisterMessageTask;

import org.joda.time.DateTime;

import java.sql.Date;
import java.util.UUID;

/**
 * Created by Fer on 17/03/2015.
 */
public class MessageDetailActivity extends Activity {
   // private String status ="";
   // private String event;
    private int temperature= -1;
    private int health = -2;
    private int voltage = -3;
    private long _id = 0;
    private String eventTime="Undefined";


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        long id = getIntent().getLongExtra("id",0);//TODO: -1 con excepcion mejor que 0?

        //DB query using the row Id
        Uri selectUri = ContentUris.withAppendedId(MessagesContentProvider.CONTENT_URI,id);
        String[] projection = { MessageTable.COLUMN_ID, MessageTable.COLUMN_TEMPERATURE, MessageTable.COLUMN_HEALTH, MessageTable.COLUMN_VOLTAGE, MessageTable.COLUMN_EVENT_TIME };
        Cursor cursor = getContentResolver().query(selectUri,projection,null,null,null);
        if(cursor != null){
            if(cursor.moveToFirst()){
                do{
                    _id = cursor.getLong(cursor.getColumnIndex("_id"));
                    temperature = cursor.getInt(cursor.getColumnIndex("Temperature"));
                    health = cursor.getInt(cursor.getColumnIndex("Health"));
                    voltage = cursor.getInt(cursor.getColumnIndex("Voltage"));
                    eventTime = cursor.getString(cursor.getColumnIndex("EventTime"));
                    //long milliseconds = cursor.getInt(cursor.getColumnIndex("EvenTime"));
                    //eventtime = new DateTime(milliseconds);

                }while (cursor.moveToNext());

            }
        }

        //UI
        setContentView(R.layout.message_detail);

        TextView temperatureDetailView = (TextView) findViewById(R.id.temperature_detail);
        temperatureDetailView.setText(""+temperature);

        TextView voltageDetailView = (TextView) findViewById(R.id.voltage_detail);
        voltageDetailView.setText(""+voltage);

        TextView healthDetailView = (TextView) findViewById(R.id.health_detail);
        healthDetailView.setText(""+health);

        TextView eventTimeDetailView = (TextView) findViewById(R.id.eventTime_detail);
        eventTimeDetailView.setText(eventTime);


    }
    @Override
    public void onDestroy(){
        super.onDestroy();
    }


}

