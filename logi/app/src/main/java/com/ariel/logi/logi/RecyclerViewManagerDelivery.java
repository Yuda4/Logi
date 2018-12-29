package com.ariel.logi.logi;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.LinearLayout;
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
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.recycler_view_manager_delivery, parent, false);
        return new RecyclerViewManagerDelivery.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerViewManagerDelivery.ViewHolder holder, int position) {
        holder.courierName.setText(mDelivery.get(position).getCourierEmail());
        holder.customerName.setText(mDelivery.get(position).getCustomerEmail());
        holder.status.setText(mDelivery.get(position).getStatus());
        holder.deliveryId.setText(mDelivery.get(position).getDeliveryId());
        holder.deliveryDate.setText(mDelivery.get(position).getDate());
    }

    @Override
    public int getItemCount() {
        return mDelivery.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder{

        private TextView courierName;
        private TextView customerName;
        private TextView status;
        private TextView deliveryId;
        private TextView deliveryDate;
        CardView cardView;
        LinearLayout linearLayout;
        ImageButton imageButton;
        public ViewHolder(View itemView) {
            super(itemView);
            courierName = (TextView) itemView.findViewById(R.id.recycler_delivery_courier_name);
            customerName = (TextView) itemView.findViewById(R.id.recycler_customer_name);
            status = (TextView) itemView.findViewById(R.id.recycler_staus);
            deliveryId = (TextView) itemView.findViewById(R.id.recycler_delivery_id);
            deliveryDate = (TextView) itemView.findViewById(R.id.recycler_delivery_date);
            cardView = (CardView) itemView.findViewById(R.id.recycler_parent_card_view);
            linearLayout = (LinearLayout) itemView.findViewById(R.id.recycler_child_linear_layout);
            imageButton = (ImageButton) itemView.findViewById(R.id.recycler_info_imag);
        }
    }
}
