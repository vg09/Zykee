package com.example.omarla.food2u_repo;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Base64;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;

/**
 * Created by hp on 3/19/2018.
 */

public class AddBusinessAdapter extends BaseAdapter {
    Context context;
    ArrayList arrayList_Business;
    ArrayList arrayList_Location;
    ArrayList arrayList_img_Url;
    Bitmap bitmap1;
    LayoutInflater inflater;

    public AddBusinessAdapter(Context context, ArrayList arrayList_business, ArrayList arrayList_Location, ArrayList arrayList_img_Url) {
        this.context = context;
        this.arrayList_Business = arrayList_business;
        this.arrayList_Location = arrayList_Location;
        this.arrayList_img_Url = arrayList_img_Url;
        Log.d("imgurllist",arrayList_img_Url+"");
        inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }


    @Override
    public int getCount() {
//        Log.d("length of array",String.valueOf(arrayList_Business.size()));
        return arrayList_Business.size();


    }

    @Override
    public Object getItem(int position) {
        return getItemId(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    public class Holder {
        TextView Bus_N;
        TextView Bus_l;
        ImageView Bus_img;
    }

    @Override
    public View getView(final int position, View view, ViewGroup viewGroup) {

        AddBusinessAdapter.Holder holder = new AddBusinessAdapter.Holder();
        View rowView;

        rowView = inflater.inflate(R.layout.bus_list, null);
        holder.Bus_N = (TextView) rowView.findViewById(R.id.bus_name);
        holder.Bus_l = (TextView) rowView.findViewById(R.id.bus_location);
        holder.Bus_img = (ImageView) rowView.findViewById(R.id.img_bus);
        holder.Bus_N.setText(arrayList_Business.get(position).toString());
        holder.Bus_l.setText(arrayList_Location.get(position).toString());

       String imgage=arrayList_img_Url.get(position).toString();

       Log.d("image","hbhgvbvhgcgvgh"+arrayList_img_Url.get(position).toString());
        if(imgage.equals(""))
                        {

                            holder.Bus_img.setImageResource(R.drawable.snacks);
                        }
                 else{
            Log.d("imagestring coming ",imgage);
                  Bitmap bitmap2=decodeImage(imgage);
                  holder.Bus_img.setImageBitmap(bitmap2);
        }

        return rowView;
    }

    public Bitmap decodeImage(String url) {
        byte[] decodedString = Base64.decode(url, Base64.DEFAULT);
        Log.d("decoded", decodedString + "");
        bitmap1 = BitmapFactory.decodeByteArray(decodedString, 0, decodedString.length);
        Log.d("image coming", bitmap1 + "");
        return bitmap1;
    }
}