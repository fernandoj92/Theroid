package com.client.thera.theroid.services;

import android.app.IntentService;
import android.content.Intent;
import android.net.Uri;
import android.util.Log;

import com.client.thera.theroid.data.MessagesContentProvider;

import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.util.EntityUtils;
import org.json.JSONObject;

/**
 * Created by Fer on 25/03/2015.
 */

//TODO AsyncTask maybe?
public class PostMessageService extends IntentService{

    public PostMessageService() {
        super("PostMessageService");
    }

    @Override
    protected void onHandleIntent(Intent workIntent) {

        // HTTP - Deprecated, but easier for now
        HttpClient httpClient = new DefaultHttpClient();
        HttpPost post =
                new HttpPost("http://10.0.2.2:2731/Api/Clientes/Cliente");
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

            //if(!respStr.equals("true")) //Ok
            //else //Error, insertamos en la BD como error

            //Start Register service (NO SE PUEDE EMPEZAR UN SERVICE DESDE OTRO SERVICE)


        }catch(Exception ex)
        {
            Log.e("Servicio REST", "Error generico", ex);

        }

    }
}
