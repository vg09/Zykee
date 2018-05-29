package com.example.omarla.food2u_repo;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RatingBar;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.paytm.pgsdk.Log;

import java.util.HashMap;
import java.util.Map;

public class Feedback extends AppCompatActivity {
    String r1,r2_view,r3_value,user_id;
    float rating_value;
    EditText ed_view;
    ProgressDialog progress;
    RatingBar stall_feedback;
    Button btn_save;
    int getting_stall_id;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_feedback);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        progress = new ProgressDialog(this);
        progress.setTitle("Please Wait");
        progress.setMessage("Loading.....");
        progress.setIndeterminate(true);

        user_id=SharedGetterSetter.getUserId(getApplicationContext());
        btn_save=(Button)findViewById(R.id.save_feedback);
        ed_view=(EditText)findViewById(R.id.cutomer_view);
        stall_feedback=(RatingBar)findViewById(R.id.ratingBarfeed);

        Intent  intent=getIntent();

         getting_stall_id = intent.getIntExtra("id_stall", -1);
         Log.d("stall_id   ",String.valueOf(getting_stall_id));




        stall_feedback.setOnRatingBarChangeListener(new RatingBar.OnRatingBarChangeListener() {
            @Override
            public void onRatingChanged(RatingBar ratingBar, float rating, boolean fromUser) {
                rating_value=stall_feedback.getRating();
            }
        });
btn_save.setOnClickListener(new View.OnClickListener() {
    @Override
    public void onClick(View v) {

        if(rating_value==0)
        {
            Toast.makeText(getApplicationContext(), "Please rate the stall first ", Toast.LENGTH_SHORT).show();
        }else{

            r2_view=ed_view.getText().toString();
            r3_value=String.valueOf(rating_value);
            callweb_service_to_save_feedback();
        }

    }
});


    }


    public void callweb_service_to_save_feedback(){

        progress.show();
        Log.d("register Url", URLs.SAVE_FEEDBACK);
        StringRequest stringRequest = new StringRequest(Request.Method.POST, URLs.SAVE_FEEDBACK, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {

              progress.dismiss();
              Intent i=new Intent(getApplicationContext(),HistoryActivity.class);
              startActivity(i);

                 Toast.makeText(Feedback.this,  "Thanks for Feedback....", Toast.LENGTH_SHORT).show();


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
                Params.put("s_id", String.valueOf(getting_stall_id));
                Params.put("value",r3_value);
                Params.put("customer_view",r2_view);

                return Params;
            }


        };
        MySingleton.getInstance(getApplicationContext()).addToRequestque(stringRequest);

    }

    public boolean onOptionsItemSelected(MenuItem item)
    {
        Intent intent=new Intent(getApplicationContext(),HistoryActivity.class);
        startActivityForResult(intent,0);
        return true;
    }
}
