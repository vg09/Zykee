package com.example.omarla.food2u_repo;

import android.app.AlertDialog;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Base64;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
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

import java.io.OptionalDataException;
import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import de.hdodenhof.circleimageview.CircleImageView;

/**
 * Created by Om Arla on 3/7/2018.
 */

public class VendorDashboard extends AppCompatActivity {

    Button addBbtn1;
    ListView List_bus;
    ArrayList arrayList_business;
    ArrayList arrayList_img_Url,arrayList_is_open;
    ArrayList arrayList_location,Stall_idlist;
    SharedPreferences myprefs;
    Bitmap bitmap1;
    Integer status=1;
    Button Transactions;

    SharedGetterSetter sharedGetterSetter;
    Button open_Stl_btn,edit_stall,del_stall,add_item_btn;


    ProgressDialog progress;
    String vendor_id;
    Integer pos;
    String stall__id,username;
    CircleImageView ven_profile_img;
    TextView ven_name;
    TextView ven_mail;
    String imageString;
    int counter=2;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_vendor_dashboard);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        progress = new ProgressDialog(this);
        progress.setTitle("Please Wait");
        progress.setMessage("Loading.....");
        progress.setIndeterminate(true);

        arrayList_img_Url=new ArrayList();
        arrayList_location= new ArrayList();
        arrayList_business=new ArrayList();
        arrayList_is_open=new ArrayList();
        Stall_idlist=new ArrayList<>();
        List_bus = (ListView) findViewById(R.id.VenStalList);

        myprefs= getSharedPreferences("session_id", MODE_WORLD_READABLE);
        ven_mail=(TextView)findViewById(R.id.email_ven);
        ven_name=(TextView) findViewById(R.id.user_ven_name);

        ven_profile_img=(CircleImageView) findViewById(R.id.profile_image123);
        imageString=SharedGetterSetter.getImgUrl(getApplicationContext());




        ven_profile_img.setImageBitmap(decodeImage(imageString));
        ven_name.setText(SharedGetterSetter.getUserName(getApplicationContext()));
        ven_mail.setText(SharedGetterSetter.getUserMail(getApplicationContext()));







       String functipmn= sharedGetterSetter.getUserId(VendorDashboard.this);
       username=sharedGetterSetter.getUserName(VendorDashboard.this);
       Log.d("username",username);





       Log.d("sharedgetter setter", functipmn);
        vendor_id= myprefs.getString("session_id", null);



        calllistwebservice(vendor_id);
Transactions=(Button)findViewById(R.id.goTransact);
Transactions.setOnClickListener(new View.OnClickListener() {
    @Override
    public void onClick(View v) {
        Intent intent=new Intent(getApplicationContext(),Vendor_Transaction.class);
        startActivity(intent);
    }
});

    addBbtn1 =(Button)findViewById(R.id.addBbtn);

        addBbtn1.setOnClickListener(new View.OnClickListener()

    {
        @Override
        public void onClick (View v){
            if(counter%2==0){
                counter++;
        FragmentManager fragmentManager = getFragmentManager();
        FragmentTransaction f1 = fragmentManager.beginTransaction();


        AddBusinessFragment fragmentAddBusiness = new AddBusinessFragment();
        f1.add(R.id.fragment_container, fragmentAddBusiness);
        f1.commit();}

        else{
                counter++;
                Intent intent=new Intent(getApplicationContext(),VendorDashboard.class);
                startActivity(intent);
            }
    }
    });

        }




    public void calllistwebservice(final String id) {
        progress.show();
        arrayList_business.clear();
        arrayList_location.clear();
        Stall_idlist.clear();
        arrayList_img_Url.clear();
        StringRequest stringRequest = new StringRequest(Request.Method.POST, URLs.VENDOR_STALL + "/" + id, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                Log.d("Itemresponse", response);

                progress.dismiss();


                try {

                    JSONArray jsonArray = new JSONArray(response);

                    for (int i = 0; i < jsonArray.length(); i++) {
                        JSONObject jsonObject = jsonArray.getJSONObject(i);
                        arrayList_business.add(jsonObject.getString("stall_name"));
                        arrayList_location.add(jsonObject.getString("location"));
                        arrayList_img_Url.add(jsonObject.getString("img_url"));
                        Stall_idlist.add(jsonObject.getString("id"));
                        arrayList_is_open.add(jsonObject.getString("is_open"));
                    }



                    /*Call custom adpter for List Of Items*/
                    AddBusinessAdapter adapter = new AddBusinessAdapter(getApplicationContext(), arrayList_business, arrayList_location,arrayList_img_Url);

                    List_bus.setAdapter(adapter);


                    List_bus.setOnItemClickListener(new AdapterView.OnItemClickListener() {

                        @Override
                        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                           pos=position;
                            stall__id=Stall_idlist.get(pos).toString();
                           final String Stall_name=arrayList_business.get(pos).toString();
                            final String Location=arrayList_location.get(pos).toString();

                         AlertDialog.Builder builder = new AlertDialog.Builder(VendorDashboard.this);
                            builder.setTitle("Perform action on "+arrayList_business.get(pos).toString()+" stall");
                            LayoutInflater inflater = (LayoutInflater) getApplicationContext().getSystemService(VendorDashboard.this.LAYOUT_INFLATER_SERVICE);
                            View layout = inflater.inflate(R.layout.button_layout,null);
                            builder.setView(layout);

                            open_Stl_btn=(Button) layout.findViewById(R.id.is_open_btn);
                            edit_stall=(Button)layout.findViewById(R.id.edt_stall);
                            edit_stall.setBackgroundResource(R.drawable.bluestyle);
                            edit_stall.setText("EDIT");
                            del_stall=(Button)layout.findViewById(R.id.del_stall);
                            del_stall.setBackgroundResource(R.drawable.redstyle);
                            del_stall.setText("Delete");
                            add_item_btn=(Button)layout.findViewById(R.id.Add_items);
                            add_item_btn.setText("Add Items");
                            add_item_btn.setBackgroundResource(R.drawable.orangestyle);


                            add_item_btn.setVisibility(view.VISIBLE);
                            edit_stall.setVisibility(view.VISIBLE);
                            del_stall.setVisibility(view.VISIBLE);
                            open_Stl_btn.setVisibility(view.VISIBLE);

                            final AlertDialog dialog = builder.create();


                            if(arrayList_is_open.get(pos).toString().equals("0"))
                            {
                                open_Stl_btn.setBackgroundResource(R.drawable.redstyle);
                                open_Stl_btn.setText("CLOSE");
                            }
                            else
                            {
                                open_Stl_btn.setBackgroundResource(R.drawable.greenstyle);
                                open_Stl_btn.setText("OPEN");
                            }
                                   open_Stl_btn.setOnClickListener(new View.OnClickListener() {
                                       @Override
                                       public void onClick(View v) {


                                           buttonClicked();

                                           dialog.dismiss();


                                       }
                                   });
                            edit_stall.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {

                                    Intent intent = new Intent(VendorDashboard.this,EditBusiness.class);
                                    intent.putExtra("id",stall__id);
                                    intent.putExtra("Stall_Name",Stall_name);
                                    intent.putExtra("Location",Location);
                                    startActivity(intent);
                                    dialog.dismiss();
                                }
                            });


                            del_stall.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {

                                    confirmDialogDemo();
                                    dialog.dismiss();
                                }
                            });

                            add_item_btn.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {
                                    dialog.dismiss();

                                    SharedGetterSetter.setStall_id(getApplicationContext(),stall__id);
                                    Intent intent1 = new Intent(getApplicationContext(), AddItem.class);
                                    intent1.putExtra("id",stall__id);
                                    startActivity(intent1);

                                }
                            });
                            dialog.create();
                            dialog.show();

                        }
                    });



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
                params.put("vendor_id", id);
                return params;
            }

        };
        MySingleton.getInstance(getApplicationContext()).addToRequestque(stringRequest);

    }








//calldelete stalls

    public void CalldeletestallWebServices(final String stall_id){

        Log.d("register Url",URLs.REMOVE_STALL);
        StringRequest stringRequest=new StringRequest(Request.Method.POST, URLs.REMOVE_STALL, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                progress.dismiss();
                Log.d("signupresponse",response);
                Log.d("response",response);
                Toast.makeText(VendorDashboard.this, "Stall is removed", Toast.LENGTH_SHORT).show();


                calllistwebservice(vendor_id);


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
               Params.put("id",stall_id);
                return Params;
            }



        };
        MySingleton.getInstance(getApplicationContext()).addToRequestque(stringRequest);
    }

    public void CallstallWebServicesForClose(final String stall_id){

        progress.show();
        Log.d("register Url",URLs.CLOSESTALL);
        StringRequest stringRequest=new StringRequest(Request.Method.POST, URLs.CLOSESTALL, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {


                calllistwebservice(vendor_id);

                Toast.makeText(VendorDashboard.this, "Stall is closed", Toast.LENGTH_SHORT).show();




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
                Params.put("is_open","1");
                Params.put("id",stall_id);
                return Params;
            }



        };
        MySingleton.getInstance(getApplicationContext()).addToRequestque(stringRequest);

    }


    public void CallstallWebServicesForOpen(final String stall_id){

        progress.show();

        StringRequest stringRequest=new StringRequest(Request.Method.POST, URLs.OPENSTALL, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {


                Toast.makeText(VendorDashboard.this, "Stall is opened", Toast.LENGTH_SHORT).show();

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
                Params.put("is_open","0");
                Params.put("id",stall_id);
                return Params;
            }



        };
        MySingleton.getInstance(getApplicationContext()).addToRequestque(stringRequest);
    }
    private void confirmDialogDemoforclose() {

        AlertDialog.Builder builder=new AlertDialog.Builder(VendorDashboard.this);
        builder.setTitle("Do you want to close the stall?");

        builder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

                CallstallWebServicesForClose(stall__id);

            }
        });

        builder.setNegativeButton("No", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                Toast.makeText(VendorDashboard.this, "No operation selected", Toast.LENGTH_SHORT).show();
            }
        });

        builder.create();
        builder.show();}

    private void confirmDialogDemo() {
        AlertDialog.Builder builder=new AlertDialog.Builder(VendorDashboard.this);
        builder.setTitle("Do you want to delete?");
        builder.setMessage("You are about to delete all records of database. Do you really want to proceed ?");
        builder.setCancelable(false);
        builder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                CalldeletestallWebServices(stall__id);

                // Toast.makeText(VendorDashboard.this, "Yes Button Clicked", Toast.LENGTH_SHORT).show();
            }
        });

        builder.setNegativeButton("No", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                Toast.makeText(VendorDashboard.this, "No Button Clicked", Toast.LENGTH_SHORT).show();
            }
        });

        builder.create();
        builder.show();
    }
    public Bitmap decodeImage(String url){
        byte[] decodedString = Base64.decode(url, Base64.DEFAULT);
        Log.d("decoded",decodedString+"");
        bitmap1= BitmapFactory.decodeByteArray(decodedString, 0, decodedString.length);
        Log.d("image coming",bitmap1+"");
      return bitmap1;}

    public boolean onOptionsItemSelected(MenuItem item)
    {
        Intent intent=new Intent(getApplicationContext(),CustomerDashboard.class);
        startActivityForResult(intent,0);
        return true;
    }
    public void onBackPressed ()
    {Intent startMain = new Intent(VendorDashboard.this,CustomerDashboard.class);
        startActivity(startMain);}


        public void buttonClicked()
        {
Log.d("position ro",arrayList_is_open.get(pos).toString());
            if(arrayList_is_open.get(pos).toString().equals("0"))
            {

               arrayList_is_open.set(pos,1);
                confirmDialogDemoforclose();


            }
            else{

                arrayList_is_open.set(pos,0);
                CallstallWebServicesForOpen(stall__id);



            }

        }
}

