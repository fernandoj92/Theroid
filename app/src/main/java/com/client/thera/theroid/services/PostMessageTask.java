package com.client.thera.theroid.services;

import android.content.ContentValues;
import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;

import com.client.thera.theroid.data.MessageTable;
import com.client.thera.theroid.data.MessagesContentProvider;
import com.client.thera.theroid.domain.Message;

import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.util.EntityUtils;
import org.json.JSONObject;

/**
 * Created by Fer on 01/04/2015.
 */
public class PostMessageTask extends AsyncTask<Message,Void,Boolean> {//<Input,Partial,Output>

    private Context context;

    public PostMessageTask(Context context){
        this.context = context;
    }

    @Override
    protected Boolean doInBackground(Message...batteryInfo) {

        // HTTP - Deprecated, but easier for now
        HttpClient httpClient = new DefaultHttpClient();
        //REST Service URL
        HttpPost post = new HttpPost("http://10.0.2.2:2731/Api/Clientes/Cliente");
        post.setHeader("content-type", "application/json");
        try {

            //Constructing JSON Object
            JSONObject data = new JSONObject();

            data.put("Nombre", "Nombre prueba");
            data.put("Telefono", "Telefono prueba");

            StringEntity entity = new StringEntity(data.toString());
            post.setEntity(entity);

            HttpResponse resp = httpClient.execute(post);
            String respStr = EntityUtils.toString(resp.getEntity());

            if(respStr.equals("true"))//Ok
            {
                //Actualizamos la BD con Ok
            }
            else{//Error, actualizamos la BD con error
            }

            //Start Register service (NO SE PUEDE EMPEZAR UN SERVICE DESDE OTRO SERVICE)


        }catch(Exception ex)
        {
            Log.e("REST Service", "Generic Error", ex);
            return false;
        }

        return true;
    }

    @Override
    protected void onPostExecute(Boolean result) {
        //Edit view, Toast
    }
}