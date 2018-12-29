package com.ariel.logi.logi;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;
import com.ariel.User.User;
import java.util.ArrayList;

public class RecyclerViewCourier extends RecyclerView.Adapter<RecyclerViewCourier.ViewHolder> {
    private ArrayList<User> mCourier;
    private Context context;

    public RecyclerViewCourier(Context context, ArrayList<User> mCourier) {
        this.mCourier = mCourier;
        this.context = context;
    }

    @NonNull
    @Override
    public RecyclerViewCourier.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.recycler_view_item_courier, parent, false);
        return new RecyclerViewCourier.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerViewCourier.ViewHolder holder, int position) {
        holder.name.setText(mCourier.get(position).getName());
        holder.phone.setText(mCourier.get(position).getPhone());
        holder.email.setText(mCourier.get(position).getEmail());
    }

    @Override
    public int getItemCount() {
        return mCourier.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder{

        private TextView name;
        private TextView phone;
        private TextView email;
        CardView cardView;
        LinearLayout linearLayout;

        public ViewHolder(View itemView) {
            super(itemView);
            name = (TextView) itemView.findViewById(R.id.recycler_courier_manager_name_item);
            phone = (TextView) itemView.findViewById(R.id.recycler_courier_manager_phone_item);
            email = (TextView) itemView.findViewById(R.id.recycler_courier_manager_email_item);
            cardView = (CardView) itemView.findViewById(R.id.recycler_courier_manager_parent_card_view);
            linearLayout = (LinearLayout) itemView.findViewById(R.id.recycler_courier_manager_child_linear_layout);
        }
    }
}
