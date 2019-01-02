package com.ariel.logi.logi;

import android.app.DatePickerDialog;
import android.app.Dialog;
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
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.ariel.DeliverySystem.Delivery;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;
import java.util.Calendar;

public class RecyclerViewDate extends RecyclerView.Adapter<RecyclerViewDate.ViewHolder>{

    private ArrayList<Delivery> mDelivery;
    protected static Context iContext;
    private DatabaseReference mDatabaseDeliveries;
    public static FirebaseAuth auth;

    public RecyclerViewDate(Context iContext, ArrayList<Delivery> mDelivery) {
        this.iContext = iContext;
        this.mDelivery = mDelivery;
        auth = FirebaseAuth.getInstance();
        mDatabaseDeliveries = FirebaseDatabase.getInstance().getReference("deliveries");
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
        final Calendar myCalendar = Calendar.getInstance();
        holder.datebtn.setOnClickListener(new View.OnClickListener() {
            DatePickerDialog.OnDateSetListener date = new DatePickerDialog.OnDateSetListener() {

                @Override
                public void onDateSet(DatePicker view, int year, int monthOfYear,
                                      int dayOfMonth) {
                    // TODO Auto-generated method stub
                    myCalendar.set(Calendar.YEAR, year);
                    myCalendar.set(Calendar.MONTH, monthOfYear);
                    myCalendar.set(Calendar.DAY_OF_MONTH, dayOfMonth);
                    mDatabaseDeliveries.child(mDelivery.get(position).getStorage_id()).child(mDelivery.get(position).getDelivery_id()).child("date").setValue(myCalendar.getTime().toString());
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
        holder.addrBtn.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub
                final Dialog d = new Dialog(RecyclerViewDate.this.iContext);
                d.setTitle("Set Address");
                d.setContentView(R.layout.textdialog);
                Button okBtn = (Button) d.findViewById(R.id.button1);
                Button cancelBtn = (Button) d.findViewById(R.id.button2);
                final EditText editText = (EditText)  d.findViewById(R.id.textPicker);
                okBtn.setOnClickListener(new View.OnClickListener()
                {
                    @Override
                    public void onClick(View v) {
                        mDatabaseDeliveries.child(mDelivery.get(position).getStorage_id()).child(mDelivery.get(position).getDelivery_id()).child("address").setValue(editText.getText().toString());
                        d.dismiss();
                    }
                });
                cancelBtn.setOnClickListener(new View.OnClickListener()
                {
                    @Override
                    public void onClick(View v) {
                        d.dismiss();
                    }
                });
                d.show();
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
        private ImageButton datebtn, addrBtn;
        public ViewHolder(View itemView){
            super(itemView);
            name = (TextView) itemView.findViewById(R.id.recycler_delivery_product_name);
            date = (TextView) itemView.findViewById(R.id.recycler_delivery_date);
            id = (TextView) itemView.findViewById(R.id.recycler_delivery_id);
            cour = (TextView) itemView.findViewById(R.id.recycler_courier_name);
            stat = (TextView) itemView.findViewById(R.id.recycler_staus);
            datebtn = (ImageButton) itemView.findViewById(R.id.recycler_setD_img);
            parentLayout = (CardView)itemView.findViewById(R.id.recycler_cust_del);
            addrBtn = (ImageButton)itemView.findViewById(R.id.recycler_setA_img);
        }

        @Override
        public void onClick(View v) {

        }
    }
}


