package com.example.omarla.food2u_repo;

import android.app.AlertDialog;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
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

/**
 * Created by hp on 3/17/2018.
 */

public class AddItem extends AppCompatActivity {

    ListView list_items;
    ProgressDialog progress;
    ArrayList arraylist_item,arraylist_cost,itemlistid,item_img_url,arrayList_is_available;
    Integer pos;
    Button edt_item,del_item,make_avble;
    Button add_item_btn;
    SharedPreferences myprefs1;


    String item_id,stall_id;
   // Bundle fragBundle;


    protected void onCreate(final Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.additemactivity);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        progress = new ProgressDialog(this);
        progress.setTitle("Please Wait");
        progress.setMessage("Loading.....");
        progress.setIndeterminate(true);


//        Bundle bundle =new Bundle();
//        bundle=getIntent().getExtras();
//        stall_id=bundle.getString("id");
        stall_id=SharedGetterSetter.getStall_id(getApplicationContext());



       arraylist_cost=new ArrayList();
       arraylist_item=new ArrayList();
       itemlistid    =new ArrayList();
       item_img_url=new ArrayList();
       arrayList_is_available=new ArrayList();


        list_items=(ListView)findViewById(R.id.itemlist_itemAct);
        calllistwebservice(stall_id);



        add_item_btn =(Button)findViewById(R.id.add_new_item);
        add_item_btn.setOnClickListener(new View.OnClickListener()
     {
         @Override
           public void onClick (View v){

            stall_id=SharedGetterSetter.getStall_id(getApplicationContext());
             Bundle fragBundle=new Bundle();
             fragBundle.putString("id",stall_id);
             FragmentManager fragmentManager = getFragmentManager();

                AddNewItemFrag frg=new AddNewItemFrag();
                frg.setArguments(fragBundle);
                FragmentTransaction ft = fragmentManager.beginTransaction();
                ft.add(R.id.fragment_additem, frg).commit();
         }
       });





    }
    public void calllistwebservice(final String id){
        progress.show();
        arraylist_item.clear();
        arraylist_cost.clear();
        itemlistid.clear();
        item_img_url.clear();
        arrayList_is_available.clear();
        StringRequest stringRequest= new StringRequest(Request.Method.POST, URLs.ITEM_LIST+"/"+id,new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                Log.d("Itemresponse",response);

                progress.dismiss();



                try {

                    JSONArray jsonArray = new JSONArray(response);

                    for(int i=0; i<jsonArray.length();i++){
                        JSONObject jsonObject = jsonArray.getJSONObject(i);
                        arraylist_item.add(jsonObject.getString("item_name"));
                        arraylist_cost.add(jsonObject.getString("cost"));
                        itemlistid.add(jsonObject.getString("id"));
                        item_img_url.add(jsonObject.getString("img_url"));
                       arrayList_is_available.add(jsonObject.getString("is_available"));

                    }

                    /*Call custom adpter for List Of Items*/
                    myAdapter adapter = new myAdapter(getApplicationContext(),arraylist_item,arraylist_cost,item_img_url);

                    list_items.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                        @Override
                        public void onItemClick(AdapterView<?> parent, View view, final int position, long id) {


                            pos=position;
                            item_id=itemlistid.get(position).toString();



                            AlertDialog.Builder builder = new AlertDialog.Builder(AddItem.this);
                            builder.setTitle("Perform action on "+arraylist_item.get(pos).toString()+" stall");
                            LayoutInflater inflater = (LayoutInflater) getApplicationContext().getSystemService(AddItem.this.LAYOUT_INFLATER_SERVICE);
                            View layout = inflater.inflate(R.layout.button_layout,null);
                            builder.setView(layout);

                            make_avble=(Button) layout.findViewById(R.id.is_open_btn);
                            edt_item=(Button)layout.findViewById(R.id.edt_stall);
                            edt_item.setBackgroundResource(R.drawable.bluestyle);
                            edt_item.setText("EDIT");
                            del_item=(Button)layout.findViewById(R.id.del_stall);
                            del_item.setBackgroundResource(R.drawable.redstyle);
                            del_item.setText("Delete");
                      ;


                            edt_item.setVisibility(view.VISIBLE);
                            del_item.setVisibility(view.VISIBLE);
                           make_avble.setVisibility(view.VISIBLE);

                            final AlertDialog dialog = builder.create();


                            if(arrayList_is_available.get(pos).toString().equals("0"))
                            {
                                make_avble.setBackgroundResource(R.drawable.redstyle);
                                make_avble.setText("make unavailable");
                            }
                            else
                            {
                                make_avble.setBackgroundResource(R.drawable.greenstyle);
                                make_avble.setText("make available");
                            }
                            make_avble.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {


                                    buttonClicked();

                                    dialog.dismiss();


                                }
                            });
                            edt_item.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {


                                    Intent intent = new Intent(getApplicationContext(), Edit_item.class);
                                    String item_id=itemlistid.get(position).toString();
                                    String Item_name=arraylist_item.get(pos).toString();
                                    String item_price=arraylist_cost.get(pos).toString();

                                    intent.putExtra("id",item_id);
                                    intent.putExtra("item_name",Item_name);
                                    intent.putExtra("item_price",item_price);
                                    startActivity(intent);
                                    dialog.dismiss();
                                }
                            });


                            del_item.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {

                                    confirmDialogDemo();
                                    dialog.dismiss();
                                }
                            });


                            dialog.create();
                            dialog.show();

                        }
                    });




                    list_items.setAdapter(adapter);


                    progress.dismiss();


                } catch (JSONException e) {
                    e.printStackTrace();
                }

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(getApplicationContext(), "Error Message" + error.getMessage(), Toast.LENGTH_SHORT).show();
                progress.dismiss();

            }
        }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<String, String>();
                params.put("stall_id", stall_id);
                return params;
            }

        };
        MySingleton.getInstance(getApplicationContext()).addToRequestque(stringRequest);

    }


    private void confirmDialogDemo() {
        AlertDialog.Builder builder=new AlertDialog.Builder(AddItem.this);
        builder.setTitle("Do you want to delete?");
        builder.setMessage("You are about to delete all records of database. Do you really want to proceed ?");
        builder.setCancelable(false);
        builder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                CalldeleteitemWebServices(item_id);
                // Toast.makeText(VendorDashboard.this, "Yes Button Clicked", Toast.LENGTH_SHORT).show();
            }
        });

        builder.setNegativeButton("No", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                Toast.makeText(getApplicationContext(), "No Button Clicked", Toast.LENGTH_SHORT).show();
            }
        });

        builder.create();
        builder.show();
    }

    public void CalldeleteitemWebServices(final String item_id){

        Log.d("register Url",URLs.REMOVE_ITEM);
        StringRequest stringRequest=new StringRequest(Request.Method.PUT, URLs.REMOVE_ITEM+"/"+item_id, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                progress.dismiss();
              //  Log.d("signupresponse",response);
                Log.d("response",response);
                Toast.makeText(getApplicationContext(), "Item you have selected is removed", Toast.LENGTH_SHORT).show();


                calllistwebservice(stall_id);



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
                Params.put("status","0");
                Params.put("id",item_id);
                return Params;
            }



        };
        MySingleton.getInstance(getApplicationContext()).addToRequestque(stringRequest);
    }



    public void buttonClicked()
    {
        Log.d("position ro",arrayList_is_available.get(pos).toString());
        if(arrayList_is_available.get(pos).toString().equals("0"))
        {
            arrayList_is_available.set(pos,1);
            confirmDialogDemoforUnavilable();


        }
        else{
            arrayList_is_available.set(pos,0);
            CallstallWebServicesForavailable(item_id);

        }

    }





    private void confirmDialogDemoforUnavilable() {

        AlertDialog.Builder builder=new AlertDialog.Builder(AddItem.this);
        builder.setTitle("Do you really want to change the availability of "+arraylist_item.get(pos).toString()+"?");

        builder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

                CallstallWebServicesForUnavailable(item_id);



                Toast.makeText(AddItem.this, "Yes ", Toast.LENGTH_SHORT).show();

            }
        });

        builder.setNegativeButton("No", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                Toast.makeText(AddItem.this, "No Button Clicked", Toast.LENGTH_SHORT).show();
            }
        });

        builder.create();
        builder.show();}

    public void CallstallWebServicesForUnavailable(final String item_id){

        progress.show();
        Log.d("register Url",URLs.UNAVAILABLE);
        StringRequest stringRequest=new StringRequest(Request.Method.POST, URLs.UNAVAILABLE, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {



                Toast.makeText(AddItem.this, "Item is made unavailable", Toast.LENGTH_SHORT).show();
                calllistwebservice(stall_id);








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
                Params.put("is_available","1");
                Params.put("id",item_id);
                return Params;
            }



        };
        MySingleton.getInstance(getApplicationContext()).addToRequestque(stringRequest);

    }



    public void CallstallWebServicesForavailable(final String item_id){

        progress.show();
        Log.d("register Url",URLs.AVAILABLE);
        StringRequest stringRequest=new StringRequest(Request.Method.POST, URLs.AVAILABLE, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {


                Toast.makeText(AddItem.this,  "Item is made Available to customer", Toast.LENGTH_SHORT).show();


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
                Params.put("is_available","0");
                Params.put("id",item_id);
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

    public void onBackPressed ()
    {Intent startMain = new Intent(AddItem.this,VendorDashboard.class);
        startActivity(startMain);}
}
