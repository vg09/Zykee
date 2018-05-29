package com.example.omarla.food2u_repo;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.ArrayList;

/**
 * Created by hp on 4/26/2018.
 */

public class Paymentadapter extends BaseAdapter {


    Context context;
    ArrayList pay_date,vendor_mobile,day_amt,vendor_name,user_id_list;

    LayoutInflater inflater;

    public Paymentadapter(Context context, ArrayList pay_date, ArrayList vendor_mobile,ArrayList day_amt,ArrayList vendor_name,ArrayList user_id_list){
        this.context=context;

        this.pay_date=pay_date;
        this.vendor_mobile=vendor_mobile;
        this.day_amt=day_amt;
        this.vendor_name=vendor_name;
        this.user_id_list=user_id_list;

        inflater=(LayoutInflater)context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }


    @Override
    public int getCount() {
        // Log.d("length of array",String.valueOf(arrayList_item.size()));
        int a= pay_date.size();

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
        TextView user_id;
        TextView venname;
        TextView mobile;
        TextView date;
        TextView daily_amt;


    }

    @Override
    public View getView(final int position, View view, ViewGroup viewGroup) {

        Holder holder = new Holder();
        View rowView;

        rowView = inflater.inflate(R.layout.paymentadapter, null);
        holder.user_id=(TextView)rowView.findViewById(R.id.p_user_id);
        holder.venname=(TextView) rowView.findViewById(R.id.p_name);
        holder.mobile=(TextView) rowView.findViewById(R.id.p_mobile_number);
        holder.daily_amt=(TextView)rowView.findViewById(R.id.p_amount);
        holder.date=(TextView)rowView.findViewById(R.id.p_date);


        holder.venname.setText(vendor_name.get(position).toString());
        holder.user_id.setText(user_id_list.get(position).toString());
        holder.mobile.setText(vendor_mobile.get(position).toString());
        holder.daily_amt.setText(day_amt.get(position).toString());
        holder.date.setText(pay_date.get(position).toString());


        return rowView;
    }









}
