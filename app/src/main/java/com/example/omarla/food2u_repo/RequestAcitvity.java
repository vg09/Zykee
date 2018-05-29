package com.example.omarla.food2u_repo;

import android.app.ProgressDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;

import java.util.HashMap;
import java.util.Map;

public class RequestAcitvity extends AppCompatActivity {
    Button request;
    String userId;
    ProgressDialog progress;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_request_acitvity);
        userId=SharedGetterSetter.getUserId(getApplicationContext());



        progress = new ProgressDialog(this);
        progress.setTitle("Please Wait");
        progress.setMessage("Loading.....");
        progress.setIndeterminate(true);


        request=(Button)findViewById(R.id.rqst_btn);

        request.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) { StringRequest stringRequest=new StringRequest(Request.Method.POST, URLs.REMOVE_ITEM, new Response.Listener<String>() {
                @Override
                public void onResponse(String response) {
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
                    Params.put("status","6");
                    Params.put("id",userId);
                    return Params;
                }



            };
                MySingleton.getInstance(getApplicationContext()).addToRequestque(stringRequest);

            }
        });

    }
}
