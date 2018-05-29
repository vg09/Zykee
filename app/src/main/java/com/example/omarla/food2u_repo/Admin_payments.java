package com.example.omarla.food2u_repo;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Toast;

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

public class Admin_payments extends AppCompatActivity {
    ProgressDialog progress;
    String user_id,date;
    ArrayList pay_date,vendor_mobile,day_amt,vendor_name,user_id_list;
    Button pay_btn;

    ListView ven_payment_list;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_payments);



        progress = new ProgressDialog(getApplicationContext());
        progress.setTitle("Please Wait");
        progress.setMessage("Loading.....");
        progress.setIndeterminate(true);

        ven_payment_list=(ListView) findViewById(R.id.list_payment_admin);


        pay_date=new ArrayList();
        vendor_mobile=new ArrayList();
        day_amt=new ArrayList();
        vendor_name=new ArrayList();
        user_id_list=new ArrayList();

        callpaymentwebservice();
    }

    public void callpaymentwebservice()
    {
        Log.d("register Url",URLs.PAYMENT_DETAILS_BY_DATE);
        StringRequest stringRequest=new StringRequest(Request.Method.POST, URLs.PAYMENT_DETAILS_BY_DATE, new Response.Listener<String>() {
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


                    ven_payment_list.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                        @Override
                        public void onItemClick(AdapterView<?> parent, View view, final int position, long id) {
                            Log.d("position",position+"");


                            final Integer pos=position;
                            user_id=user_id_list.get(pos).toString();
                            date=day_amt.get(pos).toString();





                            AlertDialog.Builder builder = new AlertDialog.Builder(Admin_payments.this);
                            builder.setTitle("Send "+day_amt.get(pos).toString()+" to"+vendor_name.get(pos).toString());
                            LayoutInflater inflater = (LayoutInflater) getApplicationContext().getSystemService(Admin_payments.this.LAYOUT_INFLATER_SERVICE);
                            View layout = inflater.inflate(R.layout.button_layout,null);
                            builder.setView(layout);



                            pay_btn = (Button)layout.findViewById(R.id.edt_stall);
                            pay_btn.setText("Pay");
                            pay_btn.setBackgroundResource(R.drawable.greenstyle);



                            pay_btn.setVisibility(View.VISIBLE);


                            final AlertDialog dialog = builder.create();



                            pay_btn.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {
                                    Log.d("accept button","btn");
                                    Intent intent=new Intent(getApplicationContext(),Paytm_main.class);
                                    intent.putExtra("cost",day_amt.get(position).toString());
                                    startActivity(intent);
                                    dialog.dismiss();
                                }
                            });




                            dialog.create();
                            dialog.show();

                        }
                    });


                    ven_payment_list.setAdapter(paymentadapter);
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



    public void   callpaywebservice(final String request, final String user_id, final String date)
    {
        progress.show();

        StringRequest stringRequest= new StringRequest(Request.Method.POST, URLs.IS_PAID,new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                Log.d("fetch_response",response);
                Intent i=new Intent(getApplicationContext(),Vendor_payments.class);
                startActivity(i);

                progress.dismiss();



            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(getApplicationContext(), "Error Message "+error.getMessage(), Toast.LENGTH_SHORT).show();
                progress.dismiss();

            }
        }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<String, String>();
                params.put("is_paid",request);
                params.put("id",user_id);
                params.put("date",date);

                return params;
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
