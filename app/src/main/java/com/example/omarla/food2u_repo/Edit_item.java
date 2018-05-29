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
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
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

import javax.sql.StatementEvent;

/**
 * Created by Om Arla on 3/23/2018.
 */

public class Edit_item extends AppCompatActivity{
    EditText edt_itemName;
    EditText edt_itemCost;
    EditText edt_descrption;
    Button edt_item_save;
    ImageView edit_img;
    Bitmap bitmap;
    String name;
    String imagf;
    ProgressDialog progress;
String new_Name,new_Price,new_Description;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.edit_item);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);//back button

        Bundle bundle = new Bundle();
        bundle = getIntent().getExtras();
        final String i_id = bundle.getString("id");
        final int item_id = Integer.parseInt(i_id);
        final String item_name=bundle.getString("item_name");
        final String item_price=bundle.getString("item_price");


        TextView item_name_fixed=(TextView) findViewById(R.id.item_name_fixed);
        item_name_fixed.setText(item_name);

        TextView item_price_fixed=(TextView) findViewById(R.id.item_price_fixed);
        item_price_fixed.setText(item_price);

        progress = new ProgressDialog(this);
        progress.setTitle("Please Wait");
        progress.setMessage("Loading.....");
        progress.setIndeterminate(true);

        edt_itemCost = (EditText) findViewById(R.id.edt_itemCost);
        edt_itemName = (EditText) findViewById(R.id.edt_itemName);
        edt_descrption = (EditText) findViewById(R.id.edit_description);
        edt_item_save = (Button) findViewById(R.id.edtitem_save_btn);
        edit_img = (ImageView) findViewById(R.id.edit_item_img);

        edit_img.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                selectImage();
            }
        });

        edt_item_save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new_Name = edt_itemName.getText().toString();
                new_Price = edt_itemCost.getText().toString();
                new_Description = edt_descrption.getText().toString();
                CallWebEditItem(i_id);
            }
        });


    }


    public void CallWebEditItem(final String item_id){
        progress.show();
    //serviceto save the new details of the items
    Log.d("item_name",new_Name);
    Log.d("cost",new_Price);
    Log.d("desc",new_Description);
    Log.d("imagffff",""+imagf);

    Log.d("hiiii","dued");
    StringRequest stringRequest = new StringRequest(Request.Method.PUT, URLs.UPDATE_ITEM+ "/" +item_id, new Response.Listener<String>() {


        @Override
        public void onResponse(String response) {



            progress.dismiss();


        }
    }, new Response.ErrorListener() {
        @Override
        public void onErrorResponse(VolleyError error) {
            Toast.makeText(Edit_item.this, "Error Message" + error.getMessage(), Toast.LENGTH_SHORT).show();
            progress.dismiss();

        }
    }) {
        @Override
        protected Map<String, String> getParams() throws AuthFailureError {
            Map<String, String> params = new HashMap<String, String>();
            params.put("id", item_id);
            params.put("item_name",new_Name);
            params.put("cost",new_Price);
            params.put("description",new_Description);
            params.put("img_url",imagf);
            return params;
        }

    };
    MySingleton.getInstance(Edit_item.this).addToRequestque(stringRequest);
    Intent intent =new Intent(getApplicationContext(),AddItem.class);
    startActivity(intent);
}


    public boolean onOptionsItemSelected(MenuItem item)
    {
        Intent intent=new Intent(getApplicationContext(),CustomerDashboard.class);
        startActivityForResult(intent,0);
        return true;
    }



    private void selectImage() {

        final CharSequence[] options = {"Take Photo", "Choose from Gallery", "Cancel"};
        android.app.AlertDialog.Builder builder = new android.app.AlertDialog.Builder(Edit_item.this);
        builder.setTitle("Add Photo!");
        builder.setItems(options, new DialogInterface.OnClickListener() {

            @Override

            public void onClick(DialogInterface dialog, int item) {

                if (options[item].equals("Take Photo"))

                {
                    Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);

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


   public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        switch(requestCode) {
            case 1:
                if (resultCode == RESULT_OK) {
                    edit_img.setVisibility(View.VISIBLE);
                    Bundle extras = data.getExtras();
                    bitmap = (Bitmap) extras.get("data");
                    imagf=getStringImage(bitmap);
                    edit_img.setImageBitmap(bitmap);
Log.d("imagesssssss0",""+imagf);



                    Uri tempUri = getImageUri(getApplicationContext(), bitmap);

                    // CALL THIS METHOD TO GET THE ACTUAL PATH
                    File finalFile = new File(getRealPathFromURI(tempUri));
                    String f = finalFile.getAbsolutePath();
                    f = f.substring(f.lastIndexOf("/") + 1);
                    name=f;
                    // i.setText(f);
                    Log.i("Path", f);






                }

                break;
            case 2:
                if(resultCode == RESULT_OK) {

                    //getting image from gallery
                    Uri selectedImage = data.getData();
                    try {

                        progress.show();
                        bitmap = MediaStore.Images.Media.getBitmap(getContentResolver(),selectedImage);
                        edit_img.setImageBitmap(bitmap);
                        imagf=getStringImage(bitmap);
                       edit_img.setVisibility(View.VISIBLE);
                        Uri tempUri = getImageUri(getApplicationContext(), bitmap);

                        // CALL THIS METHOD TO GET THE ACTUAL PATH
                        File finalFile = new File(getRealPathFromURI(tempUri));
                        String f = finalFile.getAbsolutePath();
                        f = f.substring(f.lastIndexOf("/")+1);
                        name=f;
                        Log.i("Path",f);
                        Log.d("image before service ",imagf);



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
            Log.d("String",""+Base64.encodeToString(byteArrayOS.toByteArray(), Base64.DEFAULT));
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










