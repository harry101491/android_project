package com.cloud.harshitpareek.cloud_project;

import android.content.Intent;
import android.os.Bundle;
import android.provider.ContactsContract;
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
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.Toast;

import java.io.BufferedReader;
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

public class StatActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener
{
    private int zipcode = -1;
    //private String data_url = "https://kynproj22.appspot.com/complaints/zip/";
    //private String stat_url = "https://kynproj22.appspot.com/rating/zip/";
    private String data_url = "http://ec2-34-209-156-29.us-west-2.compute.amazonaws.com:3000/complaints/zip/";
    private String stat_url = "http://ec2-34-209-156-29.us-west-2.compute.amazonaws.com:3000/rating/zip/" ;
    private Ratings ratings;
    private List<Data_Point> data_points = new ArrayList<>();
    private final String TAG = "Inside Stat";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.stat_layout_main);

        Intent intent = getIntent();
        zipcode = intent.getIntExtra("zipCode", 10000);

        stat_url += String.valueOf(zipcode);
        //data_url += String.valueOf(zipcode);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar_stat);
        setSupportActionBar(toolbar);

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout_stat);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        Thread t = new Thread(new Runnable() {
            @Override
            public void run() {
                ratings = getRatingsFromURL();
                //getTopDataPoints();
            }
        });
        t.start();

        try
        {
            t.join();

            Log.e(TAG, "the length of the value is:"+data_points.size());

            RatingBar rBar = (RatingBar) findViewById(R.id.ratingBar);
            ImageView imageView = (ImageView) findViewById(R.id.emoji_image);
            TextView textView = (TextView) findViewById(R.id.suggestion);
            if(ratings.getLevel().equals("Low"))
            {
                float lowRate = (float) 2.5;
                rBar.setRating(lowRate);
                imageView.setImageResource(R.drawable.low_emoji);
                textView.setText(R.string.lowSuggestion);
            }
            else if(ratings.getLevel().equals("Medium"))
            {
                float midRate = (float) 3.5;
                rBar.setRating(midRate);
                imageView.setImageResource(R.drawable.mid_emoji);
                textView.setText(R.string.midSuggestion);
            }
            else if(ratings.getLevel().equals("High"))
            {
                float highRate = (float) 5;
                rBar.setRating(highRate);
                imageView.setImageResource(R.drawable.high_emoji);
                textView.setText(R.string.highSuggestion);
            }
        }
        catch (InterruptedException inExc)
        {
            Log.e(TAG, "Interrupted Exception has occured");
        }
    }


    public void getTopDataPoints()
    {
        try
        {
            URL url = new URL(data_url);
            Log.d(TAG, "Inside the connect Server");

            // create HTTP URL Connection and getting the data
            URLConnection connection = url.openConnection();
            HttpURLConnection httpURLConnection = (HttpURLConnection)connection;

            int responseCode = httpURLConnection.getResponseCode();
            if(responseCode == HttpURLConnection.HTTP_OK) {
                InputStream in = httpURLConnection.getInputStream();
                BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(in, "UTF-8"));
                String value = "";
                String line = "";
                int counter = 0;

                while((line = bufferedReader.readLine()) != null) {
                    if (line.equals("[") && line.length() == 1 && counter < 5) {
                        Log.e(TAG, "Inside the [:"+line);
                        counter++;
                    } else if (line.equals("]") && line.length() == 1 && counter < 5) {
                        Log.e(TAG, "Inside the ]:"+line);
                        counter++;
                    } else if (line.length() == 1 || counter > 5) {
                        Log.e(TAG, line);
                        counter++;
                    } else {
                        Data_Point point = parseData(line);
                        data_points.add(point);
                        counter++;
                    }
                }
            }
        }
        catch (MalformedURLException malFormeExe)
        {
            Log.e(TAG, "MalFormed url exception");
        }
        catch (IOException exc)
        {
            Log.e(TAG, "IOException has occured");
        }
    }

    private Data_Point parseData(String str)
    {
        String values[] = str.split(",");
        if(values.length == 11)
        {
            String date = values[1];
            String agency = values[2];
            String type = values[3];
            String desc = values[4];
            double lat = Double.parseDouble(values[9]);
            double lng = Double.parseDouble(values[10].replace("\"", ""));
            return new Data_Point(date, agency, type, desc, lat, lng);
        }
        else
        {

        }
        return null;
    }

    public Ratings readJsonStream(InputStream in) throws IOException {
        JsonReader reader = new JsonReader(new InputStreamReader(in, "UTF-8"));
        try {
            Log.e(TAG, "Inside Read Json Stream");
            return readMessage(reader);
        } finally {
            reader.close();
        }
    }

    public Ratings readMessage(JsonReader reader) throws IOException
    {
        String level = "";
        String text = "";

        reader.beginObject();
        while (reader.hasNext())
        {
            String name = reader.nextName();
            switch (name)
            {
                case "PeaceLevel":
                    level = reader.nextString();
                    break;
                case "text":
                    text = reader.nextString();
                    break;
                default:
                    break;
            }
        }
        reader.endObject();
        return new Ratings(level, text);
    }

    private Ratings getRatingsFromURL()
    {
        try
        {
            URL url = new URL(stat_url);
            Log.d(TAG, "Inside the connect Server");

            // create HTTP URL Connection and getting the data
            URLConnection connection = url.openConnection();
            HttpURLConnection httpURLConnection = (HttpURLConnection)connection;

            int responseCode = httpURLConnection.getResponseCode();
            if(responseCode == HttpURLConnection.HTTP_OK)
            {
                InputStream in = httpURLConnection.getInputStream();
                Ratings ratings = readJsonStream(in);
                return ratings;
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
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout_stat);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings){
            Toast.makeText(getApplicationContext(), "setting has been pressed", Toast.LENGTH_LONG).show();
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

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

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout_stat);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

}
