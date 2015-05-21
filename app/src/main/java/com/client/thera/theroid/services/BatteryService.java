package com.client.thera.theroid.services;

import android.app.Service;
import android.content.BroadcastReceiver;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.net.Uri;
import android.os.BatteryManager;
import android.os.IBinder;
import android.util.Log;

import com.client.thera.theroid.data.MessageTable;
import com.client.thera.theroid.data.MessagesContentProvider;
import com.client.thera.theroid.domain.Message;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.params.BasicHttpParams;
import org.apache.http.params.HttpConnectionParams;
import org.apache.http.params.HttpParams;
import org.apache.http.util.EntityUtils;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.UUID;

/**
 * Created by Fer on 25/03/2015.
 */
public class BatteryService extends Service{

    private Uri messageInsertedUri= Uri.EMPTY;

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
                    Message message = new Message(temperature,health,voltage,new Date());
                    //Insert data into the DB (fast)
                    insertIntoDB(message);
                    //Send data to WebService (uncertain)
                    postMessage(message);
                }
            };

            thread.start();
        }
    };

    private void insertIntoDB(Message message){

        try {

            //Creates a value to insert
            ContentValues values = new ContentValues();
            values.put(MessageTable.COLUMN_TEMPERATURE, message.getContent().getTemperature());
            values.put(MessageTable.COLUMN_HEALTH, message.getContent().getHealth());
            values.put(MessageTable.COLUMN_VOLTAGE, message.getContent().getVoltage());
            values.put(MessageTable.COLUMN_STATUS, message.getStatus().toString());
            //SQLite DateTime Format needed
            SimpleDateFormat sf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            values.put(MessageTable.COLUMN_EVENT_TIME,sf.format(message.getEventTime()));
            //Calls the Content Resolver for the insertion in the MessagesContentProvider
            messageInsertedUri = getApplicationContext().getContentResolver().insert(MessagesContentProvider.CONTENT_URI, values);

        }catch(Exception e){
            Log.e("BatteryService", "Unexpected Error");
        }
    }

    private void postMessage(Message message){
        // HTTP - Deprecated, but easier for now
        final HttpParams httpParams = new BasicHttpParams();
        HttpConnectionParams.setConnectionTimeout(httpParams, 3000);//TCP connection timeout = 3 seconds
        HttpConnectionParams.setSoTimeout(httpParams, 3000);//Wait_For_Response timeout = 3 seconds
        HttpClient httpClient = new DefaultHttpClient(httpParams);
        //REST Service URL
        HttpPost post = new HttpPost("http://54.76.225.140/api/messages/save");
        post.setHeader("content-type", "application/json");
        try {

            message.setDeviceID(UUID.fromString("671ef2f3-cd54-42c8-9598-57e0ace17c1a"));//TODO: add it to the constructor of Message.class
            ObjectMapper mapper = new ObjectMapper(); //TODO: Create one once the activity is created so i do not have to create one on each send...
            String jsonAsString = mapper.writeValueAsString(message);
            StringEntity entity = new StringEntity(jsonAsString);
            post.setEntity(entity);


            HttpResponse resp = httpClient.execute(post);

            //Expected "Ok" response (JSON)
            JSONObject okResponse = new JSONObject();
            okResponse.put("status","OK");
            okResponse.put("message date",message.getEventTime().getTime());

            //Create 2 JsonNode Objects
            JsonNode respTree = mapper.readTree(EntityUtils.toString(resp.getEntity()));
            JsonNode okRespTree = mapper.readTree(okResponse.toString());

            if(respTree.equals(okRespTree)){
                //Actualizamos la BD con Ok
                ContentValues values = new ContentValues();
                values.put(MessageTable.COLUMN_STATUS, Message.Status.SENT_OK.toString());
                getApplicationContext().getContentResolver().update(messageInsertedUri, values, null, null);
            }
            else{
                //Actualizamos la BD con Error
                ContentValues values = new ContentValues();
                values.put(MessageTable.COLUMN_STATUS, Message.Status.SENT_ERROR.toString());
                getApplicationContext().getContentResolver().update(messageInsertedUri,values,null,null);
            }


        }catch(Exception ex)
        {
            Log.e("REST Service", "Generic Error", ex);
            //Actualizamos la BD con TIMEOUT
            ContentValues values = new ContentValues();
            values.put(MessageTable.COLUMN_STATUS, Message.Status.TIMEOUT.toString());
            getApplicationContext().getContentResolver().update(messageInsertedUri, values, null, null);
        }
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
