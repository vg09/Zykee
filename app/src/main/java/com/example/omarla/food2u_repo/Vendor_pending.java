package com.example.omarla.food2u_repo;

/**
 * Created by Om Arla on 4/21/2018.
 */
import android.app.ProgressDialog;
import android.content.Intent;
import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
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

public class Vendor_pending extends Fragment {

    ProgressDialog progress;
    String user_id;
    ArrayList arr_stall_name, arr_item_name, arr_item_cost, arr_total_cost, arr_date,arr_list_id, arr_img_url,arr_list_quantity;

    ListView ven_pending;
    Button complete;
    String item_id,order_id;


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        user_id=SharedGetterSetter.getUserId(getActivity());

        progress = new ProgressDialog(getActivity());
        progress.setTitle("Please Wait");
        progress.setMessage("Loading.....");
        progress.setIndeterminate(true);


        arr_list_id=new ArrayList();
        arr_stall_name = new ArrayList();
        arr_item_name = new ArrayList();
        arr_item_cost = new ArrayList();
        arr_total_cost = new ArrayList();
        arr_date = new ArrayList();
        arr_img_url = new ArrayList();
        arr_list_quantity = new ArrayList();


    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_ven_peding, container, false);

        ven_pending=(ListView)rootView.findViewById(R.id.ven_pending);


        callhistoryservice();


        return rootView;
    }



    public void callhistoryservice()
    {
        arr_stall_name.clear();
        arr_item_name.clear();
        arr_item_cost.clear();

        arr_total_cost.clear();
        arr_date.clear();
        arr_list_quantity.clear();
        arr_img_url.clear();
        arr_list_id.clear();
        progress.show();
        StringRequest stringRequest= new StringRequest(Request.Method.POST, URLs.HISTORY,new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                Log.d("history _response",response);

                progress.dismiss();


                try {
                    JSONArray jsonArray = new JSONArray(response);
                    Log.d("Check1", "onResponse: "+jsonArray.length());

                    for(int i=0; i<jsonArray.length();i++){
                        JSONObject jsonObject = jsonArray.getJSONObject(i);
                        arr_stall_name.add(jsonObject.getString("stall_name"));
                        arr_item_name.add(jsonObject.getString("item_name"));
                        arr_item_cost.add(jsonObject.getString("item_cost"));

                        arr_total_cost.add(jsonObject.getString("total_cost"));
                        arr_date.add(jsonObject.getString("entry_date"));
                        arr_list_quantity.add(jsonObject.getString("quantity"));
                        arr_img_url.add(jsonObject.getString("img_url"));
                        arr_list_id.add(jsonObject.getString("id"));

                    }


                    HistoryAdapter historyAdapter=new HistoryAdapter(getActivity(),arr_stall_name,arr_item_name,arr_item_cost,arr_total_cost,arr_date,arr_list_quantity,arr_img_url);

                    ven_pending.setAdapter(historyAdapter);

                  ven_pending.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                      @Override
                      public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                          order_id=arr_list_id.get(position).toString();


                          complete=(Button) view.findViewById(R.id.complete_btn);
                          complete.setVisibility(view.VISIBLE);
                          complete.setOnClickListener(new View.OnClickListener() {
                              @Override
                              public void onClick(View v) {
                                  callWebServicesToNotifyUser();
                              }
                          });

                      }
                  });

                    progress.dismiss();


                } catch (JSONException e) {
                    Log.d("Check 5", "onResponse: ");
                    e.printStackTrace();
                }

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(getActivity(), "Error Message"+error.getMessage(), Toast.LENGTH_SHORT).show();
                progress.dismiss();

            }
        }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<String, String>();
                params.put("user_id", user_id);
                params.put("status","4");
                return params;
            }

        };
        MySingleton.getInstance(getActivity()).addToRequestque(stringRequest);


    }


    public void callWebServicesToNotifyUser(){


        StringRequest stringRequest=new StringRequest(Request.Method.POST, URLs.DELETE_CART, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                progress.dismiss();
                Log.d("signupresponse",response);



                callhistoryservice();


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
                Params.put("status","5");
                Params.put("id",order_id);
                return Params;
            }



        };
        MySingleton.getInstance(getActivity()).addToRequestque(stringRequest);
        Intent intent=new Intent(getActivity(),Vendor_Transaction.class);
        startActivity(intent);
    }

}
