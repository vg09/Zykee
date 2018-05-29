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

public class RequestedVendor extends AppCompatActivity {
    ProgressDialog progress;
    ArrayList  list_ven_name,list_stall_name,list_loc,list_mobile,list_adhar_no,list_stall_id;
    ListView listView_request;
    String user_id,stall_id,request;
    Button accept_btn,reject_btn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_requested_vendor);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);//
        progress = new ProgressDialog(this);
        progress.setTitle("Please Wait");
        progress.setMessage("Loading.....");
        progress.setIndeterminate(true);


        list_ven_name=  new ArrayList();
        list_stall_id=new ArrayList();
        list_stall_name=new ArrayList();
        list_loc=       new ArrayList();
        list_mobile=    new ArrayList();
        list_adhar_no=  new ArrayList();



        listView_request=(ListView)findViewById(R.id.request_list);
        callwebservicerequest();




    }


    public void callwebservicerequest()
    {
        progress.show();
        list_ven_name.clear();
        list_stall_id.clear();
        list_stall_name.clear();
        list_loc.clear();
        list_mobile.clear();
        list_adhar_no.clear();
        StringRequest stringRequest= new StringRequest(Request.Method.POST, URLs.REQUEST_FOR_STALL,new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                Log.d("fetch_response",response);

                progress.dismiss();


                try {
                    JSONArray jsonArray = new JSONArray(response);
                    Log.d("Check1", "bharath: "+jsonArray.length());

                    for(int i=0; i<jsonArray.length();i++){
                        JSONObject jsonObject = jsonArray.getJSONObject(i);
                        list_ven_name.add(jsonObject.getString("name"));
                        list_stall_name.add(jsonObject.getString("stall_name"));
                        list_loc.add(jsonObject.getString("location"));
                        list_mobile.add(jsonObject.getString("mobile_no"));

                        list_stall_id.add(jsonObject.getString("id"));


                    }
                    //adapter1.notifyDataSetChanged();
                    RequestAdapter requestAdapter=new RequestAdapter(getApplicationContext(),list_ven_name,list_stall_name,list_loc,list_mobile,list_stall_id);



                    listView_request.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                        @Override
                        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                            Log.d("position",position+"");


                            final Integer pos=position;
                            stall_id=list_stall_id.get(pos).toString();




                            AlertDialog.Builder builder = new AlertDialog.Builder(RequestedVendor.this);
                            builder.setTitle("Perform action on "+list_stall_name.get(pos).toString()+" stall");
                            LayoutInflater inflater = (LayoutInflater) getApplicationContext().getSystemService(RequestedVendor.this.LAYOUT_INFLATER_SERVICE);
                            View layout = inflater.inflate(R.layout.button_layout,null);
                            builder.setView(layout);



                            accept_btn = (Button)layout.findViewById(R.id.edt_stall);
                            accept_btn.setText("Accept");
                            accept_btn.setBackgroundResource(R.drawable.greenstyle);

                            reject_btn = (Button)layout.findViewById(R.id.del_stall);
                            reject_btn.setText("Reject");
                            reject_btn.setBackgroundResource(R.drawable.redstyle);


                            accept_btn.setVisibility(View.VISIBLE);
                            reject_btn.setVisibility(View.VISIBLE);

                            final AlertDialog dialog = builder.create();



                            accept_btn.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {
                                    Log.d("accept button","btn");
                                    callconfirmstallrequest("1");
                                    dialog.dismiss();
                                }
                            });


                            reject_btn.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {
                                    Log.d("accept button","btn");
                                    callconfirmstallrequest("2");
                                    dialog.dismiss();
                                }
                            });

                            dialog.create();
                            dialog.show();

                        }
                    });


                    listView_request.setAdapter(requestAdapter);

                    progress.dismiss();


                } catch (JSONException e) {
                    Log.d("Check 5", "onResponse: ");
                    e.printStackTrace();
                }

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
                params.put("request_stall","0");

                return params;
            }

        };
        MySingleton.getInstance(RequestedVendor.this).addToRequestque(stringRequest);

    }


    //call service toconfirm the request

    public void   callconfirmstallrequest(final String request)
    {
        progress.show();

        StringRequest stringRequest= new StringRequest(Request.Method.POST, URLs.PERMISSION_STALL,new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                Log.d("fetch_response",response);
                Intent i=new Intent(getApplicationContext(),RequestedVendor.class);
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
                params.put("request_stall",request);
                params.put("id",stall_id);

                return params;
            }

        };
        MySingleton.getInstance(RequestedVendor.this).addToRequestque(stringRequest);


    }


    public void onBackPressed ()
    {Intent startMain = new Intent(RequestedVendor.this,AdminDashboard.class);
        startActivity(startMain);}



    public boolean onOptionsItemSelected(MenuItem item)
    {
        Intent intent=new Intent(getApplicationContext(),AdminDashboard.class);
        startActivityForResult(intent,0);
        return true;
    }

}
