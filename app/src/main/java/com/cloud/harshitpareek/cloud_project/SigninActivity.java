package com.cloud.harshitpareek.cloud_project;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

/**
 * Created by harshitpareek on 5/8/17.
 */

public class SigninActivity extends AppCompatActivity
{
    Button button;
    EditText username;
    EditText password;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // set the content view as the signin_app_layout
        setContentView(R.layout.signin_app_layout);

        username = (EditText) findViewById(R.id.user_name);
        password = (EditText) findViewById(R.id.password);

        button = (Button) findViewById(R.id.signbutton_id);
        button.setOnClickListener(new View.OnClickListener(){
           @Override
            public void onClick(View view)
           {
               // the code of authentication would go here
//             Toast.makeText(getApplicationContext(), "value of original is:"+username.getText(), Toast.LENGTH_LONG).show();
           }
        });
    }

    @Override
    public void onStart()
    {
        super.onStart();
    }

    @Override
    public void onResume()
    {
        super.onResume();
    }

    @Override
    public void onStop()
    {
        super.onStop();
    }
}
