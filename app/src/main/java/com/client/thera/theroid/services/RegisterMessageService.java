package com.client.thera.theroid.services;

import android.app.IntentService;
import android.content.ContentValues;
import android.content.Intent;

import com.client.thera.theroid.data.MessageTable;
import com.client.thera.theroid.data.MessagesContentProvider;
import com.client.thera.theroid.domain.Message;

/**
 * Created by Fer on 25/03/2015.
 */
public class RegisterMessageService extends IntentService{

    public RegisterMessageService() {
        super("RegisterMessageService");
    }

    @Override
    protected void onHandleIntent(Intent workIntent) {
        // Gets data from the incoming Intent
        Message message = (Message) workIntent.getSerializableExtra("message");
        //Creates a value to insert
        ContentValues values = new ContentValues();
        values.put(MessageTable.COLUMN_TEMPERATURE,message.getTemperature());
        values.put(MessageTable.COLUMN_HEALTH,message.getHealth());
        values.put(MessageTable.COLUMN_VOLTAGE,message.getVoltage());
        values.put(MessageTable.COLUMN_STATUS,message.getStatus());
        //Calls the Content Resolver for the insertion in the MessagesContentProvider
        getContentResolver().insert(MessagesContentProvider.CONTENT_URI,values);
    }
}
