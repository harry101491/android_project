package com.cloud.harshitpareek.cloud_project;

import android.content.ComponentName;
import android.content.ServiceConnection;
import android.os.Bundle;
import android.os.IBinder;
import android.support.v7.app.AppCompatActivity;
import android.content.Intent;
import android.content.Context;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import java.util.List;
import android.util.Log;

/**
 * Created by harshitpareek on 5/10/17.
 */

public class ServiceBindingActivity extends AppCompatActivity
{
    // creating instance for the bind service
    private ConnectingService myService;
    private boolean isBound = false;
    private List<Food> listFood;

    private TextView tView;
    private Button timeButton;

    private ServiceConnection myConnection = new ServiceConnection() {
        @Override
        public void onServiceConnected(ComponentName name, IBinder service) {
            ConnectingService.MyBinder binder = (ConnectingService.MyBinder) service;
            myService = binder.getService();
            isBound = true;
        }

        @Override
        public void onServiceDisconnected(ComponentName name) {
            isBound = false;
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.bind_test);

        Intent intent = new Intent(this, ConnectingService.class);
        bindService(intent, myConnection, Context.BIND_AUTO_CREATE);

        timeButton = (Button) findViewById(R.id.time_button);
        timeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                tView = (TextView) findViewById(R.id.newtextView);
                Thread t = new Thread(new Runnable() {
                    @Override
                    public void run() {
                        listFood = myService.connectServer();
                    }
                });
                t.start();

                try
                {
                    t.join();

                    for(int i=0;i<listFood.size();i++)
                    {
                        Log.e("after getting the data","food data item name: "+listFood.get(i).getName());
                    }
                }
                catch (InterruptedException e)
                {
                    Log.e("Service", "Interuppted");
                }
                Toast.makeText(getApplicationContext(), "Service has started check the logcat", Toast.LENGTH_SHORT).show();
            }
        });



    }
}
