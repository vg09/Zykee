package com.example.omarla.food2u_repo;

/**
 * Created by Om Arla on 3/5/2018.
 */
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


public class myAdapter extends BaseAdapter{
    Context context;
    ArrayList arrayList_item;
    ArrayList arrayList_cost;
    ArrayList arrayList_item_img_url;
    Bitmap bitmap1;
    LayoutInflater inflater;

    public myAdapter(Context context, ArrayList arrayList_item, ArrayList arrayList_cost,ArrayList arrayList_item_img_url){
        this.context=context;
        this.arrayList_item=arrayList_item;
        this.arrayList_cost=arrayList_cost;
        this.arrayList_item_img_url=arrayList_item_img_url;
        inflater=(LayoutInflater)context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }


    @Override
    public int getCount() {
        Log.d("length of array",String.valueOf(arrayList_item.size()));
        int a= arrayList_item.size();

        return a;
    }

    @Override
    public Object getItem(int position) {
        return getItemId(position) ;
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    public class Holder
    {
        TextView itmN;
        TextView itmC;
        ImageView itmImg;
    }

    @Override
    public View getView(final int position, View view, ViewGroup viewGroup) {

        Holder holder = new Holder();
        View rowView;

        rowView = inflater.inflate(R.layout.itemlist, null);
        holder.itmN=(TextView) rowView.findViewById(R.id.itemName);
        holder.itmC=(TextView) rowView.findViewById(R.id.itemCost);
        holder.itmImg=(ImageView)rowView.findViewById(R.id.item_img_item_list);
        holder.itmN.setText(arrayList_item.get(position).toString());
        holder.itmC.setText(arrayList_cost.get(position).toString());


        String imgage=arrayList_item_img_url.get(position).toString();

        Log.d("image","hbhgvbvhgcgvgh"+arrayList_item_img_url.get(position).toString());
        if(imgage.equals(""))
        {

            holder.itmImg.setImageResource(R.drawable.snacks);
        }
        else{
            Log.d("imagestring coming ",imgage);
            Bitmap bitmap2=decodeImage(imgage);
            holder.itmImg.setImageBitmap(bitmap2);
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