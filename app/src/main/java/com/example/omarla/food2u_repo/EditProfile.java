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
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.text.InputFilter;
import android.util.Base64;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.util.HashMap;
import java.util.Map;

import de.hdodenhof.circleimageview.CircleImageView;


public class EditProfile extends AppCompatActivity {

    EditText user, mobile,mail;
    TextView type;
    String userText,mobileText;
    TextView edit_btn;
    Button save_btn,see_bussiness;
    AlertDialog.Builder builder;
    CircleImageView img;
    SharedGetterSetter sharedGetterSetter;
    String UserName,UserMail,UserType,UserMobile,UserId;
    String update_name,update_mobile;
    ProgressDialog progress;
    String name,imagf;
    Bitmap bitmap,bitmap1;
    String img_url;
    EditText business_count;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_profile);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);



        progress = new ProgressDialog(this);
        progress.setTitle("Please Wait");
        progress.setMessage("Loading.....");
        progress.setIndeterminate(true);


        business_count=(EditText)findViewById(R.id.B_number);
        user=(EditText)findViewById(R.id.username);
        mobile=(EditText)findViewById(R.id.m_number);
        mail=(EditText)findViewById(R.id.email);
        type=(TextView)findViewById(R.id.u_type);
        edit_btn=(TextView) findViewById(R.id.edit_details);
        save_btn=(Button)findViewById(R.id.btn_save);
        img=(CircleImageView)findViewById(R.id.profile_image);
        see_bussiness=(Button)findViewById(R.id.btn_business);
        mobile.setFilters(new InputFilter[] {new InputFilter.LengthFilter(10)});

        img.setImageResource(R.drawable.img);

        UserName=SharedGetterSetter.getUserName(getApplicationContext());
        UserMail=SharedGetterSetter.getUserMail(getApplicationContext());
        UserMobile=SharedGetterSetter.getUserMobile(getApplicationContext());
        UserType=SharedGetterSetter.getUserType(getApplicationContext());
        UserId=SharedGetterSetter.getUserId(getApplicationContext());
        img_url=SharedGetterSetter.getImgUrl(getApplicationContext());



        CallWebServiceEditProfilebusiness_number();
        decodeImage(img_url);

        Log.d("usr_id",UserId);

        user.setText(UserName);
        mobile.setText(UserMobile);
        mail.setText(UserMail);
        type.setText(UserType);

        //see bussiness button goto vendor dashboard
        see_bussiness.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
if(UserType.equalsIgnoreCase("vendor")){
                Intent i =new Intent(EditProfile.this,VendorDashboard.class);
                startActivity(i);}
                else{
    Toast.makeText(EditProfile.this, "we are working on the fucntionality", Toast.LENGTH_SHORT).show();
}
            }
        });






        edit_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                edit_btn.setVisibility(View.GONE);
                user.setEnabled(true);
                mobile.setEnabled(true);


            }
        });

        builder = new AlertDialog.Builder(EditProfile.this);
       save_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                userText=user.getText().toString();
                mobileText=mobile.getText().toString();

//                Log.d("user name",userText);
//                Log.d("mobile text",mobileText);

                if (user.length()<1 && mobile.length()<1) {
                    builder.setTitle("Something went wrong");
                    builder.setMessage("Please fill all the fields");
                    displayAlert("1");
                }
                else if(userText.equals(""))
                {
                    builder.setTitle("Username can not be Empty");
                    builder.setMessage("Please fill this field");
                    displayAlert("2");

                }
                else if(user.length()<3){
                    builder.setTitle("Invalid Username");
                    builder.setMessage("Username should be minimum 3 character ?");
                    displayAlert("3");
                }
                else if(mobile.length() != 10)
                {
                    builder.setTitle("Invalid Mobile Number");
                    builder.setMessage("Please, enter valid mobile number");
                    displayAlert("4");
                }
                else
                {

                    ValidateContact();
                }

            }
        });


        img.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                selectImage();
            }
        });



    }

    public boolean onOptionsItemSelected(MenuItem item)
    {
        Intent intent=new Intent(getApplicationContext(),CustomerDashboard.class);
        startActivityForResult(intent,0);
        return true;
    }

    public void displayAlert(final String code) {
        builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

                if (code.equals("0"))
                {

                }
                else if (code.equals("1"))
                {

                }
                else if (code.equals("2"))
                {


                }
                else if (code.equals("3"))
                {


                }
                else if (code.equals("4"))
                {


                }
            }
        });

        AlertDialog alertDialog = builder.create();
        alertDialog.show();
    }



    private void selectImage() {

        final CharSequence[] options = {"Take Photo", "Choose from Gallery", "Cancel"};
        android.app.AlertDialog.Builder builder = new android.app.AlertDialog.Builder(EditProfile.this);
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
                    img.setVisibility(View.VISIBLE);
                    Bundle extras = data.getExtras();
                    bitmap = (Bitmap) extras.get("data");
                    imagf=getStringImage(bitmap);
                    img.setImageBitmap(bitmap);




                    // Toast.makeText(this,String.valueOf(imageBitmap),Toast.LENGTH_LONG).show();
                    Uri tempUri = getImageUri(getApplicationContext(), bitmap);

                    // CALL THIS METHOD TO GET THE ACTUAL PATH
                    File finalFile = new File(getRealPathFromURI(tempUri));
                    String f = finalFile.getAbsolutePath();
                    f = f.substring(f.lastIndexOf("/") + 1);
                    name=f;
                    // i.setText(f);
                    Log.i("Path", f);

                   CallWebServiceEditProfilePic();




                }

                break;
            case 2:
                if(resultCode == RESULT_OK) {

                    //getting image from gallery
                    Uri selectedImage = data.getData();
                    try {

                        progress.show();
                        bitmap = MediaStore.Images.Media.getBitmap(getContentResolver(),selectedImage);
                        img.setImageBitmap(bitmap);
                        imagf=getStringImage(bitmap);
                        img.setVisibility(View.VISIBLE);
                        Uri tempUri = getImageUri(getApplicationContext(), bitmap);

                        // CALL THIS METHOD TO GET THE ACTUAL PATH
                        File finalFile = new File(getRealPathFromURI(tempUri));
                        String f = finalFile.getAbsolutePath();
                        f = f.substring(f.lastIndexOf("/")+1);
                        name=f;
                        Log.i("Path",f);
                        Log.d("image before service ",imagf);


                        CallWebServiceEditProfilePic();
                        //i.setText(f);
                    } catch (Exception e) {
                        Log.i("TAG", "Some exception " + e);
                    }

                }
                break;

        }
    }







    //validate mobile number which the user want to update
    public void ValidateContact(){
        progress.show();
        Log.d("mobile_number",mobileText);

        StringRequest stringRequest=new StringRequest(Request.Method.GET, URLs.ValidateContactURL1 + mobileText + URLs.ValidateContactURL2,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        Log.d("valid contact",response);
                        try {
                            JSONObject jsonObject=new JSONObject(response);
                            boolean valid=jsonObject.getBoolean("valid");

                            if(valid==true){
                                callwebeditprofilelistservice();
                               progress.dismiss();
                                // Toast.makeText(SignUp.this, ""+jsonObject.toString(), Toast.LENGTH_LONG).show();
                            }
                            else{

                                Toast.makeText(EditProfile.this, "Entered Contact no is wrong.", Toast.LENGTH_LONG).show();
                                progress.dismiss();
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();

                            Toast.makeText(EditProfile.this, ""+e.toString(), Toast.LENGTH_LONG).show();
                            progress.dismiss();
                        }
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                progress.dismiss();

                Toast.makeText(EditProfile.this, ""+error.toString(), Toast.LENGTH_LONG).show();
            }
        });
        MySingleton.getInstance(EditProfile.this).addToRequestque(stringRequest);
    }



    public void callwebeditprofilelistservice()
    {

        Log.d("name",userText);
        Log.d("mobile",mobileText);
        StringRequest stringRequest= new StringRequest(Request.Method.POST, URLs.EDIT_PROFILE,new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                SharedGetterSetter.setUserName(getApplicationContext(),userText);
                SharedGetterSetter.setUserMobile(getApplicationContext(),mobileText);

                progress.dismiss();

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(EditProfile.this, "Error Message"+error.getMessage(), Toast.LENGTH_SHORT).show();
                progress.dismiss();

            }
        }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<String, String>();
                params.put("user_id",UserId);
                params.put("name",userText );
                params.put("mobile_no",mobileText);

                return params;
            }

        };
        MySingleton.getInstance(EditProfile.this).addToRequestque(stringRequest);

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

//call service to edit the image............................
        public void CallWebServiceEditProfilePic(){

        Log.d("userid",UserId);
        Log.d("imageString",imagf);
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
                        Intent intent=new Intent(getApplicationContext(),EditProfile.class);
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
                    Toast.makeText(EditProfile.this, "Some error occurred -> "+volleyError, Toast.LENGTH_LONG).show();;
                }



            }) {
                //adding parameters to send
                @Override
                protected Map<String, String> getParams() throws AuthFailureError {
                    Map<String, String> params = new HashMap<String, String>();
                    params.put("user_id",UserId);
                    params.put("img_url",imagf);
                    return params;
                }
            };

            MySingleton.getInstance(EditProfile.this).addToRequestque(request);
        }

    public void decodeImage(String url){
    byte[] decodedString = Base64.decode(url, Base64.DEFAULT);
        Log.d("decoded",decodedString+"");
    bitmap1= BitmapFactory.decodeByteArray(decodedString, 0, decodedString.length);
        Log.d("image coming",bitmap1+"");
        img.setImageBitmap(bitmap1);
        img.setVisibility(View.VISIBLE);}


    public void onBackPressed ()
    {Intent startMain = new Intent(EditProfile.this,CustomerDashboard.class);
        startActivity(startMain);}


    //callweb service to count the number of the business
    public void CallWebServiceEditProfilebusiness_number(){

        progress.show();
        Log.d("userid",UserId);

        StringRequest request = new StringRequest(Request.Method.POST,  URLs.USER_BUSINESS_COUNT, new Response.Listener<String>(){
            @Override
            public void onResponse(String response) {


                try {

                    Log.d("try block","ok");
                    JSONObject jsonObject=new JSONObject(response);
                    business_count.setText(jsonObject.getString("number"));//set the count of the number of the business
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
                Toast.makeText(EditProfile.this, "Some error occurred -> "+volleyError, Toast.LENGTH_LONG).show();;
            }



        }) {
            //adding parameters to send
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<String, String>();
                params.put("user_id",UserId);

                return params;
            }
        };

        MySingleton.getInstance(EditProfile.this).addToRequestque(request);
    }

    public void change_password_activity(View view) {

        Intent i=new Intent(getApplicationContext(),Change_Password.class);
        startActivity(i);
    }
}
