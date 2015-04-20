package com.client.thera.theroid.services;

import android.content.ContentValues;
import android.content.Context;
import android.os.AsyncTask;
import android.widget.Toast;

import com.client.thera.theroid.data.MessageTable;
import com.client.thera.theroid.data.MessagesContentProvider;
import com.client.thera.theroid.domain.Message;

/**
 * Created by Fer on 01/04/2015.
 */
public class RegisterMessageTask extends AsyncTask<Message,Void,Boolean> {//<Input,Partial,Output>

    private Context context;

    public RegisterMessageTask(Context context){
        this.context = context;
    }

    @Override
    protected Boolean doInBackground(Message...batteryInfo) {

        try {

            for (Message message : batteryInfo) {
                //Creates a value to insert
                ContentValues values = new ContentValues();
                values.put(MessageTable.COLUMN_TEMPERATURE, message.getTemperature());
                values.put(MessageTable.COLUMN_HEALTH, message.getHealth());
                values.put(MessageTable.COLUMN_VOLTAGE, message.getVoltage());
                values.put(MessageTable.COLUMN_STATUS, message.getStatus());
                //Calls the Content Resolver for the insertion in the MessagesContentProvider
                context.getContentResolver().insert(MessagesContentProvider.CONTENT_URI, values);
            }

        }catch(Exception ex){
            return false;
        }

        return true;
    }

    @Override
    protected void onPostExecute(Boolean result) {

        String message = "Ha ocurrido un error registrando los datos";
        if(result)
            message = "Datos registrados correctamente";

        //Toast, edit view
        Toast toast = Toast.makeText(context, message, Toast.LENGTH_SHORT);
        toast.show();
    }
}
