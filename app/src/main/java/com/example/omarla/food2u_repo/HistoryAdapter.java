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
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;

/**
 * Created by hp on 4/9/2018.
 */

public class HistoryAdapter extends BaseAdapter {

    Context context;
    ArrayList arr_stall_name, arr_item_name, arr_item_cost, arr_total_cost, arr_date, arr_img_url,arr_list_quantity;

    Bitmap bitmap1;
    LayoutInflater inflater;

    public HistoryAdapter(Context context, ArrayList arr_stall_name, ArrayList arr_item_name,ArrayList arr_item_cost,ArrayList arr_total_cost,ArrayList arr_date,ArrayList arr_list_quantity,ArrayList arr_img_url){
        this.context=context;

        this.arr_stall_name=arr_stall_name;
        this.arr_item_name=arr_item_name;
        this.arr_item_cost=arr_item_cost;
        this.arr_total_cost=arr_total_cost;
        this.arr_date=arr_date;
        this.arr_list_quantity=arr_list_quantity;
        this.arr_img_url=arr_img_url;
        inflater=(LayoutInflater)context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }


    @Override
    public int getCount() {
       // Log.d("length of array",String.valueOf(arrayList_item.size()));
       int a= arr_stall_name.size();

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
        TextView stlN;
        TextView date;
        TextView ttlC;
        TextView ttquant;
        ImageView itmImg;
        Button save_feedback;
    }

    @Override
    public View getView(final int position, View view, final ViewGroup viewGroup) {

        Holder holder = new Holder();
        View rowView;

        rowView = inflater.inflate(R.layout.historylayout, null);
        holder.itmN=(TextView) rowView.findViewById(R.id.h_itemName);
        holder.itmC=(TextView) rowView.findViewById(R.id.h_itemCost);
        holder.stlN=(TextView)rowView.findViewById(R.id.h_stallName);
        holder.date=(TextView)rowView.findViewById(R.id.h_date);
        holder.ttlC=(TextView)rowView.findViewById(R.id.h_total_cost);
        holder.itmImg=(ImageView) rowView.findViewById(R.id.h_order_img);
        holder.ttquant=(TextView)rowView.findViewById(R.id.h_history_quant);

        holder.save_feedback=(Button)rowView.findViewById(R.id.save_feedback);


        holder.itmC.setText(arr_item_cost.get(position).toString());
        holder.stlN.setText(arr_stall_name.get(position).toString());
        holder.date.setText(arr_date.get(position).toString());
        holder.itmN.setText(arr_item_name.get(position).toString());

        holder.ttlC.setText(arr_total_cost.get(position).toString());
        holder.ttquant.setText(arr_list_quantity.get(position).toString());

//

        String imgage=arr_img_url.get(position).toString();

        //Log.d("image","hbhgvbvhgcgvgh"+arrayList_item_img_url.get(position).toString());
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
