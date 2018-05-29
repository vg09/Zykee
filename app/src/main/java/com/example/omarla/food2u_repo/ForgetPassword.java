package com.example.omarla.food2u_repo;


import android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
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

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class ForgetPassword extends AppCompatActivity {
    Button btn_back, btn_send;
    EditText ed_mail;
    String reciever_email,body_password;
    ProgressDialog progress;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        setContentView(R.layout.activity_forget_password);
        btn_send  = (Button) findViewById(R.id.btn_verify);
        ed_mail=(EditText)findViewById(R.id.email);
        btn_back=(Button)findViewById(R.id.btn_back);




        progress = new ProgressDialog(this);
        progress.setTitle("Please Wait");
        progress.setMessage("Loading.....");
        progress.setIndeterminate(true);



        btn_send.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                reciever_email=ed_mail.getText().toString();
                webservicetogetpassword();

                new SendMail().execute("");
            }
        });

        btn_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });


    }

    private class SendMail extends AsyncTask<String, Integer, Void> {

        private ProgressDialog progressDialog;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            progressDialog = ProgressDialog.show(ForgetPassword.this, "Please wait", "Sending mail", true, false);
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);
            progressDialog.dismiss();
        }

        protected Void doInBackground(String... params) {
            Log.d("reciever",reciever_email);
            Mail m = new Mail("food2uinfotech@gmail.com", "project@123");

            String[] toArr = {reciever_email, "food2uinfotech@gmail.com"};
            m.setTo(toArr);
            m.setFrom("food2uinfotech@gmail.com");
            m.setSubject("Zykee... PLEASE IGNORE IF YOU ARE NOT AUTHORISED USER...");
            m.setBody("Your Password For Zykee is "+body_password);

            try {
                if(m.send()) {
                    Toast.makeText(ForgetPassword.this, "Email was sent successfully.", Toast.LENGTH_LONG).show();
                } else {
                    Toast.makeText(ForgetPassword.this, "Email was not sent.", Toast.LENGTH_LONG).show();
                }
            } catch(Exception e) {
                Log.e("MailApp", "Could not send email", e);
            }
            return null;
        }
    }



    public void webservicetogetpassword()
    {
        progress.show();
        Log.d("register Url",URLs.GETPASSWORD);
        StringRequest stringRequest=new StringRequest(Request.Method.POST, URLs.GETPASSWORD, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {

                try {
                    JSONObject jsonObject=new JSONObject(response);
                    body_password=jsonObject.getString("password");
                    if(body_password.length()<=0)
                    {
                        Toast.makeText(ForgetPassword.this, "EMAIL DOES NOT EXIST..", Toast.LENGTH_SHORT).show();
                    }
                    progress.dismiss();
                } catch (JSONException e) {
                    e.printStackTrace();
                    progress.dismiss();
                }


                Toast.makeText(ForgetPassword.this,  "Wait for mail..", Toast.LENGTH_SHORT).show();


                progress.dismiss();



            }


        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.d("volley error",error.toString());
                progress.dismiss();

            }
        })
        {
            protected Map<String,String> getParams() throws AuthFailureError
            {
                Map<String,String> Params=new HashMap<String,String>();
                Params.put("email",reciever_email);

                return Params;
            }



        };
        MySingleton.getInstance(getApplicationContext()).addToRequestque(stringRequest);

    }


    public boolean onOptionsItemSelected(MenuItem item)
    {
        Intent intent=new Intent(getApplicationContext(),LogIn.class);
        startActivityForResult(intent,0);
        return true;
    }


}