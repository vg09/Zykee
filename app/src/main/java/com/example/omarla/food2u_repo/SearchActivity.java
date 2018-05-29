
package com.example.omarla.food2u_repo;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.SearchView;
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

public class SearchActivity extends AppCompatActivity {
ListView searchlist;
    ArrayList arraylist,arraylistid;
    ArrayList arraylist_item,arraylist_cost,itemlistid,arraylist_item_img_url;
    ProgressDialog progress;
    ArrayList arrayListStall_id,arrayListStall_name;

    String stallId,itemid,item_name;
    Integer  pos;
    String name;
    String item_id;
    SearchView searchView;
    String stall_name;


    ArrayAdapter adapter;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);


//        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
////        setSupportActionBar(toolbar);
//        toolbar.setNavigationIcon(R.drawable.backarrow);

//        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                startActivity(new Intent(getApplicationContext(),CustomerDashboard.class));
//            }
//        });

        arraylist = new ArrayList();
        arraylistid=new ArrayList();//saving stall id in the array list
        arrayListStall_name=new ArrayList();

        arraylist_item = new ArrayList();
        arraylist_cost=new ArrayList();
        itemlistid=new ArrayList();
        arraylist_item_img_url=new ArrayList();
        arrayListStall_id=new ArrayList();


        Button back_BTN=(Button) findViewById(R.id.searchbck);
        back_BTN.setOnClickListener(new View.OnClickListener() {
                                        @Override
                                        public void onClick(View v) {
                                            Intent intent=new Intent(getApplicationContext(),CustomerDashboard.class);
                                            startActivity(intent);
                                        }
                                    }
        );



        progress = new ProgressDialog(this);
        progress.setTitle("Please Wait");
        progress.setMessage("Loading.....");
        progress.setIndeterminate(true);


        Bundle extras = getIntent().getExtras();
        name    = extras.getString("locationx");
        Toast.makeText(this, ""+name, Toast.LENGTH_SHORT).show();
        callWebServiceforSearch();

         searchlist=(ListView)findViewById(R.id.searchList);

        adapter = new ArrayAdapter(SearchActivity.this, android.R.layout.simple_list_item_1, arraylist_item);

        searchlist.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                itemid=itemlistid.get(position).toString();
                item_name=arraylist_item.get(position).toString();
                stallId= arrayListStall_id.get(position).toString();
                stall_name=arrayListStall_name.get(position).toString();

                Intent intent=new Intent(getApplicationContext(),Description.class);
                intent.putExtra("id",itemid);
                intent.putExtra("name",item_name);
                intent.putExtra("stallid",stallId);
                intent.putExtra("stall_name",stall_name);
                startActivity(intent);
            }
        });

            searchlist.setAdapter(adapter);






    }

    @Override
    public boolean onCreateOptionsMenu (Menu menu){
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_search, menu);
        MenuItem item = menu.findItem(R.id.menuSearch);

        searchView = (SearchView) item.getActionView();
        searchView.setIconified(false);
        CharSequence query = searchView.getQuery();
        searchView.setQueryHint("Search for the item at "+name.toString());


        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {



           return false;
            }
            @Override
            public boolean onQueryTextChange(String newText) {
                TextView text=(TextView) findViewById(R.id.backtext);
                text.setVisibility(View.GONE);
                adapter .getFilter().filter(newText);

                return false;
            }
        });


        return super.onCreateOptionsMenu(menu);
    }





    public void callWebServiceforSearch(){
        progress.show();

        StringRequest stringRequest= new StringRequest(Request.Method.POST, URLs.SEARCH ,new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                Log.d("kilbill",response);

                progress.dismiss();



                try {

                    JSONArray jsonArray = new JSONArray(response);

                    for (int i = 0; i < jsonArray.length(); i++) {
                        JSONObject jsonObject = jsonArray.getJSONObject(i);
                        arraylist_item.add(jsonObject.getString("item_name"));
                        arrayListStall_name.add(jsonObject.getString("stall_name"));
                        arrayListStall_id.add(jsonObject.getString("stall_id"));
                        itemlistid.add(jsonObject.getString("id"));


                    }



                } catch (JSONException e) {
                    e.printStackTrace();
                }

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(SearchActivity.this, "Error Message"+error.getMessage(), Toast.LENGTH_SHORT).show();
                progress.dismiss();

            }
        }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<String, String>();
                params.put("location",name);
                return params;
            }

        };
        MySingleton.getInstance(SearchActivity.this).addToRequestque(stringRequest);

    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();
        if (item.getItemId() == android.R.id.home) {
            Intent i=new Intent(getApplicationContext(),CustomerDashboard.class);
            startActivity(i);// close this activity and return to preview activity (if there is any)
        }

        //noinspection SimplifiableIfStatement


        return super.onOptionsItemSelected(item);
    }


}