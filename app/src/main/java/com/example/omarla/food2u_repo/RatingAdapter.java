package com.example.omarla.food2u_repo;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.RatingBar;
import android.widget.TextView;

import java.util.ArrayList;

/**
 * Created by hp on 5/5/2018.
 */

public class RatingAdapter extends BaseAdapter {

    Context context;
    ArrayList arrayList_stall_name;
    ArrayList arrayList_location;
    ArrayList arrayList_rating;

    LayoutInflater inflater;

    public RatingAdapter(Context context, ArrayList arrayList_stall_name, ArrayList arrayList_location,ArrayList arrayList_rating ){
        this.context=context;
        this.arrayList_stall_name=arrayList_stall_name;
        this.arrayList_location=arrayList_location;
        this.arrayList_rating=arrayList_rating;
        inflater=(LayoutInflater)context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }


    @Override
    public int getCount() {
        Log.d("length of array",String.valueOf(arrayList_stall_name.size()));
        int a= arrayList_stall_name.size();

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
        TextView stall;
        TextView location;
        RatingBar ratingBar;
    }

    @Override
    public View getView(final int position, View view, ViewGroup viewGroup) {

        Holder holder = new Holder();
        View rowView;

        rowView = inflater.inflate(R.layout.stallsratingadapter, null);
        holder.stall=(TextView) rowView.findViewById(R.id.r_stall_name);
        holder.location=(TextView) rowView.findViewById(R.id.r_location);
        holder.ratingBar=(RatingBar) rowView.findViewById(R.id.r_Rating);

        holder.stall.setText(arrayList_stall_name.get(position).toString());
        holder.location.setText(arrayList_location.get(position).toString());
        holder.ratingBar.setRating(Float.parseFloat(arrayList_rating.get(position).toString()));



        return rowView;
    }






}
