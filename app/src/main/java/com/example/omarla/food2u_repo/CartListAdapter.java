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
 * Created by admin on 4/3/2018.
 */

public class CartListAdapter extends BaseAdapter {

    Context context;
    private   ArrayList arrayList_item;
    private ArrayList arrayList_cost;
    private ArrayList arrayList_cost_total;
    private   ArrayList arrayList_stall;
    private ArrayList arrayList_img_url;
    private ArrayList arrayList_quantity;
    Bitmap bitmap1;
    private LayoutInflater inflater;


    CartListAdapter(Context context, ArrayList arrayList_item, ArrayList arrayList_cost, ArrayList arrayList_stall, ArrayList arrayList_img_url,ArrayList arrayList_cost_total,ArrayList arrayList_quantity){
        this.context=context;
        this.arrayList_item=arrayList_item;
        this.arrayList_cost=arrayList_cost;
        this.arrayList_cost_total=arrayList_cost_total;
        this.arrayList_stall=arrayList_stall;
        this.arrayList_img_url=arrayList_img_url;
        this.arrayList_quantity=arrayList_quantity;
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
        TextView stallN;
        TextView totalCost;
        TextView quantity;
        ImageView order_img;
    }

    @Override
    public View getView(final int position, View view, ViewGroup viewGroup) {

        Holder holder = new Holder();
        View rowView;

        rowView = inflater.inflate(R.layout.cart, null);
        holder.itmN=(TextView) rowView.findViewById(R.id.item_Name_cart);
        holder.itmC=(TextView) rowView.findViewById(R.id.item_Cost_cart);
        holder.stallN=(TextView)rowView.findViewById(R.id.stall_Name_cart);
        holder.order_img=(ImageView)rowView.findViewById(R.id.order_img_cart);
        holder.quantity=(TextView)rowView.findViewById(R.id.quantity_cart);
        holder.totalCost=(TextView)rowView.findViewById(R.id.total_cost);
        holder.itmN.setText(arrayList_item.get(position).toString());
        holder.itmC.setText(arrayList_cost.get(position).toString());
        holder.stallN.setText(arrayList_stall.get(position).toString());
        holder.quantity.setText(arrayList_quantity.get(position).toString());
        holder.totalCost.setText(arrayList_cost_total.get(position).toString());

        String imgage=arrayList_img_url.get(position).toString();

        Log.d("image","hbhgvbvhgcgvgh"+arrayList_img_url.get(position).toString());
        if(imgage.equals(""))
        {

            holder.order_img.setImageResource(R.drawable.snacks);
        }
        else{
            Log.d("imagestring coming ",imgage);
            Bitmap bitmap2=decodeImage(imgage);
            holder.order_img.setImageBitmap(bitmap2);
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
