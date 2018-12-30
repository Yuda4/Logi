package com.ariel.logi.logi;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.ariel.User.Courier;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Set;

public class RecyclerViewManagerCourier extends RecyclerView.Adapter<RecyclerViewManagerCourier.ViewHolder> {
    private static final int REQUEST_CALL = 1;

    private ArrayList<Courier> mCourier;
    private Set<Courier> mCourierIsFull;
    private Context context;

    public RecyclerViewManagerCourier(Context context, ArrayList<Courier> mCourier) {
        this.mCourier = mCourier;
        this.context = context;
        this.mCourierIsFull = new HashSet<>(mCourier);
    }

    @NonNull
    @Override
    public RecyclerViewManagerCourier.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.recycler_view_manager_courier, parent, false);
        return new RecyclerViewManagerCourier.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerViewManagerCourier.ViewHolder holder, final int position) {
        holder.name.setText(mCourier.get(position).getName());
        holder.phone = mCourier.get(position).getPhone();
        holder.textViewNothing.setVisibility(View.GONE);
        if (getItemCount() == 0) holder.textViewNothing.setVisibility(View.VISIBLE);
        holder.imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(!mCourier.get(position).getPhone().isEmpty() && ContextCompat.checkSelfPermission(context,
                        Manifest.permission.CALL_PHONE) != PackageManager.PERMISSION_GRANTED){
                    ActivityCompat.requestPermissions((Activity) context,new String[] {Manifest.permission.CALL_PHONE}, REQUEST_CALL);
                }else{
                    String dial = "tel:" + mCourier.get(position).getPhone().trim();
                    context.startActivity(new Intent(Intent.ACTION_CALL, Uri.parse(dial)));
                }
            }
        });
    }

    @Override
    public int getItemCount() {
        return mCourier.size();
    }

    public void filterCourierName (String charText) {
        mCourierIsFull.addAll(mCourier);
        mCourier.clear();
        if(charText == null || charText.length() == 0){
            mCourier.addAll(mCourierIsFull);
        }else{
            String filterPattern = charText.toLowerCase().trim();
            for(Courier courier : mCourierIsFull){
                if(courier.getName().toLowerCase().contains(filterPattern)){
                    mCourier.add(courier);
                }
            }
        }
        notifyDataSetChanged();
    }

    public class ViewHolder extends RecyclerView.ViewHolder{

        private TextView name;
        private ImageView imageView;
        private CardView cardView;
        private RelativeLayout linearLayout;
        private String phone;
        private TextView textViewNothing;

        public ViewHolder(View itemView) {
            super(itemView);
            name = (TextView) itemView.findViewById(R.id.recycler_courier_manager_name_item);
            cardView = (CardView) itemView.findViewById(R.id.recycler_courier_manager_parent_card_view);
            linearLayout = (RelativeLayout) itemView.findViewById(R.id.recycler_courier_manager_child_linear_layout);
            imageView = (ImageView) itemView.findViewById(R.id.call_icon_card_view);
            phone = "";
            textViewNothing = (TextView) itemView.findViewById(R.id.no_item_to_show_courier);
            }
    }
}


