package com.example.omarla.food2u_repo;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Base64;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.paytm.pgsdk.Log;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.util.HashMap;
import java.util.Map;

import de.hdodenhof.circleimageview.CircleImageView;

public class AdminDashboard extends AppCompatActivity {
    String U_name,E_mail,Img_url,ImAGE,UserId,name;
    String pending_request;
    TextView admin_name,admin_mail,req_count,payment_count;
    CircleImageView admin_img;
    Bitmap bitmap1,bitmap;
    ProgressDialog progress;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_dashboard);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        if (getSupportActionBar() != null){
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setDisplayShowHomeEnabled(true);
        }


        progress = new ProgressDialog(this);
        progress.setTitle("Please Wait");
        progress.setMessage("Loading.....");
        progress.setIndeterminate(true);



        admin_img=(CircleImageView)findViewById(R.id.admin_image);
        admin_mail=(TextView)findViewById(R.id.a_mail);
        admin_name=(TextView)findViewById(R.id.a_name);
        req_count=(TextView)findViewById(R.id.request_count);
        payment_count=(TextView)findViewById(R.id.payment_count);

        U_name= SharedGetterSetter.getUserName(getApplicationContext());
        E_mail=SharedGetterSetter.getUserMail(getApplicationContext());
        Img_url=SharedGetterSetter.getImgUrl(getApplicationContext());
        UserId=SharedGetterSetter.getUserId(getApplicationContext());


        admin_name.setText(U_name);
        admin_mail.setText(E_mail);
        decodeImage(Img_url);

        admin_img.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                selectImage();
            }
        });

        callcountrequest("0");


    }

    public void confirmstall_show(View view) {
        Intent intent=new Intent(getApplicationContext(),ConfirmStallsInfo.class);
        startActivity(intent);
    }

    public void show(View view) {
        Intent intent=new Intent(getApplicationContext(),RequestedVendor.class);
        startActivity(intent);

    }
    public void show_paid(View view) {
        Intent intent=new Intent(getApplicationContext(),admin_Confirmed_payments.class);
        startActivity(intent);

    }

    private void selectImage() {

        final CharSequence[] options = {"Take Photo", "Choose from Gallery", "Cancel"};
        android.app.AlertDialog.Builder builder = new android.app.AlertDialog.Builder(AdminDashboard.this);
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

    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        switch(requestCode) {
            case 1:
                if (resultCode == RESULT_OK) {
                    admin_img.setVisibility(View.VISIBLE);
                    Bundle extras = data.getExtras();
                    bitmap = (Bitmap) extras.get("data");
                    ImAGE=getStringImage(bitmap);
                    admin_img.setImageBitmap(bitmap);




                    // Toast.makeText(this,String.valueOf(imageBitmap),Toast.LENGTH_LONG).show();
                    Uri tempUri = getImageUri(getApplicationContext(), bitmap);

                    // CALL THIS METHOD TO GET THE ACTUAL PATH
                    File finalFile = new File(getRealPathFromURI(tempUri));
                    String f = finalFile.getAbsolutePath();
                    f = f.substring(f.lastIndexOf("/") + 1);
                    name=f;
                    // i.setText(f);
                    Log.i("Path", f);

                    //   CallWebServiceEditProfilePic();




                }

                break;
            case 2:
                if(resultCode == RESULT_OK) {

                    //getting image from gallery
                    Uri selectedImage = data.getData();
                    try {

                        progress.show();
                        bitmap = MediaStore.Images.Media.getBitmap(getContentResolver(),selectedImage);
                        admin_img.setImageBitmap(bitmap);
                        ImAGE=getStringImage(bitmap);
                        admin_img.setVisibility(View.VISIBLE);
                        Uri tempUri = getImageUri(getApplicationContext(), bitmap);

                        // CALL THIS METHOD TO GET THE ACTUAL PATH
                        File finalFile = new File(getRealPathFromURI(tempUri));
                        String f = finalFile.getAbsolutePath();
                        f = f.substring(f.lastIndexOf("/")+1);
                        name=f;
                        Log.i("Path",f);
                        // Log.d("image before service ",imagf);


                        CallWebServiceEditProfilePic();
                        //i.setText(f);
                    } catch (Exception e) {
                        Log.i("TAG", "Some exception " + e);
                    }

                }
                break;

        }
    }


    public void CallWebServiceEditProfilePic(){

        Log.d("userid",UserId);
        Log.d("imageString",ImAGE);
        StringRequest request = new StringRequest(Request.Method.POST,  URLs.EDIT_PROFILE_PIC, new Response.Listener<String>(){
            @Override
            public void onResponse(String response) {


                try {

                    Log.d("try block","ok");
                    JSONObject jsonObject=new JSONObject(response);
                    String imgurl=jsonObject.getString("img_url");
                    decodeImage(imgurl);
                    SharedGetterSetter.setImgUrl(getApplicationContext(),imgurl);
                    Log.d("imgurl",imgurl);
                    Intent intent=new Intent(getApplicationContext(),AdminDashboard.class);
                    startActivity(intent);
                    progress.dismiss();
                } catch (JSONException e) {
                    e.printStackTrace();
                    progress.dismiss();
                }
//                    progress.dismiss();
//                    Log.d("Response",response.toString());
            }
        },new Response.ErrorListener(){
            @Override
            public void onErrorResponse(VolleyError volleyError) {
                progress.dismiss();
                Toast.makeText(AdminDashboard.this, "Some error occurred -> "+volleyError, Toast.LENGTH_LONG).show();;
            }



        }) {
            //adding parameters to send
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<String, String>();
                params.put("user_id",UserId);
                params.put("img_url",ImAGE);
                return params;
            }
        };

        MySingleton.getInstance(AdminDashboard.this).addToRequestque(request);
    }






    public void decodeImage(String url){
        byte[] decodedString = Base64.decode(url, Base64.DEFAULT);
        Log.d("decoded",decodedString+"");
        bitmap1= BitmapFactory.decodeByteArray(decodedString, 0, decodedString.length);
        Log.d("image coming",bitmap1+"");
        admin_img.setImageBitmap(bitmap1);
        admin_img.setVisibility(View.VISIBLE);}




    //WEB SERVICE TO COUNT REQUEST PENDING AND PENDING PAYMENT
    public void   callcountrequest(final String request)
    {
        progress.show();

        StringRequest stringRequest= new StringRequest(Request.Method.POST, URLs.REQUEST_COUNT,new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                Log.d("response count",response.toString());

                try {
                    JSONObject jsonObject=new JSONObject(response);
                    pending_request=jsonObject.getString("number");
                    Log.d("pending",pending_request);
                    req_count.setText(pending_request);
                } catch (JSONException e) {
                    e.printStackTrace();
                }


                progress.dismiss();



            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(getApplicationContext(), "Error Message "+error.getMessage(), Toast.LENGTH_SHORT).show();
                progress.dismiss();

            }
        }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<String, String>();
                params.put("request_stall",request);


                return params;
            }

        };
        MySingleton.getInstance(AdminDashboard.this).addToRequestque(stringRequest);


    }


    public void peding_pay_method(View view) {

        Intent intent=new Intent(getApplicationContext(),Admin_payments.class);
        startActivity(intent);
    }


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

    public void onBackPressed()
    {Intent startMain = new Intent(AdminDashboard.this,CustomerDashboard.class);
        startActivity(startMain);}


}
