package com.cloud.harshitpareek.cloud_project;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.JsonReader;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ListView;
import android.widget.Toast;

import com.google.android.gms.common.api.Scope;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by harshitpareek on 5/11/17.
 */

public class SocialListActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener
{
    private int zipcode = -1;
    private ListView listView;
    //private String social_url = "https://kynproj22.appspot.com/social/zip/";
    private String social_url = "http://ec2-34-209-156-29.us-west-2.compute.amazonaws.com:3000/social/zip/";
    private List<Social> social_list;
    private final String TAG = "Inside social List";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.social_list_layout);

        Intent intent = getIntent();
        zipcode = intent.getIntExtra("zipcode", 10002);

        social_url += String.valueOf(zipcode);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar_social);
        setSupportActionBar(toolbar);

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout_list);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        // new thread for fetching the data
        Thread t = new Thread(new Runnable() {
            @Override
            public void run() {
                social_list = getSocialListFromURL();
                //Log.e(TAG, "the value of address in food_list 0th element:"+food_list.get(0).getAddress());
            }
        });
        t.start();

        try
        {
            t.join();
            listView = (ListView) findViewById(R.id.social_list_view);
            listView.setAdapter(new SocialAdapter(getApplicationContext(), social_list));

        }
        catch (InterruptedException ext)
        {
            Log.e(TAG, "Interrupted exception has occured");
        }
    }

    public List<Social> readJsonStream(InputStream in) throws IOException {
        JsonReader reader = new JsonReader(new InputStreamReader(in, "UTF-8"));
        try {
            Log.e(TAG, "Inside Read Json Stream");
            return readMessagesArray(reader);
        } finally {
            reader.close();
        }
    }

    public List<Social> readMessagesArray(JsonReader reader) throws IOException {
        List<Social> messages = new ArrayList<>();

        reader.beginArray();
        while (reader.hasNext()) {
            messages.add(readMessage(reader));
        }
        reader.endArray();
        return messages;
    }

    public Social readMessage(JsonReader reader) throws IOException
    {
        long id = 0;
        String name = "";
        String img_url = "";
        String site_url = "";
        double rating = 0;
        double lat = 0.0;
        double lng = 0.0;
        String address = null;
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
                                reader.beginArray();
                                address = reader.nextString();
                                reader.skipValue();
                                reader.endArray();
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
        return new Social(id, name, img_url, site_url, rating, lat, lng, address, phone, zip, city);

    }

    private List<Social> getSocialListFromURL()
    {
        try
        {
            URL url = new URL(social_url);
            Log.d(TAG, "Inside the connect Server");

            // create HTTP URL Connection and getting the data
            URLConnection connection = url.openConnection();
            HttpURLConnection httpURLConnection = (HttpURLConnection)connection;

            int responseCode = httpURLConnection.getResponseCode();
            if(responseCode == HttpURLConnection.HTTP_OK)
            {
                InputStream in = httpURLConnection.getInputStream();
                List<Social> socialList = readJsonStream(in);
                return socialList;
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


    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout_list);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            // do something when the setting is selected
            Toast.makeText(getApplicationContext(), "setting has been selected", Toast.LENGTH_LONG).show();
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.nav_camera) {
            // Handle the camera action
        } else if (id == R.id.nav_gallery) {

        } else if (id == R.id.nav_slideshow) {

        } else if (id == R.id.nav_manage) {

        } else if (id == R.id.nav_share) {

        } else if (id == R.id.nav_send) {

        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout_list);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

}
