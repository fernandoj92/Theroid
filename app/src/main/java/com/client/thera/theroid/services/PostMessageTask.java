package com.client.thera.theroid.services;

import android.content.ContentValues;
import android.content.Context;
import android.net.Uri;
import android.os.AsyncTask;
import android.util.Log;
import android.widget.Toast;

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
import java.util.UUID;

/**
 * Created by Fer on 01/04/2015.
 */
public class PostMessageTask extends AsyncTask<Message,Void,Boolean> {//<Input,Partial,Output>

    private Context context;
    private Uri messageInsertedUri= Uri.EMPTY;

    public PostMessageTask(Context context){
        this.context = context;
    }

    @Override
    protected Boolean doInBackground(Message...batteryInfo) {

        Boolean insertionResponse;
        Boolean sendResponse;

        insertionResponse = insertIntoDB(batteryInfo[0]);
        sendResponse = sendData(batteryInfo[0]);

        return insertionResponse && sendResponse;
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

    private Boolean insertIntoDB(Message message){

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
                messageInsertedUri = context.getContentResolver().insert(MessagesContentProvider.CONTENT_URI, values);

        }catch(Exception ex){
            return false;
        }

        return true;
    }

    private Boolean sendData(Message message){
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
                context.getContentResolver().update(messageInsertedUri, values, null, null);
            }
            else{
                //Actualizamos la BD con Error
                ContentValues values = new ContentValues();
                values.put(MessageTable.COLUMN_STATUS, Message.Status.SENT_ERROR.toString());
                context.getContentResolver().update(messageInsertedUri,values,null,null);
                return false;
            }


        }catch(Exception ex)
        {
            Log.e("REST Service", "Generic Error", ex);
            //Actualizamos la BD con TIMEOUT
            ContentValues values = new ContentValues();
            values.put(MessageTable.COLUMN_STATUS, Message.Status.TIMEOUT.toString());
            context.getContentResolver().update(messageInsertedUri, values, null, null);
            return false;
        }

        return true;
    }
}
