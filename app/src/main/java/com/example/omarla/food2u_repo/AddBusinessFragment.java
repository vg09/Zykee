package com.example.omarla.food2u_repo;

import android.app.Fragment;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Base64;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
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

import static android.app.Activity.RESULT_OK;


/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link AddBusinessFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link AddBusinessFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class AddBusinessFragment extends Fragment{

    EditText ed_busName;
    SharedPreferences myprefs;
    Spinner spin_loc;
    Button btn_addBus;
    String bus_name,loc;
    ImageView addBusImage;
    Bitmap bitmap;
    String imagf,name;
    static String error="0";
    Context ctx;
    Button close;


    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;
    ProgressDialog progress;
    Context this_context;






    private OnFragmentInteractionListener mListener;

    public AddBusinessFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment AddBusinessFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static AddBusinessFragment newInstance(String param1, String param2) {
        AddBusinessFragment fragment = new AddBusinessFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);


        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
       imagf=SharedGetterSetter.getImgUrl(getActivity());

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        LayoutInflater lf = getActivity().getLayoutInflater();

        View view =lf.inflate(R.layout.fragment_add_business, container, false);
        ed_busName=(EditText)view.findViewById(R.id.bus_name);
        spin_loc=(Spinner)view.findViewById(R.id.spin_loc2);
        btn_addBus=(Button)view.findViewById(R.id.next);
        close=(Button) view.findViewById(R.id.close);



        addBusImage=(ImageView)view.findViewById(R.id.addBusImage);
        this_context=container.getContext();

        myprefs = this.getActivity().getSharedPreferences("session_id", Context.MODE_WORLD_WRITEABLE);
        final int vendor_id= Integer.parseInt(myprefs.getString("session_id", null));
        Log.d("vendor_id",String.valueOf(vendor_id));


        progress = new ProgressDialog(this_context);
        progress.setTitle("Please Wait");
        progress.setMessage("Loading.....");
        progress.setIndeterminate(true);


        close.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent=new Intent(getActivity(),VendorDashboard.class);
                startActivity(intent);
            }
        });

        spin_loc.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

                loc=parent.getItemAtPosition(position).toString();
                Toast.makeText(getActivity().getApplicationContext(), "Toat value"+loc ,Toast.LENGTH_SHORT).show();

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

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

                Toast.makeText(this_context, "please select the location ", Toast.LENGTH_SHORT).show();
            }
        });

        addBusImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                selectImage();
            }
        });

        btn_addBus.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                progress.show();
                 bus_name=ed_busName.getText().toString();
                 if(bus_name.equals(""))
                 {
                     Toast.makeText(getActivity(), "abey ooo naam kon dega", Toast.LENGTH_SHORT).show();
                 }


                //Response on business...............
                StringRequest stringRequest=new StringRequest(Request.Method.POST, URLs.ADD_STALL, new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        Log.d("Stall_master",response);

                        try {
                            Log.d("try","try");
                           JSONObject jsonObject = new JSONObject(response);
                           Log.d("json",jsonObject.toString());
                            error=jsonObject.getString("error");


                            if(error=="1")
                            {
                                Toast.makeText(this_context, "Stall Already exist. PLease refresh to see the business", Toast.LENGTH_LONG).show();
                              //  startActivity(new Intent(getActivity(),AddItem.class));
                                getActivity().getFragmentManager().popBackStack();
                            }
                            else if(error=="0"){

                                getActivity().getFragmentManager().popBackStack();
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }




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
                        Params.put("stall_name",bus_name);
                        Params.put("location",loc);
                        Params.put("img_url",imagf);
                        Params.put("user_id",String.valueOf(vendor_id));

                        return Params;
                    }



                };
                MySingleton.getInstance(this_context).addToRequestque(stringRequest);


                 Intent intent=new Intent(getActivity(),VendorDashboard.class);
                 startActivity(intent);


        }
        });


        return view;
    }

    // TODO: Rename method, update argument and hook method into UI event
    public void onButtonPressed(Uri uri) {
        if (mListener != null) {
            mListener.onFragmentInteraction(uri);
        }
    }
    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnFragmentInteractionListener) {
            mListener = (OnFragmentInteractionListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnFragmentInteractionListener");
        }
    }
    private void selectImage() {

        final CharSequence[] options = {"Take Photo", "Choose from Gallery", "Cancel"};
        android.app.AlertDialog.Builder builder = new android.app.AlertDialog.Builder(getActivity());
        builder.setTitle("Add Photo!");
        builder.setItems(options, new DialogInterface.OnClickListener() {

            @Override

            public void onClick(DialogInterface dialog, int item) {

                if (options[item].equals("Take Photo"))

                {
                    Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);

                    if (intent.resolveActivity(getActivity().getPackageManager()) != null) {
                        startActivityForResult(intent, 1);
                    }

                } else

                    if (options[item].equals("Choose from Gallery"))

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
                    addBusImage.setVisibility(View.VISIBLE);
                    Bundle extras = data.getExtras();
                    bitmap = (Bitmap) extras.get("data");
                    imagf=getStringImage(bitmap);
                    addBusImage.setImageBitmap(bitmap);





                    Uri tempUri = getImageUri(getActivity(), bitmap);

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


                        bitmap = MediaStore.Images.Media.getBitmap(getActivity().getContentResolver(),selectedImage);
                        addBusImage.setImageBitmap(bitmap);
                        imagf=getStringImage(bitmap);
                        addBusImage.setVisibility(View.VISIBLE);
                        Uri tempUri = getImageUri(getActivity(), bitmap);

                        // CALL THIS METHOD TO GET THE ACTUAL PATH
                        File finalFile = new File(getRealPathFromURI(tempUri));
                        String f = finalFile.getAbsolutePath();
                        f = f.substring(f.lastIndexOf("/")+1);
                        name=f;
                        Log.i("Path",f);
                        Log.d("image before service ",imagf);


                       // CallWebServiceEditProfilePic();
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
        Cursor cursor = getActivity().getContentResolver().query(uri, null, null, null, null);
        cursor.moveToFirst();
        int idx = cursor.getColumnIndex(MediaStore.Images.ImageColumns.DATA);
        return cursor.getString(idx);
    }


    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    /**
     * This interface must be implemented by activities that contain this
     * fragment to allow an interaction in this fragment to be communicated
     * to the activity and potentially other fragments contained in that
     * activity.
     * <p>
     * See the Android Training lesson <a href=
     * "http://developer.android.com/training/basics/fragments/communicating.html"
     * >Communicating with Other Fragments</a> for more information.
     */
    public interface OnFragmentInteractionListener {
        // TODO: Update argument type and name
        void onFragmentInteraction(Uri uri);


    }



}
