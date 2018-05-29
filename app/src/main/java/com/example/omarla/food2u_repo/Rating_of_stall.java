package com.example.omarla.food2u_repo;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.MenuItem;
import android.widget.ListView;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.paytm.pgsdk.Log;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class Rating_of_stall extends AppCompatActivity {

  ListView listview_rating;
    ProgressDialog progress;
    ArrayList list_name,list_location,list_rating;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_rating_of_stall);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        progress = new ProgressDialog(this);
        progress.setTitle("Please Wait");
        progress.setMessage("Loading.....");
        progress.setIndeterminate(true);

        listview_rating=(ListView)findViewById(R.id.rating_listview);


        list_name=new ArrayList();
        list_location=new ArrayList();
        list_rating=new ArrayList();



        callwebservicetoshowratings();



    }


    public void callwebservicetoshowratings() {
        progress.show();
        Log.d("register Url", URLs.SHOW_FEEDBACK);
        StringRequest stringRequest = new StringRequest(Request.Method.POST, URLs.SHOW_FEEDBACK, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {

                try {
                    JSONArray jsonArray=new JSONArray(response);
                    for(int i=0;i<jsonArray.length();i++){
                        JSONObject jsonObject=jsonArray.getJSONObject(i);
                        list_name.add(jsonObject.getString("stall_name"));
                        list_location.add(jsonObject.getString("location"));
                        list_rating.add(jsonObject.getString("feedback"));


                        progress.dismiss();
                    }
                    RatingAdapter ratingAdapter=new RatingAdapter(getApplicationContext(),list_name,list_location,list_rating);

                    listview_rating.setAdapter(ratingAdapter);


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

                return Params;
            }


        };
        MySingleton.getInstance(getApplicationContext()).addToRequestque(stringRequest);

    }

    public boolean onOptionsItemSelected(MenuItem item)
    {
        Intent intent=new Intent(getApplicationContext(),CustomerDashboard.class);
        startActivityForResult(intent,0);
        return true;
    }

}
