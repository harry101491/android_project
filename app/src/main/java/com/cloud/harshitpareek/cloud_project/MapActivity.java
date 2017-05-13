package com.cloud.harshitpareek.cloud_project;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;

import android.widget.Toast;
import java.io.BufferedReader;
import java.io.InputStreamReader;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import java.util.ArrayList;
import java.util.List;

import com.google.android.gms.maps.*;
import com.google.android.gms.maps.model.*;


/**
 * Created by harshitpareek on 5/9/17.
 */

public class MapActivity extends AppCompatActivity implements OnMapReadyCallback, NavigationView.OnNavigationItemSelectedListener
{

    private int zipcode = -1;
    //private String data_url = "https://kynproj22.appspot.com/complaints/zip/";
    private String data_url = "http://ec2-34-209-156-29.us-west-2.compute.amazonaws.com:3000/complaints/zip/";
    private Marker marker;
    private List<Data_Point> list_data = new ArrayList<>();
    private final String TAG = "Map Related";
    private GoogleMap map;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.map_layout_main);

        Intent intent = getIntent();
        zipcode = intent.getIntExtra("zipcode", 10002);

        Toolbar tool = (Toolbar) findViewById(R.id.toolbar_map);
        setSupportActionBar(tool);

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout_map);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, tool, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        // creating a new thread and parsing the data from the url
        if(zipcode != -1)
        {
            //add the zipcode to the url
            data_url += String.valueOf(zipcode);

            // creating a new thread and calling the data
            Thread t = new Thread(new Runnable() {
                @Override
                public void run() {
                    getData();
                    Log.e(TAG, "Inside the run function");
                }
            });
            t.start();

            try
            {
                t.join();

                MapFragment mapFragment = (MapFragment) getFragmentManager().findFragmentById(R.id.map);
                mapFragment.getMapAsync(this);



            }
            catch (InterruptedException intrupt)
            {
                intrupt.printStackTrace();
            }

        }
        else
        {

        }

    }

    private void getData()
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
                    if (line.equals("[") && line.length() == 1 && counter < 25) {
                        Log.e(TAG, "Inside the [:"+line);
                        counter++;
                    } else if (line.equals("]") && line.length() == 1 && counter < 25) {
                        Log.e(TAG, "Inside the ]:"+line);
                        counter++;
                    } else if (line.length() == 1 || counter > 25) {
                        Log.e(TAG, line);
                        counter++;
                    } else {
                        Data_Point point = parseData(line);
                        list_data.add(point);
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
    private boolean isDouble(String str)
    {
        try
        {
            double value = Double.parseDouble(str);
            return true;
        }
        catch (NumberFormatException exe)
        {
            return false;
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

    @Override
    public void onMapReady(GoogleMap newmap)
    {
        // setting the info_window for the perticular icon
        map = newmap;

        LatLng newYork = new LatLng(40.7128, -74.0059);
        map.moveCamera(CameraUpdateFactory.newLatLngZoom(newYork, 13));

        if(list_data.size() != 0)
        {

            for(int i=0;i<list_data.size();i++) {
                Data_Point obj = list_data.get(i);
                LatLng newLatLng = new LatLng(obj.getLat(), obj.getLng());
                MarkerOptions markerOptions = new MarkerOptions();
                if (obj.getType().contains("Noise")) {
                    //markerOptions.icon(BitmapDescriptorFactory.fromResource(R.drawable.noise_icon));
                    markerOptions.title(obj.getAgency());
                    markerOptions.snippet(obj.getDescription());
                    markerOptions.position(newLatLng);
                } else if (obj.getType().contains("Traffic")) {
                    //markerOptions.icon(BitmapDescriptorFactory.fromResource(R.drawable.bike_icon));
                    markerOptions.title(obj.getAgency());
                    markerOptions.snippet(obj.getDescription());
                    markerOptions.position(newLatLng);
                } else if (obj.getType().contains("health")) {
                    //markerOptions.icon(BitmapDescriptorFactory.fromResource(R.drawable.health_care_icon));
                    markerOptions.title(obj.getAgency());
                    markerOptions.snippet(obj.getDescription());
                    markerOptions.position(newLatLng);
                } else {
                    //markerOptions.icon(BitmapDescriptorFactory.fromResource(R.drawable.other_icon));
                    markerOptions.title(obj.getAgency());
                    markerOptions.snippet(obj.getDescription());
                    markerOptions.position(newLatLng);
                }
                map.addMarker(markerOptions);
            }

        }
        else
        {

        }

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout_map);
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

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout_map);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }
}
