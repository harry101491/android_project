package com.cloud.harshitpareek.cloud_project;

import android.app.Service;
import android.content.Intent;
import android.os.Binder;
import android.os.IBinder;
import android.util.Log;
import java.util.List;
import android.util.JsonReader;
import android.widget.Toast;

import java.util.ArrayList;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

/**
 * Created by harshitpareek on 5/10/17.
 */

public class ConnectingService extends Service
{
    private final IBinder myBinder = new MyBinder();
    //private String url_string = "https://kynproj22.appspot.com/restaurants/zip/";
    private String url_string = "http://ec2-34-209-156-29.us-west-2.compute.amazonaws.com:3000/restaurants/zip/";
    private final String TAG = "Inside Service";

    @Override
    public IBinder onBind(Intent in)
    {
        return myBinder;
    }

    // function to read the Json Stream
    public List<Food> readJsonStream(InputStream in) throws IOException {
        JsonReader reader = new JsonReader(new InputStreamReader(in, "UTF-8"));
        try {
            Log.e(TAG, "Inside Read Json Stream");
            return readMessagesArray(reader);
        } finally {
            reader.close();
        }
    }

    public List<Food> readMessagesArray(JsonReader reader) throws IOException {
        List<Food> messages = new ArrayList<>();

        reader.beginArray();
        while (reader.hasNext()) {
            messages.add(readMessage(reader));
        }
        reader.endArray();
        return messages;
    }

    public Food readMessage(JsonReader reader) throws IOException
    {
        long id = 0;
        String name = "";
        String img_url = "";
        String site_url = "";
        double rating = 0;
        double lat = 0.0;
        double lng = 0.0;
        String[] address = null;
        String phone = "";
        double zip = 0;
        String city = "";

        reader.beginObject();
        while (reader.hasNext()) {
            String obj = reader.nextName();
            //Log.e(TAG, "the string value in the obj: "+obj);
            switch (obj)
            {
                case "_id":
                    id = Long.parseLong(reader.nextString());
                    break;
                case "_source":
                    reader.beginObject();
                    while(reader.hasNext())
                    {
                        String nameValue = reader.nextName();
                        switch (nameValue)
                        {
                            case "name":
                                name = reader.nextString();
                                break;
                            case "img":
                                img_url = reader.nextString();
                                break;
                            case "url":
                                site_url = reader.nextString();
                                break;
                            case "rating":
                                rating = reader.nextDouble();
                                break;
                            case "lat":
                                lat = reader.nextDouble();
                                break;
                            case "lng":
                                lng = reader.nextDouble();
                                break;
                            case "fullAddress":
                                reader.skipValue();
                                break;
                            case "phone":
                                phone = reader.nextString();
                                break;
                            case "zip":
                                zip = Double.parseDouble(reader.nextString());
                                break;
                            case "city":
                                city = reader.nextString();
                                break;
                            default:
                                reader.skipValue();
                                break;
                        }
                    }
                    reader.endObject();
                    break;
                default:
                    reader.skipValue();
                    break;
            }
        }
        reader.endObject();
        return new Food(id, name, img_url, site_url, rating, lat, lng, null, phone, zip, city);

    }



    public List<Food> connectServer()
    {
        try
        {
            URL url = new URL(url_string);
            Log.d(TAG, "Inside the connect Server");

            // create HTTP URL Connection and getting the data
            URLConnection connection = url.openConnection();
            HttpURLConnection httpURLConnection = (HttpURLConnection)connection;

            int responseCode = httpURLConnection.getResponseCode();
            if(responseCode == HttpURLConnection.HTTP_OK)
            {
                InputStream in = httpURLConnection.getInputStream();
                List<Food> foodList = readJsonStream(in);
                return foodList;
            }
        }
        catch (MalformedURLException malFormeExe)
        {
            Log.e(TAG, "MalFormed url exception");
        }
        catch (IOException e)
        {
            Log.e(TAG, "IOException has occured");
        }
        return null;
    }

    public class MyBinder extends Binder
    {
        ConnectingService getService()
        {
            return ConnectingService.this;
        }
    }
}
