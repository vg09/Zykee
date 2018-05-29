package com.example.omarla.food2u_repo;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;
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

public class Cart extends AppCompatActivity {
    ListView order_item_list;
    ProgressDialog progress;
    ArrayList arrayList_item, arrayList_cost, arrayList_stall, arrayList_stallid,
            arrayList_itemid, arrayList_img_url, arrayList_cost_total,
            arrayList_Cartid, arrayList_Userid, arrayList_Quantity;
    Button del_item_btn;
    Button go_to_shop_btn;
    String item_id, user_id, order_id;
    ArrayList<Integer> sumarr;
    String valueToPay;
    Button proceed;

    TextView sumOfItems;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cart);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        toolbar.setNavigationIcon(R.drawable.backarrow);

        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getApplicationContext(),CustomerDashboard.class));
            }
        });


        progress = new ProgressDialog(Cart.this);
        progress.setTitle("Please Wait");
        progress.setMessage("Loading.....");
        progress.setIndeterminate(true);


        arrayList_cost = new ArrayList();
        arrayList_item = new ArrayList();
        arrayList_stall = new ArrayList();
        arrayList_stallid = new ArrayList();
        arrayList_itemid = new ArrayList();
        arrayList_img_url = new ArrayList();
        arrayList_cost_total = new ArrayList();
        arrayList_Quantity = new ArrayList();
        arrayList_Userid = new ArrayList();
        arrayList_Cartid = new ArrayList();
        sumarr = new ArrayList<Integer>();
        user_id = SharedGetterSetter.getUserId(getApplicationContext());


        order_item_list = (ListView) findViewById(R.id.my_order_list1);
        callListWebservice(user_id);


proceed=(Button) findViewById(R.id.confirmation);
proceed.setOnClickListener(new View.OnClickListener() {
    @Override
    public void onClick(View v) {
        Intent intent=new Intent(getApplicationContext(),Paytm_main.class);
         intent.putExtra("cost",valueToPay);
        startActivity(intent);

    }
});

        go_to_shop_btn = (Button) findViewById(R.id.go_for_shop1);
        go_to_shop_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent goBack = new Intent(getApplicationContext(), CustomerDashboard.class);
                startActivity(goBack);
            }


        });
    }


    public void callListWebservice(final String id) {
        Log.d("user id is coomng", "" + id);
        progress.show();
        arrayList_Cartid.clear();
        arrayList_itemid.clear();
        arrayList_item.clear();
        arrayList_stall.clear();
        arrayList_cost.clear();
        arrayList_cost_total.clear();
        arrayList_Quantity.clear();
        arrayList_img_url.clear();
        sumarr.clear();

        StringRequest stringRequest = new StringRequest(Request.Method.POST, URLs.CART_LIST, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                Log.d("Item_response12345", response);

                progress.dismiss();

                try {

                    JSONArray jsonArray = new JSONArray(response);

                    for (int i = 0; i < jsonArray.length(); i++) {
                        JSONObject jsonObject = jsonArray.getJSONObject(i);

                        arrayList_Cartid.add(jsonObject.getString("id"));
                        //arrayList_Userid.add(jsonObject.getString("user_id"));
                        arrayList_itemid.add(jsonObject.getString("item_id"));
                        arrayList_item.add(jsonObject.getString("item_name"));
                        //arrayList_stallid.add(jsonObject.getString("stall_id"));
                        arrayList_stall.add(jsonObject.getString("stall_name"));
                        arrayList_cost.add(jsonObject.getString("item_cost"));
                        arrayList_cost_total.add(jsonObject.getString("total_cost"));
                        arrayList_Quantity.add(jsonObject.getString("quantity"));
                        arrayList_img_url.add(jsonObject.getString("img_url"));
                        sumarr.add(jsonObject.getInt("total_cost"));

                    }


                         Integer sum=0;

                        for(Integer d : sumarr)
                            sum += d;
                        Log.d("sumstring",sum+"");
                    sumOfItems=(TextView) findViewById(R.id.sumOfItems);
                        sumOfItems.setText(String.valueOf(sum));

                       valueToPay=sum.toString();



                    /*Call custom adapter for List Of Ordered Items*/
                    CartListAdapter order_adapter = new CartListAdapter(getApplicationContext(), arrayList_item, arrayList_cost, arrayList_stall, arrayList_img_url, arrayList_cost_total, arrayList_Quantity);

                    order_item_list.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                        @Override
                        public void onItemClick(AdapterView<?> parent, View view, final int position, long id) {

                            order_id = arrayList_Cartid.get(position).toString();
                            String item=arrayList_item.get(position).toString();
                            AlertDialog.Builder builder = new AlertDialog.Builder(Cart.this);
                            builder.setTitle("Delete this item from your cart?");
                            LayoutInflater inflater = (LayoutInflater) getApplicationContext().getSystemService(Cart.this.LAYOUT_INFLATER_SERVICE);
                            View layout = inflater.inflate(R.layout.button_layout,null);
                            builder.setView(layout);
                            //Delete the cart from the list in the my order activity
                            del_item_btn = (Button) layout.findViewById(R.id.del_stall);
                            del_item_btn.setVisibility(view.VISIBLE);
                            del_item_btn.setText("delete");
                            del_item_btn.setBackgroundResource(R.drawable.redstyle);



                            final AlertDialog dialog = builder.create();

                            del_item_btn.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {
                                    //services to delete the item based on the item id
                                    confirmDialogDemo();
                                      dialog.dismiss();

                                }

                            });
                            dialog.create();
                            dialog.show();
                        }
                    });
                    order_item_list.setAdapter(order_adapter);
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
                params.put("user_id", id);
                params.put("status", "0");
                return params;
            }

        };
        MySingleton.getInstance(getApplicationContext()).addToRequestque(stringRequest);

    }


    private void confirmDialogDemo() {
        AlertDialog.Builder builder = new AlertDialog.Builder(Cart.this);
        builder.setTitle("Do you want to delete?");
        builder.setCancelable(false);
        builder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                CallDeleteItemFromCartWebServices(order_id);
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

    public void CallDeleteItemFromCartWebServices(final String order_id) {
progress.show();
        Log.d("register Url", URLs.DELETE_CART);
        StringRequest stringRequest = new StringRequest(Request.Method.POST, URLs.DELETE_CART, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {


                Toast.makeText(getApplicationContext(), "Item you have selected is removed", Toast.LENGTH_SHORT).show();


                callListWebservice(user_id);


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
                Params.put("id", order_id);
                Params.put("status", "2");


                return Params;
            }

        };
        MySingleton.getInstance(getApplicationContext()).addToRequestque(stringRequest);
    }


    public void onBackPressed() {
        Intent startMain = new Intent(getApplicationContext(), CustomerDashboard.class);
        startActivity(startMain);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_cart, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();



        return super.onOptionsItemSelected(item);
    }




}
