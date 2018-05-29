package com.example.omarla.food2u_repo;

import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Base64;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
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

/**
 * Created by Om Arla on 3/12/2018.
 */

public class Description extends AppCompatActivity {
    ProgressDialog progress;
    TextView text_name, text_cost, text_desc;
    ArrayList arrraylistdesc, arraylistname, arraylistid,arrayList_img_url;
    Spinner quan_spinner;
    String quantity,name,cost,item_img_url;
    Button add_cart;
    Bitmap bitmap2;
    ImageView description_image;
    String stall_id,stall_name;
    String item_id;
    String totalCost;
    Integer counter=1;
    ImageView addQuan,removeQuan;
    TextView number;
    String QuanVal;
    FloatingActionButton ft;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_description_activity);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        if (getSupportActionBar() != null){
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setDisplayShowHomeEnabled(true);
        }

        progress = new ProgressDialog(this);
        progress.setTitle("Please Wait");
        progress.setMessage("Loading.....");
        progress.setIndeterminate(true);

        text_cost = (TextView) findViewById(R.id.itemCost1);
        text_desc = (TextView) findViewById(R.id.description1);
        text_name = (TextView) findViewById(R.id.itemName1);
        add_cart=(Button) findViewById(R.id.add2cart_btn);
        description_image=(ImageView) findViewById(R.id.descrption_item);
ft=(FloatingActionButton) findViewById(R.id.fltcart);
ft.setOnClickListener(new View.OnClickListener() {
    @Override
    public void onClick(View v) {
        Intent intent=new Intent(getApplicationContext(),Cart.class);
        startActivity(intent);
    }
});




        Bundle bundle = new Bundle();
        bundle = getIntent().getExtras();
        item_id = bundle.getString("id");
        stall_id=bundle.getString("stallid");
        stall_name=bundle.getString("stall_name");
        String item_name=bundle.getString("name");

        int i_id = Integer.parseInt(item_id);
        Log.d("idmsg",item_id);
        Log.d("namemsg",item_name);




        arrraylistdesc = new ArrayList();
        arraylistname = new ArrayList();
        arraylistid = new ArrayList();
        arrayList_img_url=new ArrayList();
        callwebservice(i_id);

        addQuan=(ImageView) findViewById(R.id.addsome);
        removeQuan=(ImageView) findViewById(R.id.removesome);

        addQuan.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(counter==9){
                    Toast.makeText(Description.this, "You cant buy more than 9 items at a time", Toast.LENGTH_SHORT).show();
                }
                else{
                counter++;
                number=(TextView) findViewById(R.id.textNumberhere);
                number.setText(String.valueOf(counter));
                QuanVal=counter.toString();
                }
            }
        });

        removeQuan.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(counter==1){
                    QuanVal=counter.toString();
                    Toast.makeText(Description.this, "You should select atleast one as quntity", Toast.LENGTH_SHORT).show();
                }
                else{
                    counter--;
                    number=(TextView) findViewById(R.id.textNumberhere);
                    number.setText(String.valueOf(counter));
                    QuanVal=counter.toString();
                    Log.d("pu",QuanVal);
                }
            }
        });







        add_cart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

Log.d("qu",QuanVal+"");
                Integer totalcost=totalCostFunction(Integer.parseInt(QuanVal),Integer.parseInt(cost));
                totalCost=String.valueOf(totalcost);
                CallWebServiceForAddCart();

            }
        });

    }


    public void callwebservice(final int i_id) {
        progress.show();
        StringRequest stringRequest = new StringRequest(Request.Method.POST, URLs.ITEM_DESC, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                Log.d("descresp", response);


                progress.dismiss();


                try {


                    JSONArray jsonArray =  new JSONArray(response);


                    for (int i = 0; i < jsonArray.length(); i++) {
                        ;
                        JSONObject jsonObject = jsonArray.getJSONObject(i);


                        text_name.setText("");
                        text_cost.setText("");
                        text_desc.setText("");
                        name=jsonObject.getString("item_name");
                        cost=jsonObject.getString("cost");
                        item_img_url=jsonObject.getString("img_url");
                        String description=jsonObject.getString("description");

                        text_cost.setText(cost);
                        text_name.setText(name);
                        text_desc.setText(description);

                        if(item_img_url.equals(""))
                        {

                            description_image.setImageResource(R.drawable.snacks);
                        }
                        else{


                            byte[] decodedString = Base64.decode(item_img_url, Base64.DEFAULT);
                            Log.d("decoded", decodedString + "");
                            bitmap2 = BitmapFactory.decodeByteArray(decodedString, 0, decodedString.length);

                            description_image.setImageBitmap(bitmap2);
                        }



                    }

                    progress.dismiss();


                } catch (JSONException e) {
                    Log.d("Check 5", "onResponse: ");
                    e.printStackTrace();
                }

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(Description.this, "Error Message" + error.getMessage(), Toast.LENGTH_SHORT).show();
                progress.dismiss();

            }
        }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<String, String>();
                params.put("id", String.valueOf(i_id));
                return params;
            }

        };
        MySingleton.getInstance(Description.this).addToRequestque(stringRequest);
    }









    public int totalCostFunction(int quantity,int cost){
        int totalcost=quantity*cost;
        return totalcost;
    }

    public  void  CallWebServiceForAddCart()
    {
        progress.show();
        StringRequest stringRequest = new StringRequest(Request.Method.POST, URLs.ADD_CART , new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                Log.d("responsecart", response);


                progress.dismiss();


            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(Description.this, "Error Message" + error.getMessage(), Toast.LENGTH_SHORT).show();
                progress.dismiss();

            }
        }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<String, String>();
                params.put("item_id", item_id);
                params.put("user_id",SharedGetterSetter.getUserId(getApplicationContext()));
                params.put("stall_id",stall_id);
                params.put("item_name",name);
                params.put("item_cost",cost);
                params.put("stall_name",stall_name);
                params.put("quantity",QuanVal);
                params.put("total_cost",totalCost);
                params.put("img_url",item_img_url);
                params.put("status","0");

                return params;
            }

        };
        MySingleton.getInstance(Description.this).addToRequestque(stringRequest);


        Intent intent=new Intent(Description.this,Cart.class);
        startActivity(intent);
    }


    public boolean onOptionsItemSelected(MenuItem item)
    {
        if (item.getItemId() == android.R.id.home) {
            finish(); // close this activity and return to preview activity (if there is any)
        }

        return super.onOptionsItemSelected(item);

    }




}




