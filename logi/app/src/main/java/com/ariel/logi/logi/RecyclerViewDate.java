package com.ariel.logi.logi;

import android.app.DatePickerDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.support.annotation.NonNull;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.ImageButton;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.ariel.DeliverySystem.Delivery;

import java.util.ArrayList;
import java.util.Calendar;

public class RecyclerViewDate extends RecyclerView.Adapter<RecyclerViewDate.ViewHolder>{

//    private ArrayList<String> dNames;
//    private ArrayList<String> dDates;
//    private ArrayList<String> dDelID;
//    private ArrayList<String> dCour;
//    private ArrayList<String> dStat;
    private ArrayList<Delivery> mDelivery;
    private ImageButton btnDate;
    protected static Context iContext;
    private DatePicker datePicker;
    private String date;

    public RecyclerViewDate(Context iContext, ArrayList<Delivery> mDelivery, ImageButton btnDate) {
//        this.dNames = dNames;
//        this.dDates = dDates;
//        this.dStat = dStat;
//        this.dDelID = dDelID;
//        this.dCour = dCour;
        this.btnDate = btnDate;
        this.iContext = iContext;
        this.mDelivery = mDelivery;
    }
    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.layout_listdate, parent, false);
        ViewHolder holder = new ViewHolder(view);
        return holder;
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, final int position) {
        holder.date.setText(mDelivery.get(position).getDate());
        holder.name.setText(mDelivery.get(position).getProduct_id());
        holder.stat.setText(mDelivery.get(position).getStatus());
        holder.id.setText(mDelivery.get(position).getDelivery_id());
        holder.cour.setText(mDelivery.get(position).getCourier_email());
       holder.datebtn.setOnClickListener(new View.OnClickListener() {
            final Calendar myCalendar = Calendar.getInstance();
            DatePickerDialog.OnDateSetListener date = new DatePickerDialog.OnDateSetListener() {

                @Override
                public void onDateSet(DatePicker view, int year, int monthOfYear,
                                      int dayOfMonth) {
                    // TODO Auto-generated method stub
                    myCalendar.set(Calendar.YEAR, year);
                    myCalendar.set(Calendar.MONTH, monthOfYear);
                    myCalendar.set(Calendar.DAY_OF_MONTH, dayOfMonth);
                    mDelivery.get(position).setDate(myCalendar.getTime().toString());
                }

            };
            @Override
            public void onClick(View v) {
                    // TODO Auto-generated method stub
                    new DatePickerDialog(RecyclerViewDate.this.iContext, date, myCalendar
                            .get(Calendar.YEAR), myCalendar.get(Calendar.MONTH),
                            myCalendar.get(Calendar.DAY_OF_MONTH)).show();
            }
        });
    }

    @Override
    public int getItemCount() {
        return mDelivery.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{
        CardView parentLayout;
        private TextView name, date, id, cour, stat;
        private ImageButton datebtn;
        public ViewHolder(View itemView){
            super(itemView);
            name = (TextView) itemView.findViewById(R.id.recycler_delivery_product_name);
            date = (TextView) itemView.findViewById(R.id.recycler_delivery_date);
            id = (TextView) itemView.findViewById(R.id.recycler_delivery_id);
            cour = (TextView) itemView.findViewById(R.id.recycler_courier_name);
            stat = (TextView) itemView.findViewById(R.id.recycler_staus);
            datebtn = (ImageButton) itemView.findViewById(R.id.recycler_setD_img);
            parentLayout = (CardView)itemView.findViewById(R.id.recycler_cust_del);
        }

        @Override
        public void onClick(View v) {

        }
    }
}


