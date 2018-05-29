package com.example.omarla.food2u_repo;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.v7.app.AppCompatActivity;
import android.util.Base64;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by Om Arla on 3/23/2018.
 */

public class EditBusiness extends AppCompatActivity{
EditText edit_businessName;
ImageView edit_bus_img;
Spinner edt_locSpin;
String new_busName;
String loc;
Button save_btn_bus_details;
ProgressDialog progress;
Bitmap bitmap;
String name;
String imagf;
String business_name,Location;


Button save_busDetails;
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.edit_business);

        progress = new ProgressDialog(this);
        progress.setTitle("Please Wait");
        progress.setMessage("Loading.....");
        progress.setIndeterminate(true);


        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        Bundle bundle = new Bundle();
        bundle = getIntent().getExtras();
        final String stall_id = bundle.getString("id");
        business_name=bundle.getString("Stall_Name");
        Location=bundle.getString("Location");

        TextView business_name_fixed=(TextView) findViewById(R.id.business_name_fixed);
        TextView Location_fixed=(TextView) findViewById(R.id.location_here);

        business_name_fixed.setText(business_name);
        Location_fixed.setText(Location);


        save_btn_bus_details=(Button) findViewById(R.id.edt_save_business);
        edit_businessName=(EditText) findViewById(R.id.edt_bus_name);
        edt_locSpin=(Spinner) findViewById(R.id.edt_spin_loc);
        edit_bus_img=(ImageView) findViewById(R.id.edited_image);


        edit_bus_img.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                selectImage();
            }
        });


        edt_locSpin.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

                loc=parent.getItemAtPosition(position).toString();
              //  Toast.makeText(getActivity().getApplicationContext(), "Toat value"+loc ,Toast.LENGTH_SHORT).show();

                if(loc.equalsIgnoreCase("Boys-Hostel1 FC"))
                {
                    loc="bh1";
                }
                else if(loc.equalsIgnoreCase("Boys-Hostel3 FC"))
                {
                    loc="bh3";
                }
                else if(loc.equalsIgnoreCase("Other Fc"))
                {
                    loc="other";
                }
                else if(loc.equalsIgnoreCase("34-Block FC"))
                {
                    loc="34";
                }
                else if(loc.equalsIgnoreCase("Campus Cafe FC"))
                {
                    loc="cc";
                }
                else if(loc.equalsIgnoreCase("Mall FC"))
                {
                    loc="mall";
                }

            }
            public boolean onOptionsItemSelected(MenuItem item)
            {
                Intent intent=new Intent(getApplicationContext(),CustomerDashboard.class);
                startActivityForResult(intent,0);
                return true;
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

                Toast.makeText(getApplicationContext(), "please select the location ", Toast.LENGTH_SHORT).show();
            }


    });

        save_btn_bus_details.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new_busName=edit_businessName.getText().toString();
                if(imagf.equals(null))
                {
                    edit_bus_img.setImageResource(R.drawable.campus_main);

                }
                Log.d("save btn",new_busName);
                Log.d("save btn",loc);
                callEditBusinessServices(stall_id);
            }
        });

}

    public boolean onOptionsItemSelected(MenuItem item)
    {
        Intent intent=new Intent(getApplicationContext(),VendorDashboard.class);
        startActivityForResult(intent,0);
        return true;
    }


public void callEditBusinessServices(final String id){
progress.show();
    Log.d("register Url",URLs.UPDATE_STALL);
    Log.d("call service btn",new_busName);
    Log.d("call service btn",loc);
    StringRequest stringRequest=new StringRequest(Request.Method.PUT, URLs.UPDATE_STALL+"/"+id, new Response.Listener<String>() {
        @Override
        public void onResponse(String response) {
            progress.dismiss();

            Log.d("response",response);
            Toast.makeText(getApplicationContext(), "Item Is Updated", Toast.LENGTH_SHORT).show();
            Intent intent=new Intent(getApplicationContext(),VendorDashboard.class);
            startActivity(intent);






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

            Params.put("id",id);
            Params.put("stall_name",new_busName);
            Params.put("location",loc);
            Params.put("img_url",imagf);
            return Params;
        }



    };
    MySingleton.getInstance(getApplicationContext()).addToRequestque(stringRequest);
    Intent intent=new Intent(getApplicationContext(),VendorDashboard.class);
    startActivity(intent);
}




    private void selectImage() {

        final CharSequence[] options = {"Take Photo", "Choose from Gallery", "Cancel"};
        android.app.AlertDialog.Builder builder = new android.app.AlertDialog.Builder(EditBusiness.this);
        builder.setTitle("Add Photo!");
        builder.setItems(options, new DialogInterface.OnClickListener() {

            @Override

            public void onClick(DialogInterface dialog, int item) {

                if (options[item].equals("Take Photo"))

                {
                    Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                    /*File f = new File(Environment.getExternalStorageDirectory(), "temp.jpg");
                    intent.putExtra(MediaStore.EXTRA_OUTPUT, Uri.fromFile(f));
                    startActivityForResult(intent, 1);*/

                    if (intent.resolveActivity(getPackageManager()) != null) {
                        startActivityForResult(intent, 1);
                    }

                } else if (options[item].equals("Choose from Gallery"))

                {
                    Intent intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                    startActivityForResult(intent, 2);
                } else if (options[item].equals("Cancel")) {
                    dialog.dismiss();
                }
            }
        });
        builder.show();

    }


    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        switch(requestCode) {
            case 1:
                if (resultCode == RESULT_OK) {
                   edit_bus_img.setVisibility(View.VISIBLE);
                    Bundle extras = data.getExtras();
                    bitmap = (Bitmap) extras.get("data");
                    imagf=getStringImage(bitmap);
                    edit_bus_img.setImageBitmap(bitmap);




                    // Toast.makeText(this,String.valueOf(imageBitmap),Toast.LENGTH_LONG).show();
                    Uri tempUri = getImageUri(getApplicationContext(), bitmap);

                    // CALL THIS METHOD TO GET THE ACTUAL PATH
                    File finalFile = new File(getRealPathFromURI(tempUri));
                    String f = finalFile.getAbsolutePath();
                    f = f.substring(f.lastIndexOf("/") + 1);
                    name=f;
                    // i.setText(f);
                    Log.i("Path", f);

                    //CallWebServiceEditProfilePic();




                }

                break;
            case 2:
                if(resultCode == RESULT_OK) {

                    //getting image from gallery
                    Uri selectedImage = data.getData();
                    try {

                        progress.show();
                        bitmap = MediaStore.Images.Media.getBitmap(getContentResolver(),selectedImage);
                        edit_bus_img.setImageBitmap(bitmap);
                        imagf=getStringImage(bitmap);
                       edit_bus_img.setVisibility(View.VISIBLE);
                        Uri tempUri = getImageUri(getApplicationContext(), bitmap);

                        // CALL THIS METHOD TO GET THE ACTUAL PATH
                        File finalFile = new File(getRealPathFromURI(tempUri));
                        String f = finalFile.getAbsolutePath();
                        f = f.substring(f.lastIndexOf("/")+1);
                        name=f;
                        Log.i("Path",f);
                        Log.d("image before service ",imagf);


                        //CallWebServiceEditProfilePic();
                        //i.setText(f);
                    } catch (Exception e) {
                        Log.i("TAG", "Some exception " + e);
                    }

                }
                break;

        }
    }

    public String getStringImage(Bitmap bitmap){
        if (bitmap==null)
        {
            return  "";
        }
        else {
            ByteArrayOutputStream byteArrayOS = new ByteArrayOutputStream();
            bitmap.compress(Bitmap.CompressFormat.JPEG,100 ,byteArrayOS);
            Log.d("String",""+ Base64.encodeToString(byteArrayOS.toByteArray(), Base64.DEFAULT));
            return Base64.encodeToString(byteArrayOS.toByteArray(), Base64.DEFAULT);
        }
    }

    public Uri getImageUri(Context inContext, Bitmap inImage) {
        ByteArrayOutputStream bytes = new ByteArrayOutputStream();
        inImage.compress(Bitmap.CompressFormat.JPEG, 100, bytes);
        String path = MediaStore.Images.Media.insertImage(inContext.getContentResolver(), inImage, "Title", null);
        return Uri.parse(path);
    }

    public String getRealPathFromURI(Uri uri) {
        Cursor cursor = getContentResolver().query(uri, null, null, null, null);
        cursor.moveToFirst();
        int idx = cursor.getColumnIndex(MediaStore.Images.ImageColumns.DATA);
        return cursor.getString(idx);
    }



}