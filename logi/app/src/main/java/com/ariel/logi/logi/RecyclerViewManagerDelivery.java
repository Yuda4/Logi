package com.ariel.logi.logi;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.CardView;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.ariel.DeliverySystem.Delivery;

import java.util.ArrayList;

public class RecyclerViewManagerDelivery extends RecyclerView.Adapter<RecyclerViewManagerDelivery.ViewHolder> {
    private ArrayList<Delivery> mDelivery;
    private Context context;

    public RecyclerViewManagerDelivery(Context context, ArrayList<Delivery> mDelivery) {
        this.mDelivery = mDelivery;
        this.context = context;
    }

    @NonNull
    @Override
    public RecyclerViewManagerDelivery.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.recycler_view_manager_pending, parent, false);
        return new RecyclerViewManagerDelivery.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerViewManagerDelivery.ViewHolder holder, int position) {
        holder.name.setText(mDelivery.get(position).getProductName());
        holder.id.setText(mDelivery.get(position).getDeliveryId());
    }

    @Override
    public int getItemCount() {
        return mDelivery.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder{

        private TextView name;
        private TextView id;
        CardView cardView;
        LinearLayout linearLayout;
        ImageButton imageButton;
        public ViewHolder(View itemView) {
            super(itemView);
            name = (TextView) itemView.findViewById(R.id.recycler_delivery_name_item);
            id = (TextView) itemView.findViewById(R.id.recycler_delivery_id_item);
            cardView = (CardView) itemView.findViewById(R.id.recycler_parent_card_view);
            linearLayout = (LinearLayout) itemView.findViewById(R.id.recycler_child_linear_layout);
            imageButton = (ImageButton) itemView.findViewById(R.id.recycler_info_imag);
        }
    }
}
