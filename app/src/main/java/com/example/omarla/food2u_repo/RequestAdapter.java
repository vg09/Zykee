package com.example.omarla.food2u_repo;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.ArrayList;

/**
 * Created by admin on 4/19/2018.
 */

public class RequestAdapter extends BaseAdapter{

    Context context;
    ArrayList  list_ven_name,list_stall_name,list_loc,list_mobile,list_adhar_no,list_stall_id;

    LayoutInflater inflater;

    public RequestAdapter(Context context, ArrayList list_ven_name, ArrayList list_stall_name,ArrayList list_loc,ArrayList list_mobile,ArrayList list_stall_id){
        this.context=context;

        this.list_ven_name=list_ven_name;
        this.list_stall_name=list_stall_name;
        this.list_loc=list_loc;
        this.list_mobile=list_mobile;
        this.list_stall_id=list_stall_id;

        inflater=(LayoutInflater)context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }


    @Override
    public int getCount() {
        // Log.d("length of array",String.valueOf(arrayList_item.size()));
        int a= list_mobile.size();

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
        TextView venname;
        TextView mobile;
        TextView stalname;
        TextView location;
        TextView stall_id;

    }

    @Override
    public View getView(final int position, View view, ViewGroup viewGroup) {

        Holder holder = new Holder();
        View rowView;

        rowView = inflater.inflate(R.layout.vendor_reqadapter, null);
        holder.venname=(TextView) rowView.findViewById(R.id.r_v_name);
        holder.mobile=(TextView) rowView.findViewById(R.id.r_mobile_number);
        holder.stalname=(TextView)rowView.findViewById(R.id.r_stall_name);
        holder.location=(TextView)rowView.findViewById(R.id.r_location);
        holder.stall_id=(TextView)rowView.findViewById(R.id.r_stall_id);



        holder.venname.setText(list_ven_name.get(position).toString());
        holder.mobile.setText(list_mobile.get(position).toString());
        holder.stalname.setText(list_stall_name.get(position).toString());
        holder.location.setText(list_loc.get(position).toString());
        holder.stall_id.setText(list_stall_id.get(position).toString());


        return rowView;
    }








}
