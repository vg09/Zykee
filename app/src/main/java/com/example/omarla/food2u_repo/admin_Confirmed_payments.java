package com.example.omarla.food2u_repo;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.MenuItem;
import android.widget.Button;
import android.widget.ListView;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class admin_Confirmed_payments extends AppCompatActivity {
    ProgressDialog progress;
    String user_id,date;
    ArrayList pay_date,vendor_mobile,day_amt,vendor_name,user_id_list;
    Button pay_btn;
    ListView conf_list;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin__confirmed_payments);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        progress = new ProgressDialog(getApplicationContext());
        progress.setTitle("Please Wait");
        progress.setMessage("Loading.....");
        progress.setIndeterminate(true);

        conf_list=(ListView) findViewById(R.id.confirm_payment);


        pay_date=new ArrayList();
        vendor_mobile=new ArrayList();
        day_amt=new ArrayList();
        vendor_name=new ArrayList();
        user_id_list=new ArrayList();

        callpaymentwebservice();
    }

    public void callpaymentwebservice()
    {
        Log.d("register Url",URLs.PAYMENT_DETAILS_BY_DATE_paid);
        StringRequest stringRequest=new StringRequest(Request.Method.POST, URLs.PAYMENT_DETAILS_BY_DATE_paid, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {

                Log.d("response payment",response);



                try {
                    JSONArray jsonArray=new JSONArray(response);

                    for(int i=0;i<jsonArray.length();i++){
                        JSONObject jsonObject = jsonArray.getJSONObject(i);
                        vendor_name.add(jsonObject.getString("name"));
                        vendor_mobile.add(jsonObject.getString("mobile"));
                        pay_date.add(jsonObject.getString("date"));
                        day_amt.add(jsonObject.getString("total_cost"));
                        user_id_list.add(jsonObject.getString("user_id"));

                    }

                    Paymentadapter paymentadapter=new Paymentadapter(getApplicationContext(),pay_date,vendor_mobile,day_amt,vendor_name,user_id_list);


                                       conf_list.setAdapter(paymentadapter);
                    progress.dismiss();



                } catch (JSONException e) {
                    e.printStackTrace();
                    progress.dismiss();
                }


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


                return Params;
            }



        };
        MySingleton.getInstance(getApplicationContext()).addToRequestque(stringRequest);

    }




    public void onBackPressed ()
    {Intent startMain = new Intent(getApplicationContext(),AdminDashboard.class);
        startActivity(startMain);}



    public boolean onOptionsItemSelected(MenuItem item)
    {
        Intent intent=new Intent(getApplicationContext(),AdminDashboard.class);
        startActivityForResult(intent,0);
        return true;
    }



}

