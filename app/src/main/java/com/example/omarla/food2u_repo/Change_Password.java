package com.example.omarla.food2u_repo;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.paytm.pgsdk.Log;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class Change_Password extends AppCompatActivity {
    EditText ed_current,ed_new,ed_conf;
    AlertDialog.Builder builder;
    String current, new_password,con_paswd,old_password,user_id;
    ProgressDialog progress;
    Button save;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_change__password);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        progress = new ProgressDialog(this);
        progress.setTitle("Please Wait");
        progress.setMessage("Loading.....");
        progress.setIndeterminate(true);

        save = (Button) findViewById(R.id.save_password);
        ed_current = (EditText) findViewById(R.id.old_password);
        ed_new = (EditText) findViewById(R.id.new_password);
        ed_conf = (EditText) findViewById(R.id.confirm_password);

        user_id = SharedGetterSetter.getUserId(getApplicationContext());
        old_password=SharedGetterSetter.getPASSWORD(getApplicationContext());

        save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                current = ed_current.getText().toString();
                new_password = ed_new.getText().toString();
                con_paswd = ed_conf.getText().toString();
                if (current.length() < 1 || new_password.length() < 1 || con_paswd.length() < 1) {


                    Toast.makeText(Change_Password.this, "password length is not ok", Toast.LENGTH_SHORT).show();

                } else if (!new_password.equals(con_paswd)) {
                    Toast.makeText(Change_Password.this, "new password and confirm password not same ", Toast.LENGTH_SHORT).show();


                } else {

                    Log.d("oldpassword2",old_password);
                    if (current.equals(old_password)) {
                        callwebservicetochangeassword();
                    } else{
                        Toast.makeText(Change_Password.this, "old password not same ", Toast.LENGTH_SHORT).show();

                    }
                }


            }


//            public void callwebservicetogetoldpassword() {
//                progress.show();
//                StringRequest stringRequest = new StringRequest(Request.Method.POST, URLs.GETPASSWORDUSER, new Response.Listener<String>() {
//                    @Override
//                    public void onResponse(String response) {
//
//                        try {
//                            JSONObject jsonObject = new JSONObject(response);
//                            old_password = jsonObject.getString("password").toString();
//                            Log.d("old password",old_password);
//
//                            progress.dismiss();
//                        } catch (JSONException e) {
//                            e.printStackTrace();
//                            progress.dismiss();
//                        }
//
//
//                       // Toast.makeText(Change_Password.this, "Wait for mail..", Toast.LENGTH_SHORT).show();
//
//
//                        progress.dismiss();
//
//
//                    }
//
//
//                }, new Response.ErrorListener() {
//                    @Override
//                    public void onErrorResponse(VolleyError error) {
//                        Log.d("volley error", error.toString());
//                        progress.dismiss();
//
//                    }
//                }) {
//                    protected Map<String, String> getParams() throws AuthFailureError {
//                        Map<String, String> Params = new HashMap<String, String>();
//                        Params.put("user_id", user_id);
//
//                        return Params;
//                    }
//
//
//                };
//                MySingleton.getInstance(getApplicationContext()).addToRequestque(stringRequest);
//
//            }
//

            //update password
            public void callwebservicetochangeassword() {
                progress.show();
                Log.d("register Url", URLs.GETPASSWORD);
                StringRequest stringRequest = new StringRequest(Request.Method.POST, URLs.CHANGEPASSWORD, new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {

                        try {
                            JSONObject jsonObject = new JSONObject(response);
                            String pwd=jsonObject.getString("password");

                            Toast.makeText(Change_Password.this, "Your Password Is Changed..Please Login Again.", Toast.LENGTH_SHORT).show();
                            Intent i = new Intent(getApplicationContext(), LogIn.class);
                            startActivity(i);


                            progress.dismiss();
                        } catch (JSONException e) {
                            e.printStackTrace();
                            progress.dismiss();
                        }


                        // Toast.makeText(Change_Password.this,  "Wait for mail..", Toast.LENGTH_SHORT).show();


                        progress.dismiss();


                    }


                }, new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Log.d("volley error", error.toString());
                        progress.dismiss();

                    }
                }) {
                    protected Map<String, String> getParams() throws AuthFailureError {
                        Map<String, String> Params = new HashMap<String, String>();
                        Params.put("user_id", user_id);
                        Params.put("password",new_password);

                        return Params;
                    }


                };
                MySingleton.getInstance(getApplicationContext()).addToRequestque(stringRequest);

            }




        });

    }

    public void onBackPressed ()
    {Intent startMain = new Intent(Change_Password.this,CustomerDashboard.class);
        startActivity(startMain);}

    public boolean onOptionsItemSelected(MenuItem item)
    {
        Intent intent=new Intent(getApplicationContext(),EditProfile.class);
        startActivityForResult(intent,0);
        return true;
    }
}
