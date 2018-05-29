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
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
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
 * {@link OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link AddNewItemFrag#newInstance} factory method to
 * create an instance of this fragment.
 */
public class AddNewItemFrag extends Fragment {

    Context this_frag_context;
    EditText item_name;
    EditText item_price;
    EditText item_descripton;
    ImageView addItemImage;
    SharedPreferences myprefs1;
    Button   add_new_item;
   String name,imagf;
   Bitmap bitmap;
    String itemname,itemcost,itemdescription,Stall_Id;



    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    private OnFragmentInteractionListener mListener;

    public AddNewItemFrag() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment AddNewItemFrag.
     */
    // TODO: Rename and change types and number of parameters
    public static AddNewItemFrag newInstance(String param1, String param2) {
        AddNewItemFrag fragment = new AddNewItemFrag();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
//        Bundle bundle=getArguments();
//         Stall_Id=bundle.getString("id");
//        Log.d("Stall ID By BUndle",Stall_Id);
//        if (getArguments() != null) {
//            Stall_Id = getArguments().getString("id");
//
//            Log.d("stall_id",Stall_Id);
//        }
//        else{
//            Toast.makeText(this_frag_context, "there is no value in arguments ", Toast.LENGTH_SHORT).show();
//        }


        Stall_Id=SharedGetterSetter.getStall_id(getActivity());
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        LayoutInflater lf = getActivity().getLayoutInflater();
        View view =lf.inflate(R.layout.fragment_add_new_item, container, false);

        addItemImage=(ImageView) view.findViewById(R.id.addItemImage);
        item_name=(EditText) view.findViewById(R.id.fragedt_itemName);
        item_price=(EditText) view.findViewById(R.id.fragedt_itemCost);
        item_descripton=(EditText) view.findViewById(R.id.frag_edit_description);
        add_new_item=(Button) view.findViewById(R.id.frag_item_save_btn);
        this_frag_context=container.getContext();
      final ProgressDialog  progress = new ProgressDialog(this_frag_context);
        progress.setTitle("Please Wait");
        progress.setMessage("Loading.....");
        progress.setIndeterminate(true);

        imagf=SharedGetterSetter.getImgUrl(getActivity());
        addItemImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                selectImage();
            }
        });



    add_new_item.setOnClickListener(new View.OnClickListener() {

        @Override
        public void onClick(View v) {
            progress.show();
            itemname=item_name.getText().toString();
            itemcost=item_price.getText().toString();
            itemdescription=item_descripton.getText().toString();
            Log.d("imagefuck",imagf);
            StringRequest stringRequest=new StringRequest(Request.Method.POST, URLs.ADD_ITEM, new Response.Listener<String>() {
                @Override
                public void onResponse(String response) {
                    Log.d("item_master",response);

                    try {
                        Log.d("try","try");
                        JSONObject jsonObject = new JSONObject(response);
                        Log.d("json",jsonObject.toString());
                        String   error=jsonObject.getString("error");
                        Log.d("error value",error);


                        if(error=="1")
                        {
                            Toast.makeText(this_frag_context, "Item Already exist", Toast.LENGTH_LONG).show();
                            //startActivity(new Intent(getActivity(),AddItem.class));
                        }
                       else
                        {
                            Toast.makeText(this_frag_context, "Your Item Is saved ", Toast.LENGTH_SHORT).show();
                            item_name.setText(" ");
                            item_price.setText(" ");
                            item_descripton.setText(" ");


                        }

                    } catch (JSONException e) {
                        e.printStackTrace();
                    }




                    progress.dismiss();
                }


            }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
                    Log.d("volley error ",error.toString());
                    progress.dismiss();

                }
            })
            {
                protected Map<String,String> getParams() throws AuthFailureError
                {
                    Map<String,String> Params=new HashMap<String,String>();
                    Params.put("item_name",itemname);
                    Params.put("cost",itemcost);
                    Params.put("description",itemdescription);
                    Params.put("stall_id",Stall_Id);
                    Params.put("img_url",imagf);

                    return Params;
                }



            };
            MySingleton.getInstance(this_frag_context).addToRequestque(stringRequest);


Intent intent=new Intent(getActivity(),AddItem.class);
startActivity(intent);

        }
    });



        // Inflate the layout for this fragment
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
                    addItemImage.setVisibility(View.VISIBLE);
                    Bundle extras = data.getExtras();
                    bitmap = (Bitmap) extras.get("data");
                    imagf=getStringImage(bitmap);
                    addItemImage.setImageBitmap(bitmap);





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
                        addItemImage.setImageBitmap(bitmap);
                        imagf=getStringImage(bitmap);
                        addItemImage.setVisibility(View.VISIBLE);
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
