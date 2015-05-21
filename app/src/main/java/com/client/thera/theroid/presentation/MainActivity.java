package com.client.thera.theroid.presentation;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.BatteryManager;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

import com.client.thera.theroid.R;
import com.client.thera.theroid.domain.Message;
import com.client.thera.theroid.presentation.messages.MessageListActivity;
import com.client.thera.theroid.presentation.settings.SettingsActivity;
import com.client.thera.theroid.services.BatteryService;
import com.client.thera.theroid.services.PostMessageTask;

import java.util.Date;

/**
 * Created by Fer on 17/03/2015.
 */
public class MainActivity extends ActionBarActivity {

    private TextView batteryInfoView;
    private Message batteryInfo;

    private BroadcastReceiver batteryInfoReceiver = new BroadcastReceiver() {

        @Override
        public void onReceive(Context context, Intent intent) {
            int  health= intent.getIntExtra(BatteryManager.EXTRA_HEALTH,0);
            int  icon_small= intent.getIntExtra(BatteryManager.EXTRA_ICON_SMALL,0);
            int  level= intent.getIntExtra(BatteryManager.EXTRA_LEVEL,0);
            int  plugged= intent.getIntExtra(BatteryManager.EXTRA_PLUGGED, 0);
            boolean  present= intent.getExtras().getBoolean(BatteryManager.EXTRA_PRESENT);
            int  scale= intent.getIntExtra(BatteryManager.EXTRA_SCALE, 0);
            int  status= intent.getIntExtra(BatteryManager.EXTRA_STATUS, 0);
            String  technology= intent.getExtras().getString(BatteryManager.EXTRA_TECHNOLOGY);
            int  temperature= intent.getIntExtra(BatteryManager.EXTRA_TEMPERATURE, 0);//Devuelve 10x la temperatura, para obtener asi el decimal sin ser double
            int  voltage= intent.getIntExtra(BatteryManager.EXTRA_VOLTAGE, 0);//Devuelve 10x el voltage

            //We only set a few parameters (need some tweaks)
            TextView temperatureDetailView = (TextView) findViewById(R.id.temperature_main_detail);
            temperatureDetailView.setText(""+temperature);

            TextView voltageDetailView = (TextView) findViewById(R.id.voltage_main_detail);
            voltageDetailView.setText(""+voltage);

            TextView healthDetailView = (TextView) findViewById(R.id.health_main_detail);
            healthDetailView.setText(""+health);

            batteryInfo = new Message(temperature,health,voltage);
        }
    };


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);

        this.registerReceiver(this.batteryInfoReceiver, new IntentFilter(Intent.ACTION_BATTERY_CHANGED));
    }
    @Override
    public void onDestroy(){
        super.onDestroy();
        unregisterReceiver(batteryInfoReceiver);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle item selection
        switch (item.getItemId()) {
            case R.id.action_settings:
                Intent settingsIntent = new Intent(this, SettingsActivity.class);
                startActivity(settingsIntent);
                return true;
            case R.id.action_messages:
                Intent messagesIntent = new Intent(this, MessageListActivity.class);
                startActivity(messagesIntent);
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

        public void sendBatteryInfo(View view){
        //EventTime
        batteryInfo.setEventTime(new Date());

        //Store data into the SQLite DB and send it to the server afterwards
        PostMessageTask postMessageTask = new PostMessageTask(getApplicationContext());
        postMessageTask.execute(batteryInfo);

    }

    public void startBatteryService(View view){
        Intent intent = new Intent(this, BatteryService.class);
        //Bundle bundle = new Bundle();
        //bundle.putSerializable("batteryInfo",batteryInfo);
        //intent.putExtras(bundle);
        startService(intent);
    }

    public void stopBatteryService(View view){
        Intent intent = new Intent(this, BatteryService.class);
        stopService(intent);
    }

}

