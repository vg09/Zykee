package com.example.omarla.food2u_repo;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.MenuItem;
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

public class ConfirmStallsInfo extends AppCompatActivity {

    ProgressDialog progress;
    ArrayList  list_ven_name,list_stall_name,list_loc,list_mobile,list_adhar_no,list_stall_id;
    ListView listView_confirm;
    String user_id,stall_id,request;
    Button accept_btn,reject_btn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_confirm_stalls_info);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);//
        progress = new ProgressDialog(this);
        progress.setTitle("Please Wait");
        progress.setMessage("Loading.....");
        progress.setIndeterminate(true);

this.setTitle("Confirmed Stalls");
        list_ven_name=  new ArrayList();
        list_stall_id=  new ArrayList();
        list_stall_name=new ArrayList();
        list_loc=       new ArrayList();
        list_mobile=    new ArrayList();
        list_adhar_no=  new ArrayList();



        listView_confirm=(ListView)findViewById(R.id.conf_list);
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
                    Log.d("Check1", "onResponse: "+jsonArray.length());

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

                    listView_confirm.setAdapter(requestAdapter);

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
                params.put("request_stall","1");

                return params;
            }

        };
        MySingleton.getInstance(ConfirmStallsInfo.this).addToRequestque(stringRequest);

    }

    public boolean onOptionsItemSelected(MenuItem item)
    {
        Intent intent=new Intent(getApplicationContext(),AdminDashboard.class);
        startActivityForResult(intent,0);
        return true;
    }


}
