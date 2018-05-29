package com.example.omarla.food2u_repo;

import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.google.android.gms.auth.api.Auth;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.auth.api.signin.GoogleSignInResult;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.SignInButton;
import com.google.android.gms.common.api.GoogleApiClient;

import java.util.HashMap;
import java.util.Map;

public class googlelogin extends AppCompatActivity implements View.OnClickListener,GoogleApiClient.OnConnectionFailedListener{


    private static final int RC_SIGN_IN =9006 ;
    private static final String TAG = googlelogin.class.getSimpleName();
    private GoogleApiClient mGoogleApiClient;

    SignInButton btn_sign_in;
    Button btn_sign_out,btn;

    ProgressDialog progress;

    String email,person_name;
    LinearLayout llProfile;
    ImageView imgProfilePic;
    TextView txtName,txtEmail;



    @SuppressLint("RestrictedApi")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
       setContentView(R.layout.activity_log_in);


        progress = new ProgressDialog(this);
        progress.setTitle("Please Wait");
        progress.setMessage("Loading.....");
        progress.setIndeterminate(true);
        //  btn=findViewById(R.id.btn);
        btn_sign_in = findViewById(R.id.btn_sign_in);

//        btn_sign_out=findViewById(R.id.btn_sign_out);
//        llProfile=findViewById(R.id.llProfile);
//        imgProfilePic=findViewById(R.id.imgProfilePic);
//        txtName=findViewById(R.id.txtName);
//        txtEmail=findViewById(R.id.txtEmail);

        btn_sign_in.setOnClickListener(this);
//
//        btn_sign_out.setOnClickListener(this);


        // Configure sign-in to request the user's ID, email address, and basic
        // profile. ID and basic profile are included in DEFAULT_SIGN_IN.
        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestEmail()
                .build();


        mGoogleApiClient = new GoogleApiClient.Builder(this)
                .enableAutoManage(this, this)
                .addApi(Auth.GOOGLE_SIGN_IN_API, gso)
                .build();


        btn_sign_in.setSize(SignInButton.SIZE_STANDARD);    // Set the dimensions of the sign-in button.
        //btn_sign_in.setScopes(gso.getScopeArray());
    }


    private void signIn()
    {
        @SuppressLint("RestrictedApi")
        Intent signInIntent = Auth.GoogleSignInApi.getSignInIntent(mGoogleApiClient);
        startActivityForResult(signInIntent, RC_SIGN_IN);
    }

//
//    private void signOut() {
//        Auth.GoogleSignInApi.signOut(mGoogleApiClient).setResultCallback(
//                new ResultCallback<Status>() {
//                    @Override
//                    public void onResult(Status status) {
//                        updateUI(false);
//                    }
//                });
//    }


    private void handleSignInResult(GoogleSignInResult result)
    {
        Log.d(TAG, "handleSignInResult:" + result.isSuccess());
        if (result.isSuccess())
        {
            // Signed in successfully, show authenticated UI.
            GoogleSignInAccount acct = result.getSignInAccount();

            Log.e(TAG, "display name: " + acct.getDisplayName());

             person_name = acct.getDisplayName();
            String personPhotoUrl = acct.getPhotoUrl().toString();
            email = acct.getEmail();


            Log.e(TAG, "Name: " + person_name + ", email: " + email + ", Image: " + personPhotoUrl);

//            txtName.setText(personName);
//            txtEmail.setText(email);
            Glide.with(getApplicationContext()).load(personPhotoUrl)
                    .thumbnail(0.5f)
                    .crossFade()
                    .diskCacheStrategy(DiskCacheStrategy.ALL)
                    .into(imgProfilePic);


            updateUI(true);
        }
        else
        {
            // Signed out, show unauthenticated UI.
            updateUI(false);
        }
    }
    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {
        // An unresolvable error has occurred and Google APIs (including Sign-In) will not
        // be available.
        Log.d(TAG, "onConnectionFailed:" + connectionResult);
    }


    @Override
    public void onClick(View v)
    {

        switch (v.getId())
        {
            case R.id.btn_sign_in:
                signIn();
                web_service_google_signin();
                break;

            case R.id.btn_sign_out:

                break;
        }
    }



    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data)

    {
        super.onActivityResult(requestCode, resultCode, data);

        // Result returned from launching the Intent from GoogleSignInApi.getSignInIntent(...);
        if (requestCode == RC_SIGN_IN) {
            GoogleSignInResult result = Auth.GoogleSignInApi.getSignInResultFromIntent(data);

            handleSignInResult(result);
        }
    }



    @Override
    protected void onStart() {
        // Check for existing Google Sign In account, if the user is already signed in
        // the GoogleSignInAccount will be non-null.
        @SuppressLint("RestrictedApi")
        GoogleSignInAccount account = GoogleSignIn.getLastSignedInAccount(this);
        updateUI(true);
        super.onStart();
    }
    @Override
    protected void onStop() {
        super.onStop();
        mGoogleApiClient.disconnect();
    }




    private void updateUI(boolean isSignedIn)
    {
//        if (isSignedIn) {
//            btn_sign_in.setVisibility(View.GONE);
//            btn_sign_out.setVisibility(View.VISIBLE);
//            llProfile.setVisibility(View.VISIBLE);
//            //btn.setVisibility(View.GONE);
//        } else {
//            btn_sign_in.setVisibility(View.VISIBLE);
//            btn_sign_out.setVisibility(View.GONE);
//            llProfile.setVisibility(View.GONE);
//          //  btn.setVisibility(View.VISIBLE);
//
//        }
    }

    public void web_service_google_signin()
    {
        Log.d("sercive called", person_name);
        Log.d("sercive called", email);
        progress.show();
        StringRequest stringRequest = new StringRequest(Request.Method.POST, URLs.GOOGLE_LOGIN , new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                Log.d("item came or not", response);


                progress.dismiss();


            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(googlelogin.this, "Error Message" + error.getMessage(), Toast.LENGTH_SHORT).show();
                progress.dismiss();

            }
        }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<String, String>();
                params.put("username",person_name);
               params.put("email",email);

                return params;
            }

        };
        MySingleton.getInstance(googlelogin.this).addToRequestque(stringRequest);

    }
}
