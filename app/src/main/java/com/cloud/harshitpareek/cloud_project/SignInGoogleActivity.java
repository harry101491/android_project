package com.cloud.harshitpareek.cloud_project;

import android.graphics.Bitmap;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.app.ProgressDialog;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.util.Log;
import android.content.Intent;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.auth.api.Auth;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.auth.api.signin.GoogleSignInResult;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.bumptech.glide.load.engine.DiskCacheStrategy;


/**
 * Created by harshitpareek on 5/8/17.
 */

public class SignInGoogleActivity extends AppCompatActivity implements GoogleApiClient.OnConnectionFailedListener
{
    private static final String TAG = "SignInGoogleActivity";
    private GoogleApiClient mGoogleApiClient;
    private static final int RC_SIGN_IN = 9001;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.signin_google);

        // configure the google signIn Button
        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestEmail()
                .build();

        // google signIn Client
        mGoogleApiClient = new GoogleApiClient.Builder(this)
                .enableAutoManage(this /* FragmentActivity */, this /* OnConnectionFailedListener */)
                .addApi(Auth.GOOGLE_SIGN_IN_API, gso)
                .build();

        // getting the Google Sign-In Button
        findViewById(R.id.sign_in_google_button).setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view)
            {
                Intent signInIntent = Auth.GoogleSignInApi.getSignInIntent(mGoogleApiClient);
                startActivityForResult(signInIntent, RC_SIGN_IN);
            }
        });
    }

    @Override
    public void onConnectionFailed(ConnectionResult connectionResult) {
        // An unresolvable error has occurred and Google APIs (including Sign-In) will not
        // be available.
        Log.d(TAG, "onConnectionFailed:" + connectionResult);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        // Result returned from launching the Intent from GoogleSignInApi.getSignInIntent(...);
        if (requestCode == RC_SIGN_IN) {
            GoogleSignInResult result = Auth.GoogleSignInApi.getSignInResultFromIntent(data);
            handleSignIn(result);
        }
    }
    private void handleSignIn(GoogleSignInResult result)
    {
        Log.d(TAG, "Inside the handle sign in function");
        if(result.isSuccess())
        {
            GoogleSignInAccount acct = result.getSignInAccount();

            // setting the account photo for the user including name and email address
//            Glide.with(getApplicationContext())
//                    .load(acct.getPhotoUrl().toString())
//                    .thumbnail(0.5f)
//                    .crossFade()
//                    .diskCacheStrategy(DiskCacheStrategy.ALL)
//                    .into(user_image);

            // setting the intent and sending the user to search page for the user
            Intent searchIntent = new Intent(this, SearchActivity.class);
            // putting three value in the intent object
            searchIntent.putExtra("user_name", acct.getDisplayName());
            searchIntent.putExtra("user_email", acct.getEmail());
            searchIntent.putExtra("img_url", acct.getPhotoUrl().toString());
            startActivity(searchIntent);

//            Toast.makeText(getApplicationContext(), acct.getDisplayName(),Toast.LENGTH_LONG).show();
        }
        else
        {
            Toast.makeText(getApplicationContext(), "No account can be accessed ",Toast.LENGTH_LONG).show();
        }
    }
}
