package com.example.omarla.food2u_repo;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.RatingBar;
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
import java.util.Timer;
import java.util.TimerTask;

import me.relex.circleindicator.CircleIndicator;

public class Stalls_common extends AppCompatActivity {



    ArrayList arraylist,arraylistid;
    ArrayList arraylist_item,arraylist_cost,itemlistid,arraylist_item_img_url;
    ListView listView;
    ProgressDialog progress;
    private Spinner spinner;
    String stallId,stallName;
    Integer  pos;
    String item_id;
    ArrayList arrayList_feed;
    String save_id;//saving id of stall atcorressponding position
    ArrayAdapter adapter1;
    RatingBar rating_odf_stall;
    float rating=5;

//    int[] imageArray = {R.drawable.juices, R.drawable.snacks, R.drawable.southindian, R.drawable.chinese};
//    ImageView slide_img1;

    private static ViewPager mPager;
    private static int currentPage = 0;
    private static final Integer[] XMEN = { R.drawable.chopbox,R.drawable.pastry,R.drawable.parantha, R.drawable.coffe,R.drawable.thali,R.drawable.pizza};
    private ArrayList<Integer> XMENArray = new ArrayList<Integer>();

    String loc;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_stalls_common);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        arraylist = new ArrayList();
        arraylistid=new ArrayList();//saving stall id in the array list

        arraylist_item = new ArrayList();
        arraylist_cost=new ArrayList();
        itemlistid=new ArrayList();
        arraylist_item_img_url=new ArrayList();
        arrayList_feed=new ArrayList();

        listView = (ListView) findViewById(R.id.list1);
        TextView textView = (TextView) findViewById(R.id.text);
        rating_odf_stall=(RatingBar)findViewById(R.id.ratingstall1);

        rating_odf_stall.setIsIndicator(false);

        progress = new ProgressDialog(this);
        progress.setTitle("Please Wait");
        progress.setMessage("Loading.....");
        progress.setIndeterminate(true);

        Bundle bundle =new Bundle();
        bundle=getIntent().getExtras();
        String name=bundle.getString("text");

        this.setTitle(name);



        rating_odf_stall.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                return true;
            }
        });


//getting location
        loc=bundle.getString("location");
        spinner = (Spinner) findViewById(R.id.spinner1);

            for(int i=0;i<XMEN.length;i++)
                XMENArray.add(XMEN[i]);

            mPager = (ViewPager) findViewById(R.id.pager);
            mPager.setAdapter(new MySliderAdapter(Stalls_common.this,XMENArray));
            CircleIndicator indicator = (CircleIndicator) findViewById(R.id.indicator);
            indicator.setViewPager(mPager);

            // Auto start of viewpager
            final Handler handler = new Handler();
            final Runnable Update = new Runnable() {
                public void run() {
                    if (currentPage == XMEN.length) {
                        currentPage = 0;
                    }
                    mPager.setCurrentItem(currentPage++, true);
                }
            };
            Timer swipeTimer = new Timer();
            swipeTimer.schedule(new TimerTask() {
                @Override
                public void run() {
                    handler.post(Update);
                }
            }, 40000, 40000);


        /* Call here stallName service*/

        adapter1= new ArrayAdapter(this,R.layout.support_simple_spinner_dropdown_item,arraylist);
        adapter1.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(adapter1);
        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                pos=position;
               stallId=arraylistid.get(pos).toString();
               stallName=arraylist.get(pos).toString();



                calllistwebservice(stallId);

            }
            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                Log.d("error a rehha "," okay");

            }
        });


        callwebservice();


    }





/*this code referes to the list of stall names to be shown inside the spinner */


    public void callwebservice(){
        progress.show();
        StringRequest stringRequest= new StringRequest(Request.Method.POST, URLs.STALL_NAME+"/"+loc,new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                Log.d("srp00",response);

                progress.dismiss();


                try {
                     JSONArray jsonArray = new JSONArray(response);
                    Log.d("Check1", "onResponse: "+jsonArray.length());
                    Log.d("Check1", "onResponse: "+jsonArray.toString());
                     for(int i=0; i<jsonArray.length();i++){
                         JSONObject jsonObject = jsonArray.getJSONObject(i);
                         arraylist.add(jsonObject.getString("stall_name"));
                         arraylistid.add(Integer.parseInt(jsonObject.getString("id")));
                     //    arrayList_feed.add(jsonObject.getString("feedback"));




                     }
                     adapter1.notifyDataSetChanged();

                    progress.dismiss();


                } catch (JSONException e) {
                    Log.d("Check 5", "onResponse: ");
                    e.printStackTrace();
                }

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(Stalls_common.this, "Error Message"+error.getMessage(), Toast.LENGTH_SHORT).show();
                progress.dismiss();

            }
        }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<String, String>();
                params.put("location", loc);
                return params;
            }

        };
        MySingleton.getInstance(Stalls_common.this).addToRequestque(stringRequest);

    }




    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.first,menu);
        return super.onCreateOptionsMenu(menu);
    }


    public boolean onOptionsItemSelected(MenuItem item)
{
    if(item.getItemId()==R.id.m1){
        Intent i = new Intent(this,SearchActivity.class);
        i.putExtra("locationx",loc);
        startActivity(i);
        return true;
    }
    else{
        Intent intent=new Intent(getApplicationContext(),CustomerDashboard.class);
        startActivityForResult(intent,0);
        return true;
    }

}


    /*this code refferes to the change of the stalls based on the chane in your spinner click*/



    public void calllistwebservice(final String id){
        progress.show();
        arraylist_item.clear();
        arraylist_cost.clear();
        itemlistid.clear();
        StringRequest stringRequest= new StringRequest(Request.Method.POST, URLs.ITEM_LIST2+"/"+id,new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
               Log.d("Itemresponse",response);

                calllistwebservicerating(stallId);
                progress.dismiss();



                try {

                    JSONArray jsonArray = new JSONArray(response);

                    if(jsonArray.length()==0)
                    {
                        Toast.makeText(Stalls_common.this, "Items are not present in this stall", Toast.LENGTH_SHORT).show();
                    }
                    for(int i=0; i<jsonArray.length();i++){
                        JSONObject jsonObject = jsonArray.getJSONObject(i);
                        arraylist_item.add(jsonObject.getString("item_name"));
                        arraylist_cost.add(jsonObject.getString("cost"));
                        itemlistid.add(jsonObject.getString("id"));
                        arraylist_item_img_url.add(jsonObject.getString("img_url"));

                    }

                    /*Call custom adpter for List Of Items*/
                    myAdapter adapter = new myAdapter(getApplicationContext(),arraylist_item,arraylist_cost,arraylist_item_img_url);

                    listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                        @Override
                        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                            Intent intent = new Intent(getApplicationContext(), Description.class);
                            pos=position;
                            item_id=itemlistid.get(pos).toString();
                            String name=arraylist_item.get(pos).toString();
                            intent.putExtra("id",item_id);
                            intent.putExtra("name",name);
                            intent.putExtra("stallid",stallId);
                            intent.putExtra("stall_name",stallName);


                            startActivity(intent);
                        }
                    });

                    listView.setAdapter(adapter);




                    progress.dismiss();


                } catch (JSONException e) {
                    e.printStackTrace();
                }

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(Stalls_common.this, "Error Message"+error.getMessage(), Toast.LENGTH_SHORT).show();
                progress.dismiss();

            }
        }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<String, String>();
                params.put("stall_id",id);
                return params;
            }

        };
        MySingleton.getInstance(Stalls_common.this).addToRequestque(stringRequest);

    }
//rating web service
public void calllistwebservicerating(final String id){
    progress.show();

    Log.d("s_id",id);
    StringRequest stringRequest= new StringRequest(Request.Method.POST, URLs.SHOW_FEEDBACK1,new Response.Listener<String>() {
        @Override
        public void onResponse(String response) {
            progress.dismiss();
            try {
                JSONObject jsonObject = new JSONObject(response);
                Log.d("Rating",response);
                if(jsonObject.getString("feedback")=="null"){
                    rating_odf_stall.setRating(0);
                }
                else{
                    Float rat=Float.parseFloat(jsonObject.getString("feedback"));
                    Toast.makeText(Stalls_common.this, ""+rat.toString(), Toast.LENGTH_SHORT).show();
                    rating_odf_stall.setRating(rat);
                }
                //Toast.makeText(Stalls_common.this, ""+jsonObject.getString("feedback"), Toast.LENGTH_SHORT).show();
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
    }, new Response.ErrorListener() {
        @Override
        public void onErrorResponse(VolleyError error) {
            Toast.makeText(Stalls_common.this, "Error Message"+error.getMessage(), Toast.LENGTH_SHORT).show();
            progress.dismiss();

        }
    }) {
        @Override
        protected Map<String, String> getParams() throws AuthFailureError {
            Map<String, String> params = new HashMap<String, String>();
            params.put("s_id",id);
            return params;
        }

    };
    MySingleton.getInstance(Stalls_common.this).addToRequestque(stringRequest);

}




}
