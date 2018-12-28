package com.ariel.logi.logi;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;


import com.ariel.DeliverySystem.Delivery;

import java.util.ArrayList;

public class RecyclerViewCourier extends RecyclerView.Adapter<RecyclerViewCourier.ViewHolder> {

    private ArrayList<Delivery> mContent;
    private Context context;

    public RecyclerViewCourier(Context context, ArrayList<Delivery> mContent) {
        this.mContent = mContent;
        this.context = context;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.recycler_view_item_courier, parent, false);
        return  new RecyclerViewCourier.ViewHolder(view);
    }


    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
       // holder.content.setText(mContent.get(position));
    }

    @Override
    public int getItemCount() {
        return mContent.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder{

        private TextView label;
        private TextView content;
        RelativeLayout relativeLayout;
        public ViewHolder(View itemView) {
            super(itemView);
            label = (TextView) itemView.findViewById(R.id.label_item);
            content = (TextView) itemView.findViewById(R.id.content_item);
            relativeLayout = (RelativeLayout) itemView.findViewById(R.id.parent_recycler_courier);
        }
    }
}
