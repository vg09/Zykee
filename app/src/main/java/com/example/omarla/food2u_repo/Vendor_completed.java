package com.example.omarla.food2u_repo;

/**
 * Created by Om Arla on 4/21/2018.
 */
import android.app.ProgressDialog;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
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

public class Vendor_completed extends Fragment{


    ProgressDialog progress;
    String user_id;
    ArrayList arr_stall_name, arr_item_name, arr_item_cost, arr_total_cost, arr_date,arr_list_id, arr_img_url,arr_list_quantity;

    ListView vendor_complete_list;


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
        View rootView = inflater.inflate(R.layout.frgament_ven_completed, container, false);

        vendor_complete_list=(ListView)rootView.findViewById(R.id.ven_completed);

        callhistoryservice();

        return rootView;
    }

    public void callhistoryservice()
    {
        progress.show();
        StringRequest stringRequest= new StringRequest(Request.Method.POST, URLs.HISTORY,new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                Log.d("complete",response);

                progress.dismiss();


                try {
                    JSONArray jsonArray = new JSONArray(response);

                    for(int i=0; i<jsonArray.length();i++){
                        JSONObject jsonObject = jsonArray.getJSONObject(i);
                        arr_stall_name.add(jsonObject.getString("stall_name"));
                        arr_item_name.add(jsonObject.getString("item_name"));
                        arr_item_cost.add(jsonObject.getString("item_cost"));

                        arr_total_cost.add(jsonObject.getString("total_cost"));
                        arr_date.add(jsonObject.getString("entry_date"));
                        arr_list_quantity.add(jsonObject.getString("quantity"));
                        arr_img_url.add(jsonObject.getString("img_url"));
                        //    arr_list_id.add(jsonObject.getString("id"));

                    }


                    HistoryAdapter historyAdapter=new HistoryAdapter(getActivity(),arr_stall_name,arr_item_name,arr_item_cost,arr_total_cost,arr_date,arr_list_quantity,arr_img_url);

                    vendor_complete_list.setAdapter(historyAdapter);
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
                params.put("status","5");
                return params;
            }

        };
        MySingleton.getInstance(getActivity()).addToRequestque(stringRequest);

    }
}
