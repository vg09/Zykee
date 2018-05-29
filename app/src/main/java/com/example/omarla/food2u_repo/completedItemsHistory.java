package com.example.omarla.food2u_repo;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
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

public class completedItemsHistory extends android.support.v4.app.Fragment {

    ProgressDialog progress;
    String user_id;
    Button btn_save;
    ArrayList arr_stall_name, arr_item_name, arr_item_cost, arr_total_cost, arr_date,arr_list_id, arr_img_url,arr_list_quantity;

    ListView historylistview;


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
        View rootView = inflater.inflate(R.layout.fragment_completed_items, container, false);
        btn_save=(Button)rootView.findViewById(R.id.save_feedback);

        Toast.makeText(getActivity(), "Touch Item to give Feedback ", Toast.LENGTH_LONG).show();
        historylistview=(ListView)rootView.findViewById(R.id.history_listview);

        callhistoryservice();

        return rootView;
    }

    public void callhistoryservice()
    {
        progress.show();
        StringRequest stringRequest= new StringRequest(Request.Method.POST, URLs.HISTORY,new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                Log.d("history _response",response);

                progress.dismiss();


                try {
                    JSONArray jsonArray = new JSONArray(response);
                    Log.d("Check1", "onResponse: "+jsonArray.length());
                  //  Log.d("Check1", "onResponse: "+jsonArray.toString());
                    for(int i=0; i<jsonArray.length();i++){
                        JSONObject jsonObject = jsonArray.getJSONObject(i);
                        arr_stall_name.add(jsonObject.getString("stall_name"));
                        arr_item_name.add(jsonObject.getString("item_name"));
                        arr_item_cost.add(jsonObject.getString("item_cost"));

                        arr_total_cost.add(jsonObject.getString("total_cost"));
                        arr_date.add(jsonObject.getString("entry_date"));
                        arr_list_quantity.add(jsonObject.getString("quantity"));
                        arr_img_url.add(jsonObject.getString("img_url"));
                       arr_list_id.add(jsonObject.getString("stall_id"));

                    }


                    HistoryAdapter historyAdapter=new HistoryAdapter(getActivity(),arr_stall_name,arr_item_name,arr_item_cost,arr_total_cost,arr_date,arr_list_quantity,arr_img_url);


                    historylistview.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                        @Override
                        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                            Log.d("position",position+"");


                            final Integer pos=position;
                            final int stall_id=Integer.parseInt(arr_list_id.get(pos).toString());


                            Log.d("complete item",String.valueOf(stall_id));


                            AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
                            builder.setTitle("Give feedback of "+arr_stall_name.get(pos).toString()+" ");
                            LayoutInflater inflater = (LayoutInflater) getActivity().getSystemService(getActivity().LAYOUT_INFLATER_SERVICE);
                            View layout = inflater.inflate(R.layout.button_layout,null);
                            builder.setView(layout);



                            btn_save = (Button)layout.findViewById(R.id.edt_stall);
                            btn_save.setText("Give feedback");
                            btn_save.setBackgroundResource(R.drawable.greenstyle);



                            btn_save.setVisibility(View.VISIBLE);


                            final AlertDialog dialog = builder.create();



                            btn_save.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {
                                    Intent i=new Intent(getActivity(),Feedback.class);
                                    i.putExtra("id_stall",stall_id);
                                    startActivity(i);
                                }
                            });




                            dialog.create();
                            dialog.show();

                        }
                    });



                    historylistview.setAdapter(historyAdapter);
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
