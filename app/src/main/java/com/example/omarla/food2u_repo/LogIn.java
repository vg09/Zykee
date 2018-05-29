package com.example.omarla.food2u_repo;

import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.text.InputFilter;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.google.android.gms.auth.api.Auth;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.auth.api.signin.GoogleSignInResult;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.SignInButton;
import com.google.android.gms.common.api.GoogleApiClient;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class LogIn extends AppCompatActivity implements GoogleApiClient.OnConnectionFailedListener {

    Button btnlogin;
    SharedPreferences myprefs;
   // public static String g_status="0";

    private static final int RC_SIGN_IN =9006 ;
    private static final String TAG = googlelogin.class.getSimpleName();
    private GoogleApiClient mGoogleApiClient;

    SignInButton btn_sign_in;
    Button btn_sign_out,btn;

  //  ProgressDialog progress2;

    String email,person_name;
    TextView btnsignup;
    EditText edt_mobile, edt_password;
    Button googlesign_in;
    String mobile, password;
    ProgressDialog progress;
    AlertDialog.Builder builder;
    Boolean net_check = false;
SharedGetterSetter sharedGetterSetter;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_log_in);
        btnlogin = (Button)findViewById(R.id.login_id);
       btn_sign_in=findViewById(R.id.btn_sign_in);//google sign in button
        btnsignup = (TextView) findViewById(R.id.signup_id);
        edt_mobile = (EditText) findViewById(R.id.mobile_id);
        edt_password = (EditText) findViewById(R.id.password_id);
      //  edt_password.setFilters(new  InputFilter[]{ new InputFilter.LengthFilter(12)});
        progress = new ProgressDialog(this);
        progress.setTitle("Please Wait");
        progress.setMessage("Loading.....");
        progress.setIndeterminate(true);

        myprefs=this.getSharedPreferences("session_id",MODE_WORLD_WRITEABLE);
        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestEmail()
                .build();


        mGoogleApiClient = new GoogleApiClient.Builder(this)
                .enableAutoManage(this, this)
                .addApi(Auth.GOOGLE_SIGN_IN_API, gso)
                .build();



        edt_mobile.setFilters(new InputFilter[] {new InputFilter.LengthFilter(10)});//limiting the length of the mobile no upto 10 digit

        builder = new AlertDialog.Builder(LogIn.this);
        net_check=netCheck();

        if(net_check== true){                                         //checking currently available data network info
            displayNetwork();
        }
        btnsignup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(LogIn.this, SignUp.class));
            }
        });

btn_sign_in.setOnClickListener(new View.OnClickListener() {
    @Override
    public void onClick(View v) {
        //Toast.makeText(getApplicationContext(),"Y",Toast.LENGTH_LONG).show();
        signIn();

    }
});


        btnlogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mobile = edt_mobile.getText().toString();
                password = edt_password.getText().toString();
//                Toast.makeText(LogIn.this, "mobile"+mobile, Toast.LENGTH_SHORT).show();
//                Toast.makeText(LogIn.this, "password"+password, Toast.LENGTH_SHORT).show();


                if (mobile.length() != 10) {
                    builder.setTitle("Mobile_number");
                    builder.setMessage("Invalid mobile number");
                    displayAlert("Input_error0");
                } else {
                    if (password.length()<=0) {
                        builder.setTitle("Password");
                        builder.setMessage("Please Enter Password!");
                        displayAlert("Input_error1");
                    } else {
                        progress.show();
                        //Log.d("url",URLs.LOGIN);
                        StringRequest stringRequest= new StringRequest(Request.Method.POST, URLs.LOGIN,new Response.Listener<String>() {
                            @Override
                                    public void onResponse(String response) {
                                Log.d("login response",response.toString());
                                        progress.dismiss();
                                        try {
                                            JSONObject jsonObj = new JSONObject(response);
                                            String Error=jsonObj.getString("error");
                                            //Log.d("Error",Error);



                                            if(Error=="1") {
                                                builder.setMessage("Mobile or Password is Wrong...");
                                                builder.setTitle("Alert!");
                                                displayAlert(Error);
                                            }
                                          else if(Error=="0") {



                                                JSONObject UserData=jsonObj.getJSONObject("data");
                                               // Log.d("UserData",UserData.toString());
                                                String UID=UserData.getString("user_id");
                                                String NAME=UserData.getString("name");
                                                String UPH_NO=UserData.getString("mobile_no");
                                                String UMAIL=UserData.getString("email");
                                                String IMG_URL=UserData.getString("img_url");
                                                sharedGetterSetter.setUserId(LogIn.this,UID);
                                                sharedGetterSetter.setUserName(LogIn.this,NAME);
                                                sharedGetterSetter.setUserMobile(LogIn.this,UPH_NO);
                                                sharedGetterSetter.setUserMail(LogIn.this,UMAIL);
                                                sharedGetterSetter.setImgUrl(LogIn.this,IMG_URL);
                                               myprefs.edit().putString("session_id", UID).commit();
                                              //  Log.d("UID",UID.toString());
                                                String UTYPE=UserData.getString("user_type");
                                                sharedGetterSetter.setUserType(LogIn.this,UTYPE);
                                                String password=UserData.getString("password");
                                                sharedGetterSetter.setPassword(LogIn.this,password);

                                                Log.d("UTYPE",UTYPE);
                                               if (UTYPE.equalsIgnoreCase("Customer")||UTYPE.equalsIgnoreCase("vendor")) {
                                                   startActivity(new Intent(LogIn.this, CustomerDashboard.class));
                                               }
                                               if(UTYPE.equalsIgnoreCase("admin")){
                                                   startActivity(new Intent(LogIn.this,AdminDashboard.class));
                                               }
                                                Toast.makeText(LogIn.this, "Login Successful", Toast.LENGTH_SHORT).show();


                                            }

                                      } catch (JSONException e) {
                                            e.printStackTrace();
                                        }

                                    }
                                }, new Response.ErrorListener() {
                            @Override
                            public void onErrorResponse(VolleyError error) {
                                Toast.makeText(LogIn.this, "Error Message"+error.getMessage(), Toast.LENGTH_SHORT).show();
                                progress.dismiss();

                            }
                        }) {
                            @Override
                            protected Map<String, String> getParams() throws AuthFailureError {
                                Map<String, String> params = new HashMap<String, String>();
                                params.put("mobile_no", mobile);
                                params.put("password", password);
                                return params;
                            }

                        };
                        MySingleton.getInstance(LogIn.this).addToRequestque(stringRequest);

                    }
                }
            }
        });
    }


    public void displayAlert(final String code) {
        builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

                if (code.equals("input_error0"))
                {
                    edt_mobile.setText("");
                    edt_password.setText("");
                }
                else if (code.equals("input_error1"))
                {
                    edt_password.setText("");
                    edt_mobile.setText("");
                }
                else if (code.equals("error"))
                {
                    edt_password.setText("");

                }
                else if (code.equals("0"))
                {
                    // session.createLoginSession(mobile);
                    //startActivity(new Intent(LogIn.this.class));
                }
                else if (code.equals("1")) {
                    edt_mobile.setText("");
                    edt_password.setText("");
                }

            }
        });

        AlertDialog alertDialog = builder.create();
        alertDialog.show();
    }


    public void displayNetwork() {
        new AlertDialog.Builder(this).setMessage("Please check your internet connection")
                .setTitle("Network Error!!").setCancelable(true).setNeutralButton(android.R.string.ok, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                finish();
            }
        }).show();
    }


    public boolean netCheck() {

        ConnectivityManager connectionManager = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        if (connectionManager.getNetworkInfo(0).getState() == NetworkInfo.State.DISCONNECTED
                && connectionManager.getNetworkInfo(1).getState() == NetworkInfo.State.DISCONNECTED) {
            net_check = true;
        }
        return net_check;
    }

    public void forget_method(View view) {
        startActivity(new Intent(LogIn.this, ForgetPassword.class));

    }
    public void onBackPressed () {

            Intent startMain = new Intent(Intent.ACTION_MAIN);
            startMain.addCategory(Intent.CATEGORY_HOME);
            startMain.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            startActivity(startMain);

        }


    private void signIn()
    {
        @SuppressLint("RestrictedApi")
        Intent signInIntent = Auth.GoogleSignInApi.getSignInIntent(mGoogleApiClient);
        startActivityForResult(signInIntent, RC_SIGN_IN);
    }

    //google sign_up

    private void handleSignInResult(GoogleSignInResult result)
    {
        Log.d(TAG, "handleSignInResult:" + result.isSuccess());
        if (result.isSuccess())
        {
            // Signed in successfully, show authenticated UI.
            GoogleSignInAccount acct = result.getSignInAccount();

            Log.e(TAG, "display name: " + acct.getDisplayName());

            person_name = acct.getDisplayName();
//            String personPhotoUrl = acct.getPhotoUrl().toString();
            email = acct.getEmail();



            Log.e(TAG, "Name: " + person_name + ", email: " + email + ", Image: " );

           web_service_google_signin();



        }
        else
        {
            // Signed out, show unauthenticated UI.

        }
    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {

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

//    public void signOut() {
//        Auth.GoogleSignInApi.signOut(mGoogleApiClient).setResultCallback(
//                new ResultCallback<Status>() {
//                    @Override
//                    public void onResult(Status status) {
//
//                    }
//                });
//    }
//








    public void web_service_google_signin()
    {

         progress.show();
        StringRequest stringRequest = new StringRequest(Request.Method.POST, URLs.GOOGLE_LOGIN , new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                Log.d("google sign in ", response);
                try {
                    JSONObject jsonObject=new JSONObject(response);
                    String error=jsonObject.getString("error");
                    Log.d("error value",error);
                    if(error=="0"){
                        JSONObject UserData=jsonObject.getJSONObject("data");

                        String UID=UserData.getString("user_id");
                        String NAME=UserData.getString("name");
                        String UMAIL=UserData.getString("email");
                        progress.dismiss();
                        startActivity(new Intent(LogIn.this, CustomerDashboard.class));

                        Toast.makeText(LogIn.this, "Login Successful", Toast.LENGTH_SHORT).show();


                    }
                    if(error=="1")
                    {
                        Log.d("error 1 aya",error);
                        JSONObject UserData=jsonObject.getJSONObject("data");

                        String UID=UserData.getString("user_id");
                        String NAME=UserData.getString("name");
                        String UPH_NO=UserData.getString("mobile_no");
                        String UMAIL=UserData.getString("email");
                        String IMG_URL=UserData.getString("img_url");
                        sharedGetterSetter.setUserId(LogIn.this,UID);
                        sharedGetterSetter.setUserName(LogIn.this,NAME);
                        sharedGetterSetter.setUserMobile(LogIn.this,UPH_NO);
                        sharedGetterSetter.setUserMail(LogIn.this,UMAIL);
                        sharedGetterSetter.setImgUrl(LogIn.this,IMG_URL);

                        String UTYPE=UserData.getString("user_type");
                        sharedGetterSetter.setUserType(LogIn.this,UTYPE);

                       progress.dismiss(); //if (UTYPE.equalsIgnoreCase("Customer")) {
                        startActivity(new Intent(LogIn.this, CustomerDashboard.class));

                        Toast.makeText(LogIn.this, "Login Successful", Toast.LENGTH_SHORT).show();


                    }

                } catch (JSONException e) {
                    e.printStackTrace();
                    progress.dismiss();
                }


                //    progress.dismiss();


            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(LogIn.this, "Error Message" + error.getMessage(), Toast.LENGTH_SHORT).show();
                progress.dismiss();

            }
        }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<String, String>();
                params.put("name",person_name);
                params.put("email",email);

                return params;
            }

        };
        MySingleton.getInstance(LogIn.this).addToRequestque(stringRequest);

    }





}


