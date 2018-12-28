package com.ariel.logi.logi;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import java.util.ArrayList;

public class RecyclerViewProfile extends RecyclerView.Adapter<RecyclerViewProfile.ViewHolder> {
    private ArrayList<String> mLable;
    private ArrayList<String> mContent;
    private Context context;

    public RecyclerViewProfile(Context context, ArrayList<String> mLable, ArrayList<String> mContent) {
        this.mContent = mContent;
        this.mLable = mLable;
        this.context = context;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.recycler_view_item_profile, parent, false);
        ViewHolder holder = new ViewHolder(view);
        return holder;
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        holder.label.setText(mLable.get(position));
        holder.content.setText(mContent.get(position));
    }

    @Override
    public int getItemCount() {
        return mLable.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder{

        private TextView label;
        private TextView content;
        RelativeLayout relativeLayout;
        CardView cardView;
        LinearLayout linearLayout;
        ImageButton imageButton;
        public ViewHolder(View itemView) {
            super(itemView);
            label = (TextView) itemView.findViewById(R.id.label_item);
            content = (TextView) itemView.findViewById(R.id.content_item);
            cardView = (CardView) itemView.findViewById(R.id.parent_recycler_profile_card_view);
            linearLayout = (LinearLayout) itemView.findViewById(R.id.child_recycler_profile_linear_layout);

        }
    }
}
